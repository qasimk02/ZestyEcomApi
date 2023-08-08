package com.zesty.ecom.Mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.zesty.ecom.Model.Order;
import com.zesty.ecom.Payload.Dto.OrderDto;

public class OrderMapper {

	@Autowired
	private ModelMapper modelMapper;
	
	public OrderDto mapToDto(Order order) {
		return modelMapper.map(order,OrderDto.class);
	}
	
	public Order mapToEntity(OrderDto orderDto) {
		return modelMapper.map(orderDto, Order.class);
	}
	
}
