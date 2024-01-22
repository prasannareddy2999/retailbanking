package com.mfpe.account.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.mfpe.account.model.AccountType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
//@AllArgsConstructor
//@Getter
//@ToString
public class Account {

	/**
	 *  Account Entity Class persisted in Repository
	 */
	@Id
	@Column
	private String accountId;
	
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
	
	@Column
	private String customerId;

	@Column
	private double balance;
	
	@Column
	private AccountType accountType;

}