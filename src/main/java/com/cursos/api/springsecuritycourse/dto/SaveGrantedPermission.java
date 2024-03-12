package com.cursos.api.springsecuritycourse.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class SaveGrantedPermission implements Serializable {
	
    @NotBlank
    private String role;
    @NotBlank
    private String operation;

}
