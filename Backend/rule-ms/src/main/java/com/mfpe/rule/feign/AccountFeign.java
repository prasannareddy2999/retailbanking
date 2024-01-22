package com.mfpe.rule.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import com.mfpe.rule.model.AccountDto;

@FeignClient(name = "Account-Microservice", url = "${feign-account-url}")
public interface AccountFeign {

	/**
	 * Feign client for Account Microservice
	 */
	@GetMapping("/getAccount/{accountId}")
	public ResponseEntity<AccountDto> getAccount(@RequestHeader(value = "Authorization", required = false) String token,
			@PathVariable("accountId") String accountId);
	
	
	@GetMapping("/getCustomerAccounts/{customerId}")
	public ResponseEntity<List<AccountDto>> getCustomerAccounts(@PathVariable("customerId") String customerId);

}
