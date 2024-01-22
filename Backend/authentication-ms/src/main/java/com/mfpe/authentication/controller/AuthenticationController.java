package com.mfpe.authentication.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.mfpe.authentication.AuthenticationMsApplication;
import com.mfpe.authentication.exceptionhandling.AppUserNotFoundException;
import com.mfpe.authentication.model.AppUser;
import com.mfpe.authentication.model.AuthenticationResponse;
import com.mfpe.authentication.repository.UserRepository;
import com.mfpe.authentication.service.LoginService;
import com.mfpe.authentication.service.Validationservice;

import lombok.extern.slf4j.Slf4j;


/**
 * The AuthController class for request controller
 *
 */
@Slf4j
@RestController
@CrossOrigin(origins = "*")
public class AuthenticationController {
	
	private static final Logger log = LoggerFactory.getLogger(AuthenticationMsApplication.class);
	
	// Users Repository
	@Autowired
	private UserRepository userRepository;
	
	//Service class login
	@Autowired
	private LoginService loginService;
	
	//Service class for login
	@Autowired
	private Validationservice validationService;
	
	/**
	 * The health method to check app
	 *
	 */
	@GetMapping("/health")
	public ResponseEntity<String> healthCheckup() {
		log.info("Health Check for Authentication Microservice");
		log.info("health checkup ----->{}","up");
		return new ResponseEntity<>("UP", HttpStatus.OK);
	}

	/**
	 * The login method with post request
	 *
	 */
	@PostMapping("/login")
	public ResponseEntity<AppUser> login(@RequestBody AppUser appUserloginCredentials) throws UsernameNotFoundException, AppUserNotFoundException {
		AppUser user = loginService.userLogin(appUserloginCredentials);
		log.info("Credentials ----->{}",user);
		return new ResponseEntity<>(user , HttpStatus.ACCEPTED);
	}
	
	/**
	 * The token validation method
	 *
	 */
	@GetMapping("/validateToken")
	public AuthenticationResponse getValidity(@RequestHeader("Authorization") final String token) {
		log.info("Token Validation ----->{}",token);
		return validationService.validate(token);
	}
	
	/**
	 * The user is created with login credentials
	 *
	 */
	@PostMapping("/createUser")
	public ResponseEntity<?> createUser(@RequestBody AppUser appUserCredentials)
	{
		//AppUser createdUser = null;
//		try {
			AppUser createdUser = userRepository.save(appUserCredentials);
//		}
//		catch(Exception e)
//		{
//			createdUser = new AppUser();
//			createdUser.setUserid("User not created");
//			return new ResponseEntity<>(createdUser, HttpStatus.NOT_ACCEPTABLE);
//		}
		log.info("user creation---->{}",createdUser);
		return new ResponseEntity<>(createdUser,HttpStatus.CREATED);
		
	}
	
	
	/**
	 * The find users method to find all users
	 *
	 */	
	@GetMapping("/role/{id}")
	public String getRole(@PathVariable("id") String id) {
		return userRepository.findById(id).get().getRole();
	}
	
}