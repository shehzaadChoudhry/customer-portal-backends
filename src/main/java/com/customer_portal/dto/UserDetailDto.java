package com.customer_portal.dto;

import java.sql.Date;

import lombok.Data;

@Data
public class UserDetailDto {
	private Long id;
	private String firstName;
	private String lastName;
	private String username;
	private String userEmail;
	private String userPassword;
	private String mobile;
	private String dob;
	private String token;
	private Date loginTime;
}
