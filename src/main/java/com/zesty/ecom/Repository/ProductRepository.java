package com.zesty.ecom.Repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import com.zesty.ecom.Model.Category;
import com.zesty.ecom.Model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

	Optional<Product> findByProductId(Long id);

	Page<Product> findByCategory(Category category, Pageable pageable);

	Page<Product> findAll(Specification<Product> spec, Pageable pageable);

}