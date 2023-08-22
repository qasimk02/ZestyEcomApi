package com.zesty.ecom.Repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zesty.ecom.Model.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer>{

	boolean existsByTitle(String title);

	Optional<Category> findByTitle(String cat);
	
}
