package com.zesty.ecom.Service;

import java.util.List;

import com.zesty.ecom.Payload.Dto.ProductDto;
import com.zesty.ecom.Payload.Response.ProductResponse;

public interface ProductService {

	// get
	ProductResponse getAllProducts(int pageNumber, int pageSize, String sortBy, String sortOrder);

	ProductDto getProductById(Long id);

	ProductResponse getProductsByCategory(String category, int pageNumber, int pageSize, String sortBy,
			String sortOrder);

	// create
	ProductDto createProduct(ProductDto p);

	// update
	ProductDto updateProduct(Long id, ProductDto newP);

	// delete
	void deleteProduct(Long id);

	// filter
	ProductResponse filterAllProducts(int pageNumber, int pageSize, String sortBy, String sortOrder,
			List<String> colors, List<String> sizes, Integer minPrice, Integer maxPrice, Integer minDiscount);

	ProductResponse filterProductsByCategory(String cat, int pageNumber, int pageSize, String sortBy, String sortOrder,
			List<String> colors, List<String> sizes, Integer minPrice, Integer maxPrice, Integer minDiscount);

	// not needed just for development purpose
	List<ProductDto> addAllProducts(List<ProductDto> products);
}
