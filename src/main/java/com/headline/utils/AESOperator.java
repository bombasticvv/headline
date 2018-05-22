package com.headline.utils;

import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class AESOperator {
	
	public static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";
	private static final String CHARSET = "UTF-8";
	private String VECTOR = "!WFN@FU_{H%M(S|a";
	
	private AESOperator() {

    }
	
	public static AESOperator getInstance() {
        return Nested.instance;
    }
	
    static class Nested {
        private static AESOperator instance = new AESOperator();
    }
	
	public String encrypt(String content,String randomKey) throws Exception{
		SecretKeySpec skeySpec =toKey(randomKey);
		IvParameterSpec iv = new IvParameterSpec(VECTOR.getBytes());
		
		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
		byte[] encrypted = cipher.doFinal(content.getBytes());
		
		return new BASE64Encoder().encode(encrypted);
	}
	
	public String decrypt(String content,String randomKey) throws Exception{
		SecretKeySpec skeySpec =toKey(randomKey);
		IvParameterSpec iv = new IvParameterSpec(VECTOR.getBytes());
		
		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
		byte[] encrypted = new BASE64Decoder().decodeBuffer(content);
		byte[] original = cipher.doFinal(encrypted);
        String originalString = new String(original, CHARSET);
		return originalString;
	}
	
	public String generateString() {
		StringBuffer sb = new StringBuffer();
		Random random = new Random();
		
		for(int i = 0,len=16; i < len; ++i) {
			sb.append("1234567890abcdef".charAt(random.nextInt(len)));
		}
		
		return sb.toString();
	}
	
	private SecretKeySpec toKey(String randomKey) throws Exception {
		String secretKey=randomKey+VECTOR;
		byte[] neiKey = new byte[] { 17, 34, 51, 68, 85, 102, 119, 26, 42, 43,
				44, 45, 46, 47, 58, 59, 17, 34, 51, 68, 85, 102, 119, 26, 42,
				43, 44, 45, 46, 47, 58, 59 };
		byte[] relkey = new byte[32];

		for (int i = 0; i < secretKey.length(); ++i) {
			byte b = (byte) (secretKey.charAt(i) ^ neiKey[i]);
			relkey[i] = b;
		}

		SecretKeySpec key = new SecretKeySpec(relkey, "AES");
		return key;
	}
	
	public static void main(String[] args) throws Exception {
		String randomKey=AESOperator.getInstance().generateString();
		System.out.println("随机字符串:"+randomKey);
		String content="xueyuanyong";
		String encData=AESOperator.getInstance().encrypt(content, randomKey);
		System.out.println("加密后串:"+encData);
		String decData=AESOperator.getInstance().decrypt(encData, randomKey);
		System.out.println("解密后串:"+decData);
	}
}
