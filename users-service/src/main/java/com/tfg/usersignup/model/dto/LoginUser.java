package com.tfg.usersignup.model.dto;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class LoginUser {
	
	@NotBlank
	private String loginName;
	
	@NotBlank
	private String password;

}
