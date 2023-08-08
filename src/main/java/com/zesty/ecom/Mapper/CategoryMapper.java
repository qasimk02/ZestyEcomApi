package com.zesty.ecom.Mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.zesty.ecom.Model.Category;
import com.zesty.ecom.Payload.Dto.CategoryDto;

public class CategoryMapper {
	
	@Autowired
	private  ModelMapper modelMapper;
	
	public Category mapToEntity(CategoryDto cDto) {
//		Category c = new Category(
//				cDto.getId(),
//				cDto.getTitle(),
//				cDto.getProducts()
//				);
		//by using model mapper
		Category c = modelMapper.map(cDto,Category.class);
		return c;
	}
	
	public CategoryDto mapToDto(Category c) {
//		CategoryDto cDto = new CategoryDto(
//				c.getId(),
//				c.getTitle(),
//				c.getProducts()
//				);
		//by using model mapper
		CategoryDto cDto = modelMapper.map(c,CategoryDto.class);
		return cDto;
	}
	
}
