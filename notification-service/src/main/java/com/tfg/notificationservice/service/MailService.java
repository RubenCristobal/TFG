package com.tfg.notificationservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.tfg.notificationservice.model.dto.Booking;
import com.tfg.notificationservice.model.dto.Calendar;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MailService {

	@Autowired
	private JavaMailSender mailSender;

	private String subject;

	private String body;

	@Value("${spring.mail.username}")
	private String sender;

	@Value("${receiver}")
	private String receiver;
	
	private SimpleMailMessage mail = new SimpleMailMessage();

	@Async
	public void sendMailBooking(Booking booking) {
 
    	this.subject = "Reserva de tutoría del alumno " + booking.getStudentName() + " para el profesor " + booking.getTeacherName();
    	
    	this.body = "Detalles de la reserva:\n\n" + "\t- Alumno: " + booking.getStudentName() + "\n"
    				+ "\t- Profesor: " + booking.getTeacherName() + "\n"
    				+ "\t- Día: " + booking.getDate() + "\n"
    				+ "\t- Hora: " + booking.getTime() + "\n"
    				+ "\t- Formato: " + booking.getFormat()+ "\n"
    				+ "\t- Descripción: " + booking.getDescription()+ "\n";
    				
    	mail.setFrom(sender);
    	mail.setTo(receiver);
    	mail.setSubject(this.subject);
    	mail.setText(this.body);

        mailSender.send(mail);
        log.info("Email de reserva: " + booking.getStudentName() + " enviado con éxito");
    }
	
	@Async
	public void sendMailBookingDelete(Booking booking) {
		
    	this.subject = "Reserva de tutoría ELIMINADA del alumno " + booking.getStudentName() + " para el profesor " + booking.getTeacherName();
    	
    	this.body = "Detalles de la reserva ELIMINADA:\n\n" + "\t- Alumno: " + booking.getStudentName() + "\n"
    				+ "\t- Profesor: " + booking.getTeacherName() + "\n"
    				+ "\t- Día: " + booking.getDate() + "\n"
    				+ "\t- Hora: " + booking.getTime() + "\n"
    				+ "\t- Formato: " + booking.getFormat()+ "\n"
    				+ "\t- Descripción: " + booking.getDescription()+ "\n";
		
		mail.setFrom(sender);
		mail.setTo(receiver);
		mail.setSubject(this.subject);
		mail.setText(this.body);
		
		mailSender.send(mail);
        log.info("Email de reserva ELIMINADA: " + booking.getStudentName() + " enviado con éxito");
	}

	@Async
	public void sendMailCalendar(Calendar calendar) {
 
    	this.subject = "Calendario del profesor " + calendar.getTeacherName() + " establecido.";
    	
    	this.body = "Detalles del calendario:\n\n" + "\t- Profesor: " + calendar.getTeacherName() + "\n"
    				+ "\t- Lunes: " + calendar.getLunes() + "\n"
    				+ "\t- Mates: " + calendar.getMartes() + "\n"
    				+ "\t- Miercoles: " + calendar.getMiercoles() + "\n"
    				+ "\t- Jueves: " + calendar.getJueves()+ "\n"
    				+ "\t- Viernes: " + calendar.getViernes()+ "\n";
    				
    	mail.setFrom(sender);
    	mail.setTo(receiver);
    	mail.setSubject(this.subject);
    	mail.setText(this.body);

        mailSender.send(mail);
        log.info("Email de calendario " + calendar.getTeacherName() + " enviado con éxito");
    }

}
