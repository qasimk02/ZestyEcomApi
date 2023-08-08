package com.zesty.ecom.Service;

import java.util.List;

import com.zesty.ecom.Payload.Dto.ProductDto;
import com.zesty.ecom.Payload.Response.ProductResponse;

public interface ProductService {

	//get
	ProductResponse getAllProduct(int pageNumber, int pageSize, String sortBy, String sortOrder);

	ProductDto getProductById(Long id);
	
	ProductResponse getProductsByCategoryId(int cId, int pageNumber, int pageSize, String sortBy, String sortOrder);
	
	//create
	ProductDto createProduct(ProductDto p);

	//update
	ProductDto updateProduct(Long id, ProductDto newP);

	//delete
	void deleteProduct(Long id);

	// not needed just for development purpose
	void deleteAllProduct();
	List<ProductDto> addAllProducts(List<ProductDto> products);
}
