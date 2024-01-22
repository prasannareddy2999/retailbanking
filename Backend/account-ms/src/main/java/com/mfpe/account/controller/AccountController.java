package com.mfpe.account.controller;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.mfpe.account.AccountMsApplication;
import com.mfpe.account.dto.AccountCreationStatus;
import com.mfpe.account.dto.AccountDto;
import com.mfpe.account.dto.Statement;
import com.mfpe.account.dto.TransactionStatus;
import com.mfpe.account.exception.AccessDeniedException;
import com.mfpe.account.exception.TokenNotFoundException;
import com.mfpe.account.feign.AuthenticationFeign;
import com.mfpe.account.model.AuthenticationResponse;
import com.mfpe.account.service.AccountService;

import lombok.extern.slf4j.Slf4j;

@RestController
//@Slf4j
@CrossOrigin(origins = "*")
public class AccountController {
	
	private static final Logger log = LoggerFactory.getLogger(AccountMsApplication.class); 
  

	/**
	 * Controller Layer class for Account Microservice
	 */
	@Autowired  
	private AccountService accountServiceImpl;
	
	@Autowired
	private AuthenticationFeign authenticationFeign;

	/**
	 * @param customerId
	 * @return
	 */
	@PostMapping("/createAccount/{customerId}")
	public ResponseEntity<AccountCreationStatus> createAccount(@PathVariable("customerId") String customerId) {

		log.info("requesting account service to create accounts");
		return new ResponseEntity<>(this.accountServiceImpl.createAccount(customerId), HttpStatus.CREATED);

	} 

	/**
	 * @param customerId
	 * @return
	 */
	@GetMapping("/getCustomerAccounts/{customerId}")
	public ResponseEntity<List<AccountDto>> getCustomerAccounts(@PathVariable("customerId") String customerId) {
		
			log.info("requesting account service to get all associated accounts' details");
			return new ResponseEntity<>(this.accountServiceImpl.getCustomerAccounts(customerId), HttpStatus.OK);

	}

	/**
	 * @param accountId
	 * @return
	 */
	@GetMapping("/getAccount/{accountId}")
	public ResponseEntity<AccountDto> getAccount(@RequestHeader(value = "Authorization", required = false) String token, @PathVariable("accountId") String accountId) {
		
		if(token == null) {
			throw new TokenNotFoundException();
		}
		
		AuthenticationResponse validity = authenticationFeign.getValidity(token);
		if (authenticationFeign.getRole(validity.getUserid()).equals("CUSTOMER")) {

			log.info("requesting account service to get account details");
			return new ResponseEntity<>(this.accountServiceImpl.getAccount(accountId, validity.getUserid()), HttpStatus.OK);
		}
		else {
			throw new AccessDeniedException();
		}
	}

	/**
	 * @param accountId
	 * @param fromDate
	 * @param toDate
	 * @return
	 */
	@GetMapping("/getAccountStatement/{accountId}/{fromDate}/{toDate}")
	public ResponseEntity<Statement> getAccountStatementBetweenDates(@RequestHeader(value = "Authorization", required = false) String token, @PathVariable("accountId") String accountId, @PathVariable("fromDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate, @PathVariable("toDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate) {

		if(token == null) {
			throw new TokenNotFoundException();
		}
		
		AuthenticationResponse validity = authenticationFeign.getValidity(token);
		if (authenticationFeign.getRole(validity.getUserid()).equals("CUSTOMER")) {
		
		log.info("requesting account service to get account statement");
		return new ResponseEntity<>(this.accountServiceImpl.getAccountStatement(accountId, fromDate, toDate, token),
				HttpStatus.OK);
		}
		else {
			throw new AccessDeniedException();
		}

	}
	
	/**
	 * @param accountId
	 * @return
	 */
	@GetMapping("/getAccountStatement/{accountId}")
	public ResponseEntity<Statement> getAccountStatement(@RequestHeader(value = "Authorization", required = false) String token, @PathVariable("accountId") String accountId) {

		if(token == null) {
			throw new TokenNotFoundException();
		}
		
		AuthenticationResponse validity = authenticationFeign.getValidity(token);
		if (authenticationFeign.getRole(validity.getUserid()).equals("CUSTOMER")) {
		
		log.info("requesting account service to get account statement");
		return new ResponseEntity<>(this.accountServiceImpl.getAccountStatement(accountId, token),
				HttpStatus.OK);
		}
		else {
			throw new AccessDeniedException();
		}

	}

	/**
	 * @param accountId
	 * @param amount
	 * @return
	 */
	@PostMapping("/deposit/{accountId}/{amount}")
	public ResponseEntity<TransactionStatus> deposit(@PathVariable("accountId") String accountId,
			@PathVariable("amount") double amount) {

		log.info("requesting account service to deposit amount");
		return new ResponseEntity<>(this.accountServiceImpl.deposit(accountId, amount), HttpStatus.OK);

	}

	/**
	 * @param accountId
	 * @param amount
	 * @return
	 */
	@PostMapping("/withdraw/{accountId}/{amount}")
	public ResponseEntity<TransactionStatus> withdraw(@RequestHeader(value = "Authorization", required = false) String token, @PathVariable("accountId") String accountId,
			@PathVariable("amount") double amount) {
		
		if(token == null) {
			throw new TokenNotFoundException();
		}
		
		AuthenticationResponse validity = authenticationFeign.getValidity(token);
		String customerId = validity.getUserid();
		
		log.info("requesting account service to withdraw amount");
		return new ResponseEntity<>(this.accountServiceImpl.withdraw(accountId, amount, customerId), HttpStatus.OK);

	}

}