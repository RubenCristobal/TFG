package com.tfg.notificationservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tfg.notificationservice.model.dto.Booking;
import com.tfg.notificationservice.model.dto.Calendar;
import com.tfg.notificationservice.service.MailService;

@RestController
@RequestMapping("/mail")
@CrossOrigin
public class MailController {
	
	@Autowired
	MailService mailService;
	
	@PostMapping("/booking")
	public Boolean sendMailBooking(@RequestBody Booking booking) {
		mailService.sendMailBooking(booking);
		return Boolean.TRUE;
	}
	
	@PostMapping("/booking/delete")
	public Boolean sendMailBookingDelete(@RequestBody Booking booking) {
		mailService.sendMailBookingDelete(booking);
		return Boolean.TRUE;
	}
	
	@PostMapping("/calendar")
	public Boolean sendMailCalendar(@RequestBody Calendar calendar) {
		mailService.sendMailCalendar(calendar);
		return Boolean.TRUE;
	}
	

}
