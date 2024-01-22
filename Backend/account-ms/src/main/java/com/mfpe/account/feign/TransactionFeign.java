package com.mfpe.account.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import com.mfpe.account.model.TransactionsHistory;

@FeignClient(name="transaction-Microservice", url="${feign-transaction-url}")
public interface TransactionFeign {

	@GetMapping("/getTransactions/{accountId}")
	public ResponseEntity<List<TransactionsHistory>> getTransactions(@RequestHeader(value = "Authorization", required = false) String token, @PathVariable("accountId") String accountId);

	
}
