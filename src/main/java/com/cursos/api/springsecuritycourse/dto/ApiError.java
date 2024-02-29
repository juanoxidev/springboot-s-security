package com.cursos.api.springsecuritycourse.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class ApiError implements Serializable {

	private String backendMessage;
	
	private String message;
	
	private String url;
	
	private String method;
	
	private LocalDate timestamp;
	
	private List<String> submessages;
	
}
