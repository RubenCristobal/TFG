package com.tfg.bookingservice.utils;

import org.springframework.web.reactive.function.client.WebClientRequestException;

import com.tfg.bookingservice.exceptions.NotAvailableUsersService;

public class ErrorHandler {
	
    private ErrorHandler () {
    }
    
    public static Throwable handleError(Throwable exception) {
		if (exception instanceof WebClientRequestException)
			return new NotAvailableUsersService();
		else return exception;
    }

}
