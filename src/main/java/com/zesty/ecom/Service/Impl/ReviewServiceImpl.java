package com.zesty.ecom.Service.Impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zesty.ecom.Exception.CustomException;
import com.zesty.ecom.Exception.ResourceNotFoundException;
import com.zesty.ecom.Mapper.ReviewMapper;
import com.zesty.ecom.Model.Product;
import com.zesty.ecom.Model.Review;
import com.zesty.ecom.Model.User;
import com.zesty.ecom.Payload.Dto.ReviewDto;
import com.zesty.ecom.Repository.ProductRepository;
import com.zesty.ecom.Repository.ReviewRepository;
import com.zesty.ecom.Repository.UserRepository;
import com.zesty.ecom.Service.ReviewService;

@Service
public class ReviewServiceImpl implements ReviewService{

	@Autowired
	private ReviewRepository reviewRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ReviewMapper reviewMapper;

	@Override
	public ReviewDto getReviewById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ReviewDto getReviewByUserAndProduct(Long uId, Long pId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ReviewDto> getAllReviewByProduct(Long pId) {
		Product p = this.productRepository.findById(pId).orElseThrow(()->new ResourceNotFoundException("Product", "Id", Long.toString(pId)));
		List<Review> allReviews = this.reviewRepository.findByProduct(p).orElseThrow(()->new CustomException("There is no review for this product"));
		List<ReviewDto> allReviewsDto = allReviews.stream().map((r)->this.reviewMapper.mapToDto(r)).collect(Collectors.toList());
		return allReviewsDto;
	}

	@Override
	public ReviewDto addReview(ReviewDto reviewDto,Long pId,String username) {
		User u = this.userRepository.findByEmail(username).orElseThrow(()->new ResourceNotFoundException("User", "Id", username));
		Product p = this.productRepository.findById(pId).orElseThrow(()->new ResourceNotFoundException("Product", "Id", Long.toString(pId)));
		Review review = this.reviewMapper.mapToEntity(reviewDto);

		//setting product and user in rating
		review.getRating().setUser(u);
		review.getRating().setProduct(p);
		//setting product and user in review
		review.setUser(u);
		review.setProduct(p);
		System.out.println("review service "+review);
		Review savedReview = this.reviewRepository.save(review);
		
		return this.reviewMapper.mapToDto(savedReview);
	}

	@Override
	public ReviewDto updateReview(ReviewDto review) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ReviewDto deleteReview(Long id) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
