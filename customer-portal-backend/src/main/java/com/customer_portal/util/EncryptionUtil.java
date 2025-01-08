package com.customer_portal.util;

import java.nio.charset.StandardCharsets;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;

@Component
public class EncryptionUtil {
	private String algo = "AES/CBC/PKCS5Padding";
	
	public String encrypt(String value) {
		try {
			IvParameterSpec iv = new IvParameterSpec(CommonUtil.JWT_SECRET.getBytes(StandardCharsets.UTF_8));
			SecretKeySpec skeySpec = new SecretKeySpec(CommonUtil.JWT_SECRET.getBytes(StandardCharsets.UTF_8), "AES");
			
			Cipher cipher = Cipher.getInstance(algo);
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
			
			byte[] encrypted = cipher.doFinal(value.getBytes());
		    return Base64.encodeBase64String(encrypted);
		    
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String decrypt(String encrypted) {
		try {
			IvParameterSpec iv = new IvParameterSpec(CommonUtil.JWT_SECRET.getBytes(StandardCharsets.UTF_8));
		    SecretKeySpec skeySpec = new SecretKeySpec(CommonUtil.JWT_SECRET.getBytes(StandardCharsets.UTF_8), "AES");
		    
		    Cipher cipher = Cipher.getInstance(algo);
		    cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
		    
		    byte[] original = cipher.doFinal(Base64.decodeBase64(encrypted));
		    return new String(original);
		    
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}
}
