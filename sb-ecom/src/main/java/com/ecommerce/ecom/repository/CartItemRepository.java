package com.ecommerce.ecom.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.ecom.model.CartItems;

public interface CartItemRepository extends JpaRepository<CartItems, Long>{

}
