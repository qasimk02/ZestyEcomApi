package com.zesty.ecom.Service;

import java.util.List;

import com.zesty.ecom.Payload.Dto.ReviewDto;

public interface ReviewService {

	//get
	ReviewDto getReviewById(Long id);
	ReviewDto getReviewByUserAndProduct(Long uId,Long pId);
	List<ReviewDto> getAllReviewByProduct(Long pId);
	
	//add
	ReviewDto addReview(ReviewDto review, Long pId, String username);
	
	//update
	ReviewDto updateReview(ReviewDto review);
	
	//delete
	ReviewDto deleteReview(Long id);
}
