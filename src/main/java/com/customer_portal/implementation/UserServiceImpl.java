package com.customer_portal.implementation;

import java.util.concurrent.TimeUnit;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.customer_portal.dto.UserDetailDto;
import com.customer_portal.dto.UserLogin;
import com.customer_portal.entity.OtpDetail;
import com.customer_portal.entity.UserDetail;
import com.customer_portal.mapper.UserDetailMapper;
import com.customer_portal.repository.OtpRepository;
import com.customer_portal.repository.UserRepository;
import com.customer_portal.service.EmailService;
import com.customer_portal.service.UserService;
import com.customer_portal.util.CommonUtil;

@Service
public class UserServiceImpl implements UserService {

	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	UserRepository userRepo;

	@Autowired
	UserDetailMapper userDetailMapper;

	@Autowired
	OtpRepository otpRepository;

	@Autowired
	EmailService emailService;

	@Override
	public UserDetailDto verifyUser(UserLogin userLogin) {
		logger.info("Verify User method " + userLogin);
		com.customer_portal.entity.UserDetail user = null;
		UserDetailDto userDto = null;

		try {
			logger.info(
					"user " + userLogin.getUserName() + " pass " + CommonUtil.getMD5String(userLogin.getPassword()));
			user = userRepo.findUserByUserNameAndPassword(userLogin.getUserName(),
					CommonUtil.getMD5String(userLogin.getPassword()));

			if (user != null) {
				user.setLoginTime(new java.util.Date());
				user = userRepo.save(user);
				userDto = userDetailMapper.toDTO(user);
			}
		} catch (Exception e) {
			logger.error("Error in verifyUser method " + e.getMessage());
			e.printStackTrace();
		}
		return userDto;
	}

	@Override
	public com.customer_portal.entity.UserDetail signupUser(com.customer_portal.entity.UserDetail user) {
		if (user.getUserPassword() != null) {
			String password = CommonUtil.getMD5String(user.getUserPassword());
			user.setUserPassword(password);
		}
		return userRepo.save(user);
	}

	@Override
	public String sendOtpEmail(@Valid String email) {
		try {
			String otp = CommonUtil.generateOtp();
			OtpDetail otpDetail = new OtpDetail(email, otp,
					System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(CommonUtil.OTP_EXPIRY_MINUTES));

			otpRepository.save(otpDetail);

			String mailStatus = emailService.sendOtpEmail(email, otp);

			return mailStatus;

		} catch (Exception e) {
			logger.error("Exception occurs sendOtpEmail at " + e.getMessage() + " ; ");
		}
		return null;
	}

	@Override
	public String verifyOtp(@Valid String email, @Valid String otp) {
		String response = "";
		try {
			OtpDetail otpDetail = otpRepository.findOtpByEmailOtp(email, otp);
			if (otpDetail != null) {
				response = "Otp verified Successfully.";

				otpRepository.delete(otpDetail);
			} else {
				response = "Incorrect Otp, please try again";
			}
		} catch (Exception e) {
			logger.error("Exception occurs verifyOtp at " + e.getMessage() + " ; ");
		}
		return response;
	}

	@Override
	public String updatePassword(@Valid String email, @Valid String password, @Valid String newPassword) {
		String response = "";
		try {
			UserDetail user = userRepo.findUserByUserNameAndPassword(email, CommonUtil.getMD5String(password));
			if (user != null) {
				user.setUserPassword(CommonUtil.getMD5String(newPassword));
				userRepo.save(user);
				response = "Password updated successfully.";
			} else {
				response = "Old password does not matched";
			}
		} catch (Exception e) {
			logger.error("Exception occurs updatePassword at " + e.getMessage() + " ; ");
		}
		return response;
	}
}
