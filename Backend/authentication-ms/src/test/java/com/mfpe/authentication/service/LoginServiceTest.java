package com.mfpe.authentication.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import com.mfpe.authentication.exceptionhandling.AppUserNotFoundException;
import com.mfpe.authentication.model.AppUser;

@SpringBootTest
class LoginServiceTest {
	
	UserDetails userDetails;

	@Mock
	private JwtUtil jwtutil;
	
	@Mock
	private CustomerDetailsService customerDetailservice;
	
	@InjectMocks
	private LoginService loginService;
	
	private Base64.Encoder encoder = Base64.getEncoder();
	
	
	
	@Test
	void testUserLogin() throws AppUserNotFoundException {
		
		AppUser appUser = new AppUser("SBEM000001", "emp", "emp", null, "EMPLOYEE");
		
		userDetails = new User("SBEM000001", "ZW1w", new ArrayList<>());
		
		when(customerDetailservice.loadUserByUsername("SBEM000001")).thenReturn(userDetails);
		
		when(jwtutil.generateToken(userDetails)).thenReturn("token");
		
		AppUser appUser1 = new AppUser("SBEM000001", null, null, "token", "EMPLOYEE");
		
		AppUser appUser2 = loginService.userLogin(appUser);
		
		assertThat(appUser1.getUserid()).isEqualTo(appUser2.getUserid());
		
		
	}
	
	@Test
	void testUserLoginInvalidRole() {
		
		AppUser appUser = new AppUser("SBEM000001", "emp", "emp", null, "CUSTOMER");
		
		userDetails = new User("SBEM000001", "ZW1w", new ArrayList<>());
		when(customerDetailservice.loadUserByUsername("SBEM000001")).thenReturn(userDetails);
		
		assertThrows(AppUserNotFoundException.class, () -> loginService.userLogin(appUser));
		
	}
	
	@Test
	void testUserLoginInvalidUserId() {
		AppUser appUser = new AppUser("SBCU000000", "cust", "cust", null, "EMPLOYEE");
		
		userDetails = new User("SBCU000000", "Y3VzdA==", new ArrayList<>());
		
		when(customerDetailservice.loadUserByUsername("SBCU000000")).thenReturn(userDetails);
		
		assertThrows(AppUserNotFoundException.class, () -> loginService.userLogin(appUser));
		
		
	}
	
	
	@Test
	void testUserLoginCustomer() throws AppUserNotFoundException {
		
		AppUser appUser = new AppUser("SBCU000000", "cust", "cust", null, "CUSTOMER");
		
		userDetails = new User("SBCU000000", "Y3VzdA==", new ArrayList<>());
		
		when(customerDetailservice.loadUserByUsername("SBCU000000")).thenReturn(userDetails);
		
		when(jwtutil.generateToken(userDetails)).thenReturn("token");
		
		AppUser appUser1 = new AppUser("SBCU000000", null, null, "token", "CUSTOMER");
		
		AppUser appUser2 = loginService.userLogin(appUser);
		
		assertThat(appUser1.getUserid()).isEqualTo(appUser2.getUserid());
		
		
	}
	
	@Test
	void testUserLoginInvalidCustomerRole() {
		
		AppUser appUser = new AppUser("SBCU000000", "cust", "cust", null, "EMPLOYEE");
		
		userDetails = new User("SBCU000000", "Y3VzdA==", new ArrayList<>());
		when(customerDetailservice.loadUserByUsername("SBCU000000")).thenReturn(userDetails);
		
		assertThrows(AppUserNotFoundException.class, () -> loginService.userLogin(appUser));
		
	}
	
	
	@Test
	void testUserLoginInvalidCustomerUserId() {
		AppUser appUser = new AppUser("SBEM000001", "emp", "emp", null, "CUSTOMER");
		
		userDetails = new User("SBEM000001", "ZW1w", new ArrayList<>());
		
		when(customerDetailservice.loadUserByUsername("SBEM000001")).thenReturn(userDetails);
		
		assertThrows(AppUserNotFoundException.class, () -> loginService.userLogin(appUser));
		
		
	}
	
	
	
	@Test
	void testUserLoginInvalidPassword() {
		AppUser appUser = new AppUser("SBEM000001", "emp", "cust", null, "EMPLOYEE");
		
		userDetails = new User("SBEM000001", "ZW1w", new ArrayList<>());
		
		when(customerDetailservice.loadUserByUsername("SBEM000001")).thenReturn(userDetails);
		
		assertThrows(AppUserNotFoundException.class, () -> loginService.userLogin(appUser));
		
		
	}
	
	
	

}
