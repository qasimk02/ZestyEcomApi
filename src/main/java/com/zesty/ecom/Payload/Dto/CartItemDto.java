package com.zesty.ecom.Payload.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDto {

	private Long cartItemId;
	private Integer quantity;
	private String size;
	private Double totalPrice;
	private Double totalDiscountedPrice;
//	private Cart cart; not needed here
	private ProductDto product;
	
}
