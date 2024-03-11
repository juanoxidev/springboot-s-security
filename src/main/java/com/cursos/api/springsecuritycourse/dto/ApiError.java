package com.cursos.api.springsecuritycourse.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class ApiError implements Serializable {

	private String backendMessage;
	
	private String message;
	
	private String url;
	
	private String method;
	
	@JsonFormat(pattern ="dd/MM/yyyy HH:mm:ss")
//	@JsonFormat(pattern ="yyyy/MM/dd HH:mm:ss")
	private LocalDateTime timestamp;
	
	private List<String> submessages;
	
	
}
