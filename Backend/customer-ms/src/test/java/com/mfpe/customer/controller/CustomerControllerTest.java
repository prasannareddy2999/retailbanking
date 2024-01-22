package com.mfpe.customer.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mfpe.customer.dto.AccountDto;
import com.mfpe.customer.dto.CustomerDTO;
import com.mfpe.customer.entity.Customer;
import com.mfpe.customer.feign.AuthenticationFeign;
import com.mfpe.customer.model.AuthenticationResponse;
import com.mfpe.customer.model.CustomerCreationStatus;
import com.mfpe.customer.model.Gender;
import com.mfpe.customer.service.CustomerService;

@WebMvcTest
class CustomerControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CustomerService customerService;

	@MockBean
	private AuthenticationFeign authenticationClient;

	@Test
	void testCreateCustomerWithoutHeader() throws JsonProcessingException, Exception {

		Customer customer = new Customer();
		Date dateOfBirth = new SimpleDateFormat("yyyy-MM-dd").parse("2005-12-12");
		customer.setAddress("Hyderabad");
		customer.setCustomerId("SBCU000001");
		customer.setCustomerName("Ram");
		customer.setDateOfBirth(dateOfBirth);
		customer.setGender(Gender.MALE);
		customer.setPanNo("ABCDE1234F");
		customer.setPassword("Ram@12345");

		this.mockMvc.perform(post("/createCustomer").accept(MediaType.APPLICATION_JSON).content(asJsonString(customer))
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isUnauthorized());

	}

	@Test
	void testCreateCustomerValid() throws Exception {

		Customer customer = new Customer();
		Date dateOfBirth = new SimpleDateFormat("yyyy-MM-dd").parse("2005-12-12");
		customer.setAddress("Hyderabad");
		customer.setCustomerId("SBCU000001");
		customer.setCustomerName("Ram");
		customer.setDateOfBirth(dateOfBirth);
		customer.setGender(Gender.MALE);
		customer.setPanNo("ABCDE1234F");
		customer.setPassword("Ram@12345");

		AuthenticationResponse authResponse = new AuthenticationResponse("Ram", "SBEM000001", true);
		when(authenticationClient.getValidity("token")).thenReturn(authResponse);
		when(authenticationClient.getRole(authResponse.getUserid())).thenReturn("EMPLOYEE");

		CustomerCreationStatus customerCreationStatus = new CustomerCreationStatus("Customer Created Successfully",
				"SBCU000001");

		when(customerService.createCustomer(customer, "token")).thenReturn(customerCreationStatus);

		this.mockMvc
				.perform(post("/createCustomer").header("Authorization", "token").accept(MediaType.APPLICATION_JSON)
						.content(asJsonString(customer)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());

	}

	@Test
	void testCreationCustomerInvalid() throws Exception {

		Customer customer = new Customer();
		Date dateOfBirth = new SimpleDateFormat("yyyy-MM-dd").parse("2005-12-12");
		customer.setAddress("Hyderabad");
		customer.setCustomerId("SBCU000001");
		customer.setCustomerName("Ram");
		customer.setDateOfBirth(dateOfBirth);
		customer.setGender(Gender.MALE);
		customer.setPanNo("ABCDE1234F");
		customer.setPassword("Ram@12345");

		AuthenticationResponse authResponse = new AuthenticationResponse("Ram", "SBCU000001", false);
		when(authenticationClient.getValidity("token")).thenReturn(authResponse);
		when(authenticationClient.getRole(authResponse.getUserid())).thenReturn("CUSTOMER");

		this.mockMvc
				.perform(post("/createCustomer").header("Authorization", "token").accept(MediaType.APPLICATION_JSON)
						.content(asJsonString(customer)).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized());

	}

	@Test
	void testGetCustomerDetailsInvalidToken() throws Exception {

		this.mockMvc.perform(get("/getCustomerDetails/{customerId}", "SBCU000001"))
				.andExpect(status().isUnauthorized());

	}

	@Test
	void testGetCustomerDetailsValid() throws Exception {

		Date dateOfBirth = new SimpleDateFormat("yyyy-MM-dd").parse("2005-12-12");
		AuthenticationResponse authResponse = new AuthenticationResponse("Ram", "SBCU000001", true);
		when(authenticationClient.getValidity("token")).thenReturn(authResponse);

		AccountDto account = new AccountDto("SBACS000001", 0.0);

		List<AccountDto> accounts = new ArrayList<AccountDto>();
		accounts.add(account);

		CustomerDTO customer = new CustomerDTO("SBCU000001", "Ram", "Hyderabad", dateOfBirth, "ABCDE1234F", Gender.MALE,
				accounts);

		when(customerService.getCustomerDetails("SBCU000001", "SBCU000001")).thenReturn(customer);

		this.mockMvc.perform(get("/getCustomerDetails/{customerId}", "SBCU000001").header("Authorization", "token"))
				.andExpect(status().isOk());

	}

	@Test
	void testGetCustomerDetailsNotValid() throws Exception {
		Date dateOfBirth = new SimpleDateFormat("yyyy-MM-dd").parse("2005-12-12");
		AuthenticationResponse authResponse = new AuthenticationResponse("Ram", "SBCU000001", false);
		when(authenticationClient.getValidity("token")).thenReturn(authResponse);

		AccountDto account = new AccountDto("SBACS000001", 0.0);

		List<AccountDto> accounts = new ArrayList<AccountDto>();
		accounts.add(account);

		CustomerDTO customer = new CustomerDTO("SBCU000001", "Ram", "Hyderabad", dateOfBirth, "ABCDE1234F", Gender.MALE,
				accounts);

		when(customerService.getCustomerDetails("SBCU000001", "SBCU000002")).thenReturn(customer);

		this.mockMvc.perform(get("/getCustomerDetails/{customerId}", "SBCU000001").header("Authorization", "token"))
				.andExpect(status().isUnauthorized());

	}

	private static String asJsonString(final Object obj) throws JsonProcessingException {
		return new ObjectMapper().writeValueAsString(obj);
	}

}