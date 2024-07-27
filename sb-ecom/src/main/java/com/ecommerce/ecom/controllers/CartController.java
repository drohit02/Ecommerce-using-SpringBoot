package com.ecommerce.ecom.controllers;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.ecom.custom_error_response.MessageResponse;
import com.ecommerce.ecom.custom_exception.ResourceNotFoundException;
import com.ecommerce.ecom.dto.CartDTO;
import com.ecommerce.ecom.model.Cart;
import com.ecommerce.ecom.repository.CartRepository;
import com.ecommerce.ecom.service.CartService;
import com.ecommerce.ecom.utils.AuthUtils;

@RestController
@RequestMapping("/api/carts")
public class CartController {

	@Autowired
	private CartService cartService;

	@Autowired
	private AuthUtils authUtils;

	@Autowired
	private CartRepository cartRepository;

	@PostMapping("/products/{productId}/quantity/{quantity}")
	public ResponseEntity<CartDTO> addProoductToCart(@PathVariable Long productId, @PathVariable Integer quantity) {
		CartDTO cartDTO = this.cartService.addProductToCart(productId, quantity);
		return new ResponseEntity<CartDTO>(cartDTO, HttpStatus.CREATED);
	}

	@GetMapping
	public ResponseEntity<List<CartDTO>> getAllCarts() {
		List<CartDTO> carts = this.cartService.findAllCarts();
		return new ResponseEntity<List<CartDTO>>(carts, HttpStatus.OK);
	}

	@GetMapping("/users/cart")
	public ResponseEntity<CartDTO> getCartByUserId() {
		String email = this.authUtils.loggedInEmail();
		Cart savedCart = this.cartRepository.findCartByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("Cart", "email", email));
		CartDTO cart = this.cartService.findUserCartByUserId(email,savedCart.getCartId());
		return new ResponseEntity<CartDTO>(cart, HttpStatus.OK);
	}
	
	@PutMapping("/products/{productId}/quantity/{operation}")
	public ResponseEntity<CartDTO> updateCartProduct(@PathVariable Long productId,@PathVariable String operation){
		CartDTO cartDTO = this.cartService.updateProductQuantityInCart(productId,operation.equalsIgnoreCase("delete")?-1 : 1);
		return new ResponseEntity<CartDTO>(cartDTO,HttpStatus.OK);
	}
	
	@DeleteMapping("/{cartId}/product/{productId}")
	public ResponseEntity<MessageResponse> deleteProductFromCart(@PathVariable Long cartId,@PathVariable Long productId){
		String status = this.cartService.deleteProductFromCart(cartId,productId);
		return new ResponseEntity<>(new MessageResponse(LocalDateTime.now(),status),HttpStatus.OK);
		
	}

}
