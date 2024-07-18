package com.ecommerce.ecom.controllers;

import java.io.IOException;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ecommerce.ecom.custom_error_response.ProductRemovalResponse;
import com.ecommerce.ecom.dto.ProductDTO;
import com.ecommerce.ecom.payload.ProductResponse;
import com.ecommerce.ecom.service.ProductService;

import jakarta.validation.Valid;

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
	public ResponseEntity<ProductDTO> addProduct(@Valid @RequestBody ProductDTO productDTO,@PathVariable Long categoryId){
		ProductDTO savedProductDTO = this.productService.addProduct(productDTO,categoryId);
		return new ResponseEntity<ProductDTO>(savedProductDTO,HttpStatus.CREATED);
	}
	
	@PutMapping("/admin/product/{productId}")
	public ResponseEntity<ProductDTO> updateProductData(@Valid @RequestBody ProductDTO productDTO,@PathVariable Long productId){
		ProductDTO updatedProductDTO = this.productService.updateExitingProductData(productDTO,productId);	
		return new ResponseEntity<ProductDTO>(updatedProductDTO,HttpStatus.OK);
	}
	
	@PutMapping("/admin/product/{productId}/image")
	public ResponseEntity<ProductDTO> updateProductImage(@PathVariable Long productId, @RequestParam("image") MultipartFile image) throws IOException{
		ProductDTO updateProductDTO = this.productService.updateProductWithImage(productId,image);
		return new ResponseEntity<>(updateProductDTO,HttpStatus.OK);
	}
	
	@DeleteMapping("/admin/product/{productId}")
	public ResponseEntity<ProductRemovalResponse> removeProductById(@PathVariable Long productId){
	  ProductDTO productDTO	= this.productService.deleteProductById(productId);
	  ProductRemovalResponse deleteStatus = new ProductRemovalResponse(productDTO,"This product removed successfully!!!");
	  return new ResponseEntity<>(deleteStatus,HttpStatus.OK);
	}
}
