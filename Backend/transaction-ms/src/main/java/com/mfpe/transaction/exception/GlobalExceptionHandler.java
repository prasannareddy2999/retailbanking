package com.mfpe.transaction.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.mfpe.transaction.dto.ExceptionDetails;

@RestControllerAdvice
public class GlobalExceptionHandler {

	/**
	 * Global Exception Handler for handling all raised exceptions
	 */
	@ExceptionHandler(InvalidAmountException.class)
	public ResponseEntity<ExceptionDetails> handleInvalidAmountException(InvalidAmountException e) {

		ExceptionDetails response = new ExceptionDetails(LocalDateTime.now(),
				HttpStatus.NOT_FOUND.value(), "Enter valid amount");
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

	}

	@ExceptionHandler(NotEnoughBalanceException.class)
	public ResponseEntity<ExceptionDetails> handleNotEnoughAmountException(NotEnoughBalanceException e) {

		ExceptionDetails response = new ExceptionDetails(LocalDateTime.now(),
				HttpStatus.BAD_REQUEST.value(), "No enough balance for proceeding this transaction");
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ExceptionDetails> handleAccessDeniedException(AccessDeniedException e) {

		ExceptionDetails response = new ExceptionDetails(LocalDateTime.now(),
				HttpStatus.UNAUTHORIZED.value(), "Not Allowed");
		return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);

	}

	@ExceptionHandler(InvalidTransactionException.class)
	public ResponseEntity<ExceptionDetails> handleInvalidTransactionException(InvalidTransactionException e) {

		ExceptionDetails response = new ExceptionDetails(LocalDateTime.now(),
				HttpStatus.BAD_REQUEST.value(), "Not Allowed");
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

	}

	@ExceptionHandler(TokenNotFoundException.class)
	public ResponseEntity<ExceptionDetails> handleTokenNotFoundException(TokenNotFoundException e) {

		ExceptionDetails response = new ExceptionDetails(LocalDateTime.now(),
				HttpStatus.UNAUTHORIZED.value(), "Please Enter Token");
		return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);

	}

}