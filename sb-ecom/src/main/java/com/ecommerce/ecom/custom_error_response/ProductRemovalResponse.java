package com.ecommerce.ecom.custom_error_response;

import com.ecommerce.ecom.dto.ProductDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRemovalResponse {
	
	private ProductDTO productDTO;
	private String status;

}
