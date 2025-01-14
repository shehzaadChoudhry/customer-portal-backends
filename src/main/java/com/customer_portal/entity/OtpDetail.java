package com.customer_portal.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "otp_detail")
public class OtpDetail {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String userEmail;
	private String otp;
	private long expiryTime;

	public OtpDetail(String userEmail, String otp, long expiryTime) {
		this.userEmail = userEmail;
		this.otp = otp;
		this.expiryTime = expiryTime;
	}

	public OtpDetail() {
	}
}
