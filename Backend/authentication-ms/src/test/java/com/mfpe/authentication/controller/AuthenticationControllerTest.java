package com.mfpe.authentication.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mfpe.authentication.model.AppUser;
import com.mfpe.authentication.model.AuthenticationResponse;
import com.mfpe.authentication.repository.UserRepository;
import com.mfpe.authentication.service.LoginService;
import com.mfpe.authentication.service.Validationservice;

//@WebMvcTest(AuthenticationController.class)
//@SpringBootTest
//@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserRepository userRepository;

	@MockBean
	private LoginService loginService;

	@MockBean
	private Validationservice validationService;

	@InjectMocks
	private AuthenticationController authController;

	private AppUser appUser;
	private String validToken;
	private AuthenticationResponse authResponse;

	@BeforeEach
	void setUp() {

		appUser = new AppUser("SBCU000001", "Ram", "Ram@12345", "validtoken", "CUSTOMER");
		validToken = "validtoken";
		authResponse = new AuthenticationResponse("SBCU000001", "Ram", true);

	}

	@Test
	void testHealthCheck() throws Exception {
		this.mockMvc.perform(get("/health")).andExpect(status().isOk());
	}

	@Test
	void testLogin() throws Exception {

		when(this.loginService.userLogin(appUser)).thenReturn(appUser);

		this.mockMvc.perform(post("/login").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(appUser))).andExpect(status().isAccepted());

	}

	@Test
	void testGetValidity() throws Exception {

		when(this.validationService.validate(validToken)).thenReturn(authResponse);

		this.mockMvc.perform(get("/validateToken").header("Authorization", validToken))
				.andExpect(jsonPath("$.userid").value("SBCU000001")).andExpect(jsonPath("$.name").value("Ram"))
				.andExpect(jsonPath("$.valid").value(true));

	}

	@Test
	void testCreateUserValid() throws Exception {

		when(this.userRepository.save(appUser)).thenReturn(appUser);

		this.mockMvc.perform(post("/createUser").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(asJsonString(appUser)))
		.andExpect(status().isCreated());

	}
	
	@Test
	void testGetRole() throws Exception {
		
		when(this.userRepository.findById(authResponse.getUserid())).thenReturn(Optional.of(appUser));
		
		this.mockMvc.perform(get("/role/{id}", authResponse.getUserid())).andExpect(jsonPath("$").value(appUser.getRole()));
		
	}

	private static String asJsonString(AppUser user) throws JsonProcessingException {
		return new ObjectMapper().writeValueAsString(user);
	}

}