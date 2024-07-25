package com.ecommerce.ecom.service;

import java.util.List;

import com.ecommerce.ecom.dto.CartDTO;

public interface CartService {
	
	CartDTO addProductToCart(Long productId,Integer quantity);

	List<CartDTO> findAllCarts();

	CartDTO findUserCartByUserId(String email, Long cartId);

}
