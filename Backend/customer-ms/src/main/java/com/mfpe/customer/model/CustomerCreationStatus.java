package com.mfpe.customer.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

//@AllArgsConstructor
public class CustomerCreationStatus {
	
	/**
	 * CustomerCreationStatus DTO for transferring the information
	 */
	
	@SuppressWarnings("unused")
	private String message;
	
	//@Getter
	private String customerId;

	public CustomerCreationStatus() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CustomerCreationStatus(String message, String customerId) {
		super();
		this.message = message;
		this.customerId = customerId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

}
