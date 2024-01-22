package com.mfpe.account.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.mfpe.account.dto.AccountCreationStatus;
import com.mfpe.account.dto.AccountDto;
import com.mfpe.account.dto.Statement;
import com.mfpe.account.dto.TransactionStatus;
import com.mfpe.account.entity.Account;
import com.mfpe.account.feign.AuthenticationFeign;
import com.mfpe.account.model.AccountType;
import com.mfpe.account.model.AuthenticationResponse;
import com.mfpe.account.model.TransactionsHistory;
import com.mfpe.account.service.AccountServiceImpl;

@WebMvcTest
class AccountControllerTest {

	@BeforeEach
	void init() {
		MockitoAnnotations.openMocks(this);
	}

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private AccountServiceImpl accountServcie;

	@MockBean
	private AuthenticationFeign authenticationClient;

	@InjectMocks
	private AccountController accountController;

	private AccountCreationStatus status;
	private Account account1;
	private String invalidToken;
	private String validToken;
	private AuthenticationResponse validAuthResponse;
	private AuthenticationResponse invalidAuthResponse;
	private String customerRole;
	private String employeeRole;
	private AccountDto accountDto1;
	private AccountDto accountDto2;
	private List<AccountDto> accountDtoList;
	private Date fromDate;
	private Date toDate;
	private List<TransactionsHistory> history;
	private Statement statement;
	private TransactionStatus depositStatus;
	private TransactionStatus withdrawStatus;

	@BeforeEach
	public void setup() throws ParseException {

		status = new AccountCreationStatus("SBACS000001", "SBACC000001",
				"Successfully created savings & current accounts");
		account1 = new Account("SBACS000001", "SBCU000001", 1000.0, AccountType.SAVINGS);
		invalidToken = "wrongtoken";
		validToken = "righttoken";
		validAuthResponse = new AuthenticationResponse("SBCU000001", "Ram", true);
		invalidAuthResponse = new AuthenticationResponse("SBEM000001", "Emp", false);
		customerRole = "CUSTOMER";
		employeeRole = "EMPLOYEE";
		accountDto1 = new AccountDto("SBACS000001", 1000.0);
		accountDto2 = new AccountDto("SBACC000001", 0.0);
		accountDtoList = new ArrayList<>();
		accountDtoList.add(accountDto1);
		accountDtoList.add(accountDto2);
		fromDate = new SimpleDateFormat("yyyy-MM-dd").parse("2021-06-10");
		toDate = new SimpleDateFormat("yyyy-MM-dd").parse("2021-10-10");
		history = new ArrayList<>();
		statement = new Statement(new SimpleDateFormat("yyyy-MM-dd hh:mm").parse("2021-03-03 03:03"), "SBACS000001",
				1000.0, history);
		depositStatus = new TransactionStatus("Money deposit transaction successfull", 1000.0, 2000.0);
		withdrawStatus = new TransactionStatus("Money withdraw transaction successfull", 1000.0, 0.0);

	}

	@Test
	void testCreateAccount() throws Exception {

		when(this.accountServcie.createAccount("SBCU000001")).thenReturn(status);

		this.mockMvc.perform(post("/createAccount/{customerId}", "SBCU000001")).andExpect(status().isCreated())
				.andExpect(jsonPath("$.savingsAccountId").value(status.getSavingsAccountId()))
				.andExpect(jsonPath("$.currentAccountId").value(status.getCurrentAccountId()))
				.andExpect(jsonPath("$.message").value(status.getMessage()));

	}

	@Test
	void testGetCustomerAccounts() throws Exception {

		when(this.accountServcie.getCustomerAccounts("SBCU000001")).thenReturn(accountDtoList);

		this.mockMvc.perform(get("/getCustomerAccounts/{customerId}", "SBCU000001")).andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)));

	}

	@Test
	void testGetAccountWithNoHeader() throws Exception {

		this.mockMvc.perform(get("/getAccount/{accountId}", account1.getAccountId()))
				.andExpect(status().isUnauthorized());

	}

	@Test
	void testGetAccountWithValidToken() throws Exception {

		when(this.authenticationClient.getValidity(validToken)).thenReturn(validAuthResponse);
		when(this.authenticationClient.getRole(validAuthResponse.getUserid())).thenReturn(customerRole);
		when(this.accountServcie.getAccount(account1.getAccountId(), validAuthResponse.getUserid()))
				.thenReturn(accountDto1);

		this.mockMvc
				.perform(get("/getAccount/{accountId}", account1.getAccountId()).header("Authorization", validToken))
				.andExpect(status().isOk());

	}

	@Test
	void testGetAccountWithInvalidToken() throws Exception {

		when(this.authenticationClient.getValidity(invalidToken)).thenReturn(invalidAuthResponse);
		when(this.authenticationClient.getRole(invalidAuthResponse.getUserid())).thenReturn(employeeRole);

		this.mockMvc
				.perform(get("/getAccount/{accountId}", account1.getAccountId()).header("Authorization", invalidToken))
				.andExpect(status().isUnauthorized());

	}

	@Test
	void testGetAccountStatementBetweenDatesWithNoHeader() throws Exception {

		this.mockMvc.perform(
				get("/getAccountStatement/{accountId}/{fromDate}/{toDate}", "SBACS000001", "2021-06-10", "2021-10-10"))
				.andExpect(status().isUnauthorized());

	}

	@Test
	void testGetAccountStatementBetweenDatesWithInvalidToken() throws Exception {

		when(this.authenticationClient.getValidity(invalidToken)).thenReturn(invalidAuthResponse);
		when(this.authenticationClient.getRole(invalidAuthResponse.getUserid())).thenReturn(employeeRole);

		this.mockMvc.perform(
				get("/getAccountStatement/{accountId}/{fromDate}/{toDate}", "SBACS000001", "2021-06-10", "2021-10-10")
						.header("Authorization", invalidToken))
				.andExpect(status().isUnauthorized());

	}

	@Test
	void testGetAccountStatementBetweenDatesWithValidToken() throws Exception {

		when(this.authenticationClient.getValidity(validToken)).thenReturn(validAuthResponse);
		when(this.authenticationClient.getRole(validAuthResponse.getUserid())).thenReturn(customerRole);
		when(this.accountServcie.getAccountStatement("SBACS000001", fromDate, toDate, validToken))
				.thenReturn(statement);

		this.mockMvc.perform(
				get("/getAccountStatement/{accountId}/{fromDate}/{toDate}", "SBACS000001", "2021-06-10", "2021-10-10")
						.header("Authorization", validToken))
				.andExpect(status().isOk());

	}

	@Test
	void testGetAccountStatementWithNoHeader() throws Exception {

		this.mockMvc.perform(get("/getAccountStatement/{accountId}", "SBACS000001"))
				.andExpect(status().isUnauthorized());

	}

	@Test
	void testGetAccountStatementWithInvalidToken() throws Exception {

		when(this.authenticationClient.getValidity(invalidToken)).thenReturn(invalidAuthResponse);
		when(this.authenticationClient.getRole(invalidAuthResponse.getUserid())).thenReturn(employeeRole);

		this.mockMvc
				.perform(get("/getAccountStatement/{accountId}", "SBACS000001").header("Authorization", invalidToken))
				.andExpect(status().isUnauthorized());

	}

	@Test
	void testGetAccountStatementWithValidToken() throws Exception {

		when(this.authenticationClient.getValidity(validToken)).thenReturn(validAuthResponse);
		when(this.authenticationClient.getRole(validAuthResponse.getUserid())).thenReturn(customerRole);
		when(this.accountServcie.getAccountStatement("SBACS000001", validToken)).thenReturn(statement);

		this.mockMvc.perform(get("/getAccountStatement/{accountId}", "SBACS000001").header("Authorization", validToken))
				.andExpect(status().isOk());

	}

	@Test
	void testDeposit() throws Exception {

		when(this.accountServcie.deposit("SBACS000001", 1000.0)).thenReturn(depositStatus);

		this.mockMvc.perform(post("/deposit/{accountId}/{amount}", "SBACS000001", 1000.0)).andExpect(status().isOk())
				.andExpect(jsonPath("$.message").value(depositStatus.getMessage()))
				.andExpect(jsonPath("$.sourceBalance").value(depositStatus.getSourceBalance()))
				.andExpect(jsonPath("$.destinationBalance").value(depositStatus.getDestinationBalance()));

	}

	@Test
	void testWithdrawWithoutHeader() throws Exception {

		this.mockMvc.perform(post("/withdraw/{accountId}/{amount}", "SBACS000001", 1000.0))
				.andExpect(status().isUnauthorized());

	}

	@Test
	void testWithdraw() throws Exception {

		when(this.accountServcie.withdraw("SBACS000001", 1000.0, "SBCU000001")).thenReturn(withdrawStatus);
		when(this.authenticationClient.getValidity(validToken)).thenReturn(validAuthResponse);

		this.mockMvc
				.perform(post("/withdraw/{accountId}/{amount}", "SBACS000001", 1000.0).header("Authorization",
						validToken))
				.andExpect(status().isOk()).andExpect(jsonPath("$.message").value(withdrawStatus.getMessage()))
				.andExpect(jsonPath("$.sourceBalance").value(withdrawStatus.getSourceBalance()))
				.andExpect(jsonPath("$.destinationBalance").value(withdrawStatus.getDestinationBalance()));

	}

}
