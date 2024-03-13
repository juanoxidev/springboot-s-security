package com.cursos.api.springsecuritycourse.controller;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cursos.api.springsecuritycourse.dto.SaveProduct;
import com.cursos.api.springsecuritycourse.entity.Product;
import com.cursos.api.springsecuritycourse.service.ProductService;

@CrossOrigin
@RestController
@RequestMapping("/products")
public class ProductController {

	@Autowired
	private ProductService productService;

	@PreAuthorize("hasAuthority('READ_ALL_PRODUCTS')")
	@GetMapping 
	public ResponseEntity<Page<Product>> findAll(Pageable pageable) {
		Page<Product> productsPage = productService.findAll(pageable);
		if(productsPage.hasContent()) {
			return ResponseEntity.ok(productsPage);
		}

		return ResponseEntity.notFound().build();
	}

	@PreAuthorize("hasAuthority('READ_ONE_PRODUCT')")
	@GetMapping("/{productId}")
	public ResponseEntity<Product> findOneById(@PathVariable Long productId) {
		Optional<Product> productsPage = productService.findById(productId);
		if(productsPage.isPresent()) {
			return ResponseEntity.ok(productsPage.get());
		}

		return ResponseEntity.notFound().build();
	}

	@PreAuthorize("hasAuthority('CREATE_ONE_PRODUCT')")
	@PostMapping
	public ResponseEntity<Product> createOne(@RequestBody @Valid SaveProduct saveProduct) {
		Product product = productService.createOne(saveProduct);
		return ResponseEntity.status(HttpStatus.CREATED).body(product);
	}

	@PreAuthorize("hasAuthority('UPDATE_ONE_PRODUCT')")
	@PutMapping("/{productId}")
	public ResponseEntity<Product> updateOneById( @PathVariable Long productId, @RequestBody @Valid SaveProduct saveProduct) {
		Product product = productService.updateOneById(productId, saveProduct);
		return ResponseEntity.ok(product);
	}
	@PreAuthorize("hasAuthority('DISABLE_ONE_PRODUCT')")
	@PutMapping("/{productId}/disabled")
	public ResponseEntity<Product> disableOneById( @PathVariable Long productId) {
		Product product = productService.disableOneById(productId);
		return ResponseEntity.ok(product);
	}

}
