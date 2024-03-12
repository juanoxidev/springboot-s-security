package com.cursos.api.springsecuritycourse.entity.security;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;

@Data
@Entity
public class GrantedPermission {
	
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "role_id")
	private Role role;
	
	
	@ManyToOne
	@JoinColumn(name = "operation_id")
	private Operation operation;
	
	
}
