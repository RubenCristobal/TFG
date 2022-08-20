package com.tfg.bookingservice.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.tfg.bookingservice.exceptions.NotAuthorizedException;
import com.tfg.bookingservice.exceptions.NotAvailableUsersService;
import com.tfg.bookingservice.exceptions.NotFoundBookingException;
import com.tfg.bookingservice.exceptions.NotFoundUserException;
import com.tfg.bookingservice.exceptions.UserServiceInternalServerError;

@ControllerAdvice
public class ExceptionsController {

	@ResponseBody
    @ExceptionHandler(NotAuthorizedException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
    String onExceptionServer(NotAuthorizedException ex) {
    	return ex.getMessage();
    }
	
	@ResponseBody
    @ExceptionHandler(UserServiceInternalServerError.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    String onExceptionServer(UserServiceInternalServerError ex) {
    	return ex.getMessage();
    }
	
	@ResponseBody
	@ExceptionHandler(NotFoundUserException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	String onExceptionServer(NotFoundUserException ex) {
		return ex.getMessage();
	}
	
	@ResponseBody
	@ExceptionHandler(NotFoundBookingException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	String onExceptionServer(NotFoundBookingException ex) {
		return ex.getMessage();
	}
	
	@ResponseBody
	@ExceptionHandler(NotAvailableUsersService.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	String onExceptionServer(NotAvailableUsersService ex) {
		return ex.getMessage();
	}
	
	@ResponseBody
	@ExceptionHandler(DataIntegrityViolationException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	String onExceptionServer(DataIntegrityViolationException ex) {
		return "LA RESERVA CON ESOS DATOS YA EXISTE";
	}
    
}
