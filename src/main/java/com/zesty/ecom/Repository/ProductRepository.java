package com.zesty.ecom.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.zesty.ecom.Model.Category;
import com.zesty.ecom.Model.Product;

public interface ProductRepository extends JpaRepository<Product, Integer>{

	Page<Product> findByCategory(Category category, Pageable pageable);

}