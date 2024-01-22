package com.mfpe.rule.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.mfpe.rule.dto.RuleStatus;
import com.mfpe.rule.dto.ServiceCharge;
import com.mfpe.rule.service.RuleService;

@RestController
@CrossOrigin
public class RuleController {

	/**
	 * Controller layer class for Rule Microservice
	 */
	@Autowired
	private RuleService ruleService;

	@GetMapping("/evaluateMinBal/{accountId}/{amount}")
	public ResponseEntity<RuleStatus> evaluateMinBal(
			@RequestHeader(value = "Authorization", required = false) String token,
			@PathVariable("accountId") String accountId, @PathVariable("amount") double amount) {
		return new ResponseEntity<>(this.ruleService.evaluateMinBal(amount, accountId, token), HttpStatus.OK);

	}

	@GetMapping("/getServiceCharges/{customerId}")
	public ResponseEntity<List<ServiceCharge>> getServiceCharges(
			@RequestHeader(value = "Authorization", required = false) String token,
			@PathVariable("customerId") String customerId) {
		return new ResponseEntity<>(this.ruleService.getServiceCharges(customerId, token), HttpStatus.OK);
	}

}
