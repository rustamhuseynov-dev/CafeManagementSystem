package com.inn.cafe.management.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.inn.cafe.management.constants.CafeConstants;
import com.inn.cafe.management.dao.UserDao;
import com.inn.cafe.management.entity.User;
import com.inn.cafe.management.jwt.CustomerUserDetailsService;
import com.inn.cafe.management.jwt.JwtAuthFilter;
import com.inn.cafe.management.jwt.JwtService;
import com.inn.cafe.management.service.UserService;
import com.inn.cafe.management.utils.CafeUtils;
import com.inn.cafe.management.utils.EmailUtils;
import com.inn.cafe.management.wrapper.UserWrapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

	private final UserDao dao;
	
	private final AuthenticationManager authenticationManager;
	
	private final CustomerUserDetailsService customerUserDetailsService;
	
	private final JwtService jwtService;

	private final JwtAuthFilter  jwtAuthFilter;
	
	private final EmailUtils emailUtils;
	@Override
	public ResponseEntity<String> signUp(Map<String, String> requestMap) {
		log.info("Inside signup {}", requestMap);
		try {
			if (validateSignUpMap(requestMap)) {
				User user = dao.findByEmailId(requestMap.get("email"));
				if (Objects.isNull(user)) {
					dao.save(getUserFromMap(requestMap));
					return CafeUtils.getResponseEntity("Successfully Registered.", HttpStatus.OK);
				} else {
					return CafeUtils.getResponseEntity("Email already exists", HttpStatus.BAD_REQUEST);
				}
			} else {
				return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private boolean validateSignUpMap(Map<String, String> requestMap) {
		if (requestMap.containsKey("name") && requestMap.containsKey("contactNumber") && requestMap.containsKey("email")
				&& requestMap.containsKey("password")) {
			return true;
		}
		return false;
	}

	private User getUserFromMap(Map<String, String> requestMap) {
		User user = new User();
		user.setName(requestMap.get("name"));
		user.setContactNumber(requestMap.get("contactNumber"));
		user.setEmail(requestMap.get("email"));
		user.setPassword(requestMap.get("password"));
		user.setStatus("false");
		user.setRole("user");
		return user;
	}
	
	@Override
	public ResponseEntity<String> login(Map<String, String> requestMap) {
		log.info("Inside Login");
		try {
			Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestMap.get("email"), requestMap.get("password"))
					);
			if (auth.isAuthenticated()) {
				if (customerUserDetailsService.getUserDetail().getStatus().equalsIgnoreCase("true")) {
					return new ResponseEntity<String>("{\"token\":\""+
							jwtService.generateToken(customerUserDetailsService
									.getUserDetail()
									.getEmail(),
								customerUserDetailsService
								.getUserDetail()
								.getRole()) + "\"}",HttpStatus.OK);
				}
				else {
					return new ResponseEntity<String>("{\"message\":\""+"Wait for admin approvel."+"\"}",
							HttpStatus.BAD_REQUEST);
				}
			}
		} catch (Exception e) {
			log.error("{}",e);
		}
		return new ResponseEntity<String>("{\"message\":\""+"Bad Credentials."+"\"}",
				HttpStatus.BAD_REQUEST);
	}
	
	@Override
	public ResponseEntity<List<UserWrapper>> getAllUser() {
		try {
			if (jwtAuthFilter.isAdmin()) {
				return new ResponseEntity<>(dao.getAllUser(),HttpStatus.OK);
			}else {
				return new ResponseEntity<>(new ArrayList<>(),HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@Override
	public ResponseEntity<String> update(Map<String, String> requestMap) {
		try {
			if (jwtAuthFilter.isAdmin()) {
				Optional<User> optional = dao.findById(Integer.parseInt(requestMap.get("id")));
				if (optional.isPresent()) {
					dao.updateStatus(requestMap.get("status"),Integer.parseInt(requestMap.get("id")));
					sendMailToAllAdmin(requestMap.get("status"),optional.get().getEmail(),dao.getAllAdmin());
					return CafeUtils.getResponseEntity("User status updated successfully.", HttpStatus.OK);
				}
				else {
					return CafeUtils.getResponseEntity("User id doesn`t not exist", HttpStatus.OK);
				}
			}
			else {
				return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private void sendMailToAllAdmin(String status, String user, List<String> allAdmin) {
		allAdmin.remove(jwtAuthFilter.getCurrentUser());
		if (status != null && status.equalsIgnoreCase("true")) {
			emailUtils.sendSimpleMessage(jwtAuthFilter.getCurrentUser(),"Account Approved","USER:-"+user+"\n is approved by \nADMIN:-"+jwtAuthFilter.getCurrentUser(),allAdmin);
		}
		else {
			emailUtils.sendSimpleMessage(jwtAuthFilter.getCurrentUser(),"Account Disabled","USER:-"+user+"\n is disabled by \nADMIN:-"+jwtAuthFilter.getCurrentUser(),allAdmin);
		}
		
	}

}
