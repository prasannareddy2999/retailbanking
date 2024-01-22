package com.mfpe.customer.exception;

public class TokenNotFoundException extends RuntimeException {

	/**
	 * TokenNotFoundException Exception Class which will be raised when 
	 * request header doesn't contains JWT token
	 */
	private static final long serialVersionUID = -6481965221919278587L;

	public TokenNotFoundException() {
		super();
	}
	
}
