package com.mfpe.account.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

//@AllArgsConstructor
//@Getter
public class AccountCreationStatus {

	/**
	 *  AccountCreationStatus DTO for transferring the information
	 */
	private String savingsAccountId;
	private String currentAccountId;
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
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public AccountCreationStatus(String savingsAccountId, String currentAccountId, String message) {
		super();
		this.savingsAccountId = savingsAccountId;
		this.currentAccountId = currentAccountId;
		this.message = message;
	}
	public AccountCreationStatus() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
