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
import org.springframework.web.bind.annotation.RequestParam;
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
	public ResponseEntity<GeneralResponse> verifyUsers(@Valid @RequestBody UserLogin userLogin) {
		UserDetailDto user = null;
		JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();

		Integer checkUserIsActive = userRepo.checkUserIsActive(userLogin.getUserName());
		logger.info("active " + checkUserIsActive + " user " + userLogin.getUserName());
		if (checkUserIsActive == 0) {
			return new ResponseEntity<>(
					(new GeneralResponse("User Inactive", 404, "Please Contact System Administration", null)),
					HttpStatus.OK);
		}

		user = userService.verifyUser(userLogin);
		if (user != null) {
			String userToken = jwtTokenUtil.generateTokenFromUserId(jwtTokenUtil.getAuthenticationObj(userLogin),
					CommonUtil.AUTHORIZATION_HEADER);
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

	@PostMapping("forgotPassword")
	public ResponseEntity<GeneralResponse> forgotPassword(@Valid @RequestParam String email) {

		try {
			Integer checkUserIsActive = userRepo.checkUserIsActive(email);
			if (checkUserIsActive == 0) {
				return new ResponseEntity<>((new GeneralResponse("No user found", 404,
						"Please Contact System Administration/Please Sign up", null)), HttpStatus.OK);
			} else {
				String response = userService.sendOtpEmail(email);
				return new ResponseEntity<>(
						(new GeneralResponse("Success", 200, "User details updated successfull", response)),
						HttpStatus.OK);
			}
		} catch (Exception e) {
			logger.error("Exception occurs at " + e.getMessage() + " ; ");
		}
		return null;
	}

	@PostMapping("verifyOtp")
	public ResponseEntity<GeneralResponse> verifyOtp(@Valid @RequestParam String email,
			@Valid @RequestParam String otp) {

		try {
			String verifyOtp = userService.verifyOtp(email, otp);
			if (verifyOtp.equalsIgnoreCase("Otp verified Successfully.")) {
				return new ResponseEntity<>((new GeneralResponse("Success", 200, "Otp Verified Successfully", email)),
						HttpStatus.OK);
			} else {
				return ResponseEntity.ok().body(new GeneralResponse("Bad Request", 400, verifyOtp, email));
			}
		} catch (Exception e) {
			logger.error("Exception occurs at " + e.getMessage() + " ; ");
		}
		return null;
	}

	@PostMapping("updatePassword")
	public ResponseEntity<GeneralResponse> updatePassword(@Valid @RequestParam String email,
			@Valid @RequestParam String password, @Valid @RequestParam String newPassword) {
		String response = userService.updatePassword(email, password, newPassword);
		if (response.equalsIgnoreCase("Password updated successfully.")) {
			return new ResponseEntity<>((new GeneralResponse("Success", 200, "Password updated successfully.", email)),
					HttpStatus.OK);
		} else {
			return ResponseEntity.ok().body(new GeneralResponse("Bad Request", 400, response, email));
		}
	}
}
