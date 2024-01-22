package com.mfpe.rule.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

//@AllArgsConstructor
public class Account {

	/**
	 * Account Entity Class persisted in Repository
	 */
	//@Getter
	private String accountId;

	@SuppressWarnings("unused")
	private String customerId;

	//@Getter
	private double balance;

	public Account() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Account(String accountId, String customerId, double balance, AccountType accountType) {
		super();
		this.accountId = accountId;
		this.customerId = customerId;
		this.balance = balance;
		this.accountType = accountType;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public AccountType getAccountType() {
		return accountType;
	}

	public void setAccountType(AccountType accountType) {
		this.accountType = accountType;
	}

	@SuppressWarnings("unused")
	private AccountType accountType;

}