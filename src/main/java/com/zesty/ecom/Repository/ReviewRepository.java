package com.zesty.ecom.Repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zesty.ecom.Model.Product;
import com.zesty.ecom.Model.Review;

public interface ReviewRepository extends JpaRepository<Review, Long>{

	Optional<List<Review>> findByProduct(Product p);

}
