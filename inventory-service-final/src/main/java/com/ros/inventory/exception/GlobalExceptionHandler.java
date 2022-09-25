package com.ros.inventory.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No Supplier found")
	@ExceptionHandler(SupplierNotFoundException.class)
	public void supplierNotFound() {
		
	}
	
//	@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Supplier with that Id already exists")
	@ExceptionHandler(SupplierAlreadyExistsException.class)
	public ResponseEntity<String> supplierAlreadyExists() {

		return new ResponseEntity<>("Supplier with that Id already exists",HttpStatus.BAD_REQUEST);
	}
	
	@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No Product Found")
	@ExceptionHandler(ProductNotFoundException.class)
	public void productNotFound() {
//		return "No Product Found";
	}
	
//	@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "")
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<String> illegalArgument(Throwable throwable) {
		
		return new ResponseEntity<>(throwable.getMessage(),HttpStatus.BAD_REQUEST);
	}

}
