package com.zesty.ecom.Service;

import java.util.List;

import com.zesty.ecom.Payload.ProductDto;
import com.zesty.ecom.Payload.ProductResponse;

public interface ProductService {
	
	ProductDto createProduct(ProductDto p);
	ProductResponse getAllProduct(int pageNumber,int pageSize,String sortBy,String sortOrder);
	ProductDto getProductById(int id);
	ProductDto updateProduct(int id,ProductDto newP);
	void deleteProduct(int id);
	//not needed just for development purpose
	void deleteAllProduct();
	ProductResponse getProductsByCategoryId(int cId,int pageNumber, int pageSize, String sortBy, String sortOrder);
	List<ProductDto> addAllProducts(List<ProductDto> products);
}
