package com.cursos.api.springsecuritycourse.entity;

import java.math.BigDecimal;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	@Enumerated(EnumType.STRING)
	private CategoryStatus status;
	
	public static enum CategoryStatus {
		ENABLED, DISABLED;
	}
	
}
