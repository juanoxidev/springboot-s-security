package com.cursos.api.springsecuritycourse.dto.auth;

import java.io.Serializable;

import lombok.Data;

@Data
public class AuthenticationResponse implements Serializable {

	private String jwt;
	
	private String role;
}
