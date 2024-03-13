package com.cursos.api.springsecuritycourse.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LogoutResponse implements Serializable {

	private String message;

}
