package com.zesty.ecom.Mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.zesty.ecom.Model.Product;
import com.zesty.ecom.Payload.ProductDto;

//methods are static because static method can be call with className
public class ProductMapper {
	
	@Autowired
	private ModelMapper modelMapper;
	
	//we can use Model Mapper to map the object we don't need to do manual mapping
	//converting product to productDto
	public ProductDto mapToDto(Product p) {
//		//setting the category
//		Category c = new Category();
//		c.setId(p.getCategory().getId());
//		c.setTitle(p.getCategory().getTitle());
//		ProductDto pDto = new ProductDto(
//				p.getProductId(),
//				p.getProductName(),
//				p.getProductPrize(),
//				p.isInStock(),
//				p.getProductQuantity(),
//				p.isLive(),
//				p.getProductImageName(),
//				p.getProductDesc(),
//				c
//				);
		ProductDto pDto = modelMapper.map(p,ProductDto.class);
		return pDto;
	}
	
	//converting productDto to product
	public Product mapToEntity(ProductDto pDto) {
		//setting the category
//		Category c = new Category();
//		c.setId(pDto.getCategory().getId());
//		c.setTitle(pDto.getCategory().getTitle());
//		
//		Product p = new Product(
//				pDto.getProductId(),
//				pDto.getProductName(),
//				pDto.getProductPrize(),
//				pDto.isInStock(),
//				pDto.getProductQuantity(),
//				pDto.isLive(),
//				pDto.getProductImageName(),
//				pDto.getProductDesc(),
//				c
//				);
		Product p = modelMapper.map(pDto, Product.class);
		return p;
	}
}
