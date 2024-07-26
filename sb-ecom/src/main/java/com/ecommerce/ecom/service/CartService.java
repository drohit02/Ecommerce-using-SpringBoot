package com.ecommerce.ecom.service;

import java.util.List;

import com.ecommerce.ecom.dto.CartDTO;

import jakarta.transaction.Transactional;

public interface CartService {
	
	CartDTO addProductToCart(Long productId,Integer quantity);

	List<CartDTO> findAllCarts();

	CartDTO findUserCartByUserId(String email, Long cartId);

	@Transactional
	CartDTO updateProductQuantityInCart(Long productId, Integer quantity);

	String deleteProductFromCart(Long cartId, Long productId);

	
	
	
	
	
}
