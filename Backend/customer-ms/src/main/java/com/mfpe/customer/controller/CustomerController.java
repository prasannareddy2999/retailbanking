package com.mfpe.customer.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.mfpe.customer.dto.CustomerDTO;
import com.mfpe.customer.entity.Customer;
import com.mfpe.customer.exception.AccessDeniedException;
import com.mfpe.customer.exception.TokenNotFoundException;
import com.mfpe.customer.feign.AuthenticationFeign;
import com.mfpe.customer.model.AuthenticationResponse;
import com.mfpe.customer.model.CustomerCreationStatus;
import com.mfpe.customer.service.CustomerService;

@RestController
@CrossOrigin(origins ="*")
public class CustomerController {
	
	
	/**
	 * Controller Layer class for Customer Microservice
	 */
	
	// autowiring the Customer Service
	@Autowired 
	private CustomerService customerService;
	
	
	// autowiring the Authentication feign client
	@Autowired
	private AuthenticationFeign authenticationClient;
	
	
	/**
	 * @param token, customer
	 * @return ResponseEntiy of CustomerCreationStatus which contains a success
	 *         message and created Customer IDs
	 * @throws TokenNotFoundException when request header doesn't contains JWT token
	 * @throws AccessDeniedException  when JWT token attached with the request is
	 *                                invalid or if the customer tries to access
	 *                                this resource
	 */

	@PostMapping("/createCustomer")
	public ResponseEntity<CustomerCreationStatus> createCustomer(
			@RequestHeader(value = "Authorization", required = false) String token,
			@RequestBody @Valid Customer customer) {

		if(token == null) {
			throw new TokenNotFoundException();
		}
		 
		AuthenticationResponse validity = authenticationClient.getValidity(token);
		if (authenticationClient.getRole(validity.getUserid()).equals("EMPLOYEE")) {
			CustomerCreationStatus customerCreationStatus = customerService.createCustomer(customer, token);
			return new ResponseEntity<>(customerCreationStatus, HttpStatus.CREATED);
		} else {
			throw new AccessDeniedException(); 
		}

	}
	
	/**
	 * @param token, customerId
	 * @return ResponseEntity of CustomerDto of the customer which contains
	 *         Customer Id, Name, Address, Date Of Birth, PAN Number, Gender, 
	 *         Account Type and respective accountId, account's balance
	 * @throws TokenNotFoundException when request header doesn't contains JWT token
	 * @throws AccessDeniedException  when JWT token attached with the request is
	 *                                invalid
	 */

	@GetMapping("/getCustomerDetails/{customerId}")
	public ResponseEntity<CustomerDTO> getCustomerDetails(
			@RequestHeader(value = "Authorization", required = false) String token,
			@PathVariable("customerId") String customerId) {

		if(token == null) {
			throw new TokenNotFoundException();
		}
		
		AuthenticationResponse validity = authenticationClient.getValidity(token);
		if (validity.isValid()) {
			CustomerDTO customer = customerService.getCustomerDetails(customerId, validity.getUserid());
			return new ResponseEntity<>(customer, HttpStatus.OK);
		} else {
			throw new AccessDeniedException();
		}

	}

}