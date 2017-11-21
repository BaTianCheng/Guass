package com.esb.guass.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;

/**
 * MD5工具类
 * 
 * @author wicks
 */
public class MD5Utils {

	static MessageDigest md = null;

	static {
		try {
			md = MessageDigest.getInstance("MD5");
		} catch(NoSuchAlgorithmException ne) {
			LogUtils.error("无法初始化MD5加密类", ne);
		}
	}

	/**
	 * 利用MD5进行加密
	 * 
	 * @param str
	 *            待加密的字符串
	 * @return 加密后的字符串
	 * @throws NoSuchAlgorithmException
	 *             没有这种产生消息摘要的算法
	 * @throws UnsupportedEncodingException
	 */
	public static String EncoderByMd5(String str) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(str.getBytes());
			byte b[] = md.digest();

			int i;

			StringBuffer buf = new StringBuffer("");
			for(int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if(i < 0)
					i += 256;
				if(i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			// 32位加密
			return buf.toString();
		} catch(Exception ex) {
			return null;
		}
	}

	/**
	 * 对一个文件求他的md5值(大文件分块处理)
	 * 
	 * @param f
	 *            要求md5值的文件
	 * @return md5串
	 */
	public static String EncoderByMd5(File f) {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(f);
			byte[] buffer = new byte[8192];
			int length;
			while((length = fis.read(buffer)) != -1) {
				md.update(buffer, 0, length);
			}

			return new String(Hex.encodeHex(md.digest()));
		} catch(Exception e) {
			LogUtils.error("无法MD5加密", e);
			return null;
		} finally {
			try {
				if(fis != null)
					fis.close();
			} catch(Exception e) {
				LogUtils.error("无法MD5加密", e);
			}
		}
	}

}
