package com.ecommerce.ecom.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {
	
	private Long categoryId;
	
	@NotBlank(message = "category name is required")
	@Size(min = 5,message = "category name must have at least 5 character")
	private String categoryName;
}
