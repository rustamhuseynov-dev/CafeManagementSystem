package com.inn.cafe.management.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
	
	private final JwtService jwtService;
	
	private final CustomerUserDetailsService customerUserDetailsService;
	
	Claims claims = null;
	private String userName = null;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		if (request.getServletPath().matches("/user/login|/user/forgotPassword|/user/signup")) {
			filterChain.doFilter(request, response);
		}else {
			String authorizationHeader = request.getHeader("Authorization");
			String token = null;
			
			if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
				token = authorizationHeader.substring(7);
				userName = jwtService.extractUsername(token);
				claims = jwtService.extractAllClaims(token);
			}
			
			if (userName != null && SecurityContextHolder.getContext().getAuthentication()==null) {
				UserDetails user = customerUserDetailsService.loadUserByUsername(userName);
				if (jwtService.validateToken(token, user)) {
					UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
							new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
					usernamePasswordAuthenticationToken
					.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					
					
					SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
				}
			}
			filterChain.doFilter(request, response);
		}		
		}

        public Boolean isAdmin() {
	       return "admin".equalsIgnoreCase((String) claims.get("role"));
        }
        public Boolean isUser() {
 	       return "user".equalsIgnoreCase((String) claims.get("role"));
        }
        public String getCurrentUser() {
        	return userName;
        }

}
