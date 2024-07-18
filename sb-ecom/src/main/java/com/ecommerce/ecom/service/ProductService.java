package com.ecommerce.ecom.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.ecommerce.ecom.dto.ProductDTO;
import com.ecommerce.ecom.payload.ProductResponse;

public interface ProductService {

	ProductDTO addProduct(ProductDTO productDTO, Long categoryId);

	ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

	ProductResponse getProductWithCategoryId(Long categoryId);

	ProductResponse getProductWithKeyword(String keyword);

	ProductDTO updateExitingProductData(ProductDTO productDTO, Long productId);

	ProductDTO deleteProductById(Long productId);

	ProductDTO updateProductWithImage(Long productId, MultipartFile image) throws IOException;

}
