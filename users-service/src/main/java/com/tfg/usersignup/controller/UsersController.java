package com.tfg.usersignup.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tfg.usersignup.model.dto.Message;
import com.tfg.usersignup.model.dto.UserInformation;
import com.tfg.usersignup.service.UserService;
import com.tfg.usersignup.util.DtoConverter;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UsersController {

	@Autowired
	UserService userService;
	
	@GetMapping("/all")
	public ResponseEntity<?> getAllUsers() {
		try {
			List<UserInformation> users = DtoConverter.convertUserList(userService.findAll());
			return new ResponseEntity<List<UserInformation>>(users, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Message>(new Message(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/students")
	public ResponseEntity<?> getAllTeachers() {
		try {
			List<UserInformation> students = DtoConverter.convertUserList(userService.findStudents());
			return new ResponseEntity<List<UserInformation>>(students, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Message>(new Message(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/teachers")
	public ResponseEntity<?> getAllStudents() {
		try {
			List<UserInformation> teachers = DtoConverter.convertUserList(userService.findTeachers());
			return new ResponseEntity<List<UserInformation>>(teachers, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Message>(new Message(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getUser(@PathVariable("id") Long id) {
		if(!userService.existsById(id))
			return new ResponseEntity<Message>(new Message("Not found"), HttpStatus.NOT_FOUND);
		
		UserInformation userInformation = DtoConverter.convertUser(userService.findById(id).get());
		
		return new ResponseEntity<UserInformation>(userInformation, HttpStatus.OK);
	}
	
	@GetMapping("/loginName/{loginName}")
	public ResponseEntity<?> getUser(@PathVariable("loginName") String loginName) {
		if(!userService.existsByLoginName(loginName))
			return new ResponseEntity<Message>(new Message("Not found"), HttpStatus.NOT_FOUND);
		
		UserInformation userInformation = DtoConverter.convertUser(userService.getLoginName(loginName).get());
		
		return new ResponseEntity<UserInformation>(userInformation, HttpStatus.OK);
	}
	
	@GetMapping("/exists/loginName/{loginName}")
	public ResponseEntity<Boolean> existsByLoginName(@PathVariable("loginName") String loginName) {
		if(userService.existsByLoginName(loginName))
			return new ResponseEntity<Boolean>(Boolean.TRUE, HttpStatus.OK);
		else 
			return new ResponseEntity<Boolean>(Boolean.FALSE, HttpStatus.NOT_FOUND);
	}
	
	@GetMapping("/exists/email/{email}")
	public ResponseEntity<Boolean> existsByEmail(@PathVariable("email") String email) {
		if(userService.existsByEmail(email))
			return new ResponseEntity<Boolean>(Boolean.TRUE, HttpStatus.OK);
		else 
			return new ResponseEntity<Boolean>(Boolean.FALSE, HttpStatus.NOT_FOUND);
	}
	
	@GetMapping("/checkToken")
	public ResponseEntity<Boolean> check() {
		return new ResponseEntity<Boolean>(Boolean.TRUE, HttpStatus.OK);
	}
}
