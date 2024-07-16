package com.ecommerce.ecom.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.ecom.service.ProductService;

@RestController
public class ProductController {

	@Autowired private ProductService productService;
}
