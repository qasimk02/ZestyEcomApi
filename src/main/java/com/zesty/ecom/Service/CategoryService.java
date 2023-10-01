package com.zesty.ecom.Service;

import java.util.List;

import com.zesty.ecom.Model.Category;
import com.zesty.ecom.Payload.Dto.CategoryDto;

public interface CategoryService {

	//create
	CategoryDto createCategory(CategoryDto category);

	//get
	List<CategoryDto> getAllCategory();
	List<CategoryDto> getAllCategoyById(List<Integer> ids);
	CategoryDto getCategoryById(Integer id);
	List<Category> getChildCategories(Integer id);
	List<CategoryDto> getCategoriesByLevel(Integer id);

	//update
	CategoryDto updateCategory(CategoryDto category, Integer id);

	//delete
	void deleteCategoryById(Integer id);

	//to dump multiple categories
	List<CategoryDto> addAllCategories (List<CategoryDto> categories);

}
