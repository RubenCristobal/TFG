package com.tfg.bookingservice.exceptions;

import lombok.Getter;
import lombok.Setter;

//Excepcion para cuando no se ha encontrado ese usuario en la base de datos desde el WebClient
@Getter
@Setter
public class NotFoundUserException extends Exception {

	private static final long serialVersionUID = 1L;

	private static final String NOT_FOUND_ERROR = "USUARIO ALUMNO O PROFESOR NO EXISTE";
	
    public NotFoundUserException() {
        super(NOT_FOUND_ERROR);
    }

}
