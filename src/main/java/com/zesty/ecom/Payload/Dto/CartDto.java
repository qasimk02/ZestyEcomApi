package com.zesty.ecom.Payload.Dto;

import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartDto {

	private Long cartId;
	
	private Double totalPrice;

	private Double totalDiscountedPrice;

	private Integer totalCartItems;
	
	private Set<CartItemDto> cartItems = new HashSet<>();
//	private User user;
}
