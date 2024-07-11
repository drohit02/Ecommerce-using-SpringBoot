package com.ecommerce.ecom.serviceimpl;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ecommerce.ecom.custom_exception.APIException;
import com.ecommerce.ecom.custom_exception.ResourceNotFoundException;
import com.ecommerce.ecom.dto.CategoryDTO;
import com.ecommerce.ecom.model.Category;
import com.ecommerce.ecom.payload.CategoryResponse;
import com.ecommerce.ecom.repository.CategoryRepository;
import com.ecommerce.ecom.service.CategoryService;

import jakarta.validation.Valid;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize) {

		/* Pagination logic */

		Pageable pageData = PageRequest.of(pageNumber, pageSize);
		Page<Category> categoryPage = this.categoryRepository.findAll(pageData);
		List<Category> categories = categoryPage.getContent();

		if (categories.isEmpty())
			throw new APIException("No category is present");

		/*
		 * Category->CategoryDTO->CategoryResponse Decoupling model from
		 * Request-Response
		 * 
		 * 1 . Category to CategoryDTO (Model to DTO)
		 */
		List<CategoryDTO> categoryDTO = categories.stream()
				.map(category -> modelMapper.map(category, CategoryDTO.class)).toList();

		// 2 . CategoryDTO to CategoryResponse (DTO to Payload{i.e. Response})
		CategoryResponse categoryResponse = new CategoryResponse();
		
		/*pagination metadata set here*/
		categoryResponse.setContent(categoryDTO);
		categoryResponse.setPageNumber(categoryPage.getNumber());
		categoryResponse.setPageSize(categoryPage.getSize());
		categoryResponse.setTotalElements(categoryPage.getTotalElements());
		categoryResponse.setTotalPages(categoryPage.getTotalPages());
		categoryResponse.setLasPage(categoryPage.isLast());
		return categoryResponse;
	}

	@Override
	public CategoryDTO createCategory(CategoryDTO categoryDTO) {
		Optional<Category> existingCategory = this.categoryRepository.findByCategoryName(categoryDTO.getCategoryName());
		if (existingCategory.isPresent())
			throw new APIException("Category already present!!");

		Category category = modelMapper.map(categoryDTO, Category.class);
		category = this.categoryRepository.save(category);
		categoryDTO = modelMapper.map(category, CategoryDTO.class);
		return categoryDTO;
	}

	@Override
	public CategoryDTO deleteCategory(Long categoryId) {

		Optional<Category> findCategory = this.categoryRepository.findById(categoryId);

		Category savedCategory = findCategory
				.orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

		this.categoryRepository.delete(savedCategory);
		CategoryDTO deleteCategoryDTO = modelMapper.map(savedCategory, CategoryDTO.class);
		return deleteCategoryDTO;
	}

	@Override
	public CategoryDTO updateCategory(Long categoryId, @Valid CategoryDTO categoryDTO) {

		Optional<Category> findCategory = this.categoryRepository.findById(categoryId);

		Category updateCategory = findCategory
				.orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

		updateCategory.setCategoryName(categoryDTO.getCategoryName());
		updateCategory = this.categoryRepository.save(updateCategory);
		return modelMapper.map(updateCategory, CategoryDTO.class);
	}

	@Override
	public CategoryDTO findCategoryById(Long categoryId) {
		Category category = this.categoryRepository.findById(categoryId)
				.orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));
		return modelMapper.map(category, CategoryDTO.class);
	}

}
