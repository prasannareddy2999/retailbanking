package com.mfpe.customer.exception;

public class CustomerNotFoundException extends RuntimeException {
	
	/**
	 * CustomerNotFoundException is raised when the user requests for Customer
	 * which does not exist
	 */
	private static final long serialVersionUID = -8445340862384469098L;

	public CustomerNotFoundException() {
		super();
	}

}
