package com.mfpe.customer.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.mfpe.customer.dto.AccountDto;
import com.mfpe.customer.model.AccountCreationStatus;

/**
 * Feign Client for Account Microservice
 */

@FeignClient(name="Account-Microservice", url="${feign-account-url}")
public interface AccountFeign {
	
	@PostMapping("/createAccount/{customerId}")
	public ResponseEntity<AccountCreationStatus> createAccount(@PathVariable("customerId") String customerId);
	
	@GetMapping("/getCustomerAccounts/{customerId}")
	public ResponseEntity<List<AccountDto>> getCustomerAccounts(@PathVariable("customerId") String customerId);

}
