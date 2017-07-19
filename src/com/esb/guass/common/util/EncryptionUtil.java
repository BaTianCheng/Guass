package com.esb.guass.common.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 加密工具类
 * @author wicks
 */
public class EncryptionUtil {
	
	/**
	 * SHA1加密算法
	 * @param str
	 * @return
	 */
	public static String getSha1(String appId, String timestamp, String nonce, String appKey){
		return getSha1(appId+timestamp+nonce+appKey);
	}
	
	/**
	 * SHA1加密算法
	 * @param str
	 * @return
	 */
	public static String getSha1(String str){
		try {
			MessageDigest digest = java.security.MessageDigest
					.getInstance("SHA1");
			digest.update(str.getBytes());
			byte messageDigest[] = digest.digest();
			// Create Hex String
			StringBuffer hexString = new StringBuffer();
			// 字节数组转换为 十六进制 数
			for (int i = 0; i < messageDigest.length; i++) {
				String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
				if (shaHex.length() < 2) {
					hexString.append(0);
				}
				hexString.append(shaHex);
			}
			return hexString.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}
	
}
