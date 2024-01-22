package com.mfpe.authentication.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
@Entity
@Table(name = "appuser")
public class AppUser {

	/**
	 * AppUser Entity persisted in repository
	 */
	@Id
	@Column(name = "userid", length = 20)
	@NotNull
	private String userid;

	public AppUser() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AppUser(@NotNull String userid, String username, String password, String authToken, String role) {
		super();
		this.userid = userid;
		this.username = username;
		this.password = password;
		this.authToken = authToken;
		this.role = role;
	}

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

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@Column(name = "username", length = 20)
	private String username;

	@Column(name = "password")
	private String password;

	private String authToken;

	private String role;
}