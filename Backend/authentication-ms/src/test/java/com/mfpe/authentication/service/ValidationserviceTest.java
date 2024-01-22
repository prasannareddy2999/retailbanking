package com.mfpe.authentication.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.mfpe.authentication.model.AppUser;
import com.mfpe.authentication.model.AuthenticationResponse;
import com.mfpe.authentication.repository.UserRepository;


@SpringBootTest
class ValidationserviceTest {

	@InjectMocks
	private Validationservice validationservice;
	
	@Mock
	private JwtUtil jwtutil;
	
	@Mock
	private UserRepository userRepo;
	
	@Test
	void testValidate() {
		
		AuthenticationResponse authenticationResponse = new AuthenticationResponse("SBEM000001", "emp", true);
		
		AppUser appUser = new AppUser("SBEM000001", "emp", "ZW1w", null, "EMPLOYEE");
		
		when(jwtutil.validateToken("token")).thenReturn(true);
		
		when(jwtutil.extractUsername("token")).thenReturn("SBEM000001");
		when(userRepo.findById("SBEM000001")).thenReturn(Optional.of(appUser));
		
		AuthenticationResponse auth = validationservice.validate("Bearer token");
		
		
		assertThat(authenticationResponse.getUserid()).isEqualTo(auth.getUserid());
		
			
	}
	
	@Test
	void testValidateInvalid() {
		
		AuthenticationResponse authenticationResponse = new AuthenticationResponse("SBEM000001", "emp", false);
		
		when(jwtutil.validateToken("token")).thenReturn(false);
		
		AuthenticationResponse auth = validationservice.validate("Bearer token");
		
		assertThat(authenticationResponse.isValid()).isEqualTo(auth.isValid());
		
		
		
	}

}
