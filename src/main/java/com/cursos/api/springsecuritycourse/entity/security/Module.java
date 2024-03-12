package com.cursos.api.springsecuritycourse.entity.security;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;
@Data
@Entity
public class Module {

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;
	
	private String name; // PRODUCT, CATEGORY, AUTH, COSTUMER
	
	private String basePath; // /products
	
	
}
