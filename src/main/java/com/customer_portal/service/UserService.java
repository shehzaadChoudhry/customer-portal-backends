package com.customer_portal.service;

import javax.validation.Valid;

import org.springframework.stereotype.Service;

import com.customer_portal.dto.UserDetailDto;
import com.customer_portal.dto.UserLogin;
import com.customer_portal.entity.UserDetail;

@Service
public interface UserService {

	UserDetailDto verifyUser(UserLogin userLogin);

	UserDetail signupUser(UserDetail user);

	String sendOtpEmail(@Valid String email);

	String verifyOtp(@Valid String email, @Valid String otp);

	String updatePassword(@Valid String email, @Valid String password, @Valid String newPassword);

}
