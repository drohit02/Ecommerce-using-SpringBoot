package com.ecommerce.ecom.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.ecom.model.Role;

public interface RoleRepository extends JpaRepository<Role, Integer>{

	Optional<Role> findByRoleName(String string);

}
