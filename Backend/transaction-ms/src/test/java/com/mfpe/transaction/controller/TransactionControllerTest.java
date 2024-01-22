package com.mfpe.transaction.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.mfpe.transaction.dto.TransactionStatus;
import com.mfpe.transaction.entity.TransactionsHistory;
import com.mfpe.transaction.feign.AuthenticationFeign;
import com.mfpe.transaction.model.AuthenticationResponse;
import com.mfpe.transaction.model.ServiceCharge;
import com.mfpe.transaction.service.TransactionServiceImpl;

@WebMvcTest
class TransactionControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private TransactionServiceImpl transactionService;

	@MockBean
	private AuthenticationFeign authenticationClient;

	@InjectMocks
	private TransactionController transactionController;

	private String invalidToken;
	private String validToken;
	private AuthenticationResponse invalidAuthResponse;
	private AuthenticationResponse validAuthResponse;
	private String customerRole;
	private String employeeRole;
	private TransactionStatus depositStatus;
	private TransactionStatus withdrawStatus;
	private TransactionStatus transferStatus;
	private List<TransactionsHistory> history;
	private ServiceCharge charges;

	@BeforeEach
	void init() {
		MockitoAnnotations.openMocks(this);
	}
	
	@BeforeEach
	void setup() {

		invalidToken = "invalidtoken";
		validToken = "validtoken";
		invalidAuthResponse = new AuthenticationResponse("SBACS000001", "Ram", true);
		validAuthResponse = new AuthenticationResponse("SBACS000001", "Ram", true);
		customerRole = "CUSTOMER";
		employeeRole = "EMPLOYEE";
		depositStatus = new TransactionStatus("Money deposit transaction successfull", 0.0, 1000.0);
		withdrawStatus = new TransactionStatus("Money withdraw transaction successfull", 1000.0, 0.0);
		transferStatus = new TransactionStatus("Money transfer transaction successfull", 1000.0, 0.0);
		charges = new ServiceCharge("SBACS000001", "maintaining minimum amount, no detection", 1000.0);
		history = new ArrayList<>();

	}

	@Test
	void testDepositWithNoHeader() throws Exception {

		this.mockMvc.perform(post("/deposit/{accountId}/{amount}", "SBACS000001", 1000.0))
				.andExpect(status().isUnauthorized());

	}

	@Test
	void testDepositWithInvalidToken() throws Exception {

		when(this.authenticationClient.getValidity(invalidToken)).thenReturn(invalidAuthResponse);
		when(this.authenticationClient.getRole(invalidAuthResponse.getUserid())).thenReturn(employeeRole);

		this.mockMvc.perform(
				post("/deposit/{accountId}/{amount}", "SBACS000001", 1000.0).header("Authorization", invalidToken))
				.andExpect(status().isUnauthorized());

	}

	@Test
	void testDepositWithValidToken() throws Exception {

		when(this.authenticationClient.getValidity(validToken)).thenReturn(validAuthResponse);
		when(this.authenticationClient.getRole(validAuthResponse.getUserid())).thenReturn(customerRole);
		when(this.transactionService.deposit("SBACS000001", 1000.0)).thenReturn(depositStatus);

		this.mockMvc
				.perform(post("/deposit/{accountId}/{amount}", "SBACS000001", 1000.0).header("Authorization",
						validToken))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.message").value("Money deposit transaction successfull"))
				.andExpect(jsonPath("$.sourceBalance").value(0.0))
				.andExpect(jsonPath("$.destinationBalance").value(1000.0));

	}

	@Test
	void testWithdrawWithNoHeader() throws Exception {

		this.mockMvc.perform(post("/withdraw/{accountId}/{amount}", "SBACS000001", 1000.0))
				.andExpect(status().isUnauthorized());

	}

	@Test
	void testWithdrawWithInvalidToken() throws Exception {

		when(this.authenticationClient.getValidity(invalidToken)).thenReturn(invalidAuthResponse);
		when(this.authenticationClient.getRole(invalidAuthResponse.getUserid())).thenReturn(employeeRole);

		this.mockMvc.perform(
				post("/withdraw/{accountId}/{amount}", "SBACS000001", 1000.0).header("Authorization", invalidToken))
				.andExpect(status().isUnauthorized());

	}

	@Test
	void testWithdrawWithValidToken() throws Exception {

		when(this.authenticationClient.getValidity(validToken)).thenReturn(validAuthResponse);
		when(this.authenticationClient.getRole(validAuthResponse.getUserid())).thenReturn(customerRole);
		when(this.transactionService.withdraw("SBACS000001", 1000.0, validToken)).thenReturn(withdrawStatus);

		this.mockMvc
				.perform(post("/withdraw/{accountId}/{amount}", "SBACS000001", 1000.0).header("Authorization",
						validToken))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.message").value("Money withdraw transaction successfull"))
				.andExpect(jsonPath("$.sourceBalance").value(1000.0))
				.andExpect(jsonPath("$.destinationBalance").value(0.0));

	}

	@Test
	void testTransferWithNoHeader() throws Exception {

		this.mockMvc.perform(post("/transfer/{sourceAccountId}/{destinationAccountId}/{amount}", "SBACS000001",
				"SBACS000002", 1000.0)).andExpect(status().isUnauthorized());

	}

	@Test
	void testTransferWithInvalidToken() throws Exception {

		when(this.authenticationClient.getValidity(invalidToken)).thenReturn(invalidAuthResponse);
		when(this.authenticationClient.getRole(invalidAuthResponse.getUserid())).thenReturn(employeeRole);

		this.mockMvc
				.perform(post("/transfer/{sourceAccountId}/{destinationAccountId}/{amount}", "SBACS000001",
						"SBACS000002", 1000.0).header("Authorization", invalidToken))
				.andExpect(status().isUnauthorized());

	}

	@Test
	void testTransferWithValidToken() throws Exception {

		when(this.authenticationClient.getValidity(validToken)).thenReturn(validAuthResponse);
		when(this.authenticationClient.getRole(validAuthResponse.getUserid())).thenReturn(customerRole);
		when(this.transactionService.transfer("SBACS000001", "SBACS000002", 1000.0, validToken))
				.thenReturn(transferStatus);

		this.mockMvc
				.perform(post("/transfer/{sourceAccountId}/{destinationAccountId}/{amount}", "SBACS000001",
						"SBACS000002", 1000.0).header("Authorization", validToken))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.message").value("Money transfer transaction successfull"))
				.andExpect(jsonPath("$.sourceBalance").value(1000.0))
				.andExpect(jsonPath("$.destinationBalance").value(0.0));

	}

	@Test
	void testGetTransactionsWithNoHeader() throws Exception {

		this.mockMvc.perform(get("/getTransactions/{accountId}", "SBACS000001", 1000.0))
				.andExpect(status().isUnauthorized());

	}

	@Test
	void testGetTransactionsWithInvalidToken() throws Exception {

		when(this.authenticationClient.getValidity(invalidToken)).thenReturn(invalidAuthResponse);
		when(this.authenticationClient.getRole(invalidAuthResponse.getUserid())).thenReturn(employeeRole);

		this.mockMvc.perform(
				get("/getTransactions/{accountId}", "SBACS000001", 1000.0).header("Authorization", invalidToken))
				.andExpect(status().isUnauthorized());

	}

	@Test
	void testGetTransactionsWithValidToken() throws Exception {

		when(this.authenticationClient.getValidity(validToken)).thenReturn(validAuthResponse);
		when(this.authenticationClient.getRole(validAuthResponse.getUserid())).thenReturn(customerRole);
		when(this.transactionService.getTransactions("SBACS000001", validToken)).thenReturn(history);

		this.mockMvc.perform(get("/getTransactions/{accountId}", "SBACS000001").header("Authorization", validToken))
				.andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(0)));

	}

	@Test
	void testGetServiceChargesWithNoHeader() throws Exception {

		this.mockMvc.perform(get("/getServiceCharges/{accountId}", "SBACS000001")).andExpect(status().isUnauthorized());

	}

	@Test
	void testGetServiceChargesWithInvalidToken() throws Exception {

		when(this.authenticationClient.getValidity(invalidToken)).thenReturn(invalidAuthResponse);
		when(this.authenticationClient.getRole(invalidAuthResponse.getUserid())).thenReturn(employeeRole);

		this.mockMvc.perform(get("/getServiceCharges/{customerId}", "SBCU000001").header("Authorization", invalidToken))
				.andExpect(status().isUnauthorized());

	}

	@Test
	void testGetServiceChargesWithValidToken() throws Exception {
		List<ServiceCharge> serviceChargeList = new ArrayList<>();
		serviceChargeList.add(new ServiceCharge("SBACS000001","maintaining minimum amount, no detection.", 12000));
		serviceChargeList.add(new ServiceCharge("SBACC000001","maintaining minimum amount, no detection.", 15000));

		when(this.authenticationClient.getValidity(validToken)).thenReturn(validAuthResponse);
		when(this.authenticationClient.getRole(validAuthResponse.getUserid())).thenReturn(customerRole);
		when(this.transactionService.getServiceCharges("SBCU000001", validToken)).thenReturn(serviceChargeList);

		this.mockMvc.perform(get("/getServiceCharges/{accountId}", "SBCU000001").header("Authorization", validToken))
				.andExpect(status().isOk());

	}

}
