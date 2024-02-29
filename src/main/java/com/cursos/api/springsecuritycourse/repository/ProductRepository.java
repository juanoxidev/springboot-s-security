package com.cursos.api.springsecuritycourse.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cursos.api.springsecuritycourse.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long>{

	
}
