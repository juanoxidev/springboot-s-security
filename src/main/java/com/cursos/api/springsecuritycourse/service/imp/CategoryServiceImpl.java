package com.cursos.api.springsecuritycourse.service.imp;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cursos.api.springsecuritycourse.dto.SaveCategory;
import com.cursos.api.springsecuritycourse.dto.SaveProduct;
import com.cursos.api.springsecuritycourse.entity.Category;
import com.cursos.api.springsecuritycourse.entity.Category.CategoryStatus;
import com.cursos.api.springsecuritycourse.entity.Product;
import com.cursos.api.springsecuritycourse.exception.ObjectNotFoundException;
import com.cursos.api.springsecuritycourse.repository.CategoryRepository;
import com.cursos.api.springsecuritycourse.repository.ProductRepository;
import com.cursos.api.springsecuritycourse.service.CategoryService;
import com.cursos.api.springsecuritycourse.service.ProductService;

@Service
public class CategoryServiceImpl implements CategoryService  {

	@Autowired
	private CategoryRepository categoryRepository;

	@Override
	public Category createOne(SaveCategory saveCategory) {
		Category cat = new Category();
		cat.setName(saveCategory.getName());
		cat.setStatus(Category.CategoryStatus.ENABLED);
		return categoryRepository.save(cat);
	}

	@Override
	public Category updateOneById(Long categoryId, SaveCategory saveCategory) {
		Category catFromDB = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ObjectNotFoundException("No se encontro la categoria con el id: " + categoryId));
		catFromDB.setName(saveCategory.getName());
		return categoryRepository.save(catFromDB);
	}

	@Override
	public Category disableOneById(Long categoryId) {
		Category catFromDB = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ObjectNotFoundException("No se encontro la categoria con el id: " + categoryId));
		catFromDB.setStatus(Category.CategoryStatus.DISABLED);
		return categoryRepository.save(catFromDB);
	}

	@Override
	public Optional<Category> findById(Long categoryId) {
		return categoryRepository.findById(categoryId);
	}

	@Override
	public Page<Category> findAll(Pageable pageable) {
		return categoryRepository.findAll(pageable);
	}
	

	

}
