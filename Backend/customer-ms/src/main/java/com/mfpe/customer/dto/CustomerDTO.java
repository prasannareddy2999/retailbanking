package com.mfpe.customer.dto;

import java.util.Date;
import java.util.List;

import com.mfpe.customer.model.Gender;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

//@NoArgsConstructor
//@AllArgsConstructor
//@Getter
//@Setter
//@ToString
public class CustomerDTO {
	
	/**
	 * Customer DTO for transferring the information
	 * */
	
	private String customerId;
	private String customerName;
	private String address;
	private Date dateOfBirth;
	private String panNo;
	private Gender gender;
	private List<AccountDto> accounts;
	
	public CustomerDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public CustomerDTO(String customerId, String customerName, String address, Date dateOfBirth, String panNo,
			Gender gender, List<AccountDto> accounts) {
		super();
		this.customerId = customerId;
		this.customerName = customerName;
		this.address = address;
		this.dateOfBirth = dateOfBirth;
		this.panNo = panNo;
		this.gender = gender;
		this.accounts = accounts;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Date getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	public String getPanNo() {
		return panNo;
	}
	public void setPanNo(String panNo) {
		this.panNo = panNo;
	}
	public Gender getGender() {
		return gender;
	}
	public void setGender(Gender gender) {
		this.gender = gender;
	}
	public List<AccountDto> getAccounts() {
		return accounts;
	}
	public void setAccounts(List<AccountDto> accounts) {
		this.accounts = accounts;
	}
	
	
}
