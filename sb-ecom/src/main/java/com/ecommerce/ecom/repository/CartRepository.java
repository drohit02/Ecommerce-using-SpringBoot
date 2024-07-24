package com.ecommerce.ecom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ecommerce.ecom.model.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long>{
	
	@Query("SELECT c FROM Cart c WHERE c.user.email = ?1")
	Cart findCartByEmail(String email);

}
