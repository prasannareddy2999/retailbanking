package com.mfpe.rule.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.mfpe.rule.model.ExceptionDetails;

@RestControllerAdvice
public class GlobalExceptionHandler {

	/**
	 * Global Exception Handler for handling all raised exceptions
	 */
	@ExceptionHandler(TokenNotFoundException.class)
	public ResponseEntity<ExceptionDetails> handleTokenNotFoundException(TokenNotFoundException e) {

		ExceptionDetails response = new ExceptionDetails(LocalDateTime.now(),
				HttpStatus.UNAUTHORIZED.value(), "Please Enter Token");
		return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);

	}

}