package com.cursos.api.springsecuritycourse.service.auth;


import java.sql.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;


@Service
public class JwtService {
	// @value: son variables de entorno hay que ir a ver en el application.properties que valores tienen
	@Value("${security.jwt.expiration-in-minutes}")
	private Long EXPIRATION_IN_MINUTES;
	
	@Value("${security.jwt.secret-key}")
	private String SECRET_KEY;

	public String generateToken(UserDetails user, Map <String, Object> extraClaims) {
		Date issuedAt = new Date(System.currentTimeMillis());
		Date expiration = new Date((EXPIRATION_IN_MINUTES * 60 * 1000) + issuedAt.getTime());

		String jwt = Jwts.builder()
					.header()
						.type("JWT")
						.and()	
					.subject(user.getUsername())
					.issuedAt(issuedAt)
					.expiration(expiration)
					.claims(extraClaims)
					.signWith(generateKey(), Jwts.SIG.HS256)
					.compact();
		return jwt;
	}

	//toma los bytes de la palabra secreta y los hashea
	private SecretKey generateKey() {
		
		byte [] key = Decoders.BASE64.decode(SECRET_KEY);
		System.out.println(new String(key));
//		byte [] key = SECRET_KEY.getBytes();
		return Keys.hmacShaKeyFor(key);
	}

	/**
	 * Extraemos todos lso claims del payload y devolvemos el subject
	 * @param String jwt
	 * @return String username
	 */
	/*
	 * Validamos el token tratando de extraer el username, si tiene 
	 * un formato equivocado lanza excepcion, si esta firmaod con una clave dif 
	 * a la enviada originalmente o si el token ha expirado tamb da error
	 */
	public String extractUsername(String jwt) {
		return extracAllClaims(jwt).getSubject();
		
	}

	/*
	 *  jwts.parser() Pasa todo a json, valida la firma, extrae el payload y lo pasa a un objeto
	 *  del tipo Claims
	 */
	private Claims extracAllClaims(String jwt) {
		
		return Jwts.parser().verifyWith(generateKey()).build()
				.parseSignedClaims(jwt).getPayload();
	}

}
