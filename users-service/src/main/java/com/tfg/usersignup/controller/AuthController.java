package com.tfg.usersignup.controller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tfg.usersignup.model.dto.JwtDto;
import com.tfg.usersignup.model.dto.LoginUser;
import com.tfg.usersignup.model.dto.Message;
import com.tfg.usersignup.model.dto.NewUser;
import com.tfg.usersignup.service.UserService;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	PasswordEncoder encoder;
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@PostMapping("/new")
	public ResponseEntity<?> newUser(@Valid @RequestBody NewUser newUser, BindingResult bindingResult) {
		if(bindingResult.hasErrors()) {
		    Map<String, String> errors = new HashMap<>();
		    bindingResult.getAllErrors().forEach((error) -> {
		        String fieldName = ((FieldError) error).getField();
		        String errorMessage = error.getDefaultMessage();
		        errors.put(fieldName, errorMessage);
		    });
		    return new ResponseEntity<Map<String, String>>(errors, HttpStatus.BAD_REQUEST);
		}
		if(userService.existsByLoginName(newUser.getLoginName())) {
			return new ResponseEntity<Message>(new Message("Error: User name already exists"), HttpStatus.BAD_REQUEST);
		}
		if(userService.existsByEmail(newUser.getEmail())) {
			return new ResponseEntity<Message>(new Message("Error: Email already exists"), HttpStatus.BAD_REQUEST);
		}
		userService.save(newUser, encoder);
		return new ResponseEntity<Message>(new Message("Success: User created"), HttpStatus.CREATED);
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@Valid @RequestBody LoginUser loginUser, BindingResult bindingResult) {
		if(bindingResult.hasErrors()) {
		    Map<String, String> errors = new HashMap<>();
		    bindingResult.getAllErrors().forEach((error) -> {
		        String fieldName = ((FieldError) error).getField();
		        String errorMessage = error.getDefaultMessage();
		        errors.put(fieldName, errorMessage);
		    });
		    return new ResponseEntity<Map<String, String>>(errors, HttpStatus.BAD_REQUEST);
		}
		
		JwtDto jwtDto = userService.createJwtDto(loginUser, authenticationManager);
        return new ResponseEntity<JwtDto>(jwtDto, HttpStatus.OK);
	}

}
