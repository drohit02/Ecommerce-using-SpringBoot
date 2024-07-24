package com.ecommerce.ecom.dto;

import java.util.ArrayList;
import java.util.List;

import com.ecommerce.ecom.model.Product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDTO {
	
	private Long cartId;
	private Double totalPrice = 0.0;
	private List<ProductDTO> products = new ArrayList<>();
			

}
