package com.ecommerce.ecom.controllers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.ecom.dto.ProductDTO;
import com.ecommerce.ecom.model.Product;
import com.ecommerce.ecom.payload.ProductResponse;
import com.ecommerce.ecom.service.ProductService;

@RestController
@RequestMapping("/api")
public class ProductController {

	@Autowired private ProductService productService;
	@Autowired private ModelMapper modelMapper;
	
	
	@GetMapping("/public/products")
	public ResponseEntity<ProductResponse> findAllProducts() {
		System.out.println("Into the controller");
		ProductResponse products = this.productService.getAllProducts();
		return new ResponseEntity<>(products,HttpStatus.OK);
	}
	
	@PostMapping("/admin/categories/{categoryId}/product")
	public ResponseEntity<ProductDTO> addProduct(@RequestBody Product product,@PathVariable Long categoryId){
		ProductDTO productDTO = this.productService.addProduct(product,categoryId);
		return new ResponseEntity<ProductDTO>(productDTO,HttpStatus.CREATED);
	}
	
}
