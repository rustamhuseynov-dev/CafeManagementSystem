package com.inn.cafe.management.jwt;

import java.util.ArrayList;
import java.util.Objects;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.inn.cafe.management.dao.UserDao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomerUserDetailsService implements UserDetailsService {

	private final UserDao userDao;

	private com.inn.cafe.management.entity.User userDetail;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.info("inside loadUserbyUsername {}", username);
		userDetail = userDao.findByEmailId(username);
		if (!Objects.isNull(userDetail)) {
			return new User(userDetail.getEmail(), userDetail.getPassword(), new ArrayList<>());
		}

		throw new UsernameNotFoundException("invalid User");
	}

	public com.inn.cafe.management.entity.User getUserDetail() {

	}

}
