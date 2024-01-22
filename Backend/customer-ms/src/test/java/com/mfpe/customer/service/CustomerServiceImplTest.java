package com.mfpe.customer.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.mfpe.customer.dto.AccountDto;
import com.mfpe.customer.dto.CustomerDTO;
import com.mfpe.customer.entity.Customer;
import com.mfpe.customer.exception.CustomerAlreadyExistException;
import com.mfpe.customer.exception.CustomerNotFoundException;
import com.mfpe.customer.feign.AccountFeign;
import com.mfpe.customer.feign.AuthenticationFeign;
import com.mfpe.customer.model.CustomerCreationStatus;
import com.mfpe.customer.model.Gender;
import com.mfpe.customer.repository.CustomerRepository;

@SpringBootTest
class CustomerServcieImplTest {

	@Mock
	private CustomerRepository customerRepository;

	@Mock
	private AuthenticationFeign authenticationClient;

	@Mock
	private AccountFeign accountFeign;

	@InjectMocks
	private CustomerServiceImpl customerServiceImpl;

	@BeforeEach
	void init() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testCreateCustomerFirst() throws ParseException {

		Customer customer = new Customer();
		Date dateOfBirth = new SimpleDateFormat("yyyy-MM-dd").parse("2005-12-12");
		customer.setAddress("Hyderabad");
		customer.setCustomerId("SBCU000001");
		customer.setCustomerName("Ram");
		customer.setDateOfBirth(dateOfBirth);
		customer.setGender(Gender.MALE);
		customer.setPanNo("ABCDE1234F");
		customer.setPassword("cust");

		String token = "token";
		List<Customer> customerList = new ArrayList<>();

		CustomerCreationStatus customerCreationStatus = new CustomerCreationStatus("Customer Created Successfully",
				"SBCU000001");

		when(customerRepository.findByPanNo("ABCDE1234G")).thenReturn(Optional.empty());
		when(customerRepository.findAll()).thenReturn(customerList);

		CustomerCreationStatus result = customerServiceImpl.createCustomer(customer, token);
		assertThat(result.getCustomerId()).isEqualTo(customerCreationStatus.getCustomerId());

	}

	@Test
	void testCreateCustomerRemaining() throws ParseException {
		Customer customer = new Customer();
		Date dateOfBirth = new SimpleDateFormat("yyyy-MM-dd").parse("2005-12-12");
		customer.setAddress("Hyderabad");
		customer.setCustomerId("SBCU000002");
		customer.setCustomerName("Ram");
		customer.setDateOfBirth(dateOfBirth);
		customer.setGender(Gender.MALE);
		customer.setPanNo("ABCDE1234F");
		customer.setPassword("cust");

		Customer customer1 = new Customer();
		Date dateOfBirth1 = new SimpleDateFormat("yyyy-MM-dd").parse("2005-12-12");
		customer1.setAddress("Hyderabad");
		customer1.setCustomerId("SBCU000001");
		customer1.setCustomerName("Ram");
		customer1.setDateOfBirth(dateOfBirth1);
		customer1.setGender(Gender.MALE);
		customer1.setPanNo("ABCDE1234G");
		customer1.setPassword("cust");

		String token = "token";
		List<Customer> customerList = new ArrayList<>();
		customerList.add(customer1);

		CustomerCreationStatus customerCreationStatus = new CustomerCreationStatus("Customer Created Successfully",
				customer.getCustomerId());

		when(customerRepository.findByPanNo("ABCDE1234F")).thenReturn(Optional.empty());
		when(customerRepository.findAll()).thenReturn(customerList);
		when(customerRepository.max()).thenReturn(1L);

		CustomerCreationStatus result = customerServiceImpl.createCustomer(customer, token);
		assertThat(result.getCustomerId()).isEqualTo(customerCreationStatus.getCustomerId());

	}

	@Test
	void testCreateCustomerAlreadyExistException() throws ParseException {
		Customer customer = new Customer();
		Date dateOfBirth = new SimpleDateFormat("yyyy-MM-dd").parse("2005-12-12");
		customer.setAddress("Hyderabad");
		customer.setCustomerId("SBCU000002");
		customer.setCustomerName("Ram");
		customer.setDateOfBirth(dateOfBirth);
		customer.setGender(Gender.MALE);
		customer.setPanNo("ABCDE1234F");

		String token = "token";
		when(customerRepository.findByPanNo("ABCDE1234F")).thenReturn(Optional.of(customer));
		assertThrows(CustomerAlreadyExistException.class, () -> customerServiceImpl.createCustomer(customer, token));
	}

	@Test
	void testGetCustomerDetailsValid() throws ParseException {

		Customer customer = new Customer();
		Date dateOfBirth = new SimpleDateFormat("yyyy-MM-dd").parse("2005-12-12");
		customer.setAddress("Hyderabad");
		customer.setCustomerId("SBCU000001");
		customer.setCustomerName("Ram");
		customer.setDateOfBirth(dateOfBirth);
		customer.setGender(Gender.MALE);
		customer.setPanNo("ABCDE1234F");

		AccountDto account1 = new AccountDto("SBACS000001", 0);
		AccountDto account2 = new AccountDto("SBACC000001", 0);

		List<AccountDto> accountList = new ArrayList<>();
		accountList.add(account1);
		accountList.add(account2);

		String customerId = "SBCU000001";
		String custId = "SBCU000001";

		when(customerRepository.findByCustomerId(customerId)).thenReturn(Optional.of(customer));

		ResponseEntity<List<AccountDto>> account = new ResponseEntity<>(accountList, HttpStatus.OK);
		when(accountFeign.getCustomerAccounts(customerId)).thenReturn(account);

		CustomerDTO customerDto = new CustomerDTO();
		customerDto.setAccounts(accountList);
		customerDto.setAddress(customer.getAddress());
		customerDto.setCustomerId(customerId);
		customerDto.setCustomerName(customer.getCustomerName());
		customerDto.setDateOfBirth(dateOfBirth);
		customerDto.setGender(Gender.MALE);
		customerDto.setPanNo(customer.getPanNo());

		CustomerDTO result = customerServiceImpl.getCustomerDetails(customerId, custId);

		assertThat(result.getCustomerId()).isEqualTo(customerDto.getCustomerId());
	}

	@Test
	void testGetCustomerDetailsInvalid() throws ParseException {

		Customer customer = new Customer();
		Date dateOfBirth = new SimpleDateFormat("yyyy-MM-dd").parse("2005-12-12");
		customer.setAddress("Hyderabad");
		customer.setCustomerId("SBCU000001");
		customer.setCustomerName("Ram");
		customer.setDateOfBirth(dateOfBirth);
		customer.setGender(Gender.MALE);
		customer.setPanNo("ABCDE1234F");

		String customerId = "SBCU000001";
		String custId = "SBCU000002";

		when(customerRepository.findByCustomerId(customerId)).thenReturn(Optional.of(customer));

		assertThrows(CustomerNotFoundException.class, () -> customerServiceImpl.getCustomerDetails(customerId, custId));
	}

	@Test
	void testGetCustomerDetailsNotFoundException() {
		String customerId = "SBCU000001";
		String custId = "SBCU000001";

		when(customerRepository.findByCustomerId(customerId)).thenReturn(Optional.empty());

		assertThrows(CustomerNotFoundException.class, () -> customerServiceImpl.getCustomerDetails(customerId, custId));
	}

}