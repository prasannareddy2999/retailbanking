package com.mfpe.rule.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//@NoArgsConstructor
//@AllArgsConstructor
//@Getter
//@Setter
public class ServiceCharge {
	
	private String accountId;
	private String message;
	public ServiceCharge() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public ServiceCharge(String accountId, String message, double balance) {
		super();
		this.accountId = accountId;
		this.message = message;
		this.balance = balance;
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
	private double balance;
	
	

}
