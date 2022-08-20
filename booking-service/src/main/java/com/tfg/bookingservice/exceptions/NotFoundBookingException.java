package com.tfg.bookingservice.exceptions;

import lombok.Getter;
import lombok.Setter;

//Excepcion para cuando no se ha encontrar la reserva con ese id en la base de datos
@Getter
@Setter
public class NotFoundBookingException extends Exception {

	private static final long serialVersionUID = 1L;

	private static final String NOT_FOUND_ERROR = "LA RESERVA CON ESE ID NO EXISTE";
	
    public NotFoundBookingException() {
        super(NOT_FOUND_ERROR);
    }

}
