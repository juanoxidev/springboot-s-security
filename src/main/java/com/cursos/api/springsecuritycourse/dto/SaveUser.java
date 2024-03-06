package com.cursos.api.springsecuritycourse.dto;

import java.io.Serializable;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SaveUser implements Serializable {

	@Size(min = 4)
	private String name;
	private String username;
	@Size(min = 8)
	private String password;
	@Size(min = 8)
	private String repeatedPassword;
}
