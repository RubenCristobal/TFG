package com.tfg.bookingservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.client.WebClient;

@EnableWebFlux
@Configuration
public class WebClientConfig implements WebFluxConfigurer {
	
	private final String USERS_API = "http://users-service:8091";
	
//	private final String USERS_API = "http://localhost:8091";
	
	private final String NOTIFICATIONS_API = "http://notification-service:8093";
	
//	private final String NOTIFICATIONS_API = "http://localhost:8093";
	
    @Bean(name="usersWebClient")
    public WebClient usersWebClient() {
        return WebClient.builder()
                .baseUrl(USERS_API)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
    
    @Bean(name="notificationsWebClient")
    public WebClient notificationServiceWebClient() {
    	return WebClient.builder()
    			.baseUrl(NOTIFICATIONS_API)
    			.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
    			.defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
    			.build();
    }
    
    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {
        corsRegistry.addMapping("/**")
          .allowedOrigins("*")
          .allowedMethods("*");
    }
	


}