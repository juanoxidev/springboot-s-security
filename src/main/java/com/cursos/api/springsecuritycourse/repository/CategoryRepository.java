package com.cursos.api.springsecuritycourse.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cursos.api.springsecuritycourse.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
