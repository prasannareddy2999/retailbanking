package com.mfpe.transaction.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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

@SpringBootTest
class TransactionServiceImplTest {

	@Mock
	private AccountFeign accountClient;

	@Mock
	private TransactionRepository transactionRepository;

	@Mock
	private RuleFeign ruleClient;

	@InjectMocks
	private TransactionServiceImpl transactionService;

	private TransactionStatus status;
	private AccountDto accountDto;
	private TransactionsHistory singleDepositTransaction;
	private List<TransactionsHistory> history;
	
	@BeforeEach
	void init() {
		MockitoAnnotations.openMocks(this);
	}

	@BeforeEach
	public void setup() {

		accountDto = new AccountDto("SBACS000001", 0.0);
		status = new TransactionStatus("Money deposit transaction successful", 0.0, 1000.0);
		singleDepositTransaction = new TransactionsHistory(1, "SBTR-00001-00001", "Deposit", "SBACS000001", "-", 1000.0,
				new Date(), "Successful", 0.0, 1000.0);
		history = new ArrayList<>();

	}

	@Test
	void depositWithInvalidZeroAmount() {

		Assertions.assertThrows(InvalidAmountException.class,
				() -> this.transactionService.deposit("SBACS000001", 0.0));

	}

	@Test
	void depositWithInvalidNegativeAmount() {

		Assertions.assertThrows(InvalidAmountException.class,
				() -> this.transactionService.deposit("SBACS000001", -10.0));

	}

	@Test
	void depositWithValidPositiveAmountWithNoPreviousTransaction() {

		when(this.accountClient.deposit(accountDto.getAccountId(), 1000.0))
				.thenReturn(new ResponseEntity<>(status, HttpStatus.OK));
		when(this.transactionRepository.findAll()).thenReturn(history);
		TransactionStatus localStatus = this.transactionService.deposit(accountDto.getAccountId(), 1000.0);

		assertEquals(status, localStatus);

	}

	@Test
	void depositWithValidPositiveAmountWithASinglePreviousTransaction() {

		history.add(singleDepositTransaction);
		status.setSourceBalance(1000.0);
		status.setDestinationBalance(2000.0);
		accountDto.setBalance(1000.0);

		when(this.accountClient.deposit(accountDto.getAccountId(), 1000.0))
				.thenReturn(new ResponseEntity<>(status, HttpStatus.OK));
		when(this.transactionRepository.findAll()).thenReturn(history);
		when(this.transactionRepository.max()).thenReturn(2L);

		TransactionStatus localStatus = this.transactionService.deposit(accountDto.getAccountId(), 1000.0);
		this.transactionRepository.save(singleDepositTransaction);

		assertEquals(status, localStatus);

	}

	@Test
	void withdrawWithInvalidZeroAmount() {

		Assertions.assertThrows(InvalidAmountException.class,
				() -> this.transactionService.withdraw("SBACS000001", 0.0, "token"));

	}

	@Test
	void withdrawWithInvalidNegativeAmount() {

		Assertions.assertThrows(InvalidAmountException.class,
				() -> this.transactionService.withdraw("SBACS000001", -10.0, "token"));

	}

	@Test
	void testWithdrawwithNoPreviousTransactionsWithNotEnoughBalance() {

		RuleStatus ruleStatus = new RuleStatus("Denied", "Not OK");

		when(ruleClient.evaluateMinBal("token", "SBACS000001", 1000.0))
				.thenReturn(new ResponseEntity<RuleStatus>(ruleStatus, HttpStatus.OK));
		when(accountClient.getAccount("token", "SBACS000001"))
				.thenReturn(new ResponseEntity<AccountDto>(accountDto, HttpStatus.OK));
		Assertions.assertThrows(NotEnoughBalanceException.class,
				() -> this.transactionService.withdraw("SBACS000001", 1000, "token"));

	}

	@Test
	void testWithdrawwithValidNoPreviousTransaction() {
		TransactionsHistory singleWithdrawTransaction = new TransactionsHistory(1, "SBTR-00001-00001", "Withdraw",
				"SBACS000001", "-", 1000.0, new Date(), "Successful", 2000.0, 1000.0);

		history.add(singleWithdrawTransaction);
		status.setMessage("Money withdraw transaction successful");
		status.setSourceBalance(2000.0);
		status.setDestinationBalance(1000.0);
		accountDto.setBalance(1000.0);

		RuleStatus ruleStatus = new RuleStatus("Allowed", "Completely OK");
		when(ruleClient.evaluateMinBal("token", "SBACS000001", 1000.0))
				.thenReturn(new ResponseEntity<RuleStatus>(ruleStatus, HttpStatus.OK));
		when(accountClient.getAccount("token", "SBACS000001"))
				.thenReturn(new ResponseEntity<AccountDto>(accountDto, HttpStatus.OK));
		when(accountClient.withdraw("token", "SBACS000001", 1000.0))
				.thenReturn(new ResponseEntity<TransactionStatus>(status, HttpStatus.OK));

		assertEquals(status, this.transactionService.withdraw("SBACS000001", 1000, "token"));

	}

	@Test
	void testTransferInvalidZeroAmount() {

		Assertions.assertThrows(InvalidAmountException.class,
				() -> this.transactionService.transfer("SBACS000001", "SBACS000002", 0.0, "token"));
	}

	@Test
	void testTransferInvalidNegativeAmount() {

		Assertions.assertThrows(InvalidAmountException.class,
				() -> this.transactionService.transfer("SBACS000001", "SBACS000002", -10.0, "token"));
	}

	@Test
	void testTransferInvalidTransaction() {

		Assertions.assertThrows(InvalidTransactionException.class,
				() -> this.transactionService.transfer("SBACS000001", "SBACS000001", 1000.0, "token"));
	}

	@Test
	void testTransferNotEnoughBalance() {

		RuleStatus ruleStatus = new RuleStatus("Denied", "Not OK");
		when(ruleClient.evaluateMinBal("token", "SBACS000001", 1000.0))
				.thenReturn(new ResponseEntity<RuleStatus>(ruleStatus, HttpStatus.OK));
		when(accountClient.getAccount("token", "SBACS000001"))
				.thenReturn(new ResponseEntity<AccountDto>(accountDto, HttpStatus.OK));
		Assertions.assertThrows(NotEnoughBalanceException.class,
				() -> this.transactionService.transfer("SBACS000001", "SBACS000002", 1000, "token"));

	}

	@Test
	void testTransferwithValidNoPreviousTransaction() {
		TransactionsHistory singleTransferTransaction = new TransactionsHistory(1, "SBTR-00001-00001", "Transfer",
				"SBACS000001", "SBACS000002", 1000.0, new Date(), "Successful", 2000.0, 1000.0);

		history.add(singleTransferTransaction);
		accountDto.setBalance(1000.0);

		TransactionStatus withdrawStatus = new TransactionStatus("Money Tranfer transaction successful", 2000.0,
				1000.0);

		RuleStatus ruleStatus = new RuleStatus("Allowed", "Completely OK");
		when(ruleClient.evaluateMinBal("token", "SBACS000001", 1000.0))
				.thenReturn(new ResponseEntity<RuleStatus>(ruleStatus, HttpStatus.OK));
		when(accountClient.getAccount("token", "SBACS000001"))
				.thenReturn(new ResponseEntity<AccountDto>(accountDto, HttpStatus.OK));
		when(accountClient.withdraw("token", "SBACS000001", 1000.0))
				.thenReturn(new ResponseEntity<TransactionStatus>(withdrawStatus, HttpStatus.OK));
		when(accountClient.deposit("SBACS000002", 1000.0)).thenReturn(new ResponseEntity<>(status, HttpStatus.OK));

		assertEquals(withdrawStatus, this.transactionService.transfer("SBACS000001", "SBACS000002", 1000.0, "token"));

	}

	@Test
	void testGetTransfersWithEmptyHistory() {

		when(accountClient.getAccount("token", "SBACS000001")).thenReturn(null);

		when(transactionRepository.findBySourceAccountIdOrDestinationAccountId("SBACS000001", "SBACS000001"))
				.thenReturn(history);

		assertEquals(history, this.transactionService.getTransactions("SBACS000001", "token"));

	}

	@Test
	void testGetTransfersWithASingleHistoryOfTransfers() {

		TransactionsHistory singleSentTransferTransaction = new TransactionsHistory(1, "SBTR-00001-00001",
				"Transfer - Sent", "SBACS000001", "SBACS000002", 1000.0, new Date(), "Successful", 2000.0, 1000.0);

		TransactionsHistory singleReceivedTransferTransaction = new TransactionsHistory(2, "SBTR-00002-00002",
				"Transfer - Received", "SBACS000002", "SBACS000001", 1000.0, new Date(), "Successful", 1000.0, 2000.0);

		TransactionsHistory singleUnsentTransferTransaction = new TransactionsHistory(3, "SBTR-00001-00003",
				"Transfer - Unsent", "SBACS000001", "SBACS000002", 3000.0, new Date(), "Unsuccessful", 2000.0, 2000.0);

		history.add(singleSentTransferTransaction);
		history.add(singleReceivedTransferTransaction);
		history.add(singleUnsentTransferTransaction);

		when(transactionRepository.findBySourceAccountIdOrDestinationAccountId("SBACS000001", "SBACS000001"))
				.thenReturn(history);

		assertEquals(history, this.transactionService.getTransactions("SBACS000001", "token"));

	}

	@Test
	void testGetTransfersWithADepositAndWithdraw() {

		TransactionsHistory singleDepositTransaction = new TransactionsHistory(1, "SBTR-00001-00001", "Deposit",
				"SBACS000001", "-", 1000.0, new Date(), "Successful", 0.0, 1000.0);

		TransactionsHistory singleWithdrawTransaction = new TransactionsHistory(2, "SBTR-00001-00002", "Withdraw",
				"SBACS000001", "-", 1000.0, new Date(), "Successful", 1000.0, 0.0);

		history.add(singleDepositTransaction);
		history.add(singleWithdrawTransaction);

		when(transactionRepository.findBySourceAccountIdOrDestinationAccountId("SBACS000001", "SBACS000001"))
				.thenReturn(history);

		assertEquals(history, this.transactionService.getTransactions("SBACS000001", "token"));

	}

	@Test
	void getServiceCharges() {

		List<ServiceCharge> serviceChargeList = new ArrayList<>();
		serviceChargeList.add(new ServiceCharge("SBACS000001","maintaining minimum amount, no detection.", 12000));
		serviceChargeList.add(new ServiceCharge("SBACC000001","maintaining minimum amount, no detection.", 15000));

		when(ruleClient.getServiceCharges("token", "SBACS000001"))
				.thenReturn(new ResponseEntity<List<ServiceCharge>>(serviceChargeList, HttpStatus.OK));

		assertEquals(serviceChargeList, this.transactionService.getServiceCharges("SBACS000001", "token"));
	}

}
