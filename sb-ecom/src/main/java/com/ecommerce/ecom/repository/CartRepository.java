package com.ecommerce.ecom.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.ecom.model.Cart;

public interface CartRepository extends JpaRepository<Cart, Long>{

}
