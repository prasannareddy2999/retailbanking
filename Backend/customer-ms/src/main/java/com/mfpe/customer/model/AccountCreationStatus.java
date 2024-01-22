package com.mfpe.customer.model;

public class AccountCreationStatus {

	/**
	 * AccountCreationStatus DTO for transferring the information
	 */

	@SuppressWarnings("unused")
	private String savingsAccountId;
	
	@SuppressWarnings("unused")
	private String currentAccountId;

	@SuppressWarnings("unused")
	private String message;

	public String getSavingsAccountId() {
		return savingsAccountId;
	}

	public void setSavingsAccountId(String savingsAccountId) {
		this.savingsAccountId = savingsAccountId;
	}

	public String getCurrentAccountId() {
		return currentAccountId;
	}

	public void setCurrentAccountId(String currentAccountId) {
		this.currentAccountId = currentAccountId;
	}

	public AccountCreationStatus() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AccountCreationStatus(String savingsAccountId, String currentAccountId, String message) {
		super();
		this.savingsAccountId = savingsAccountId;
		this.currentAccountId = currentAccountId;
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	

}
