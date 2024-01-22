package com.mfpe.transaction.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

//@AllArgsConstructor
//@Getter
public class AccountDto {

	/**
	 * Account DTO for transferring the information
	 */
	private String accountId;
	
	//@Setter
	private double balance;

	public AccountDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AccountDto(String accountId, double balance) {
		super();
		this.accountId = accountId;
		this.balance = balance;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

}
