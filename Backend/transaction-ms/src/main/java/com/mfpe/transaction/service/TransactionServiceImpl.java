package com.mfpe.transaction.service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mfpe.transaction.TransactionMsApplication;
import com.mfpe.transaction.dto.TransactionStatus;
import com.mfpe.transaction.entity.TransactionsHistory;
import com.mfpe.transaction.exception.InvalidAmountException;
import com.mfpe.transaction.exception.InvalidTransactionException;
import com.mfpe.transaction.exception.NotEnoughBalanceException;
import com.mfpe.transaction.feign.AccountFeign;
import com.mfpe.transaction.feign.RuleFeign;
import com.mfpe.transaction.model.AccountDto;
import com.mfpe.transaction.model.RuleStatus;
import com.mfpe.transaction.model.ServiceCharge;
import com.mfpe.transaction.repository.TransactionRepository;

import lombok.extern.slf4j.Slf4j;

@Service
//@Slf4j
public class TransactionServiceImpl implements TransactionService {
	
	private static final Logger log = LoggerFactory.getLogger(TransactionMsApplication.class); 

	@Autowired
	private TransactionRepository transactionRepo;

	@Autowired
	private AccountFeign accountClient;

	@Autowired
	private RuleFeign ruleClient;

	@Override
	public TransactionStatus deposit(String accountId, double amount) {

		log.info("requesting account microservice to deposit the money");

		if (amount <= 0.0) {
			throw new InvalidAmountException();
		}

		TransactionStatus status = this.accountClient.deposit(accountId, amount).getBody();
		log.info("[Transaction status:] " + status);

		return saveTransaction(accountId, amount, status, "Deposit", "-");

	}

	@Override
	public TransactionStatus withdraw(String accountId, double amount, String token) {

		log.info("requesting the rule microservice to validate the withdrawal");

		if (amount <= 0.0) {
			throw new InvalidAmountException();
		}

		RuleStatus ruleStatus = this.ruleClient.evaluateMinBal(token, accountId, amount).getBody();
		AccountDto accountDto = this.accountClient.getAccount(token, accountId).getBody();
		if (ruleStatus.getMessage().equalsIgnoreCase("Not Ok")) {
			log.info("permission denied for transaction, not enough balance");
			TransactionStatus tStatus = new TransactionStatus("Denied", accountDto.getBalance(),
					accountDto.getBalance());
			log.info("[Transaction status:] " + tStatus);
			saveTransaction(accountId, amount, tStatus, "Withdraw", "-");
			throw new NotEnoughBalanceException();
		} else {
			log.info("permission granted for transaction, requesting Account microservice to proceed further");
			TransactionStatus tStatus = this.accountClient.withdraw(token, accountId, amount).getBody();
			log.info("[Transaction status:] " + tStatus);
			return saveTransaction(accountId, amount, tStatus, "Withdraw", "-");
		}

	}

	@Override
	public TransactionStatus transfer(String sourceAccountId, String destinationAccountId, double amount,
			String token) {

		log.info("requesting the rule microservice to validate the transfer");

		if (amount <= 0.0) {
			throw new InvalidAmountException();
		}

		if (sourceAccountId.equalsIgnoreCase(destinationAccountId)) {
			throw new InvalidTransactionException();
		}

		RuleStatus ruleStatus = this.ruleClient.evaluateMinBal(token, sourceAccountId, amount).getBody();
		AccountDto sourceAccountDto = this.accountClient.getAccount(token, sourceAccountId).getBody();
		if (ruleStatus.getMessage().equalsIgnoreCase("Not Ok")) {
			log.info("permission denied for transaction, not enough balance");
			TransactionStatus tStatus = new TransactionStatus("Denied", sourceAccountDto.getBalance(),
					sourceAccountDto.getBalance());
			log.info("[Transaction status:] " + tStatus);
			saveTransaction(sourceAccountId, amount, tStatus, "Transfer - Unsent", destinationAccountId);
			throw new NotEnoughBalanceException();
		} else {
			log.info("permission granted for transaction, requesting Account microservice to proceed further");
			TransactionStatus sourceStatus = this.accountClient.withdraw(token, sourceAccountId, amount).getBody();
			TransactionStatus destinationStatus = this.accountClient.deposit(destinationAccountId, amount).getBody();
			log.info("[Source Transaction status:] " + sourceStatus);
			log.info("[Destination Transaction status:] " + destinationStatus);

			TransactionStatus finalStatus = saveTransaction(sourceAccountId, amount, sourceStatus, "Transfer - Sent",
					destinationAccountId);
			saveTransaction(sourceAccountId, amount, destinationStatus, "Transfer - Received", destinationAccountId);
			finalStatus.setMessage(finalStatus.getMessage().replace("withdraw", "transfer"));
			return finalStatus;
		}

	}

	@Override
	public List<TransactionsHistory> getTransactions(String accountId, String token) {

		log.info("validating details");
		this.accountClient.getAccount(token, accountId);
		log.info("account found! fetching the transaction history");
		List<TransactionsHistory> records = this.transactionRepo.findBySourceAccountIdOrDestinationAccountId(accountId,
				accountId);
		records = records.stream().filter(tRecord -> {
			String transactionType = tRecord.getTransactionType();
			String sourceId = tRecord.getSourceAccountId();
			return (((transactionType.equalsIgnoreCase("Withdraw") || transactionType.equalsIgnoreCase("Deposit")
					|| transactionType.equalsIgnoreCase("Transfer - Sent")
					|| transactionType.equalsIgnoreCase("Transfer - Unsent")) && accountId.equals(sourceId))
					|| (transactionType.equalsIgnoreCase("Transfer - Received") && !accountId.equals(sourceId)));
		}).collect(Collectors.toList());

		log.info("[Transactions history details:] " + records);
		return records;
	}

	@Override
	public List<ServiceCharge> getServiceCharges(String customerId, String token) {

		log.info("requesting Rule Microservice to get service charges");
		return this.ruleClient.getServiceCharges(token, customerId).getBody();

	}

	private TransactionStatus saveTransaction(String accountId, double amount, TransactionStatus status,
			String transactionType, String destinationAccountId) {

		String transactionId = "SBTR-";

		if (!transactionType.contains("Received")) {
			transactionId += accountId.replaceAll("[^0-9]", "") + "-";
		} else {
			transactionId += destinationAccountId.replaceAll("[^0-9]", "") + "-";
		}

		List<TransactionsHistory> transactions = transactionRepo.findAll();

		if (transactions.isEmpty()) {
			String formattedStr = String.format("%06d", 1);
			transactionId += formattedStr;
		} else {
			long id = transactionRepo.max();
			id += 1;
			String formattedStr = String.format("%06d", id);
			transactionId += formattedStr;
		}

		saveRecord(accountId, amount, status, transactionId, transactionType, destinationAccountId);
		return status;
	}

	private void saveRecord(String accountId, double amount, TransactionStatus status, String transactionId,
			String transactionType, String destinationAccountId) {
		log.info("saving the record");
		TransactionsHistory tRecord = new TransactionsHistory();
		tRecord.setTransactionId(transactionId);
		tRecord.setTransactionType(transactionType);
		tRecord.setSourceAccountId(accountId);
		tRecord.setDestinationAccountId(destinationAccountId);
		tRecord.setAmount(amount);
		tRecord.setDateOfTransaction(new Date());

		if (status.getMessage().contains("Denied")) {
			tRecord.setTransactionStatus("Unsuccessful");
		} else
			tRecord.setTransactionStatus("Successful");

		tRecord.setSourceBalance(status.getSourceBalance());
		tRecord.setDestinationBalance(status.getDestinationBalance());
		log.info("[Transaction record details:] " + tRecord);
		this.transactionRepo.save(tRecord);
	}

}
