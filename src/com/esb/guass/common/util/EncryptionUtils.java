package com.esb.guass.common.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * 加密算法工具类
 * @author wicks
 */
public final class EncryptionUtils {
	
	/**
	 * 对称性加密密码
	 */
	private static final String password = "jinying@025-84707089";
	
	/**
	 * AES加密算法
	 * @param content
	 * @return
	 */
	public static String encryptAES(String content) {  
        try { 
                KeyGenerator kgen = KeyGenerator.getInstance("AES");  
                kgen.init(128, new SecureRandom(password.getBytes()));  
                SecretKey secretKey = kgen.generateKey();  
                byte[] enCodeFormat = secretKey.getEncoded();  
                SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");  
                Cipher cipher = Cipher.getInstance("AES");// 创建密码器  
                byte[] byteContent = content.getBytes("utf-8");  
                cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化  
                byte[] result = cipher.doFinal(byteContent);  
                return ConvertUtils.parseByte2HexStr(result); // 加密  
        } catch (Exception e) {  
                e.printStackTrace();  
        } 
        return null;  
	}
	
	/**
	 * AES解密 
	 * @param content  待解密内容 
	 * @return 
	 */  
	public static String decryptAES(String content) {  
	        try {  
	                 KeyGenerator kgen = KeyGenerator.getInstance("AES");  
	                 kgen.init(128, new SecureRandom(password.getBytes()));  
	                 SecretKey secretKey = kgen.generateKey();  
	                 byte[] enCodeFormat = secretKey.getEncoded();  
	                 SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");              
	                 Cipher cipher = Cipher.getInstance("AES");// 创建密码器  
	                cipher.init(Cipher.DECRYPT_MODE, key);// 初始化  
	                byte[] byteContent = ConvertUtils.parseHexStr2Byte(content);  
	                byte[] result = cipher.doFinal(byteContent);  
	                return new String(result); // 解密  
	        } catch (Exception e) {  
	                e.printStackTrace();  
	        }
	        return null;  
	}
	
	 /**
	  * MD5进行加密
     * @param str  待加密的字符串
     * @return  加密后的字符串
     * @throws NoSuchAlgorithmException  没有这种产生消息摘要的算法
     * @throws UnsupportedEncodingException  
     */
    public static String EncoderByMd5(String str) {
    	try{
    		MessageDigest md = MessageDigest.getInstance("MD5");  
            md.update(str.getBytes());  
            byte b[] = md.digest();  
  
            int i;  
  
            StringBuffer buf = new StringBuffer("");  
            for (int offset = 0; offset < b.length; offset++) {  
                i = b[offset];  
                if (i < 0)  
                    i += 256;  
                if (i < 16)  
                    buf.append("0");  
                buf.append(Integer.toHexString(i));  
            }  
            //32位加密  
            return buf.toString().toUpperCase();  
    	} 
    	catch(Exception ex){
    		return null;
    	}
    }
    
    /** 
     * 传入文本内容，返回 SHA-256 串 
     * @param strText 
     * @return 
     */  
    public static String SHA256(final String strText)  
    {  
      return SHA(strText, "SHA-256").toUpperCase();  
    }  
    
    /** 
     * 传入文本内容，返回 SHA-512 串 
     * @param strText 
     * @return 
     */  
    public static String SHA512(final String strText)  
    {  
      return SHA(strText, "SHA-512").toUpperCase(); 
    }  
    
    /** 
     * 字符串 SHA 加密 
     * @param strSourceText 
     * @return 
     */  
    private static String SHA(final String strText, final String strType)  
    {  
      // 返回值  
      String strResult = null;  
    
      // 是否是有效字符串  
      if (strText != null && strText.length() > 0)  
      {  
        try  
        {  
          // SHA 加密开始  
          // 创建加密对象 
          MessageDigest messageDigest = MessageDigest.getInstance(strType);  
          // 传入要加密的字符串  
          messageDigest.update(strText.getBytes());  
          // 得到 byte 
          byte byteBuffer[] = messageDigest.digest();  
    
          // 將 byte 转为 string  
          StringBuffer strHexString = new StringBuffer();  
          for (int i = 0; i < byteBuffer.length; i++)  
          {  
            String hex = Integer.toHexString(0xff & byteBuffer[i]);  
            if (hex.length() == 1)  
            {  
              strHexString.append('0');  
            }  
            strHexString.append(hex);  
          }  
          // 得到返回结果
          strResult = strHexString.toString();  
        }  
        catch (NoSuchAlgorithmException e)  
        {  
          e.printStackTrace();  
        }  
      }  
    
      return strResult;  
    }  
	
}
