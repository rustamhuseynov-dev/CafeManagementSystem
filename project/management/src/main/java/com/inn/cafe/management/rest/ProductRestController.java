package com.inn.cafe.management.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.inn.cafe.management.constants.CafeConstants;
import com.inn.cafe.management.service.ProductService;
import com.inn.cafe.management.utils.CafeUtils;
import com.inn.cafe.management.wrapper.ProductWrapper;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path = "/product")
@RequiredArgsConstructor
public class ProductRestController {

	private final ProductService productService;

	@PostMapping(path = "/add")
	public ResponseEntity<String> addNewProduct(@RequestBody Map<String, String> requestMap) {
		try {
			return productService.addNewProduct(requestMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@GetMapping(path = "/get")
	public ResponseEntity<List<ProductWrapper>> getAllProduct() {
		try {
			return productService.getAllProduct();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@PostMapping(path = "/update")
	public ResponseEntity<String> updateProduct(@RequestBody(required = true) Map<String, String> requestMap) {
		try {
			return productService.updateProduct(requestMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@PostMapping(path = "/delete/{id}")
	public ResponseEntity<String> deleteProduct(@PathVariable Integer id){
		try {
			return productService.deleteProduct(id);
		}catch (Exception ex){
			ex.printStackTrace();
		}
		return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@PostMapping(path = "/update-status")
	public ResponseEntity<String> updateStatus(@RequestBody Map<String, String> requestMap){
		try {
			return productService.updateStatus(requestMap);
		}catch (Exception ex){
			ex.printStackTrace();
		}
		return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@GetMapping(path = "/{id}")
	public ResponseEntity<List<ProductWrapper>> getByCategory(@PathVariable Integer id){
		try {
			return productService.getByCategory(id);
		}catch (Exception ex){
			ex.printStackTrace();
		}
		return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@GetMapping(path = "/get-by-id/{id}")
	public ResponseEntity<ProductWrapper> getProductById(@PathVariable Integer id){
		try {
			return productService.getProductById(id);
		}catch (Exception e){
			e.printStackTrace();
		}
		return new ResponseEntity<>(new ProductWrapper(),HttpStatus.INTERNAL_SERVER_ERROR);
	}


}
