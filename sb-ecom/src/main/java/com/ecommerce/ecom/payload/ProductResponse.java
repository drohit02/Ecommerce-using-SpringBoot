package com.ecommerce.ecom.payload;

import java.util.List;

import com.ecommerce.ecom.dto.ProductDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
	
	private List<ProductDTO> products;

}
