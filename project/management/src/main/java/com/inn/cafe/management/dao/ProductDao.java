package com.inn.cafe.management.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inn.cafe.management.entity.Product;
import com.inn.cafe.management.wrapper.ProductWrapper;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface ProductDao extends JpaRepository<Product, Integer> {

	List<ProductWrapper> getAllProduct();

	@Modifying
	@Transactional
	Integer updateProductStatus(@Param("status")String status,@Param("id") Integer id);

	List<ProductWrapper> getProductByCategory(@Param("id") Integer id);

	ProductWrapper getProductById(@Param("id") Integer id);
}
