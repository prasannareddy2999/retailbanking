package com.mfpe.transaction.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import com.mfpe.transaction.dto.TransactionStatus;
import com.mfpe.transaction.model.AccountDto;

@FeignClient(name = "Account-Microservice", url = "${feign-account-url}")
public interface AccountFeign {

	@GetMapping("/getAccount/{accountId}")
	public ResponseEntity<AccountDto> getAccount(@RequestHeader(value = "Authorization", required = false) String token, @PathVariable("accountId") String accountId);

	@PostMapping("/deposit/{accountId}/{amount}")
	public ResponseEntity<TransactionStatus> deposit(@PathVariable("accountId") String accountId,
			@PathVariable("amount") double amount);

	@PostMapping("/withdraw/{accountId}/{amount}")
	public ResponseEntity<TransactionStatus> withdraw(@RequestHeader(value = "Authorization", required = false) String token, @PathVariable("accountId") String accountId,
			@PathVariable("amount") double amount);

}
