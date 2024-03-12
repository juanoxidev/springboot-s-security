package com.cursos.api.springsecuritycourse.entity.security;

import javax.persistence.*;

import lombok.Data;

@Data
@Entity
public class Operation {

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;
	
	private String name; // FIND_ALL_PRODUCTS
	
	private String path; // path de operacion
	
	private String httpMethod;
	
	private boolean permitAll;
	
	@ManyToOne
	@JoinColumn(name= "module_id")
	private Module module;
}
