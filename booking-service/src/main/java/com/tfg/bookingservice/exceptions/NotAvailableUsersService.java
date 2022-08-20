package com.tfg.bookingservice.exceptions;

import lombok.Getter;
import lombok.Setter;

//Excepcion para cuando el servicio Users esta no disponible desde el WebClient
@Getter
@Setter
public class NotAvailableUsersService extends Exception {

	private static final long serialVersionUID = 1L;

	private static final String NOT_AVAILABLE_ERROR = "EL SERVICO USERS ESTA CAIDO O NO DISPONIBLE";
	
    public NotAvailableUsersService() {
        super(NOT_AVAILABLE_ERROR);
    }

}
