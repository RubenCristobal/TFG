package com.tfg.bookingservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tfg.bookingservice.model.entity.Calendar;
import com.tfg.bookingservice.service.CalendarService;
import com.tfg.bookingservice.service.MailService;
import com.tfg.bookingservice.service.RemoteUserService;
import com.tfg.bookingservice.service.TokenCheckService;
import com.tfg.bookingservice.utils.ErrorHandler;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/calendar")
@CrossOrigin
public class CalendarController {

	@Autowired
	TokenCheckService tokenCheckService;
	
	@Autowired
	RemoteUserService remoteUserService;
	
	@Autowired
	CalendarService calendarService;
	
	@Autowired
	MailService mailService;

	@GetMapping("/all")
	public Mono<List<Calendar>> getCalendars(@RequestHeader(name = "Authorization") String token) {
		return tokenCheckService.checkAuth(token)
				.onErrorMap(ErrorHandler::handleError)
				.flatMap(mono -> calendarService.findAll());
	}
	
	@GetMapping("/{id}")
	public Mono<ResponseEntity<Calendar>> getCalendarById(@RequestHeader(name = "Authorization") String token, @PathVariable("id") Long id) {
		return tokenCheckService.checkAuth(token)
				.onErrorMap(ErrorHandler::handleError)
				.flatMap(mono -> calendarService.findById(id))
				.map(calendar -> {
					ResponseEntity<Calendar> response = new ResponseEntity<Calendar>(calendar, HttpStatus.OK);
					return response;
				})
				.switchIfEmpty(Mono.defer(() -> Mono.just(new ResponseEntity<Calendar>(new Calendar(), HttpStatus.NOT_FOUND))));
	}
	
	@PostMapping("/new")
	public Mono<ResponseEntity<Calendar>> setCalendar(@RequestHeader(name = "Authorization") String token, @RequestBody Calendar calendar) {
		return tokenCheckService.checkAuth(token)
				.onErrorMap(ErrorHandler::handleError)
				.flatMap(mono -> remoteUserService.userExists(token, calendar.getTeacherName()))
				.flatMap(mono -> mailService.sendMail(calendar))
				.flatMap(mono -> calendarService.saveCalendar(calendar))
				.map(data -> {
					ResponseEntity<Calendar> response = new ResponseEntity<Calendar>(data, HttpStatus.CREATED);
					return response;
				});
	}
	
	@PutMapping("/update")
	public Mono<ResponseEntity<String>> updateCalendar(@RequestHeader(name = "Authorization") String token, @RequestBody Calendar calendar) {
		return tokenCheckService.checkAuth(token)
				.onErrorMap(ErrorHandler::handleError)
				.flatMap(mono -> calendarService.update(calendar.getId(), calendar))
				.flatMap(mono -> mailService.sendMail(calendar))
				.thenReturn(new ResponseEntity<String>("Calendario actualizado", HttpStatus.OK));
	}
	
	@GetMapping("/teacher/{teacherName}")
	public Mono<Calendar> getBookByTeacherName(@RequestHeader(name = "Authorization") String token, @PathVariable("teacherName") String teacherName) {
		return tokenCheckService.checkAuth(token)
				.onErrorMap(ErrorHandler::handleError)
				.flatMap(mono -> calendarService.findByTeacherName(teacherName));
	}

}
