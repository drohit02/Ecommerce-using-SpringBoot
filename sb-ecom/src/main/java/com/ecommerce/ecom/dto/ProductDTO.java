package com.ecommerce.ecom.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
	
	private Long productId;
	private String productName;
	private String image;
	private String description;
	private double price;
	private double discount;
	private int quantity;
	private double specialPrice;

}
