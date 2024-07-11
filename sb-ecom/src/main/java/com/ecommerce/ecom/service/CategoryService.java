package com.ecommerce.ecom.service;

import java.util.List;

import com.ecommerce.ecom.dto.CategoryDTO;
import com.ecommerce.ecom.model.Category;
import com.ecommerce.ecom.payload.CategoryResponse;

public interface CategoryService {
	
	CategoryResponse getAllCategories();
	CategoryDTO createCategory(CategoryDTO categoryDTO);
	
	CategoryDTO deleteCategory(Long categoryId);
	CategoryDTO updateCategory(Long categoryId, CategoryDTO categoryDTO);
	
	CategoryDTO findCategoryById(Long categoryId);

}
