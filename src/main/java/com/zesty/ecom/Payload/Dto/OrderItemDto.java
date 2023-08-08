package com.zesty.ecom.Payload.Dto;

import java.time.LocalDateTime;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemDto {
	
	private Long orderItemId;
	private Integer quantity;
	private Double totalPrice;
	private Double totalDiscountedPrice;
	private LocalDateTime orderDate;
	private LocalDateTime deliveryDate;
	
	private ProductDto product;
	
}
