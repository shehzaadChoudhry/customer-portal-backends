package com.customer_portal.util;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.customer_portal.dto.SecurityUser;
import com.customer_portal.dto.UserLogin;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtTokenUtil {

	EncryptionUtil encryptionUtil;

	private static final Logger logger = LoggerFactory.getLogger(JwtTokenUtil.class);

	public JwtTokenUtil() {
		encryptionUtil = new EncryptionUtil();
	}

	public Claims getJwtCLaimsData(String token) {
		return Jwts.parser().setSigningKey(CommonUtil.JWT_SECRET.getBytes(StandardCharsets.UTF_8)).parseClaimsJws(token)
				.getBody();
	}

	public void validateJwtToken(HttpServletRequest request, HttpServletResponse response) {
		String token = request.getHeader(CommonUtil.AUTHORIZATION_HEADER);
		if (token != null) {
			try {
				Claims claims = getJwtCLaimsData(encryptionUtil.decrypt(token));

				String userName =  (String) claims.get("email");
 
				UserLogin userLogin =  new UserLogin();
				userLogin.setUserName(userName);
				SecurityContextHolder.getContext().setAuthentication(getAuthenticationObj(userLogin));
			} catch (SignatureException e) {
				logger.error("Invalid JWT signature: {}", e.getMessage());
			} catch (MalformedJwtException e) {
				logger.error("Invalid JWT token: {}", e.getMessage());
			} catch (ExpiredJwtException e) {
				logger.info("JWT token is expired {}", e.getMessage());
			} catch (UnsupportedJwtException e) {
				logger.error("JWT token is unsupported: {}", e.getMessage());
			} catch (IllegalArgumentException e) {
				logger.error("JWT claims string is empty: {}", e.getMessage());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public Authentication getAuthenticationObj(UserLogin userLogin) {
		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority(userLogin.getUserName()));
		return new UsernamePasswordAuthenticationToken(userLogin, null, authorities);
	}

	public String generateTokenFromUserId(Authentication userDetails, String tokenType) {
		
		Date expiryTime = null;
		
		 expiryTime = new Date((new Date()).getTime() + TimeUnit.MINUTES.toMillis(CommonUtil.JWT_EXPIRY_MIN));
		 
		 if (CommonUtil.REFRESH_HEADER.equals(tokenType)) {
				expiryTime = new Date(
						(new Date()).getTime() + TimeUnit.MINUTES.toMillis(CommonUtil.JWT_REFRESH_EXPIRY_MIN));
			}
		
		if (CommonUtil.REFRESH_HEADER.equals(tokenType)) {
			expiryTime = new Date(
					(new Date()).getTime() + TimeUnit.MINUTES.toMillis(CommonUtil.JWT_REFRESH_EXPIRY_MIN));
		}

		UserLogin userLogin = (UserLogin) userDetails.getPrincipal();
		
		String token = Jwts.builder().setSubject(tokenType).setIssuedAt(new Date()).setExpiration(expiryTime)
				.claim("expiryTime", expiryTime)
				.claim("userName", userLogin.getUserName())
				.claim("authorities", getUserRoles(userDetails.getAuthorities()))
				.signWith(SignatureAlgorithm.HS256, CommonUtil.JWT_SECRET.getBytes(StandardCharsets.UTF_8)).compact();
		return encryptionUtil.encrypt(token);

	}

	private String getUserRoles(Collection<? extends GrantedAuthority> collection) {
		Set<String> authoritiesSet = new HashSet<>();
		for (GrantedAuthority authority : collection) {
			authoritiesSet.add(authority.getAuthority());
		}
		return String.join(",", authoritiesSet);
	}
}
