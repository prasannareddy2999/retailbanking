package com.mfpe.customer.exception;

public class AccessDeniedException extends RuntimeException {

	/**
	 * AccessDeniedException Exception Class which will be raised when JWT token
	 * attached with request header is invalid and also when employee or customer
	 * tries to access the unauthorized resource
	 */
	private static final long serialVersionUID = 3614881436315163492L;

	public AccessDeniedException() {
		super();
	}

}