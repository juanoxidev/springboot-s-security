package com.cursos.api.springsecuritycourse.entity.security;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;

@Data
@Entity
public class JwtToken {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 2048)
	private String token;

	private Date expiration;

	private boolean isValid;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

}
