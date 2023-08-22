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
import org.springframework.web.bind.annotation.RestController;

import com.zesty.ecom.Payload.Dto.CategoryDto;
import com.zesty.ecom.Payload.Response.ApiResponse;
import com.zesty.ecom.Service.CategoryService;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

	@Autowired
	private CategoryService categoryService;

	// create category
	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<CategoryDto> createCategory(@RequestBody CategoryDto category) {
		System.out.println(category);
		CategoryDto savedCategory = this.categoryService.createCategory(category);
		return new ResponseEntity<CategoryDto>(savedCategory, HttpStatus.CREATED);
	}

	// get all category
	@GetMapping
	public ResponseEntity<List<CategoryDto>> getAllCategory() {
		List<CategoryDto> allCategories = this.categoryService.getAllCategory();
		return new ResponseEntity<List<CategoryDto>>(allCategories, HttpStatus.ACCEPTED);
	}

	// get category by id
	@GetMapping("/{id}")
	public ResponseEntity<CategoryDto> getCategoryById(@PathVariable("id") int id) {
		CategoryDto category = this.categoryService.getCategoryById(id);
		return new ResponseEntity<CategoryDto>(category, HttpStatus.OK);
	}
	
	//get child categories
	@GetMapping("/child/{id}")
	public ResponseEntity<List<CategoryDto>> getChildCategories(@PathVariable("id") Integer id){
		List<CategoryDto> categories = this.categoryService.getChildCategories(id);
		return new ResponseEntity<List<CategoryDto>>(categories, HttpStatus.OK);
	}

	// update category
	@PutMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<CategoryDto> updateCategory(@RequestBody CategoryDto category, @PathVariable("id") int id) {
		CategoryDto updatedCtegory = this.categoryService.updateCategory(category, id);
		return new ResponseEntity<CategoryDto>(updatedCtegory, HttpStatus.OK);
	}

	// delete category
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse> deleteCategory(@PathVariable("id") int id) {
		this.categoryService.deleteCategoryById(id);
		ApiResponse apiResponse = new ApiResponse(String.format("Category Deleted Succesfully of Id : %s", id), Integer.toUnsignedLong(id),true);
		return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.ACCEPTED);
	}
	
	//to dump multiple categories
	@PostMapping("/dumpCategories")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<CategoryDto>> addAllCategories(@RequestBody List<CategoryDto> categories){
		List<CategoryDto> savedCats = this.categoryService.addAllCategories(categories);
		return new ResponseEntity<>(savedCats,HttpStatus.CREATED);
	}

}
