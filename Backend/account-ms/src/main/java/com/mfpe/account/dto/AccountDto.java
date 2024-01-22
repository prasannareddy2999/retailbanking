package com.mfpe.account.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

//@AllArgsConstructor
//@Getter
//@ToString
public class AccountDto {

	/**
	 * Account DTO for transferring the information
	 * */
	private String accountId;
	public AccountDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	public AccountDto(String accountId, double balance) {
		super();
		this.accountId = accountId;
		this.balance = balance;
	}
	private double balance;
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
