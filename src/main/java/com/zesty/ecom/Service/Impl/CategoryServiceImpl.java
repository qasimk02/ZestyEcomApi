package com.zesty.ecom.Service.Impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zesty.ecom.Exception.DuplicateFieldExcepiton;
import com.zesty.ecom.Exception.ResourceNotFoundException;
import com.zesty.ecom.Mapper.CategoryMapper;
import com.zesty.ecom.Model.Category;
import com.zesty.ecom.Payload.Dto.CategoryDto;
import com.zesty.ecom.Repository.CategoryRepository;
import com.zesty.ecom.Service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private CategoryMapper categoryMapper;

	@Override
	public CategoryDto createCategory(CategoryDto cDto) {
		// if title already exist then throw exception
		String title = cDto.getTitle();
		if (categoryRepository.existsByTitle(title)) {
			throw new DuplicateFieldExcepiton("Category", "Title", title);
		}
		Category category = categoryMapper.mapToEntity(cDto);
		Category savedCategory = this.categoryRepository.save(category);
		return categoryMapper.mapToDto(savedCategory);
	}

	@Override
	public List<CategoryDto> getAllCategory() {
		List<Category> categories = this.categoryRepository.findAll();
		List<CategoryDto> categoriesDto = categories.stream().map((c) -> categoryMapper.mapToDto(c))
				.collect(Collectors.toList());
		return categoriesDto;
	}

	@Override
	public CategoryDto getCategoryById(int id) {
		Category category = this.categoryRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "Id", Integer.toString(id)));
		return categoryMapper.mapToDto(category);
	}

	@Override
	public CategoryDto updateCategory(CategoryDto newCategory, int id) {
		// if title already exist then throw exception
		String title = newCategory.getTitle();
		if (categoryRepository.existsByTitle(title)) {
			throw new DuplicateFieldExcepiton("Category", "Title", title);
		}
		Category oldCategory = this.categoryRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "Id", Integer.toString(id)));
		oldCategory.setTitle(newCategory.getTitle());
		Category updatedCategory = this.categoryRepository.save(oldCategory);
		return categoryMapper.mapToDto(updatedCategory);
	}

	@Override
	public void deleteCategoryById(int id) {
		Category category = this.categoryRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "Id", Integer.toString(id)));
		this.categoryRepository.delete(category);
	}

	// to dump multiple categories
	@Override
	public List<CategoryDto> addAllCategories(List<CategoryDto> categories) {
		List<Category> cats = categories.stream().map((c) -> categoryMapper.mapToEntity(c))
				.collect(Collectors.toList());
		List<Category> savedCats = this.categoryRepository.saveAll(cats);
		List<CategoryDto> savedCatsDto = savedCats.stream().map((c) -> categoryMapper.mapToDto(c))
				.collect(Collectors.toList());
		return savedCatsDto;
	}

}
