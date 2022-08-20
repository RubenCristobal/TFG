package com.tfg.bookingservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tfg.bookingservice.exceptions.NotFoundBookingException;
import com.tfg.bookingservice.model.entity.Booking;
import com.tfg.bookingservice.service.BookingService;
import com.tfg.bookingservice.service.MailService;
import com.tfg.bookingservice.service.RemoteUserService;
import com.tfg.bookingservice.service.TokenCheckService;
import com.tfg.bookingservice.utils.ErrorHandler;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/bookings")
@CrossOrigin
public class BookingController {

	@Autowired
	TokenCheckService tokenCheckService;
	
	@Autowired
	RemoteUserService remoteUsersService;

	@Autowired
	BookingService bookingService;
	
	@Autowired
	MailService mailService;

	@GetMapping("/all")
	public Flux<Booking> getBookings(@RequestHeader(name = "Authorization") String token) {
		return tokenCheckService.checkAuth(token)
				.onErrorMap(ErrorHandler::handleError)
				.flatMapMany(mono -> bookingService.findAll());
	}
	
	@GetMapping("/{id}")
	public Mono<ResponseEntity<Booking>> getBookById(@RequestHeader(name = "Authorization") String token, @PathVariable("id") Long id) {
		return tokenCheckService.checkAuth(token)
				.onErrorMap(ErrorHandler::handleError)
				.flatMap(mono -> bookingService.findById(id))
				.map(booking -> {
					ResponseEntity<Booking> response = new ResponseEntity<Booking>(booking, HttpStatus.OK);
					return response;
				})
				.switchIfEmpty(Mono.defer(() -> Mono.just(new ResponseEntity<Booking>(new Booking(), HttpStatus.NOT_FOUND))));
	}
	
	@GetMapping("/student/{studentName}")
	public Flux<Booking> getBookByStudentName(@RequestHeader(name = "Authorization") String token, @PathVariable("studentName") String studentName) {
		return tokenCheckService.checkAuth(token)
				.onErrorMap(ErrorHandler::handleError)
				.flatMapMany(mono -> bookingService.findByStudentName(studentName));
	}
	
	@GetMapping("/student/next/{studentName}")
	public Mono<Booking> getNextBookByStudentName(@RequestHeader(name = "Authorization") String token, @PathVariable("studentName") String studentName) {
		return tokenCheckService.checkAuth(token)
				.onErrorMap(ErrorHandler::handleError)
				.flatMap(mono -> bookingService.getNextBookByStudentName(studentName))
				.switchIfEmpty(Mono.just(new Booking()));
	}
	
	@GetMapping("/student/count/{studentName}")
	public Mono<Long> countBooksByStudentName(@RequestHeader(name = "Authorization") String token, @PathVariable("studentName") String studentName) {
		return tokenCheckService.checkAuth(token)
				.onErrorMap(ErrorHandler::handleError)
				.flatMap(mono -> bookingService.countByStudentName(studentName));
	}
	
	@GetMapping("/teacher/{teacherName}")
	public Flux<Booking> getBookByTeacherName(@RequestHeader(name = "Authorization") String token, @PathVariable("teacherName") String teacherName) {
		return tokenCheckService.checkAuth(token)
				.onErrorMap(ErrorHandler::handleError)
				.flatMapMany(mono -> bookingService.findByTeacherName(teacherName));
	}
	
	@GetMapping("/teacher/next/{teacherName}")
	public Mono<Booking> getNextBookByTeacherName(@RequestHeader(name = "Authorization") String token, @PathVariable("teacherName") String teacherName) {
		return tokenCheckService.checkAuth(token)
				.onErrorMap(ErrorHandler::handleError)
				.flatMap(mono -> bookingService.getNextBookByTeacherName(teacherName))
				.switchIfEmpty(Mono.just(new Booking()));
	}

	@GetMapping("/teacher/count/{teacherName}")
	public Mono<Long> countBooksByTeacherName(@RequestHeader(name = "Authorization") String token, @PathVariable("teacherName") String teacherName) {
		return tokenCheckService.checkAuth(token)
				.onErrorMap(ErrorHandler::handleError)
				.flatMap(mono -> bookingService.countByTeacherName(teacherName));
	}
	
	@PostMapping("/new")
	public Mono<ResponseEntity<Booking>> setBooking(@RequestHeader(name = "Authorization") String token, @RequestBody Booking booking) {
		return tokenCheckService.checkAuth(token)
				.onErrorMap(ErrorHandler::handleError)
				.flatMap(mono -> remoteUsersService.userExists(token, booking.getStudentName()))
				.flatMap(mono -> remoteUsersService.userExists(token, booking.getTeacherName()))
				.flatMap(mono -> bookingService.saveBooking(booking))
				.flatMap(mono -> mailService.sendMail(booking))
				.map(reserva -> new ResponseEntity<Booking>(booking, HttpStatus.CREATED));
	}
	
	@PutMapping("/update")
	public Mono<ResponseEntity<String>> updateBooking(@RequestHeader(name = "Authorization") String token, @RequestBody Booking booking) {
		return tokenCheckService.checkAuth(token)
				.onErrorMap(ErrorHandler::handleError)
				.flatMap(mono -> bookingService.update(booking.getId(), booking))
				.flatMap(mono -> mailService.sendMail(booking))
				.thenReturn(new ResponseEntity<String>("Reserva actualizada", HttpStatus.OK));
	}
	
	@DeleteMapping("/delete/{id}")
	public Mono<ResponseEntity<String>> deleteBooking(@RequestHeader(name = "Authorization") String token, @PathVariable Long id) {
		return tokenCheckService.checkAuth(token)
				.onErrorMap(ErrorHandler::handleError)
				.flatMap(mono -> bookingService.findById(id))
				.switchIfEmpty(Mono.error(new NotFoundBookingException()))
				.flatMap(mono -> mailService.sendMailDelete(mono))
				.flatMap(mono -> bookingService.deleteById(id))
				.thenReturn(new ResponseEntity<String>("Reserva con id: " + id + " eliminada.", HttpStatus.OK));
	}
	
}
