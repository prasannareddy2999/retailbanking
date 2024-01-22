package com.mfpe.customer.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mfpe.customer.model.Gender;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
//@NoArgsConstructor
//@Getter
//@Setter
public class Customer {

	/**
	 * Customer Entity persisted in Repository
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "slno")
	private long id;

	public Customer() {
		super();
		// TODO Auto-generated constructor stub
	}

	

	public Customer(long id, String customerId,
			@NotBlank @Size(min = 3, max = 20, message = "Customer Name should be in between 3 to 20 characters") String customerName,
			@NotBlank(message = "Address should not be blank") @Size(max = 50) String address, Date dateOfBirth,
			@NotBlank(message = "PAN number should not be blank") @Pattern(regexp = "[A-Z]{5}[0-9]{4}[A-Z]{1}", message = "Pan No should contain 5 Capital letters, 4 digits, 1 Capital letter") String panNo,
			@NotNull Gender gender,
			@NotNull @Size(min = 6, message = "Password must be of atleast 6 characters") String password) {
		super();
		this.id = id;
		this.customerId = customerId;
		this.customerName = customerName;
		this.address = address;
		this.dateOfBirth = dateOfBirth;
		this.panNo = panNo;
		this.gender = gender;
		this.password = password;
	}



	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	private String customerId;

	@NotBlank
	@Size(min = 3, max = 20, message = "Customer Name should be in between 3 to 20 characters")
	private String customerName;

	@NotBlank(message = "Address should not be blank")
	@Size(max = 50)
	private String address;
 
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date dateOfBirth;

	@NotBlank(message = "PAN number should not be blank")
	@Pattern(regexp = "[A-Z]{5}[0-9]{4}[A-Z]{1}", message = "Pan No should contain 5 Capital letters, 4 digits, 1 Capital letter")
	private String panNo;

	@NotNull
	private Gender gender;

	//@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}", message = "It contains atleast 8 characters and atmost 20 characters, atleast one digit, atleast one uppercase alphabet, atleast one lowercase alphabet, atleast one special character which includes !@#$%&*()-+=^., doesn't contain any whitespace.")
	@NotNull
	@Size(min=6, message="Password must be of atleast 6 characters")
	private String password;

}
