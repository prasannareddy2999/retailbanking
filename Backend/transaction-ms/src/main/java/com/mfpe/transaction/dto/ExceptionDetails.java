package com.mfpe.transaction.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

//@AllArgsConstructor
//@Getter
public class ExceptionDetails {
	
	/**
	 *  AccountExceptionDetails DTO for transferring the error information
	 */
	private LocalDateTime timestamp;
	private int status;
	public ExceptionDetails() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ExceptionDetails(LocalDateTime timestamp, int status, String message) {
		super();
		this.timestamp = timestamp;
		this.status = status;
		this.message = message;
	}
	public LocalDateTime getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	private String message;
}
