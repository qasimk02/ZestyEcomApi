package com.zesty.ecom.Service;

import java.util.List;

import com.zesty.ecom.Payload.Dto.CategoryDto;

public interface CategoryService {

	CategoryDto createCategory(CategoryDto category);

	List<CategoryDto> getAllCategory();

	CategoryDto getCategoryById(int id);

	CategoryDto updateCategory(CategoryDto category, int id);

	void deleteCategoryById(int id);

	//to dump multiple categories
	List<CategoryDto> addAllCategories (List<CategoryDto> categories);

}
