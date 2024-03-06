package com.cursos.api.springsecuritycourse.service;


import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cursos.api.springsecuritycourse.dto.SaveProduct;
import com.cursos.api.springsecuritycourse.entity.Product;



public interface ProductService {

	Product updateOneById(Long productId, SaveProduct saveProduct);

	Product createOne(SaveProduct saveProduct);

	Optional<Product> findById(Long productId);

	Page<Product> findAll(Pageable pageable);

	Product disableOneById(Long categoryId);

}
