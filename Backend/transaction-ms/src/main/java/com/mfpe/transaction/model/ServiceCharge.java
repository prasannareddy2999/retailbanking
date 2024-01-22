package com.mfpe.transaction.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

//@Getter
//@AllArgsConstructor
public class ServiceCharge {
	
	private String accountId;
	
	public ServiceCharge(String accountId, String message, double balance) {
		super();
		this.accountId = accountId;
		this.message = message;
		this.balance = balance;
	}
	public ServiceCharge() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	private String message;
	private double balance;
	
}
