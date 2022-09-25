package com.ros.inventory.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "No Restaurant with that Id found")
public class RestaurantNotFoundException extends Exception {

	public RestaurantNotFoundException() {
		super();
	}

	public RestaurantNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public RestaurantNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public RestaurantNotFoundException(String message) {
		super(message);
	}

	public RestaurantNotFoundException(Throwable cause) {
		super(cause);
	}

}
