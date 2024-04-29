package com.np.schoolpgi.util;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AESEncryption {

	private static final String ALGO = "AES/CBC/PKCS5PADDING";

	@Value("${secret.key}")
	String secretKey;
	//String secretKey="$!NPSecurityS^@!";
	
	@Value("${iv.key}")
	String ivKey;
//	String ivKey="213A26DBB4A358C5";

	//Encryption Method
	public String encrypt(Object input)
			throws Exception {
		
		IvParameterSpec iv = new IvParameterSpec(ivKey.getBytes("UTF-8"));
		SecretKeySpec skeySpec = new SecretKeySpec(secretKey.getBytes("UTF-8"), "AES");

		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

		byte[] encrypted = cipher.doFinal(input.toString().getBytes());
		return Base64.getEncoder().encodeToString(encrypted);
		
	}
//	public String encrypt(String input)
//			throws Exception {
//		
//		IvParameterSpec iv = new IvParameterSpec(ivKey.getBytes("UTF-8"));
//		SecretKeySpec skeySpec = new SecretKeySpec(secretKey.getBytes("UTF-8"), "AES");
//		
//		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
//		cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
//		
//		byte[] encrypted = cipher.doFinal(input.getBytes());
//		return Base64.getEncoder().encodeToString(encrypted);
//		
//	}

	//Key generation of type Key
//	private Key generateKey(String secret) {
//		byte[] decoded = Base64.getDecoder().decode(secret.getBytes());
//		Key key = new SecretKeySpec(decoded, ALGO);
//		return key;
//	}

	
	//Decryption Method
	public String decrypt(Object cipherText)
			throws Exception {

		IvParameterSpec iv = new IvParameterSpec(ivKey.getBytes("UTF-8"));
		SecretKeySpec skeySpec = new SecretKeySpec(secretKey.getBytes("UTF-8"), "AES");
	
		Cipher cipher = Cipher.getInstance(ALGO);
		cipher.init(Cipher.DECRYPT_MODE, skeySpec,iv);
		byte[] plainText = cipher.doFinal(Base64.getDecoder().decode(cipherText.toString()));
		return new String(plainText);
	}

	//Encoded Key
//	public static String encodeKey(String str) {
//		byte[] encoded = Base64.getEncoder().encode(str.getBytes());
//		return new String(encoded);
//	}

}
