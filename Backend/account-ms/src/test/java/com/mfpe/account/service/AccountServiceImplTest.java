package com.mfpe.account.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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

@SpringBootTest
class AccountServiceImplTest {

	@Mock
	private AccountRepository accountRepo;

	@Mock
	private TransactionFeign transactionClient;

	@InjectMocks
	private AccountServiceImpl accountService;

	private Account account1, account2;
	private AccountCreationStatus status;
	private List<TransactionsHistory> history;
	private Statement statement;
	private String customerId;
	private String token;

	@BeforeEach
	void init() {
		MockitoAnnotations.openMocks(this);
	}

	@BeforeEach
	public void setup() {

		account1 = new Account("SBACS000001", "SBCU000001", 0.0, AccountType.SAVINGS);
		account2 = new Account("SBACC000001", "SBCU000001", 0.0, AccountType.CURRENT);
		status = new AccountCreationStatus("SBACS000001", "SBACC000001",
				"Successfully created savings & current accounts");
		history = new ArrayList<>();
		statement = new Statement();
		statement.setDate(new Date());
		statement.setCurrentBalance(account1.getBalance());
		statement.setAccountId(account1.getAccountId());
		statement.setHistory(history);
		customerId = "SBCU000001";
		token = "token";

	}

	@Test
	void testCreateAccount() {

		this.accountRepo.save(account1);
		this.accountRepo.save(account2);
		AccountCreationStatus localStatus = this.accountService.createAccount("SBCU000001");

		verify(this.accountRepo, times(1)).save(account1);
		verify(this.accountRepo, times(1)).save(account2);
		assertThat(localStatus.getCurrentAccountId()).isEqualTo(status.getCurrentAccountId());
		assertThat(localStatus.getSavingsAccountId()).isEqualTo(status.getSavingsAccountId());
		assertThat(localStatus.getMessage()).isEqualTo(status.getMessage());

	}

	@Test
	void testValidGetAccountWithValidCustomerId() {

		when(this.accountRepo.findById(account1.getAccountId())).thenReturn(Optional.of(account1));
		AccountDto accountDto = this.accountService.getAccount("SBACS000001", this.customerId);

		verify(this.accountRepo, times(1)).findById("SBACS000001");
		assertThat(accountDto.getAccountId()).isEqualTo(account1.getAccountId());
		assertThat(accountDto.getBalance()).isEqualTo(account1.getBalance());

	}

	@Test
	void testValidGetAccountWithInvalidCustomerId() {

		when(this.accountRepo.findById(account1.getAccountId())).thenReturn(Optional.of(account1));

		verify(this.accountRepo, times(0)).findById("SBACS000001");
		Assertions.assertThrows(AccountNotFoundException.class,
				() -> this.accountService.getAccount(account1.getAccountId(), this.customerId + "-"));

	}

	@Test
	void testInvalidGetAccount() {

		when(this.accountRepo.findById(account1.getAccountId())).thenReturn(Optional.empty());
		Assertions.assertThrows(AccountNotFoundException.class,
				() -> this.accountService.getAccount("SBACS000001", this.customerId));

	}

	@Test
	void testGetCustomerAccounts() {

		List<Account> accounts = Arrays.asList(account1, account2);
		when(this.accountRepo.findByCustomerId(account1.getCustomerId())).thenReturn(accounts);

		List<AccountDto> accountDtoList = this.accountService.getCustomerAccounts("SBCU000001");

		assertThat(accountDtoList).hasSize(2);

	}

	@Test
	void testValidDeposit() {

		TransactionStatus status = new TransactionStatus("Money deposit transaction successfull", 0.0, 1000.0);
		when(this.accountRepo.findById(account1.getAccountId())).thenReturn(Optional.of(account1));

		TransactionStatus tempStatus = this.accountService.deposit("SBACS000001", 1000.0);

		assertThat(tempStatus.getSourceBalance()).isEqualTo(status.getSourceBalance());
		assertThat(tempStatus.getDestinationBalance()).isEqualTo(status.getDestinationBalance());
		assertThat(tempStatus.getMessage()).isEqualTo(status.getMessage());

	}

	@Test
	void testInvalidDeposit() {

		when(this.accountRepo.findById(account1.getAccountId())).thenReturn(Optional.empty());
		Assertions.assertThrows(AccountNotFoundException.class,
				() -> this.accountService.deposit("SBACS000001", 1000.0));

	}

	@Test
	void testValidWithdrawWithValidCustomerId() {

		TransactionStatus status = new TransactionStatus("Money withdraw transaction successfull", 2000.0, 1000.0);
		account1.setBalance(2000.0);
		when(this.accountRepo.findById(account1.getAccountId())).thenReturn(Optional.of(account1));

		TransactionStatus tempStatus = this.accountService.withdraw("SBACS000001", 1000.0, this.customerId);

		assertThat(tempStatus.getSourceBalance()).isEqualTo(status.getSourceBalance());
		assertThat(tempStatus.getDestinationBalance()).isEqualTo(status.getDestinationBalance());
		assertThat(tempStatus.getMessage()).isEqualTo(status.getMessage());

	}

	@Test
	void testValidWithdrawWithInvalidCustomerId() {

		account1.setBalance(2000.0);
		when(this.accountRepo.findById(account1.getAccountId())).thenReturn(Optional.of(account1));

		Assertions.assertThrows(AccountNotFoundException.class,
				() -> this.accountService.withdraw("SBACS000001", 1000.0, this.customerId + "-"));

	}

	@Test
	void testInvalidWithdraw() {

		when(this.accountRepo.findById(account1.getAccountId())).thenReturn(Optional.empty());
		Assertions.assertThrows(AccountNotFoundException.class,
				() -> this.accountService.withdraw("SBACS000001", 1000.0, this.customerId));

	}

	@Test
	void testValidGetAccountStatement() throws ParseException {

		TransactionsHistory tBeforeDateRecord = new TransactionsHistory(1L, "SBTR-000001-000001", "Deposit",
				"SBACS000001", "-", 1000.0, new SimpleDateFormat("yyyy-MM-dd hh:mm").parse("2021-08-01 03:33"),
				"Successful", 0.0, 1000.0);
		TransactionsHistory tBetweenDateRecord = new TransactionsHistory(2L, "SBTR-000001-000002", "Withdraw",
				"SBACS000001", "-", 500.0, new SimpleDateFormat("yyyy-MM-dd hh:mm").parse("2021-08-04 03:33"),
				"Successful", 1000.0, 500.0);
		TransactionsHistory tAfterDateRecord = new TransactionsHistory(3L, "SBTR-000001-000002", "Withdraw",
				"SBACS000001", "-", 500.0, new SimpleDateFormat("yyyy-MM-dd hh:mm").parse("2021-08-07 03:33"),
				"Successful", 500.0, 0.0);

		history.add(tBeforeDateRecord);
		history.add(tBetweenDateRecord);
		history.add(tAfterDateRecord);

		when(this.accountRepo.findById(account1.getAccountId())).thenReturn(Optional.of(account1));
		when(this.transactionClient.getTransactions(this.token, account1.getAccountId()))
				.thenReturn(new ResponseEntity<>(history, HttpStatus.OK));

		Date fromDate = new SimpleDateFormat("yyyy-MM-dd").parse("2021-08-03");
		Date toDate = new SimpleDateFormat("yyyy-MM-dd").parse("2021-08-05");

		Statement localStatement = this.accountService.getAccountStatement("SBACS000001", fromDate, toDate, this.token);

		history.remove(tAfterDateRecord);
		history.remove(tBeforeDateRecord);

		assertThat(localStatement.getAccountId()).isEqualTo(statement.getAccountId());
		assertThat(localStatement.getCurrentBalance()).isEqualTo(statement.getCurrentBalance());
		assertThat(localStatement.getHistory()).hasSize(1);


	}

	@Test
	void testGetAccountStatementWithInvalidDateInput() throws ParseException {

		when(this.accountRepo.findById(account1.getAccountId())).thenReturn(Optional.of(account1));

		Date fromDate = new SimpleDateFormat("yyyy-MM-dd").parse("2021-12-10");
		Date toDate = new SimpleDateFormat("yyyy-MM-dd").parse("2021-10-10");

		Assertions.assertThrows(IncorrectDateInputException.class,
				() -> this.accountService.getAccountStatement("SBACS000001", fromDate, toDate, this.token));

	}

	@Test
	void testInvalidGetAccountStatement() throws ParseException {

		when(this.accountRepo.findById(account1.getAccountId())).thenReturn(Optional.empty());

		Date fromDate = new SimpleDateFormat("yyyy-MM-dd").parse("2021-06-10");
		Date toDate = new SimpleDateFormat("yyyy-MM-dd").parse("2021-10-10");

		Assertions.assertThrows(AccountNotFoundException.class,
				() -> this.accountService.getAccountStatement("SBACS000001", fromDate, toDate, this.token));

	}

	@Test
	void testValidGetAccountStatementWithoutdates() throws ParseException {

		TransactionsHistory tBeforeDateRecord = new TransactionsHistory(1L, "SBTR-000001-000001", "Deposit",
				"SBACS000001", "-", 1000.0, new SimpleDateFormat("yyyy-MM-dd hh:mm").parse("2021-06-01 03:33"),
				"Successful", 0.0, 1000.0);
		TransactionsHistory tBetweenDateRecord = new TransactionsHistory(2L, "SBTR-000001-000002", "Withdraw",
				"SBACS000001", "-", 500.0, new SimpleDateFormat("yyyy-MM-dd hh:mm").parse("2021-08-04 03:33"),
				"Successful", 1000.0, 500.0);
		TransactionsHistory tAfterDateRecord = new TransactionsHistory(3L, "SBTR-000001-000002", "Withdraw",
				"SBACS000001", "-", 500.0, new SimpleDateFormat("yyyy-MM-dd hh:mm").parse("2021-010-07 03:33"),
				"Successful", 500.0, 0.0);

		history.add(tBeforeDateRecord);
		history.add(tBetweenDateRecord);
		history.add(tAfterDateRecord);

		when(this.accountRepo.findById(account1.getAccountId())).thenReturn(Optional.of(account1));
		when(this.transactionClient.getTransactions(this.token, account1.getAccountId()))
				.thenReturn(new ResponseEntity<>(history, HttpStatus.OK));

		Statement localStatement = this.accountService.getAccountStatement("SBACS000001", this.token);

		history.remove(tAfterDateRecord);
		history.remove(tBeforeDateRecord);

		assertThat(localStatement.getAccountId()).isEqualTo(statement.getAccountId());
		assertThat(localStatement.getCurrentBalance()).isEqualTo(statement.getCurrentBalance());

	}

	@Test
	void testInvalidGetAccountStatementWithoutDates() {

		when(this.accountRepo.findById(account1.getAccountId())).thenReturn(Optional.empty());

		Assertions.assertThrows(AccountNotFoundException.class,
				() -> this.accountService.getAccountStatement("SBACS000001", this.token));

	}
	
	

}
