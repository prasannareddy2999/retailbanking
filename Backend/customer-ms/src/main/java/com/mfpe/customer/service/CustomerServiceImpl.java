package com.mfpe.customer.service;

import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.mfpe.customer.CustomerMsApplication;
import com.mfpe.customer.dto.AccountDto;
import com.mfpe.customer.dto.CustomerDTO;
import com.mfpe.customer.entity.Customer;
import com.mfpe.customer.exception.CustomerAlreadyExistException;
import com.mfpe.customer.exception.CustomerNotFoundException;
import com.mfpe.customer.feign.AccountFeign;
import com.mfpe.customer.feign.AuthenticationFeign;
import com.mfpe.customer.model.AppUser;
import com.mfpe.customer.model.CustomerCreationStatus;
import com.mfpe.customer.repository.CustomerRepository;

import lombok.extern.slf4j.Slf4j;

@Component
@Service
//@Slf4j
public class CustomerServiceImpl implements CustomerService {
	
	/**
	 * Service Layer class for Account Microservice
	 */
	
	private static final Logger log = LoggerFactory.getLogger(CustomerMsApplication.class);
	
	
	// autowiring the Customer Repository
	@Autowired
	private CustomerRepository customerRepository;
	
	
	// autowiring the Account feign client
	@Autowired
	private AccountFeign accountFeign;
	
	// autowiring the Authentication feign client
	@Autowired
	private AuthenticationFeign authenticationClient;
	
	// Base64 Encoder to encrypt the  customer password
	private Base64.Encoder encoder = Base64.getEncoder();  

	/**
	 * @param customer
	 * @param customerId
	 * @return Customer
	 * **/
	private Customer createCustomerObject(Customer customer, String custId) {
		Customer newCustomer = new Customer();
		newCustomer.setCustomerId(custId);
		newCustomer.setAddress(customer.getAddress());
		newCustomer.setCustomerName(customer.getCustomerName());
		newCustomer.setDateOfBirth(customer.getDateOfBirth());
		newCustomer.setGender(customer.getGender());
		newCustomer.setPanNo(customer.getPanNo());
		newCustomer.setPassword(customer.getPassword());
		return newCustomer;
	}
	
	
	/**
	 * @param customer
	 * @param token
	 * @return CustomerCreationStatus which contains a success
	 *         message and created Customer IDs
	 * @throws CustomerAlreadyExistException when the employee tries to create the
	 * 					customer who already exist.
	 */
	
	@Override
	public CustomerCreationStatus createCustomer(Customer customer, String token) {

		log.info("creating the customer");

		String custId = "SBCU";
		Optional<Customer> cust = customerRepository.findByPanNo(customer.getPanNo());

		if (!cust.isPresent()) {

			List<Customer> customerList = customerRepository.findAll();
			if (customerList.isEmpty()) {
				String formattedStr = String.format("%06d", 1);
				custId += formattedStr;

				Customer newCustomer = createCustomerObject(customer, custId);

				AppUser appUser = new AppUser(newCustomer.getCustomerId(), newCustomer.getCustomerName(),
						encoder.encodeToString(newCustomer.getPassword().getBytes()), null, "CUSTOMER");

				authenticationClient.createUser(appUser);

				customerRepository.save(newCustomer);
				log.info("created customer successfully");
				log.info("requesting account microservice to create accounts");
				accountFeign.createAccount(custId);
				log.info("accounts created successfully");

			} else {
				long id = customerRepository.max();
				String formattedStr = String.format("%06d", id + 1);

				custId += formattedStr;

				Customer newCustomer = createCustomerObject(customer, custId);

				AppUser appUser = new AppUser(newCustomer.getCustomerId(), newCustomer.getCustomerName(),
						encoder.encodeToString(newCustomer.getPassword().getBytes()), null, "CUSTOMER");

				authenticationClient.createUser(appUser);

				customerRepository.save(newCustomer);
				log.info("created customer successfully");
				log.info("requesting account microservice to create accounts");
				accountFeign.createAccount(custId);
				log.info("accounts created successfully");
			}
			return new CustomerCreationStatus("Customer Created Successfully", custId);

		} else {
			throw new CustomerAlreadyExistException();
		}

	}

	/**
	 * @param customerId
	 * @return CustomerDto of the customer which contains
	 *         Customer Id, Name, Address, Date Of Birth, PAN Number, Gender, 
	 *         Account Type and respective accountId, account's balance
	 * @throws CustomerNotFoundException when the user requests for the Customer which
	 * 												doesn't exist
	 */
	
	@Override
	public CustomerDTO getCustomerDetails(String customerId, String custId) {

		if (customerId.equals(custId) || custId.contains("EM")) {

			log.info("fetching customer details");
			Optional<Customer> cust = customerRepository.findByCustomerId(customerId);

			if (cust.isPresent()) {
				log.info("requesting account microservice to fetch all accounts of the customer");
				ResponseEntity<List<AccountDto>> account = accountFeign.getCustomerAccounts(customerId);

				CustomerDTO customer = new CustomerDTO();
				customer.setCustomerId(customerId);
				customer.setAddress(cust.get().getAddress());
				customer.setCustomerName(cust.get().getCustomerName());
				customer.setDateOfBirth(cust.get().getDateOfBirth());
				customer.setGender(cust.get().getGender());
				customer.setPanNo(cust.get().getPanNo());
				customer.setAccounts(account.getBody());
				log.info("[Customer Details:] " + customer);
				return customer;
			} else {
				throw new CustomerNotFoundException();
			}
		} else {
			throw new CustomerNotFoundException();
		}
	}

}
