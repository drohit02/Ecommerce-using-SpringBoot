package com.ecommerce.ecom.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ecommerce.ecom.model.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address,Long> {

	@Query("SELECT a FROM Address a WHERE user.id = ?1")
	List<Address> findAllByUserId(Long userId);
}