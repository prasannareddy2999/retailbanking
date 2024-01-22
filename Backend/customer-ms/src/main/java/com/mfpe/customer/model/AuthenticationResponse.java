package com.mfpe.customer.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

//@AllArgsConstructor
public class AuthenticationResponse {

	/**
	 * AuthenticationResponse Dto transferring the information
	 */
	//@Getter
	private String userid;
	
	@SuppressWarnings("unused")
	private String name;
	
	//@Getter
	private boolean isValid;

	public AuthenticationResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AuthenticationResponse(String userid, String name, boolean isValid) {
		super();
		this.userid = userid;
		this.name = name;
		this.isValid = isValid;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isValid() {
		return isValid;
	}

	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}

}