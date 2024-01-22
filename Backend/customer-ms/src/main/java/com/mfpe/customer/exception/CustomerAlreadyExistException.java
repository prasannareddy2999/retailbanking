package com.mfpe.customer.exception;

public class CustomerAlreadyExistException extends RuntimeException {

	/**
	 * CustomerAlreadyExistException Exception Class which will be raised when
	 * customer with same PAN number is being created
	 */
	private static final long serialVersionUID = -9077866631588338462L;

	public CustomerAlreadyExistException() {
		super();
	}
	
}
