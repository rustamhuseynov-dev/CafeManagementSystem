package com.inn.cafe.management.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inn.cafe.management.entity.Category;

public interface CategoryDao extends JpaRepository<Category, Integer> {

	List<Category> getAllCategory();

}
