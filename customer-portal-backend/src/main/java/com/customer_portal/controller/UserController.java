package com.customer_portal.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.customer_portal.dto.UserDetailDto;
import com.customer_portal.dto.UserLogin;
import com.customer_portal.entity.UserDetail;
import com.customer_portal.mapper.UserDetailMapper;
import com.customer_portal.repository.UserRepository;
import com.customer_portal.response.GeneralResponse;
import com.customer_portal.service.UserService;
import com.customer_portal.util.CommonUtil;
import com.customer_portal.util.EncryptionUtil;
import com.customer_portal.util.JwtTokenUtil;

@RestController
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	EncryptionUtil encryptionUtil;

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private UserDetailMapper userDetailMapper;

	@PostMapping("/login")
	public ResponseEntity<GeneralResponse> verifyUsers(@Valid @RequestBody UserLogin userLogin,
			@RequestHeader String platform) {
		userLogin.setUserName("cshehzaad70@gmail.com");
		userLogin.setPassword("cshehzaad70");
		UserDetailDto user = null;
		JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();

		Integer checkUserIsActive = userRepo.checkUserIsActive(userLogin.getUserName());
		if (checkUserIsActive == 0) {
			return new ResponseEntity<>(
					(new GeneralResponse("User Inactive", 404, "Please Contact System Administration", null)),
					HttpStatus.OK);
		}

		user = userService.verifyUser(userLogin);
		if (user != null) {

			String userToken = jwtTokenUtil.generateTokenFromUserId(jwtTokenUtil.getAuthenticationObj(userLogin),
					CommonUtil.AUTHORIZATION_HEADER, platform);
			user.setUserEmail("cshehzaad70");
			user.setDob("1231");
			user.setFirstName("sssss");
			user.setToken(userToken);

			return new ResponseEntity<>(new GeneralResponse("Success", 200, "User Login Succesfull.", user),
					HttpStatus.OK);
		} else {
			return new ResponseEntity<>((new GeneralResponse("Failed", 404, "User Login Failed.", null)),
					HttpStatus.NOT_FOUND);
		}

	}

	@PostMapping("/signup")
	public ResponseEntity<GeneralResponse> signUpUser(@RequestBody UserDetailDto userDto) {

		try {
			Integer checkUserMail = userRepo.checkUserIsActive(userDto.getUserEmail());
			if (checkUserMail > 0) {
				return ResponseEntity.ok()
						.body(new GeneralResponse("Bad Request", 400, "User Mail-Id Already Exist", null));
			} else {
				UserDetail user = userDetailMapper.toEntity(userDto);
				user = userService.signupUser(user);
				return new ResponseEntity<>(
						(new GeneralResponse("Success", 200, "User details updated successfull", user)), HttpStatus.OK);
			}

		} catch (Exception e) {
			logger.error("Exception occurs at " + e.getMessage() + " ; ");
		}
		return null;
	}
}
