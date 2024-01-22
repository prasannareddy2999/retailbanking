package com.mfpe.authentication.model;

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
public class AuthenticationResponse {

	/**
	 * AuthenticationResponse Dto transferring the information
	 */
	private String userid;
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
	private String name;
	private boolean isValid;

}