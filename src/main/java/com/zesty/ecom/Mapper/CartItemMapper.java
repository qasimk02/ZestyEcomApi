package com.zesty.ecom.Mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.zesty.ecom.Model.CartItem;
import com.zesty.ecom.Payload.Dto.CartItemDto;

public class CartItemMapper {
	
	@Autowired
	private ModelMapper modelMapper;
	
	public CartItemDto mapToDto(CartItem cartItem) {
		return modelMapper.map(cartItem,CartItemDto.class);
	}
	
	public CartItem mapToEntity(CartItemDto cartItemDto) {
		return modelMapper.map(cartItemDto, CartItem.class);
	}

}
