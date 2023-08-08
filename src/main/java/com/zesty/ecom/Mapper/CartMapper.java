package com.zesty.ecom.Mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.zesty.ecom.Model.Cart;
import com.zesty.ecom.Payload.Dto.CartDto;

public class CartMapper {

	@Autowired
	private ModelMapper modelMapper;
	
	public CartDto mapToDto(Cart cart) {
		return modelMapper.map(cart,CartDto.class);
	}
	
	public Cart mapToEntity(CartDto cartDto) {
		return modelMapper.map(cartDto, Cart.class);
	}
	
}
