package com.mfpe.customer.exception;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.mfpe.customer.dto.AccountExceptionDetails;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	/**
	 * Global Exception Handler for handling all raised exceptions
	 */
	@ExceptionHandler(CustomerNotFoundException.class)
	public ResponseEntity<AccountExceptionDetails> handleCustomerNotFoundException(CustomerNotFoundException e) {

		AccountExceptionDetails response = new AccountExceptionDetails(LocalDateTime.now(),
				HttpStatus.NOT_FOUND.value(), "Customer not found");
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

	}
	
	/**
	 * 	Exception Handler for handling CustomerAlreadyExistException
	 */

	@ExceptionHandler(CustomerAlreadyExistException.class)
	public ResponseEntity<AccountExceptionDetails> handleCustomerAlreadyExistException(
			CustomerAlreadyExistException e) {

		AccountExceptionDetails response = new AccountExceptionDetails(LocalDateTime.now(), HttpStatus.CONFLICT.value(),
				"Customer already exist");
		return new ResponseEntity<>(response, HttpStatus.CONFLICT);

	}
	
	
	/**
	 * 	Exception Handler for handling AccessDeniedException
	 */

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<AccountExceptionDetails> handleAccessDeniedException(AccessDeniedException e) {

		AccountExceptionDetails response = new AccountExceptionDetails(LocalDateTime.now(),
				HttpStatus.UNAUTHORIZED.value(), "Not Allowed");
		return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);

	}
	
	/**
	 * 	Exception Handler for handling TokenNotFoundException
	 */

	@ExceptionHandler(TokenNotFoundException.class)
	public ResponseEntity<AccountExceptionDetails> handleTokenNotFoundException(TokenNotFoundException e) {

		AccountExceptionDetails response = new AccountExceptionDetails(LocalDateTime.now(),
				HttpStatus.UNAUTHORIZED.value(), "Please Enter Token");
		return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);

	}
	
	/**
	 * 	Exception Handler for handling ConstraintViolationExcception
	 */

	@ExceptionHandler(ConstraintViolationException.class)
	public void springHandleNotFound(HttpServletResponse response) throws IOException {
		response.sendError(HttpStatus.BAD_REQUEST.value());
	}
	
	/**
	 * handelMethodArgumentNotValid is to handle the Arguments that are NotValid 
	 */
	

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", new Date());
		body.put("status", status.value());
		// Get all errors
		List<String> errors = ex.getBindingResult().getFieldErrors().stream().map(FieldError::getDefaultMessage)
				.collect(Collectors.toList());
		body.put("errors", errors);
		return new ResponseEntity<>(body, headers, status);
	}

}