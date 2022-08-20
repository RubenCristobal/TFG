package com.tfg.bookingservice.exceptions;

import lombok.Getter;
import lombok.Setter;

//Excepcion para cuando el servicio users falla en el webclient
@Getter
@Setter
public class UserServiceInternalServerError extends Exception {

	private static final long serialVersionUID = 1L;
	
	private static final String INTERNAL_ERROR = "ERROR INTERNO EN EL SERVICIO USERS";
	
    public UserServiceInternalServerError() {
        super(INTERNAL_ERROR);
    }

}
