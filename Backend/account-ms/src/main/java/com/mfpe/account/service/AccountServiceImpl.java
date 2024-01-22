package com.mfpe.account.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mfpe.account.AccountMsApplication;
import com.mfpe.account.dto.AccountCreationStatus;
import com.mfpe.account.dto.AccountDto;
import com.mfpe.account.dto.Statement;
import com.mfpe.account.dto.TransactionStatus;
import com.mfpe.account.entity.Account;
import com.mfpe.account.exception.AccountNotFoundException;
import com.mfpe.account.exception.IncorrectDateInputException;
import com.mfpe.account.feign.TransactionFeign;
import com.mfpe.account.model.AccountType;
import com.mfpe.account.model.TransactionsHistory;
import com.mfpe.account.repository.AccountRepository;

//mport lombok.extern.slf4j.Slf4j;

@Service
//@Slf4j
public class AccountServiceImpl implements AccountService { 
	
	private static final Logger log = LoggerFactory.getLogger(AccountMsApplication.class); 

	/**
	 * Service Layer class for Account Microservice
	 */ 
	@Autowired
	private AccountRepository accountRepo;

	@Autowired
	private TransactionFeign transactionClient;

	/**
	 * @param customerId
	 * @return
	 */
	@Override
	public AccountCreationStatus createAccount(String customerId) {

		log.info("Creating accounts");

		String savingsId = "SBACS" + customerId.replaceAll("[^0-9]", "");
		String currentId = "SBACC" + customerId.replaceAll("[^0-9]", "");

		Account savingsAccount = new Account(savingsId, customerId, 0.0, AccountType.SAVINGS);
		Account currentAccount = new Account(currentId, customerId, 0.0, AccountType.CURRENT);

		this.accountRepo.save(savingsAccount);
		this.accountRepo.save(currentAccount);

		log.info("Accounts successfully created");

		return new AccountCreationStatus(savingsId, currentId, "Successfully created savings & current accounts");

	}

	/**
	 * @param accountId
	 * @return
	 * @throws AccountNotFoundException
	 */
	@Override
	public AccountDto getAccount(String accountId, String customerId) {

		log.info("fetching account details");

		Optional<Account> account = this.accountRepo.findById(accountId);

		if (account.isPresent()) {
			if (customerId.equals(account.get().getCustomerId())) {
				log.info("[Account Details:] " + account.get());
				return new AccountDto(account.get().getAccountId(), account.get().getBalance());
			} else {
				throw new AccountNotFoundException();
			}
		} else {
			throw new AccountNotFoundException();
		}

	}

	/**
	 * @param customerId
	 * @return
	 */
	@Override
	public List<AccountDto> getCustomerAccounts(String customerId) {

		log.info("fetching account details");

		List<Account> accounts = this.accountRepo.findByCustomerId(customerId);
		List<AccountDto> accountDtoList = new ArrayList<>();

		accounts.forEach(account -> accountDtoList.add(new AccountDto(account.getAccountId(), account.getBalance())));
		log.info("[Account Details:] " + accountDtoList);

		return accountDtoList;

	}

	/**
	 * @param accountId
	 * @param fromDate
	 * @param toDate
	 * @return
	 */
	@Override
	public Statement getAccountStatement(String accountId, Date fromDate, Date toDate, String token) {

		log.info("fetching transaction details");

		Optional<Account> account = this.accountRepo.findById(accountId);

		if (fromDate.after(toDate)) {
			throw new IncorrectDateInputException();
		}

		if (account.isPresent()) {
			log.info("creating the account statement");
			List<TransactionsHistory> records = this.transactionClient.getTransactions(token, accountId).getBody();
			records = records.stream()
					.filter(tRecord -> (!(tRecord.getDateOfTransaction().before(fromDate)
							|| tRecord.getDateOfTransaction().after(new Date(toDate.getTime() + 1000 * 60 * 60 * 24)))))
					.collect(Collectors.toList());
			Statement statement = new Statement();
			statement.setDate(new Date());
			statement.setAccountId(accountId);
			statement.setCurrentBalance(account.get().getBalance());
			statement.setHistory(records);
			log.info("[Account Statement Details:] " + statement);
			return statement;
		} else {
			throw new AccountNotFoundException();
		}

	}

	/**
	 * @param accountId
	 * @param fromDate
	 * @param toDate
	 * @return
	 */
	@SuppressWarnings("deprecation")
	@Override
	public Statement getAccountStatement(String accountId, String token) {

		log.info("fetching transaction details");

		Optional<Account> account = this.accountRepo.findById(accountId);

		if (account.isPresent()) {
			log.info("creating the account statement");
			List<TransactionsHistory> records = this.transactionClient.getTransactions(token, accountId).getBody();
			records = records.stream()
					.filter(tRecord -> tRecord.getDateOfTransaction().getMonth() == Calendar.getInstance().get(Calendar.MONTH))
					.collect(Collectors.toList());
			Statement statement = new Statement();
			statement.setDate(new Date());
			statement.setAccountId(accountId);
			statement.setCurrentBalance(account.get().getBalance());
			statement.setHistory(records);
			log.info("[Account Statement Details:] " + statement);
			return statement;
		} else {
			throw new AccountNotFoundException();
		}

	}
	
	/**
	 * @param accountId
	 * @param amount
	 * @return
	 * @throws AccountNotFoundException
	 */
	@Override
	public TransactionStatus deposit(String accountId, double amount) {

		log.info("Money deposit under process");

		Optional<Account> account = this.accountRepo.findById(accountId);

		if (account.isPresent()) {
			double sourceBalance = account.get().getBalance();
			account.get().setBalance(sourceBalance + amount);
			this.accountRepo.save(account.get());
			double destinationBalance = account.get().getBalance();
			TransactionStatus status = new TransactionStatus("Money deposit transaction successfull", sourceBalance,
					destinationBalance);
			log.info("[ " + accountId + " Transaction Details:] " + status);
			return status;
		} else {
			throw new AccountNotFoundException();
		}

	}

	/**
	 * @param accountId
	 * @param amount
	 * @return
	 * @throws AccountNotFoundException
	 */
	@Override
	public TransactionStatus withdraw(String accountId, double amount, String customerId) {

		log.info("Money withdraw under process");

		Optional<Account> account = this.accountRepo.findById(accountId);

		if (account.isPresent()) {
			String custId = account.get().getCustomerId();
			double sourceBalance = account.get().getBalance();
			if (custId.equals(customerId)) {
				account.get().setBalance(sourceBalance - amount);
				this.accountRepo.save(account.get());
				double destinationBalance = account.get().getBalance();
				TransactionStatus status = new TransactionStatus("Money withdraw transaction successfull",
						sourceBalance, destinationBalance);
				log.info("[ " + accountId + " Transaction Details:] " + status);
				return status;
			} else {
				throw new AccountNotFoundException();
			}
		} else {
			throw new AccountNotFoundException();
		}

	}

}