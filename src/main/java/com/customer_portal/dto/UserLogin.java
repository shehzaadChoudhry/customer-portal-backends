package com.customer_portal.dto;

import javax.validation.constraints.NotEmpty;

public class UserLogin {

	@NotEmpty(message = "User name field must not be null!!")
	private String userName;
	@NotEmpty(message = "Password field must not be null!!")
	private String password;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
