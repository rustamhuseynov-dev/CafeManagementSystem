package com.inn.cafe.management.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;
import com.inn.cafe.management.constants.CafeConstants;
import com.inn.cafe.management.dao.CategoryDao;
import com.inn.cafe.management.entity.Category;
import com.inn.cafe.management.jwt.JwtAuthFilter;
import com.inn.cafe.management.service.CategoryService;
import com.inn.cafe.management.utils.CafeUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

	private final CategoryDao dao;

	private final JwtAuthFilter jwtAuthFilter;

	@Override
	public ResponseEntity<String> addNewCategory(Map<String, String> requesMap) {
		try {
			if (jwtAuthFilter.isAdmin()) {
				if (validateCategoryMap(requesMap, false)) {
					dao.save(getCategoryFromMap(requesMap, false));
					return CafeUtils.getResponseEntity("Category added successfully", HttpStatus.OK);
				}
			} else {
				return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			e.printStackTrace();

		}

		return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<List<Category>> getAllCategory(String filterValue) {
		try {
			if (!Strings.isNullOrEmpty(filterValue) && filterValue.equalsIgnoreCase("true")) {
				log.info("inside if");
				return new ResponseEntity<List<Category>>(dao.getAllCategory(), HttpStatus.OK);
			}
			return new ResponseEntity<>(dao.findAll(), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<List<Category>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<String> updateCategory(Map<String, String> requestMap) {
		try {
			if (jwtAuthFilter.isAdmin()) {
				if (validateCategoryMap(requestMap, false)) {
					Optional<Category> optional = dao.findById(Integer.parseInt(requestMap.get("id")));
					if (optional.isPresent()) {
						dao.save(getCategoryFromMap(requestMap, true));
						return CafeUtils.getResponseEntity("Category updated successfully", HttpStatus.OK);
					} else {
						return CafeUtils.getResponseEntity("Category id doesn`t exist", HttpStatus.OK);
					}
				}
				return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
			} else {
				return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private boolean validateCategoryMap(Map<String, String> requesMap, boolean validateId) {
		if (requesMap.containsKey("name")) {
			if (requesMap.containsKey("id") && validateId) {
				return true;
			} else if (!validateId) {
				return true;
			}
		}
		return false;
	}

	private Category getCategoryFromMap(Map<String, String> requestMap, Boolean isAdd) {
		Category category = new Category();
		if (isAdd) {
			category.setId(Integer.parseInt(requestMap.get("id")));
		}

		category.setName(requestMap.get("name"));

		return category;
	}

}
