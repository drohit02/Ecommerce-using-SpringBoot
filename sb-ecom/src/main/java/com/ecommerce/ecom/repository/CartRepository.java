package com.ecommerce.ecom.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ecommerce.ecom.model.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long>{
	
	@Query("SELECT c FROM Cart c WHERE c.user.email = ?1")
	Optional<Cart> findCartByEmail(String email);

	@Query("SELECT c FROM Cart c WHERE c.user.email = ?1 AND c.cartId = ?2")
	Cart findByEmailIdAndCartId(String email, Long cartId);
	
	@Query("SELECT c FROM Cart c JOIN FETCH c.cartItems ci JOIN FETCH ci.product p WHERE p.id = ?1")
	List<Cart> findCartsByProductId(Long productId);

}
