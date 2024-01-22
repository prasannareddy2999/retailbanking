package com.mfpe.rule.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

//@AllArgsConstructor
//@Getter
public class RuleStatus {

	/**
	 * RuleStatus Dto for transferring the information
	 */
	private String status;
	public RuleStatus() {
		super();
		// TODO Auto-generated constructor stub
	}
	public RuleStatus(String status, String message) {
		super();
		this.status = status;
		this.message = message;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
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
