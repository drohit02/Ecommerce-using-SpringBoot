package com.ecommerce.ecom.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.ecom.config.AppConstants;
import com.ecommerce.ecom.dto.CategoryDTO;
import com.ecommerce.ecom.payload.CategoryResponse;
import com.ecommerce.ecom.service.CategoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class CategoryController {

	@Autowired
	private CategoryService categoryService;

	@GetMapping("/public/categories/q")
	public ResponseEntity<CategoryResponse> getAllCatgeories(
			@RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
			@RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE) Integer pageSize,
			@RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_CATEGORIES_BY) String sortBy,
			@RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_ORDER) String sortOrder) {
		CategoryResponse categoryResponse = this.categoryService.getAllCategories(pageNumber, pageSize,sortBy,sortOrder);
		return ResponseEntity.status(HttpStatus.OK).body(categoryResponse);
	}

	@GetMapping("/public/category/{categoryId}")
	public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long categoryId) {
		CategoryDTO categoryDTO = this.categoryService.findCategoryById(categoryId);
		return new ResponseEntity<>(categoryDTO, HttpStatus.OK);
	}

	@PostMapping("/admin/category")
	public ResponseEntity<CategoryDTO> creategory(@Valid @RequestBody CategoryDTO categoryDTO) {
		CategoryDTO savedCategoryDTO = this.categoryService.createCategory(categoryDTO);
		return new ResponseEntity<>(savedCategoryDTO, HttpStatus.OK);
	}

	@DeleteMapping("/admin/category/{categoryId}")
	public ResponseEntity<CategoryDTO> deleteCategory(@PathVariable Long categoryId) {
		CategoryDTO deletedCategoryDTO = this.categoryService.deleteCategory(categoryId);
		return new ResponseEntity<>(deletedCategoryDTO, HttpStatus.OK);
	}

	@PutMapping("/admin/category/{categoryId}")
	public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long categoryId,
			@Valid @RequestBody CategoryDTO category) {
		CategoryDTO updateStatus = this.categoryService.updateCategory(categoryId, category);
		return new ResponseEntity<>(updateStatus, HttpStatus.OK);
	}
}
