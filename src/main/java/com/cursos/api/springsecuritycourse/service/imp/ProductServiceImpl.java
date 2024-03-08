package com.cursos.api.springsecuritycourse.service.imp;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cursos.api.springsecuritycourse.dto.SaveProduct;
import com.cursos.api.springsecuritycourse.entity.Category;
import com.cursos.api.springsecuritycourse.entity.Product;
import com.cursos.api.springsecuritycourse.exception.ObjectNotFoundException;
import com.cursos.api.springsecuritycourse.repository.ProductRepository;
import com.cursos.api.springsecuritycourse.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService  {

	@Autowired
	private ProductRepository productRepository;
	

	@Override
	public Product createOne(SaveProduct saveProduct) {
		
		Product product = new Product();
		product.setName(saveProduct.getName());
		product.setPrice(saveProduct.getPrice());
		product.setStatus(Product.ProductStatus.ENABLED);
		
		
		Category category = new Category();
		category.setId(saveProduct.getCategoryId());
		product.setCategory(category);
		
	/* 
	 * No configuramos ningun tipo de actualizacion en cascada dentro de producto, 
	 * no es necesario qu eobtenamos desde bd la categoriaa para settearle al producto,
	 * solo le seteamos el id, si el id no existe: jpa va a dar un error y nos va a lanzar ese error, 
	 * no se va a guardar por una validacion de integridad de datos CONSTRAINT porque la categoria no existiria.
	 * 
	*/
		
		return productRepository.save(product);
	}

	@Override
	public Optional<Product> findById(Long productId) {
		return productRepository.findById(productId);
	}

	@Override
	public Page<Product> findAll(Pageable pageable) {
		// TODO Auto-generated method stub
		return productRepository.findAll(pageable);
	}

	@Override
	public Product disableOneById(Long productId) {
		Product product = productRepository.findById(productId).orElseThrow(()-> new ObjectNotFoundException("Product not found with id " + productId));
		product.setStatus(Product.ProductStatus.DISABLED);
		return productRepository.save(product);
	}

	@Override
	public Product updateOneById(Long productId, SaveProduct saveProduct) {
		
		Product product = productRepository.findById(productId).orElseThrow(()-> new ObjectNotFoundException("Product not found with id " + productId));
		product.setName(saveProduct.getName());
		product.setPrice(saveProduct.getPrice());
		Category category = new Category();
		category.setId(saveProduct.getCategoryId());
		product.setCategory(category);
		
	/* 
	 * No configuramos ningun tipo de actualizacion en cascada dentro de producto, 
	 * no es necesario qu eobtenamos desde bd la categoriaa para settearle al producto,
	 * solo le seteamos el id, si el id no existe: jpa va a dar un error y nos va a lanzar ese error, 
	 * no se va a guardar por una validacion de integridad de datos CONSTRAINT porque la categoria no existiria.
	 * 
	*/
		
		return productRepository.save(product);
	}

}
