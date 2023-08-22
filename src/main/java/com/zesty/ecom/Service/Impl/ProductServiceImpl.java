package com.zesty.ecom.Service.Impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.zesty.ecom.Exception.CategoriesNotFoundExcepiton;
import com.zesty.ecom.Exception.ResourceNotFoundException;
import com.zesty.ecom.Mapper.ProductMapper;
import com.zesty.ecom.Mapper.SizeMapper;
import com.zesty.ecom.Model.Category;
import com.zesty.ecom.Model.Product;
import com.zesty.ecom.Model.Sizes;
import com.zesty.ecom.Payload.Dto.ProductDto;
import com.zesty.ecom.Payload.Dto.SizesDto;
import com.zesty.ecom.Payload.Response.ProductResponse;
import com.zesty.ecom.Repository.CategoryRepository;
import com.zesty.ecom.Repository.ProductRepository;
import com.zesty.ecom.Service.ProductService;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private ProductMapper productMapper;

	@Autowired
	private SizeMapper sizeMapper;

	@Override
	@PreAuthorize("hasRole('ADMIN')")
	public ProductDto createProduct(ProductDto pDto) {

		// checking for is in stock
		pDto.setInStock(true);
		int count = 0;
		for (SizesDto size : pDto.getSizes()) {
			size.setName(size.getName().toLowerCase());
			if (size.getQuantity() <= 0) {
				count++;
			}
		}
		if (count == pDto.getSizes().size()) {
			pDto.setInStock(false);
		}

		List<Integer> allIds = pDto.getCategories().stream().map((c) -> c.getCategoryId()).collect(Collectors.toList());

		// checking if all the categories exist or not
		List<String> notExistIds = new ArrayList<>();
		allIds.forEach((id) -> {
			if (!categoryRepository.existsById(id)) {
				notExistIds.add("Category not found with Id : " + id);
			}
		});
		if (notExistIds.size() > 0) {
			throw new CategoriesNotFoundExcepiton(notExistIds);
		}

		// getting all the category by id
		List<Category> categories = categoryRepository.findAllById(allIds);

		// mapping product Dto to product entity
		Product p = productMapper.mapToEntity(pDto);

		// Associate the categories with the product
		p.setCategories(categories);

		// saving the product
		Product savedProduct = productRepository.save(p);
		ProductDto savedProductDto = productMapper.mapToDto(savedProduct);
		return savedProductDto;
	}

	@Override
	public ProductResponse getAllProducts(int pageNumber, int pageSize, String sortBy, String sortOrder) {

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
	public ProductResponse filterAllProducts(int pageNumber, int pageSize, String sortBy, String sortOrder,
			List<String> colors, List<String> sizes, Integer minPrice, Integer maxPrice, Integer minDiscount) {

		System.out.println("Entered product controller " + "\npageNumber: " + pageNumber + "\npageSize: " + pageSize
				+ "\nsortBy: " + sortBy + "\nsortOrder: " + sortOrder + "\ncolors: " + colors + "\nsizes: " + sizes
				+ "\nminDiscount: " + minDiscount + "\nmaxPrice: " + maxPrice + "\nminPrice: " + minPrice);

		// sorting the product in order and by field
		Sort sort = null;
		if (sortOrder.trim().toLowerCase().equals("ascending")) {
			sort = Sort.by(sortBy).ascending();// product_id: ASC
		} else {
			sort = Sort.by(sortBy).descending();// product_id: DESC
		}
		// paging the response
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		
		Specification<Product> spec = (root, query, criteriaBuilder) -> {
	        List<Predicate> predicates = new ArrayList<>();

	        if (colors != null && !colors.isEmpty()) {
	            predicates.add(root.get("color").in(colors));
	        }

	        if (sizes != null && !sizes.isEmpty()) {
	            Join<Product, Sizes> sizesJoin = root.join("sizes");
	            predicates.add(sizesJoin.get("name").in(sizes));
	        }

	        predicates.add(criteriaBuilder.between(root.get("price"), minPrice, maxPrice));
	        predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("discountPercent"), minDiscount));

	        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
	    };
		
		Page<Product> page = this.productRepository.findAll(spec, pageable);

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
		System.out.println(productResponse);
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
		oldP.setBrand(newP.getBrand());
		oldP.setColor(newP.getColor());
		oldP.setPrice(newP.getPrice());
		oldP.setDiscountPercent(newP.getDiscountPercent());

		// checking for is in stock
		oldP.setInStock(true);
		int count = 0;
		for (SizesDto size : newP.getSizes()) {
			if (size.getQuantity() <= 0) {
				count++;
				size.setQuantity(0);
			}
		}
		if (count == oldP.getSizes().size()) {
			oldP.setInStock(false);
		}

		oldP.setLive(newP.isLive());
		oldP.setDiscountPercent(newP.getDiscountPercent());
		oldP.setImageName(newP.getImageName());
		oldP.setDescription(newP.getDescription());

		// setting sizes
		Set<Sizes> sizes = newP.getSizes().stream().map(s -> this.sizeMapper.mapToEntity(s))
				.collect(Collectors.toSet());
		oldP.setSizes(sizes);

		// getting all ids
		List<Integer> allIds = newP.getCategories().stream().map((c) -> c.getCategoryId()).collect(Collectors.toList());
		// checking if all the categories present
		List<String> notExistIds = new ArrayList<>();
		allIds.forEach((i) -> {
			if (!categoryRepository.existsById(i)) {
				notExistIds.add("Category not found with Id : " + i);
			}
		});
		if (notExistIds.size() > 0) {
			throw new CategoriesNotFoundExcepiton(notExistIds);
		}

		// getting all category by id
		List<Category> c = categoryRepository.findAllById(allIds);

		// Associate the categories with the product
		oldP.setCategories(c);

		// save the product
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
	public ProductResponse getProductsByCategory(String cat, int pageNumber, int pageSize, String sortBy,
			String sortOrder) {
		// get category
		Category category = this.categoryRepository.findByTitle(cat.toLowerCase())
				.orElseThrow(() -> new ResourceNotFoundException("Category", "name", cat));

		// sorting the product in order and by field
		Sort sort = null;
		if (sortOrder.trim().toLowerCase().equals("ascending")) {
			sort = Sort.by(sortBy).ascending();// product_id: ASC
		} else {
			sort = Sort.by(sortBy).descending();// product_id: DESC
		}
		// paging the response
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		// getting the product
		Page<Product> page = this.productRepository.findByCategories(category, pageable);
		// getting the page content
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
	public ProductResponse filterProductsByCategory(String cat, int pageNumber, int pageSize, String sortBy,
			String sortOrder, List<String> colors, List<String> sizes, Integer minPrice, Integer maxPrice,
			Integer minDiscount) {

		// get category
		Category category = this.categoryRepository.findByTitle(cat.toLowerCase())
				.orElseThrow(() -> new ResourceNotFoundException("Category", "name", cat));

		// sorting the product in order and by field
		Sort sort = null;
		if (sortOrder.trim().toLowerCase().equals("ascending")) {
			sort = Sort.by(sortBy).ascending();// product_id: ASC
		} else {
			sort = Sort.by(sortBy).descending();// product_id: DESC
		}
		// paging the response
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		
		Specification<Product> spec = (root, query, criteriaBuilder) -> {
	        List<Predicate> predicates = new ArrayList<>();
	        
	        //category filter
	        Join<Product, Category> categoryJoin = root.join("categories");
            predicates.add(criteriaBuilder.equal(categoryJoin.get("categoryId"), category.getCategoryId()));

	        if (colors != null && !colors.isEmpty()) {
	            predicates.add(root.get("color").in(colors));
	        }

	        if (sizes != null && !sizes.isEmpty()) {
	            Join<Product, Sizes> sizesJoin = root.join("sizes");
	            predicates.add(sizesJoin.get("name").in(sizes));
	        }

	        predicates.add(criteriaBuilder.between(root.get("price"), minPrice, maxPrice));
	        predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("discountPercent"), minDiscount));
	        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
	    };
		
		// getting the product
		Page<Product> page = this.productRepository.findAll(spec,pageable);
		// getting the page contents
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

//	@Override
//	@PreAuthorize("hasRole('ADMIN')")
//	public List<ProductDto> addAllProducts(List<ProductDto> products) {
//		// checking if all the categories present
//		List<String> notExistIds = new ArrayList<>();
//		products.forEach((p) -> {
//			int id = p.getCategory().getCategoryId();
//			System.out.println("Product" + id);
//			if (!categoryRepository.existsById(id)) {
//				notExistIds.add("Category not found with Id : " + id);
//			}
//		});
//		if (notExistIds.size() > 0) {
//			throw new CategoriesNotFoundExcepiton(notExistIds);
//		}
//
//		// checking for is in stock
//		for (ProductDto p : products) {
//			if (p.getQuantity() > 0) {
//				p.setInStock(true);
//			} else {
//				p.setInStock(false);
//			}
//		}
//
//		List<Product> pds = products.stream().map((p) -> productMapper.mapToEntity(p)).collect(Collectors.toList());
//		List<Product> savedPds = this.productRepository.saveAll(pds);
//		List<ProductDto> savedPdsDto = savedPds.stream().map((p) -> productMapper.mapToDto(p))
//				.collect(Collectors.toList());
//		return savedPdsDto;
//	}

	@Override
	public List<ProductDto> addAllProducts(List<ProductDto> products) {
		// TODO Auto-generated method stub
		return null;
	}

}
