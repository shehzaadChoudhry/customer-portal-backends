package com.customer_portal.implementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.customer_portal.dto.UserDetailDto;
import com.customer_portal.dto.UserLogin;
import com.customer_portal.repository.UserRepository;
import com.customer_portal.service.UserService;
import com.customer_portal.util.CommonUtil;

@Service
public class UserServiceImpl implements UserService {

	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	UserRepository userRepo;

	@Override
	public UserDetailDto verifyUser(UserLogin userLogin) {
		logger.info("Verify User method " + userLogin);
		com.customer_portal.entity.UserDetail user = null;
		UserDetailDto userDto = null;

		try {
			user = userRepo.findUserByUserNameAndPassword(userLogin.getUserName(),
					CommonUtil.getMD5String(userLogin.getPassword()));
			
			if(user != null) {
				user.setLoginTime(new java.util.Date());
				user = userRepo.save(user);
			}
		} catch (Exception e) {
			logger.error("Error in verifyUser method " + e.getMessage());
			e.printStackTrace();
		}
		return userDto;
	}

	@Override
	public com.customer_portal.entity.UserDetail signupUser(com.customer_portal.entity.UserDetail user) {
		
		return userRepo.save(user);
	}
}
