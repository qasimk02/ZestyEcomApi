package com.zesty.ecom.Service.Impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.zesty.ecom.Exception.ResourceNotFoundException;
import com.zesty.ecom.Mapper.ProductMapper;
import com.zesty.ecom.Mapper.SizeMapper;
import com.zesty.ecom.Model.Category;
import com.zesty.ecom.Model.Product;
import com.zesty.ecom.Model.ProductImage;
import com.zesty.ecom.Model.Sizes;
import com.zesty.ecom.Payload.Dto.ProductDto;
import com.zesty.ecom.Payload.Dto.SizesDto;
import com.zesty.ecom.Payload.Response.ProductResponse;
import com.zesty.ecom.Repository.CategoryRepository;
import com.zesty.ecom.Repository.ProductRepository;
import com.zesty.ecom.Service.CategoryService;
import com.zesty.ecom.Service.ProductService;
import com.zesty.ecom.Service.S3Service;
import com.zesty.ecom.Util.ImageHandler;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private ProductMapper productMapper;

	@Autowired
	private SizeMapper sizeMapper;

	@Autowired
	private ImageHandler imageHandler;

	@Autowired
	private S3Service s3Service;

	@Override
	@PreAuthorize("hasRole('ADMIN')")
	@Transactional
	public ProductDto createProduct(List<MultipartFile> images, ProductDto pDto) {

		// checking if any size has quantity > 0 then that product is in stock
		pDto.setInStock(pDto.getSizes().stream().anyMatch(size -> size.getQuantity() > 0));
		
		// Convert size names to lower case
		pDto.getSizes().forEach(size -> size.setName(size.getName().toLowerCase()));
	
		// get category
		Integer cId = pDto.getCategory().getCategoryId();
		Category category = this.categoryRepository.findById(cId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "Id", Integer.toString(cId)));

		// mapping product Dto to product entity
		Product p = productMapper.mapToEntity(pDto);

		// Associate the categories with the product
		p.setCategory(category);

		// saving image names
		List<HashMap<String, byte[]>> allImages = new ArrayList<>();
		ArrayList<ProductImage> productImages = new ArrayList<>();
		images.forEach((image) -> {
			// setting the image to product
			ProductImage pImage = new ProductImage();
			String fileName = UUID.randomUUID().toString() + image.getOriginalFilename();
			pImage.setImageName(fileName);
			pImage.setProduct(p);
			productImages.add(pImage);

			// getting different version of single image(small,medium and large)
			allImages.add(imageHandler.handleProductImage(image, fileName));

		});

		// saving the product
		p.setActive(true);
		p.setImages(productImages);
		Product savedProduct = productRepository.save(p);

		// uploading images to s3
		allImages.forEach((map) -> {
			Iterator<Entry<String, byte[]>> iterator = map.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<String, byte[]> entry = iterator.next();
				String imageName = entry.getKey();
				String imagePath = "products"+"/"+savedProduct.getProductId()+"/"+imageName;
				byte[] imageByte = entry.getValue();
				s3Service.uploadToS3(imageByte, imagePath);
			}
		});

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
			predicates.add(root.get("active").in(true));
			predicates.add(root.get("live").in(true));
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
		oldP.setDescription(newP.getDescription());

		// setting sizes
		Set<Sizes> sizes = newP.getSizes().stream().map(s -> this.sizeMapper.mapToEntity(s))
				.collect(Collectors.toSet());
		oldP.setSizes(sizes);

		// getting and setting category

		// save the product
		Product updatedProduct = productRepository.save(oldP);

		return productMapper.mapToDto(updatedProduct);
	}

	public String addProductImage(MultipartFile image,Long pId) {
		return "";
	}
	
	public String deleteProductImage(String imageName,Long pId) {
		return "";
	}
	
	@Override
	@PreAuthorize("hasRole('ADMIN')")
	public void deleteProduct(Long id) {
		Product p = productRepository.findByProductId(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product", "Id", Long.toString(id)));
		p.setActive(false);
		productRepository.save(p);
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
		Page<Product> page = this.productRepository.findByCategory(category, pageable);
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

		List<Category> allChildCategories = this.categoryService.getChildCategories(category.getCategoryId());

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

			// category filter
			Join<Product, Category> categoryJoin = root.join("category");
			predicates.add(categoryJoin.get("id")
					.in(allChildCategories.stream().map(Category::getCategoryId).collect(Collectors.toList())));

			if (colors != null && !colors.isEmpty()) {
				predicates.add(root.get("color").in(colors));
			}

			if (sizes != null && !sizes.isEmpty()) {
				Join<Product, Sizes> sizesJoin = root.join("sizes");
				predicates.add(sizesJoin.get("name").in(sizes));
			}

			predicates.add(criteriaBuilder.between(root.get("price"), minPrice, maxPrice));
			predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("discountPercent"), minDiscount));
			predicates.add(root.get("active").in(true));
			predicates.add(root.get("live").in(true));
			return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
		};

		// getting the product
		Page<Product> page = this.productRepository.findAll(spec, pageable);
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
