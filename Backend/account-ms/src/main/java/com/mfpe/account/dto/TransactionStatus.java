package com.mfpe.account.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

//@AllArgsConstructor
//@Getter
//@ToString
public class TransactionStatus {

	/**
	 *  TransactionStatus DTO for transferring the information
	 */
	private String message;
	public TransactionStatus() {
		super();
		// TODO Auto-generated constructor stub
	}
	public TransactionStatus(String message, double sourceBalance, double destinationBalance) {
		super();
		this.message = message;
		this.sourceBalance = sourceBalance;
		this.destinationBalance = destinationBalance;
	}
	private double sourceBalance;
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
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
	private double destinationBalance;
	
}
