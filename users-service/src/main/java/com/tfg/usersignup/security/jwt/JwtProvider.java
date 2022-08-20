package com.tfg.usersignup.security.jwt;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.tfg.usersignup.model.entity.UserSecurity;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtProvider {
	
	@Value("${jwt.secret}")
	private String secret;
	
	@Value("${jwt.expiration}")
	private int expiration;
	
	
	public String generateToken(Authentication authentication) {
		UserSecurity userSecurity = (UserSecurity) authentication.getPrincipal();
		return Jwts.builder().setSubject(userSecurity.getUsername())
				.setIssuedAt(new Date())
				.setExpiration(new Date(new Date().getTime() + expiration * 1000))
				.signWith(SignatureAlgorithm.HS512, secret)
				.compact();
	}
	
	public String getUserNameFromToken(String token) {
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
	}
	
	public boolean validateToken(String token) {
		try {
			Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
			return true;
		} catch (MalformedJwtException e) {
			log.error("[JwtProvider] Malformed token");
		} catch (UnsupportedJwtException e) {
			log.error("[JwtProvider] Not supported token");
		} catch (ExpiredJwtException e) {
			log.error("[JwtProvider] Expired token");
		} catch (IllegalArgumentException e) {
			log.error("[JwtProvider] Empty token");
		} catch (SignatureException e) {
			log.error("[JwtProvider] Sign error");
		}
		return false;
	}

}
