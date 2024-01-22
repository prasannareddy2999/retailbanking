package com.mfpe.customer.model;

import lombok.AllArgsConstructor;

//@AllArgsConstructor
public class AppUser {

	/**
	 * AppUser Model Class
	 */
	@SuppressWarnings("unused")
	private String userid;
	
	@SuppressWarnings("unused")
	private String username;

	@SuppressWarnings("unused")
	private String password;

	@SuppressWarnings("unused")
	private String authToken;

	@SuppressWarnings("unused")
	private String role;

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAuthToken() {
		return authToken;
	}

	public AppUser(String userid, String username, String password, String authToken, String role) {
		super();
		this.userid = userid;
		this.username = username;
		this.password = password;
		this.authToken = authToken;
		this.role = role;
	}

	public AppUser() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	
}