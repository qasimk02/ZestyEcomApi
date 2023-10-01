package com.zesty.ecom.Service.Impl;

import java.util.ArrayList;
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
		String title = cDto.getTitle().toLowerCase();
		if (categoryRepository.existsByTitle(title)) {
			throw new DuplicateFieldExcepiton("Category", "Title", title);
		}
		Category category = categoryMapper.mapToEntity(cDto);
		category.setTitle(title);
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
	public CategoryDto getCategoryById(Integer id) {
		Category category = this.categoryRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "Id", Integer.toString(id)));
		return categoryMapper.mapToDto(category);
	}

	@Override
	public List<CategoryDto> getAllCategoyById(List<Integer> ids) {
		List<Category> categories = this.categoryRepository.findAllById(ids);
		List<CategoryDto> categoriesDto = categories.stream().map((c) -> this.categoryMapper.mapToDto(c))
				.collect(Collectors.toList());
		return categoriesDto;
	}

	@Override
	public List<CategoryDto> getCategoriesByLevel(Integer level) {
		List<Category> categories = this.categoryRepository.findByDepth(level)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "Level", Integer.toString(level)));
		List<CategoryDto> categoriesDto = categories.stream().map((c) -> this.categoryMapper.mapToDto(c))
				.collect(Collectors.toList());
		return categoriesDto;
	}

	// child categories
	@Override
	public List<Category> getChildCategories(Integer id) {
		List<Category> allChildCategories = new ArrayList<>();
		Category parentCategory = this.categoryRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "Id", Integer.toString(id)));
		dfsTraversal(parentCategory, allChildCategories);
		return allChildCategories;
	}

	private void dfsTraversal(Category category, List<Category> result) {
		result.add(category);
		List<Category> childCategories = category.getChildCategories();
		if (childCategories != null) {
			for (Category childCategory : childCategories) {
				dfsTraversal(childCategory, result);
			}
		}
	}

	@Override
	public CategoryDto updateCategory(CategoryDto newCategory, Integer id) {
		// if title already exist then throw exception
		String title = newCategory.getTitle().toLowerCase();
		if (categoryRepository.existsByTitle(title)) {
			throw new DuplicateFieldExcepiton("Category", "Title", title);
		}
		Category oldCategory = this.categoryRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "Id", Integer.toString(id)));
//		oldCategory.setDepth(newCategory.getDepth()); can;t update depth
//		oldCategory.setParentCategory(oldCategory); as of now can't update parent category
		oldCategory.setTitle(title);
		Category updatedCategory = this.categoryRepository.save(oldCategory);
		return categoryMapper.mapToDto(updatedCategory);
	}

	@Override
	public void deleteCategoryById(Integer id) {
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
