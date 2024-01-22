package com.mfpe.customer.service;

import com.mfpe.customer.dto.CustomerDTO;
import com.mfpe.customer.entity.Customer;
import com.mfpe.customer.model.CustomerCreationStatus;

public interface CustomerService {

	/**
	 * Service Layer interface for Customer Microservice
	 */
	public CustomerCreationStatus createCustomer(Customer customer, String token);

	public CustomerDTO getCustomerDetails(String customerId, String custId);

}
