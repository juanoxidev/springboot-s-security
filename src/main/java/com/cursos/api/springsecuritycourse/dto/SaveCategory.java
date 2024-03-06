package com.cursos.api.springsecuritycourse.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class SaveCategory implements Serializable{

	@NotBlank(message = "El nombre no puede estar vacio o ser nulo")
	private String name;
	
}
