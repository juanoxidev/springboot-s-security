package com.cursos.api.springsecuritycourse.service;


import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cursos.api.springsecuritycourse.dto.SaveCategory;
import com.cursos.api.springsecuritycourse.entity.Category;


public interface CategoryService {

	Category createOne( SaveCategory saveCategory);

	Category updateOneById(Long categoryId, SaveCategory saveCategory);

	Category disableOneById(Long categoryId);

	Optional<Category> findById(Long categoryId);

	Page<Category> findAll(Pageable pageable);

}
