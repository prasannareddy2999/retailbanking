package com.mfpe.transaction.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import com.mfpe.transaction.model.RuleStatus;
import com.mfpe.transaction.model.ServiceCharge;

@FeignClient(name = "Rule-Microservice", url = "${feign-rule-url}")
public interface RuleFeign {

	@GetMapping("/evaluateMinBal/{accountId}/{amount}")
	public ResponseEntity<RuleStatus> evaluateMinBal(
			@RequestHeader(value = "Authorization", required = false) String token,
			@PathVariable("accountId") String accountId, @PathVariable("amount") double amount);

	@GetMapping("/getServiceCharges/{customerId}")
	public ResponseEntity<List<ServiceCharge>> getServiceCharges(
			@RequestHeader(value = "Authorization", required = false) String token,
			@PathVariable("accountId") String customerId);
}
