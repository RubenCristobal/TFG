package com.tfg.usersignup.model.dto;

import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserInformation {

	private String loginName;
	
	private String email;
	
	private String firstName;
	
	private String lastName;
	
	private Set<String> roles = new HashSet<>();

}
