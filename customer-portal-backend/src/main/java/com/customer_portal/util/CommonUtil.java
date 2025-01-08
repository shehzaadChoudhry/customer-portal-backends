package com.customer_portal.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CommonUtil {
	public static final String AUTHORIZATION_HEADER = "Authorization";
	public static final String REFRESH_HEADER = "RefreshToken";
	public static final String JWT_SECRET = "jearixzusdmpvknb";
	public static final long JWT_EXPIRY_THRESHOLD_MIN = 15;
	public static final long JWT_EXPIRY_MIN = 600;
	public static final long JWT_EXPIRY_MIN_ANDROID = 72000;
	public static final long JWT_REFRESH_EXPIRY_MIN = 600;

	public static String getMD5String(String input) {

		String md5 = null;
		if (null == input)
			return null;
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			digest.update(input.getBytes(), 0, input.length());
			byte[] md5Bytes = digest.digest();
			StringBuilder sb = new StringBuilder();
			for (byte b : md5Bytes) {
				sb.append(String.format("%02x", b));
			}
			md5 = sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return md5;
	}
}
