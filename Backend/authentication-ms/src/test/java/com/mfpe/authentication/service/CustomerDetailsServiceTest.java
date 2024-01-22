package com.mfpe.authentication.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.mfpe.authentication.model.AppUser;
import com.mfpe.authentication.repository.UserRepository;

@SpringBootTest
class CustomerDetailsServiceTest {
	
	UserDetails userDetails;

	
	@InjectMocks
	private CustomerDetailsService customerDetailsService;
	
	@Mock
	private UserRepository userRepo;
	
	@Test
	void testLoadUserByUsername() {
		
		AppUser appUser = new AppUser("SBEM000001", "emp", "ZW1w", null, "EMPLOYEE");
		
		when(userRepo.findById("SBEM000001")).thenReturn(Optional.of(appUser));
		
		UserDetails loadUserByUsername2 = customerDetailsService.loadUserByUsername("SBEM000001");
		
		assertEquals(appUser.getUserid(), loadUserByUsername2.getUsername());
		
	}
	
	@Test
	void testLoadUserByUsernameInvalid() {
		
		//AppUser appUser = new AppUser("SBCU000000", "cust", "Y3VzdA==", null, "CUSTOMER");
		
		when(userRepo.findById("SBEM000002")).thenReturn(Optional.empty());
		
		assertThrows(UsernameNotFoundException.class, () -> customerDetailsService.loadUserByUsername("SBEM000002")); 
		
	}
	
	
	
	

}
