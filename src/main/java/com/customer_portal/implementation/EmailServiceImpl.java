package com.customer_portal.implementation;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.customer_portal.service.EmailService;

@Service
public class EmailServiceImpl implements EmailService{

	private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	@Value
	("${spring.mail.username}") private String sender;
	
    @Value
    ("${email.otp.subject}") private String emailSubject;

    @Value
    ("${email.otp.message}") private String emailMessage;
	
	@Override
	public String sendOtpEmail(@Valid String email, String otp) {
		try {
			SimpleMailMessage mailMessage = new SimpleMailMessage();
			
			String message = String.format(emailMessage, otp);
			
			mailMessage.setFrom(sender);
			mailMessage.setTo(email);
			mailMessage.setText(message);
			mailMessage.setSubject(emailSubject);
			
			javaMailSender.send(mailMessage);
			
			return "Mail sent Successfully";
			
		} catch (Exception e) {
			logger.error("Exception occurs sendOtpEmail at " + e.getMessage() + " ; ");
		}
		return null;
	}

}
