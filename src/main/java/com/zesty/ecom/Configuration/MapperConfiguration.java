package com.zesty.ecom.Configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zesty.ecom.Mapper.CategoryMapper;
import com.zesty.ecom.Mapper.ProductMapper;
import com.zesty.ecom.Mapper.UserMapper;

@Configuration
public class MapperConfiguration {
	
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
}
