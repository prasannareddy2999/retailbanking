package com.mfpe.rule.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

//@AllArgsConstructor
public class AccountDto {

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

	/**
	 * AccountDto class for transferring the information
	 */
	@SuppressWarnings("unused")
	private String accountId;
	
	@Getter
	private double balance;

}
