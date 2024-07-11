package com.ecommerce.ecom.payload;

import java.util.ArrayList;
import java.util.List;

import com.ecommerce.ecom.dto.CategoryDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse {

	private List<CategoryDTO> content = new ArrayList<>();
	private Integer pageNumber;
	private Integer pageSize;
	private Long totalElements;
	private Integer totalPages;
	private boolean lasPage;
	

}
