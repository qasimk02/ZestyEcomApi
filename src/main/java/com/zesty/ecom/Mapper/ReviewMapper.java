package com.zesty.ecom.Mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.zesty.ecom.Model.Review;
import com.zesty.ecom.Payload.Dto.ReviewDto;

public class ReviewMapper {

	@Autowired
	private ModelMapper modelMapper;
	
	public ReviewDto mapToDto(Review review) {
		return modelMapper.map(review,ReviewDto.class);
	}
	
	public Review mapToEntity(ReviewDto reviewDto) {
		return modelMapper.map(reviewDto, Review.class);
	}
	
}
