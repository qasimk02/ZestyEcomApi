package com.zesty.ecom.Payload.Dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductImageDto {
	
	private Long imageId;
	
	private String imageName;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

}
