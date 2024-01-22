package com.mfpe.account.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.mfpe.account.dto.AccountExceptionDetails;

@RestControllerAdvice
public class GlobalExceptionHandler {

	/**
	 * Global Exception Handler for handling all raised exceptions
	 */
	@ExceptionHandler(AccountNotFoundException.class)
	public ResponseEntity<AccountExceptionDetails> handleAccountNotFoundException(AccountNotFoundException e) {

		AccountExceptionDetails response = new AccountExceptionDetails(LocalDateTime.now(),
				HttpStatus.NOT_FOUND.value(), "Account Not Found");
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

	}

	@ExceptionHandler(IncorrectDateInputException.class)
	public ResponseEntity<AccountExceptionDetails> handleDateInputException(IncorrectDateInputException e) {

		AccountExceptionDetails response = new AccountExceptionDetails(LocalDateTime.now(),
				HttpStatus.BAD_REQUEST.value(), "From-date should be before To-date");
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<AccountExceptionDetails> handleAccessDeniedException(AccessDeniedException e) {

		AccountExceptionDetails response = new AccountExceptionDetails(LocalDateTime.now(),
				HttpStatus.UNAUTHORIZED.value(), "Not Allowed");
		return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);

	}

	@ExceptionHandler(TokenNotFoundException.class)
	public ResponseEntity<AccountExceptionDetails> handleTokenNotFoundException(TokenNotFoundException e) {

		AccountExceptionDetails response = new AccountExceptionDetails(LocalDateTime.now(),
				HttpStatus.UNAUTHORIZED.value(), "Please Enter Token");
		return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);

	}

}