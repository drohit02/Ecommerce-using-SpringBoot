package com.ecommerce.ecom.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.ecom.model.Category;
import com.ecommerce.ecom.model.Product;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{
	List<Product> findAllByCategory(Category category);
	Page<Product> findAllByProductNameLikeIgnoreCase(String keyword, Pageable pageData);
	Page<Product> findAllByCategoryOrderByPriceAsc(Category category,Pageable pageData);

}
