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
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	
	private BigDecimal price;
	
	@Enumerated(EnumType.STRING)
	private ProductStatus status;
	// muchos productos van a tener una categoria
	@ManyToOne
	// como debe de crear el campo category adentro de la tabla productos
	@JoinColumn(name = "category_id")
	private Category category;
	
	public static enum ProductStatus {
		ENABLED, DISABLED;
	}
	
}
