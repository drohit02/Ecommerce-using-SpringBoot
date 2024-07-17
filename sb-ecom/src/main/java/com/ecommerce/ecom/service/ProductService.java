package com.ecommerce.ecom.service;

import com.ecommerce.ecom.dto.ProductDTO;
import com.ecommerce.ecom.payload.ProductResponse;

public interface ProductService {

	ProductDTO addProduct(ProductDTO productDTO, Long categoryId);

	ProductResponse getAllProducts();

	ProductResponse getProductWithCategoryId(Long categoryId);

	ProductResponse getProductWithKeyword(String keyword);

	ProductDTO updateExitingProductData(ProductDTO productDTO, Long productId);

	ProductDTO deleteProductById(Long productId);

}
