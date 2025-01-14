package com.customer_portal.service;

import javax.validation.Valid;

public interface EmailService {

	String sendOtpEmail(@Valid String email, String otp);

}
