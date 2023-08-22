package com.zesty.ecom.Payload.Dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RatingDto {

	private Long ratingId;
	private Integer star;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	
}
