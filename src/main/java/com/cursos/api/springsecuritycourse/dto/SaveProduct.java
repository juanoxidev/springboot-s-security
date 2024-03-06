package com.cursos.api.springsecuritycourse.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.validation.constraints.*;

import lombok.Data;

@Data
public class SaveProduct implements Serializable{

	@NotBlank(message = "El nombre no puede ser nulo o estar vacio")
	private String name;
	
	@DecimalMin(value = "0.01", message = "El precio no puede ser menor o igual a 0.00")
	private BigDecimal price;
	
	@Min(value = 1)
	private Long categoryId;
}
