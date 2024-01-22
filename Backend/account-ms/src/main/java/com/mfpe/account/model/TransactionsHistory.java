package com.mfpe.account.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;

//@AllArgsConstructor
public class TransactionsHistory {

	/**
	 * TransactionsHistory Model class
	 */
	@SuppressWarnings("unused")
	private long id;

	public TransactionsHistory() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TransactionsHistory(long id, String transactionId, String transactionType, String sourceAccountId,
			String destinationAccountId, double amount, Date dateOfTransaction, String transactionStatus,
			double sourceBalance, double destinationBalance) {
		super();
		this.id = id;
		this.transactionId = transactionId;
		this.transactionType = transactionType;
		this.sourceAccountId = sourceAccountId;
		this.destinationAccountId = destinationAccountId;
		this.amount = amount;
		this.dateOfTransaction = dateOfTransaction;
		this.transactionStatus = transactionStatus;
		this.sourceBalance = sourceBalance;
		this.destinationBalance = destinationBalance;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public String getSourceAccountId() {
		return sourceAccountId;
	}

	public void setSourceAccountId(String sourceAccountId) {
		this.sourceAccountId = sourceAccountId;
	}

	public String getDestinationAccountId() {
		return destinationAccountId;
	}

	public void setDestinationAccountId(String destinationAccountId) {
		this.destinationAccountId = destinationAccountId;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public Date getDateOfTransaction() {
		return dateOfTransaction;
	}

	public void setDateOfTransaction(Date dateOfTransaction) {
		this.dateOfTransaction = dateOfTransaction;
	}

	public String getTransactionStatus() {
		return transactionStatus;
	}

	public void setTransactionStatus(String transactionStatus) {
		this.transactionStatus = transactionStatus;
	}

	public double getSourceBalance() {
		return sourceBalance;
	}

	public void setSourceBalance(double sourceBalance) {
		this.sourceBalance = sourceBalance;
	}

	public double getDestinationBalance() {
		return destinationBalance;
	}

	public void setDestinationBalance(double destinationBalance) {
		this.destinationBalance = destinationBalance;
	}

	public static String getMyTimeZone() {
		return MY_TIME_ZONE;
	}

	@SuppressWarnings("unused")
	private String transactionId;

	@SuppressWarnings("unused")
	private String transactionType;

	@SuppressWarnings("unused")
	private String sourceAccountId;

	@SuppressWarnings("unused")
	private String destinationAccountId;

	@SuppressWarnings("unused")
	private double amount;

	private static final String MY_TIME_ZONE = "Asia/Kolkata";

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm", timezone = MY_TIME_ZONE)
	@Getter
	private Date dateOfTransaction;

	@SuppressWarnings("unused")
	private String transactionStatus;

	@SuppressWarnings("unused")
	private double sourceBalance;

	@SuppressWarnings("unused")
	private double destinationBalance;

}
