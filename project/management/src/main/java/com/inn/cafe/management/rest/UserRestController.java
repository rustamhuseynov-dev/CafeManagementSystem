package com.inn.cafe.management.rest;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inn.cafe.management.constants.CafeConstants;
import com.inn.cafe.management.service.UserService;
import com.inn.cafe.management.utils.CafeUtils;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path = "user")
@RequiredArgsConstructor
public class UserRestController {

	private final UserService service;

	@PostMapping(path = "signup")
	public ResponseEntity<String> signUp(@RequestBody(required = true) Map<String, String> requestMap) {
		try {
			return service.signUp(requestMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@PostMapping(path = "/login")
	public ResponseEntity<String> login(@RequestBody(required = true) Map<String, String> requestMap) {
		try {
			return service.login(requestMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
