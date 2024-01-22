package com.mfpe.account.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import com.mfpe.account.model.AuthenticationResponse;

@FeignClient(name = "auth-service", url="${feign-auth-url}")
@Component
public interface AuthenticationFeign {

	@GetMapping(value = "/validateToken")
	public AuthenticationResponse getValidity(@RequestHeader(value = "Authorization", required = false) String token);

	@GetMapping("/role/{id}")
	public String getRole(@PathVariable("id") String id);

}