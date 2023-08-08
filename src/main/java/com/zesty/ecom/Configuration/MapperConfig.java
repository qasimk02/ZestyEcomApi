package com.zesty.ecom.Configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zesty.ecom.Mapper.AddressMapper;
import com.zesty.ecom.Mapper.CartItemMapper;
import com.zesty.ecom.Mapper.CartMapper;
import com.zesty.ecom.Mapper.CategoryMapper;
import com.zesty.ecom.Mapper.OrderItemMapper;
import com.zesty.ecom.Mapper.OrderMapper;
import com.zesty.ecom.Mapper.ProductMapper;
import com.zesty.ecom.Mapper.RatingMapper;
import com.zesty.ecom.Mapper.ReviewMapper;
import com.zesty.ecom.Mapper.UserMapper;

@Configuration
public class MapperConfig {
	
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
	
	@Bean 
	public CategoryMapper categoryMapper() {
		return new CategoryMapper();
	}
	
	@Bean
	public ProductMapper productMapper() {
		return new ProductMapper();
	}
	
	@Bean
	public UserMapper userMapper() {
		return new UserMapper();
	}
	
	@Bean
	public CartMapper cartMapper() {
		return new CartMapper();
	}
	@Bean
	public CartItemMapper cartItemMapper() {
		return new CartItemMapper();
	}
	
	@Bean
	public AddressMapper addressMapper() {
		return new AddressMapper();
	}
	
	@Bean
	public RatingMapper ratingMapper() {
		return new RatingMapper();
	}
	
	@Bean
	public ReviewMapper reviewMapper() {
		return new ReviewMapper();
	}
	
	@Bean
	public OrderMapper orderMapper() {
		return new OrderMapper();
	}
	
	@Bean
	public OrderItemMapper orderItemMapper() {
		return new OrderItemMapper();
	}
}
