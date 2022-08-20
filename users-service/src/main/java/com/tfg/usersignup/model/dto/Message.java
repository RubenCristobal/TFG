package com.tfg.usersignup.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Message {

	private String message;
	
    public Message(String message) {
        this.message = message;
    }

}
