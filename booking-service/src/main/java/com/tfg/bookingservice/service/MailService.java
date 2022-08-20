package com.tfg.bookingservice.service;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.tfg.bookingservice.model.entity.Booking;
import com.tfg.bookingservice.model.entity.Calendar;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class MailService {
	
	@Qualifier("notificationsWebClient")
    @Autowired
    WebClient webclient;

    public Mono<Boolean> sendMail(Booking booking) {
        return webclient
        		.post()
        		.uri("/mail/booking")
        		.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        		.body(Mono.just(booking), Booking.class)
        		.retrieve()
    			.onStatus(HttpStatus::is5xxServerError,
        				error -> Mono.error(new Exception()))
        		.bodyToMono(Boolean.class)
        		.timeout(Duration.ofSeconds(3))
        		.onErrorResume(e -> Mono.fromRunnable(() -> log.error("El servicio de notificaciones no está disponible.")))
        		.switchIfEmpty(Mono.just(Boolean.TRUE));
    }
    
    public Mono<Boolean> sendMailDelete(Booking booking) {
    	return webclient
    			.post()
    			.uri("/mail/booking/delete")
    			.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
    			.body(Mono.just(booking), Booking.class)
    			.retrieve()
    			.onStatus(HttpStatus::is5xxServerError,
    					error -> Mono.error(new Exception()))
    			.bodyToMono(Boolean.class)
    			.timeout(Duration.ofSeconds(3))
    			.onErrorResume(e -> Mono.fromRunnable(() -> log.error("El servicio de notificaciones no está disponible.")))
    			.switchIfEmpty(Mono.just(Boolean.TRUE));
    }
    
    public Mono<Boolean> sendMail(Calendar calendar) {
    	return webclient
    			.post()
    			.uri("/mail/calendar")
    			.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
    			.body(Mono.just(calendar), Booking.class)
    			.retrieve()
    			.onStatus(HttpStatus::is5xxServerError,
        				error -> Mono.error(new Exception()))
    			.bodyToMono(Boolean.class)
    			.timeout(Duration.ofSeconds(3))
        		.onErrorResume(e -> Mono.fromRunnable(() -> log.error("El servicio de notificaciones no está disponible.")))
        		.switchIfEmpty(Mono.just(Boolean.TRUE));
    }

}
