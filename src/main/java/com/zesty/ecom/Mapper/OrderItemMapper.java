package com.zesty.ecom.Mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.zesty.ecom.Model.Order;
import com.zesty.ecom.Model.OrderItem;
import com.zesty.ecom.Payload.Dto.OrderItemDto;

public class OrderItemMapper {
	
	@Autowired
	private ModelMapper modelMapper;
	
	public OrderItemDto mapToDto(OrderItem orderItem) {
		return modelMapper.map(orderItem,OrderItemDto.class);
	}
	
	public Order mapToEntity(OrderItemDto orderItemDto) {
		return modelMapper.map(orderItemDto, Order.class);
	}
	
}
