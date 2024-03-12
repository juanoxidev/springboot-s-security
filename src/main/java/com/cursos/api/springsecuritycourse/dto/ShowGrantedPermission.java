package com.cursos.api.springsecuritycourse.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class ShowGrantedPermission implements Serializable {

	private Long id;
	
	private String role;
	
	private String operation;
	
	private String httpMethod;
	
	private String module;
	
}
