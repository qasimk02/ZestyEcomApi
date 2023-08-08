package com.zesty.ecom.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.zesty.ecom.Model.Rating;

public interface RatingRepository extends JpaRepository<Rating, Long>{
	
	@Query("SELECT r.star, COUNT(r) FROM Rating r WHERE r.product.productId = :pId GROUP BY r.star")
	List<Object[]> getTotalRatingsByValue(Long pId);

	@Query("SELECT COUNT(r) FROM Rating r WHERE r.product.productId = :pId")
	Long getTotalRatings(Long pId);
	
	@Query("SELECT AVG(r.star) FROM Rating r WHERE r.product.productId = :pId")
	Double getAverageRating(Long pId);

	
}
