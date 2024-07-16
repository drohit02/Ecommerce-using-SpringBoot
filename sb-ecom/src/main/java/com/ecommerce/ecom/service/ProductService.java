package com.ecommerce.ecom.service;

import java.util.List;

import com.ecommerce.ecom.dto.ProductDTO;
import com.ecommerce.ecom.model.Product;
import com.ecommerce.ecom.payload.ProductResponse;

public interface ProductService {

	ProductDTO addProduct(Product product, Long categoryId);

	ProductResponse getAllProducts();

	ProductResponse getProductWithCategoryId(Long categoryId);

}
