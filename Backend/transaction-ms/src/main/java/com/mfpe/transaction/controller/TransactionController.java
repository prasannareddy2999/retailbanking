package com.mfpe.transaction.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.mfpe.transaction.TransactionMsApplication;
import com.mfpe.transaction.dto.TransactionStatus;
import com.mfpe.transaction.entity.TransactionsHistory;
import com.mfpe.transaction.exception.AccessDeniedException;
import com.mfpe.transaction.exception.TokenNotFoundException;
import com.mfpe.transaction.feign.AuthenticationFeign;
import com.mfpe.transaction.model.AuthenticationResponse;
import com.mfpe.transaction.model.ServiceCharge;
import com.mfpe.transaction.service.TransactionService;

import lombok.extern.slf4j.Slf4j;

@RestController
@EnableFeignClients
@CrossOrigin
//@Slf4j
public class TransactionController {

	private static final Logger log = LoggerFactory.getLogger(TransactionMsApplication.class); 

	
	@Autowired
	private TransactionService transactionService;

	@Autowired
	private AuthenticationFeign authenticationClient;

	@PostMapping("/deposit/{accountId}/{amount}")
	public ResponseEntity<TransactionStatus> deposit(
			@RequestHeader(value = "Authorization", required = false) String token,
			@PathVariable("accountId") String accountId, @PathVariable("amount") double amount) {

		if (token == null) {
			throw new TokenNotFoundException();
		}

		AuthenticationResponse validity = authenticationClient.getValidity(token);
		if (authenticationClient.getRole(validity.getUserid()).equals("CUSTOMER")) {

			log.info("requesting Transaction service to deposit the money");
			return new ResponseEntity<>(this.transactionService.deposit(accountId, amount), HttpStatus.OK);
		} else {
			throw new AccessDeniedException();
		}
	}

	@PostMapping("/withdraw/{accountId}/{amount}")
	public ResponseEntity<TransactionStatus> withdraw(
			@RequestHeader(value = "Authorization", required = false) String token,
			@PathVariable("accountId") String accountId, @PathVariable("amount") double amount) {

		if (token == null) {
			throw new TokenNotFoundException();
		}

		AuthenticationResponse validity = authenticationClient.getValidity(token);
		if (authenticationClient.getRole(validity.getUserid()).equals("CUSTOMER")) {
			log.info("requesting Transaction service to withdraw the money");
			return new ResponseEntity<>(this.transactionService.withdraw(accountId, amount, token), HttpStatus.OK);
		} else {
			throw new AccessDeniedException();
		}
	}

	@PostMapping("/transfer/{sourceAccountId}/{destinationAccountId}/{amount}")
	public ResponseEntity<TransactionStatus> transfer(
			@RequestHeader(value = "Authorization", required = false) String token,
			@PathVariable("sourceAccountId") String sourceAccountId,
			@PathVariable("destinationAccountId") String destinationAccountId, @PathVariable("amount") double amount) {

		if (token == null) {
			throw new TokenNotFoundException();
		}

		AuthenticationResponse validity = authenticationClient.getValidity(token);
		if (authenticationClient.getRole(validity.getUserid()).equals("CUSTOMER")) {
			log.info("requesting Transaction service to transfer the money");
			return new ResponseEntity<>(
					this.transactionService.transfer(sourceAccountId, destinationAccountId, amount, token),
					HttpStatus.OK);
		} else {
			throw new AccessDeniedException();
		}

	}

	@GetMapping("/getTransactions/{accountId}")
	public ResponseEntity<List<TransactionsHistory>> getTransactions(
			@RequestHeader(value = "Authorization", required = false) String token,
			@PathVariable("accountId") String accountId) {

		if (token == null) {
			throw new TokenNotFoundException();
		}

		AuthenticationResponse validity = authenticationClient.getValidity(token);
		if (authenticationClient.getRole(validity.getUserid()).equals("CUSTOMER")) {
			log.info("requesting Transaction service to fetch all transactions");
			return new ResponseEntity<>(this.transactionService.getTransactions(accountId, token), HttpStatus.OK);
		} else {
			throw new AccessDeniedException();
		}

	}

	@GetMapping("/getServiceCharges/{customerId}")
	public ResponseEntity<List<ServiceCharge>> getServiceCharges(
			@RequestHeader(value = "Authorization", required = false) String token,
			@PathVariable("customerId") String customerId) {

		if (token == null) {
			throw new TokenNotFoundException();
		}

		AuthenticationResponse validity = authenticationClient.getValidity(token);
		if (authenticationClient.getRole(validity.getUserid()).equals("CUSTOMER")) {
			log.info("requesting transaction service to fetch the service charges");
			return new ResponseEntity<>(this.transactionService.getServiceCharges(customerId, token), HttpStatus.OK);
		} else {
			throw new AccessDeniedException();
		}

	}

}