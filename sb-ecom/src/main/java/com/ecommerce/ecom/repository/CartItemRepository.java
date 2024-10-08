package com.ecommerce.ecom.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.ecommerce.ecom.model.Cart;
import com.ecommerce.ecom.model.CartItems;

public interface CartItemRepository extends JpaRepository<CartItems, Long>{
	

	@Query("SELECT ci FROM CartItems ci WHERE ci.cart.id = ?1 AND ci.product.id = ?2")
	CartItems findCartItemsByProductIdAndCartId(Long cartId, Long productId);

	@Modifying
	@Query("DELETE FROM CartItems ci WHERE ci.cart.id = ?1 AND ci.product.id = ?2")
	void deleteCartItemByProductIdAndCartId(Long cartId, Long productId);
	
	

}
