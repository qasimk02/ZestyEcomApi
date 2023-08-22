package com.zesty.ecom.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zesty.ecom.Payload.Dto.ProductDto;
import com.zesty.ecom.Payload.Response.ApiResponse;
import com.zesty.ecom.Payload.Response.ProductResponse;
import com.zesty.ecom.Service.ProductService;
import com.zesty.ecom.Util.AppConstants;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/products")
public class ProductController {

	@Autowired
	private ProductService productService;

	@PostMapping("")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ProductDto> createProduct(@Valid @RequestBody ProductDto p) {
		ProductDto savedProduct = productService.createProduct(p);
		return new ResponseEntity<ProductDto>(savedProduct, HttpStatus.CREATED);
	}

	@GetMapping("")
	public ResponseEntity<ProductResponse> getAllProducts(
			@RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER_STRING, required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE_STRING, required = false) Integer pageSize,
			@RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY_STRING, required = false) String sortBy,
			@RequestParam(value = "sortOrder", defaultValue = AppConstants.SORT_ORDER_STRING, required = false) String sortOrder,
			@RequestParam(value = "color", defaultValue = AppConstants.COLOR, required = false) List<String> colors,
			@RequestParam(value = "size", defaultValue = AppConstants.SIZE, required = false) List<String> sizes,
			@RequestParam(value = "minPrice", defaultValue = AppConstants.MIN_PRICE, required = false) Integer minPrice,
			@RequestParam(value = "maxPrice", defaultValue = AppConstants.MAX_PRICE, required = false) Integer maxPrice,
			@RequestParam(value = "minDiscount", defaultValue = AppConstants.MIN_DISCOUNT, required = false) Integer minDiscount) {

//		ProductResponse productResponse = productService.getAllProducts(pageNumber, pageSize, sortBy, sortOrder);
		ProductResponse productResponse = productService.filterAllProducts(pageNumber, pageSize, sortBy, sortOrder,
				colors, sizes, minPrice, maxPrice, minDiscount);
		return new ResponseEntity<ProductResponse>(productResponse, HttpStatus.ACCEPTED);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ProductDto> getProductById(@PathVariable("id") Long id) {
		ProductDto product = productService.getProductById(id);
		return new ResponseEntity<ProductDto>(product, HttpStatus.OK);
	}

	@PutMapping("/{id}")
	public ResponseEntity<ProductDto> updateProduct(@Valid @RequestBody ProductDto p, @PathVariable("id") Long id) {
		ProductDto updatedProduct = productService.updateProduct(id, p);
		return new ResponseEntity<ProductDto>(updatedProduct, HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse> deleteProduct(@PathVariable("id") Long id) {
		productService.deleteProduct(id);
		ApiResponse apiResponse = new ApiResponse(String.format("Product Deleted Succesfully of Id : %s", id), id,true);
		return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.OK);
	}

	// get by category id
	@GetMapping("category/{category}")
	public ResponseEntity<ProductResponse> getProductByCategoryId(@PathVariable("category") String category,
			@RequestParam(value = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER_STRING, required = false) Integer pageNumber,
			@RequestParam(value = "pageSize", defaultValue = AppConstants.PAGE_SIZE_STRING, required = false) Integer pageSize,
			@RequestParam(value = "sortBy", defaultValue = AppConstants.SORT_BY_STRING, required = false) String sortBy,
			@RequestParam(value = "sortOrder", defaultValue = AppConstants.SORT_ORDER_STRING, required = false) String sortOrder,
			@RequestParam(value = "color", defaultValue = AppConstants.COLOR, required = false) List<String> colors,
			@RequestParam(value = "size", defaultValue = AppConstants.SIZE, required = false) List<String> sizes,
			@RequestParam(value = "minPrice", defaultValue = AppConstants.MIN_PRICE, required = false) Integer minPrice,
			@RequestParam(value = "maxPrice", defaultValue = AppConstants.MAX_PRICE, required = false) Integer maxPrice,
			@RequestParam(value = "minDiscount", defaultValue = AppConstants.MIN_DISCOUNT, required = false) Integer minDiscount) {

//		ProductResponse productResponse = this.productService.getProductsByCategory(category, pageNumber, pageSize, sortBy,
//				sortOrder);}
		
		ProductResponse productResponse = this.productService.filterProductsByCategory(category, pageNumber, pageSize,
				sortBy, sortOrder, colors, sizes, minPrice, maxPrice, minDiscount);

		return new ResponseEntity<ProductResponse>(productResponse, HttpStatus.ACCEPTED);
	}

	// dump products in bulk
	@PostMapping("/dumpProducts")
	public ResponseEntity<List<ProductDto>> addAllProducts(@RequestBody List<ProductDto> products) {
		List<ProductDto> dumpedProducts = this.productService.addAllProducts(products);
		return new ResponseEntity<>(dumpedProducts, HttpStatus.CREATED);
	}
}
