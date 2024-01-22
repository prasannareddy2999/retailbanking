package com.mfpe.customer.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.mfpe.customer.model.AppUser;
import com.mfpe.customer.model.AuthenticationResponse;

/**
 * Feign Client of Authentication Microservice
 */

@FeignClient(name = "auth-service", url = "${feign-auth-url}")
@Component
public interface AuthenticationFeign {

	// Create Consumer
	@PostMapping(value = "/createUser")
	public ResponseEntity<?> createUser(@RequestBody AppUser appUserCredentials);

	@GetMapping(value = "/validateToken")
	public AuthenticationResponse getValidity(@RequestHeader("Authorization") String token);

	@GetMapping("/role/{id}")
	public String getRole(@PathVariable("id") String id);

}