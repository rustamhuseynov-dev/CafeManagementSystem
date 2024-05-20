package com.inn.cafe.management.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.inn.cafe.management.constants.CafeConstants;
import com.inn.cafe.management.entity.Category;
import com.inn.cafe.management.service.CategoryService;
import com.inn.cafe.management.utils.CafeUtils;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path = "/category")
@RequiredArgsConstructor
public class CategoryRestController {

	private final CategoryService categoryService;

	@PostMapping(path = "/add")
	public ResponseEntity<String> addNewCategory(@RequestBody(required = true) Map<String, String> requesMap) {
		try {
			return categoryService.addNewCategory(requesMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@GetMapping(path = "/get")
	public ResponseEntity<List<Category>> getAllCategory(@RequestParam(required = false) String filterValue) {
		try {
			return categoryService.getAllCategory(filterValue);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@PostMapping(path = "/update")
	public ResponseEntity<String> updateCategory(@RequestBody(required = true) Map<String, String> requestMap) {
		try {
			return categoryService.updateCategory(requestMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
