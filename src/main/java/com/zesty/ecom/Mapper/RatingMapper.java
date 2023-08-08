package com.zesty.ecom.Mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.zesty.ecom.Model.Rating;
import com.zesty.ecom.Payload.Dto.RatingDto;

public class RatingMapper {
	
	@Autowired
	private ModelMapper modelMapper;
	
	public RatingDto mapToDto(Rating rating) {
		return modelMapper.map(rating,RatingDto.class);
	}
	
	public Rating mapToEntity(RatingDto ratingDto) {
		return modelMapper.map(ratingDto, Rating.class);
	}
	
}
