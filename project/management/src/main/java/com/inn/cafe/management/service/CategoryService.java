package com.inn.cafe.management.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.inn.cafe.management.entity.Category;

public interface CategoryService {

	ResponseEntity<String> addNewCategory(Map<String, String> requesMap);

	ResponseEntity<List<Category>> getAllCategory(String filterValue);

	ResponseEntity<String> updateCategory(Map<String, String> requestMap);

}
