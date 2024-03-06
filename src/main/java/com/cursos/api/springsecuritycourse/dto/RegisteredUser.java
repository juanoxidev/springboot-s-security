package com.cursos.api.springsecuritycourse.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class RegisteredUser implements Serializable {

	private Long id;
	private String username;
	private String name;
	private String role;
	/*
	 * cuando nos regisdtramos automaticamente nos genera el token para que no tengamos q hacer login despues
	 * 
	 */
	private String jwt;
}
