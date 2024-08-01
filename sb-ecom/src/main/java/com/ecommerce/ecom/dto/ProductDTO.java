package com.ecommerce.ecom.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
	
	private Long productId;
	
	@NotBlank(message = "product name is required")
	@Size(min = 5,message = "product name should conatain at-least 10 letters")
	private String productName;
	
	private String image;
	
	@NotBlank(message = "product description is required")
	@Size(min = 5,message = "product description should conatain at-least 30 letters")
	private String description;
	
	@NotNull(message = "price is required")
	@Min(value = 1)
	private double price;
	
	@Min(value = 1)
	@Max(value = 100)
	private double discount;
	
	@Min(value = 1)
	private Integer quantity;
	private double specialPrice;

}
