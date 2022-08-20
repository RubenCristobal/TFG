package com.tfg.bookingservice.exceptions;

import lombok.Getter;
import lombok.Setter;

//Excepcion para cuando no se ha podido autorizar una peticion en el WebClient
@Getter
@Setter
public class NotAuthorizedException extends Exception {

	private static final long serialVersionUID = 1L;

	private static final String AUTH_ERROR = "NO AUTORIZADO DESDE EL SERVICIO USERS";
	
    public NotAuthorizedException() {
        super(AUTH_ERROR);
    }

}
