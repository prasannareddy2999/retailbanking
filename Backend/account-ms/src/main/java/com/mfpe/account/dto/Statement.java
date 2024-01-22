package com.mfpe.account.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mfpe.account.model.TransactionsHistory;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

//@Getter
//@Setter
//@ToString
//@NoArgsConstructor
//@AllArgsConstructor
public class Statement {

	/**
	 * Statement DTO for transferring the information
	 */
	private static final String MY_TIME_ZONE="Asia/Kolkata";
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm", timezone = MY_TIME_ZONE)
	private Date date;

	private String accountId;

	private double currentBalance;
	
	public Statement() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Statement(Date date, String accountId, double currentBalance, List<TransactionsHistory> history) {
		super();
		this.date = date;
		this.accountId = accountId;
		this.currentBalance = currentBalance;
		this.history = history;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public double getCurrentBalance() {
		return currentBalance;
	}

	public void setCurrentBalance(double currentBalance) {
		this.currentBalance = currentBalance;
	}

	public List<TransactionsHistory> getHistory() {
		return history;
	}

	public void setHistory(List<TransactionsHistory> history) {
		this.history = history;
	}

	public static String getMyTimeZone() {
		return MY_TIME_ZONE;
	}

	List<TransactionsHistory> history;

}
