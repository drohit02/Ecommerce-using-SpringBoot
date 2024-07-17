package com.ecommerce.ecom.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.ecom.custom_error_response.ProductRemovalResponse;
import com.ecommerce.ecom.dto.ProductDTO;
import com.ecommerce.ecom.model.Product;
import com.ecommerce.ecom.payload.ProductResponse;
import com.ecommerce.ecom.service.ProductService;

@RestController
@RequestMapping("/api")
public class ProductController {

	@Autowired private ProductService productService;
	
	
	@GetMapping("/public/products")
	public ResponseEntity<ProductResponse> findAllProducts() {
		ProductResponse products = this.productService.getAllProducts();
		return new ResponseEntity<>(products,HttpStatus.OK);
	}
	
	@GetMapping("/public/categories/{categoryId}/product")
	public ResponseEntity<ProductResponse> findProductByCategoryId(@PathVariable Long categoryId) {
		ProductResponse products  = this.productService.getProductWithCategoryId(categoryId);
		return new ResponseEntity<>(products,HttpStatus.OK);
	}
	
	@GetMapping("/public/products/keyword/{keyword}")
	public ResponseEntity<ProductResponse> findProductsWithKeyword(@PathVariable String keyword) {
		ProductResponse productResponse  = this.productService.getProductWithKeyword(keyword);
		return new ResponseEntity<>(productResponse,HttpStatus.OK);
	}
	
	@PostMapping("/admin/categories/{categoryId}/product")
	public ResponseEntity<ProductDTO> addProduct(@RequestBody Product product,@PathVariable Long categoryId){
		ProductDTO productDTO = this.productService.addProduct(product,categoryId);
		return new ResponseEntity<ProductDTO>(productDTO,HttpStatus.CREATED);
	}
	
	@PutMapping("/admin/product/productId/{productId}")
	public ResponseEntity<ProductDTO> updateProductData(@RequestBody Product product,@PathVariable Long productId){
		ProductDTO updatedProductDTO = this.productService.updateExitingProductData(product,productId);	
		return new ResponseEntity<ProductDTO>(updatedProductDTO,HttpStatus.OK);
	}
	
	@DeleteMapping("/admin/product/productId/{productId}")
	public ResponseEntity<ProductRemovalResponse> removeProductById(@PathVariable Long productId){
	  ProductDTO productDTO	= this.productService.deleteProductById(productId);
	  ProductRemovalResponse deleteStatus = new ProductRemovalResponse(productDTO,"This product removed successfully!!!");
	  return new ResponseEntity<>(deleteStatus,HttpStatus.OK);
	}
}
