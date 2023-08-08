package com.zesty.ecom.Payload.Dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReviewDto {

	private Long reviewId;
	private String subject;
	private String content;
	private String imageName;
	private RatingDto rating;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private UserDto user;
	
}
