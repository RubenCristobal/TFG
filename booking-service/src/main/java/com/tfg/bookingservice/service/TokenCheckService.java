package com.tfg.bookingservice.service;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.tfg.bookingservice.exceptions.NotAuthorizedException;
import com.tfg.bookingservice.exceptions.UserServiceInternalServerError;

import reactor.core.publisher.Mono;

@Service
public class TokenCheckService {
	
	@Qualifier("usersWebClient")
    @Autowired
    WebClient webClient;

    public Mono<Boolean> checkAuth(String token) {
        return webClient
        		.get()
        		.uri("/user/checkToken")
        		.headers(h -> h.setBearerAuth(token))
        		.retrieve()
    			.onStatus(status -> status.value() == HttpStatus.UNAUTHORIZED.value(),
					error -> Mono.error(new NotAuthorizedException()))
    			.onStatus(HttpStatus::is5xxServerError,
    				error -> Mono.error(new UserServiceInternalServerError()))
        		.bodyToMono(Boolean.class)
        		.timeout(Duration.ofSeconds(3));
    }

}
