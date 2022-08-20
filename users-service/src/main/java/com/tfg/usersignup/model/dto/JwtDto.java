package com.tfg.usersignup.model.dto;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class JwtDto {
	
	private String token;
	
	@Setter(AccessLevel.NONE)
	@Getter(AccessLevel.NONE)
	private String bearer = "bearer";
	private String userName;
	
	private Collection<? extends GrantedAuthority> authorities;
	

}
