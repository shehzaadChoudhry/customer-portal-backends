package com.customer_portal.jwtConfig;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

import com.customer_portal.util.JwtTokenUtil;

public class JwtTokenValidationFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		JwtTokenUtil jwtUtils = new JwtTokenUtil();
		jwtUtils.validateJwtToken(request, response);

		filterChain.doFilter(request, response);
	}

}
