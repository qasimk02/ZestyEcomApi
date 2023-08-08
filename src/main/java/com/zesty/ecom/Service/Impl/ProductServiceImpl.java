package com.zesty.ecom.Service.Impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.zesty.ecom.Exception.ResourceNotFoundException;
import com.zesty.ecom.Exception.CategoriesNotFoundExcepiton;
import com.zesty.ecom.Mapper.ProductMapper;
import com.zesty.ecom.Model.Category;
import com.zesty.ecom.Model.Product;
import com.zesty.ecom.Payload.Dto.ProductDto;
import com.zesty.ecom.Payload.Response.ProductResponse;
import com.zesty.ecom.Repository.CategoryRepository;
import com.zesty.ecom.Repository.ProductRepository;
import com.zesty.ecom.Service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private ProductMapper productMapper;

	@Override
	@PreAuthorize("hasRole('ADMIN')")
	public ProductDto createProduct(ProductDto pDto) {
		// checking for is in stock
		if (pDto.getQuantity() > 0) {
			pDto.setInStock(true);
		} else {
			pDto.setInStock(false);
		}
		Category c = categoryRepository.findById(pDto.getCategory().getCategoryId()).orElseThrow(
				() -> new ResourceNotFoundException("Category", "Id", Long.toString(pDto.getCategory().getCategoryId())));
		
		Product p = productMapper.mapToEntity(pDto);
		p.setCategory(c);
		Product savedProduct = productRepository.save(p);
		ProductDto savedProductDto = productMapper.mapToDto(savedProduct);
		return savedProductDto;
	}

	@Override
	public ProductResponse getAllProduct(int pageNumber, int pageSize, String sortBy, String sortOrder) {

		// sorting the product in order and by field
		Sort sort = null;
		if (sortOrder.trim().toLowerCase().equals("ascending")) {
			sort = Sort.by(sortBy).ascending();// product_id: ASC
		} else {
			sort = Sort.by(sortBy).descending();// product_id: DESC
		}
		// paging the response
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		Page<Product> page = this.productRepository.findAll(pageable);
		List<Product> pageWiseProducts = page.getContent();
		// converting entity to productDto
		List<ProductDto> pageWiseProductsDto = pageWiseProducts.stream().map((p) -> productMapper.mapToDto(p))
				.collect(Collectors.toList());
		// giving detailed product response with content,page number,pageSize,totalPage
		// and lastPage
		ProductResponse productResponse = new ProductResponse();
		productResponse.setPageContent(pageWiseProductsDto);
		productResponse.setPageSize(page.getSize());
		productResponse.setTotalPages(page.getTotalPages());
		productResponse.setLastPage(page.isLast());

		return productResponse;
	}

	@Override
	public ProductDto getProductById(Long id) {
		Product p = productRepository.findByProductId(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product", "Id", Long.toString(id)));
		return productMapper.mapToDto(p);
	}

	@Override
	@PreAuthorize("hasRole('ADMIN')")
	public ProductDto updateProduct(Long id, ProductDto newP) {
		Product oldP = productRepository.findByProductId(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product", "Id", Long.toString(id)));
		// setting the new values
		oldP.setName(newP.getName());
		oldP.setPrice(newP.getPrice());
		oldP.setInStock(newP.getInStock());
		oldP.setQuantity(newP.getQuantity());
		oldP.setLive(newP.isLive());
		oldP.setDiscountPercent(newP.getDiscountPercent());
		oldP.setImageName(newP.getImageName());
		oldP.setDescription(newP.getDescription());
		// checking if category id exist
		Category c = this.categoryRepository.findById(newP.getCategory().getCategoryId()).orElseThrow(
				() -> new ResourceNotFoundException("Category", "Id", Long.toString(newP.getCategory().getCategoryId())));
		oldP.setCategory(c);
		Product updatedProduct = productRepository.save(oldP);
		return productMapper.mapToDto(updatedProduct);
	}

	@Override
	@PreAuthorize("hasRole('ADMIN')")
	public void deleteProduct(Long id) {
		Product p = productRepository.findByProductId(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product", "Id", Long.toString(id)));
		productRepository.delete(p);
	}

	@Override
	public ProductResponse getProductsByCategoryId(int cId, int pageNumber, int pageSize, String sortBy,
			String sortOrder) {
		// get category
		Category category = this.categoryRepository.findById(cId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "Id", Integer.toString(cId)));

		// sorting the product in order and by field
		Sort sort = null;
		if (sortOrder.trim().toLowerCase().equals("ascending")) {
			sort = Sort.by(sortBy).ascending();// product_id: ASC
		} else {
			sort = Sort.by(sortBy).descending();// product_id: DESC
		}
		// paging the response
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		Page<Product> page = this.productRepository.findByCategory(category, pageable);
		List<Product> pageWiseProducts = page.getContent();
		// converting entity to productDto
		List<ProductDto> pageWiseProductsDto = pageWiseProducts.stream().map((p) -> productMapper.mapToDto(p))
				.collect(Collectors.toList());
		// giving detailed product response with content,page number,pageSize,totalPage
		// and lastPage
		ProductResponse productResponse = new ProductResponse();
		productResponse.setPageContent(pageWiseProductsDto);
		productResponse.setPageSize(page.getSize());
		productResponse.setTotalPages(page.getTotalPages());
		productResponse.setLastPage(page.isLast());

		return productResponse;
	}

	@Override
	@PreAuthorize("hasRole('ADMIN')")
	public List<ProductDto> addAllProducts(List<ProductDto> products) {
		// checking if all the categories present
		List<String> notExistIds = new ArrayList<>();
		products.forEach((p) -> {
			int id = p.getCategory().getCategoryId();
			System.out.println("Product"+id);
			if (!categoryRepository.existsById(id)) {
				notExistIds.add("Category not found with Id : " + id);
			}
		});
		if (notExistIds.size() > 0) {
			throw new CategoriesNotFoundExcepiton(notExistIds);
		}

		// checking for is in stock
		for (ProductDto p : products) {
			if (p.getQuantity() > 0) {
				p.setInStock(true);
			} else {
				p.setInStock(false);
			}
		}

		List<Product> pds = products.stream().map((p) -> productMapper.mapToEntity(p)).collect(Collectors.toList());
		List<Product> savedPds = this.productRepository.saveAll(pds);
		List<ProductDto> savedPdsDto = savedPds.stream().map((p) -> productMapper.mapToDto(p))
				.collect(Collectors.toList());
		return savedPdsDto;
	}
	
	@Override
	@PreAuthorize("hasRole('ADMIN')")
	public void deleteAllProduct() {
		productRepository.deleteAll();

	}

}
