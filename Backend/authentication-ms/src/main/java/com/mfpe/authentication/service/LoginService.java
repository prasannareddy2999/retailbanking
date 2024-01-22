package com.mfpe.authentication.service;

import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.mfpe.authentication.AuthenticationMsApplication;
import com.mfpe.authentication.exceptionhandling.AppUserNotFoundException;
import com.mfpe.authentication.model.AppUser;

import lombok.extern.slf4j.Slf4j;

@Component
//@Slf4j
public class LoginService {

	private static final Logger log = LoggerFactory.getLogger(AuthenticationMsApplication.class);	
	
	@Autowired
	private JwtUtil jwtutil;
//	@Autowired
//	private BCryptPasswordEncoder encoder;

	private Base64.Encoder encoder = Base64.getEncoder(); 
	
	@Autowired
	private CustomerDetailsService customerDetailservice;

	public AppUser userLogin(AppUser appuser) throws AppUserNotFoundException {
		final UserDetails userdetails = customerDetailservice.loadUserByUsername(appuser.getUserid());
		String userid = "";
		String role = "";
		String token = "";

		log.info("Password From DB-->{}", userdetails.getPassword());
		log.info("Password From Request-->{}", encoder.encodeToString(appuser.getPassword().getBytes()));
		
		
		
		if ((userdetails.getPassword().equals(encoder.encodeToString(appuser.getPassword().getBytes())))) {
			userid = appuser.getUserid();
			role = appuser.getRole();
			if ((userid.contains("EM") && role.equalsIgnoreCase("EMPLOYEE"))
					|| (userid.contains("CU") && role.equalsIgnoreCase("CUSTOMER"))) {
				token = jwtutil.generateToken(userdetails);

				return new AppUser(userid, null, null, token, role);
			} else {
				throw new AppUserNotFoundException("Given Role is not for this User Id");
			}
		} else {
			throw new AppUserNotFoundException("Username/Password is incorrect...Please check");
		}
	}
}