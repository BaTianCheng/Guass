package com.esb.guass.test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esb.guass.common.util.EncryptionUtils;

public class temp {

	public static void main(String[] args) {
		Logger logger = LoggerFactory.getLogger("com.esb.guass.job");
		logger.info("xxxxx");
		System.out.println(EncryptionUtils.encryptAES("123456"));
//		try {
//			JSONArray jsonArray = new JSONArray();
//			JSONObject jsonObject = new JSONObject();
//			jsonObject.put("orderId", "12345678");
//			jsonArray.add(jsonObject);
//			String value = JSON.toJSONString(jsonArray);
//
//			JSONObject params = new JSONObject();
//			params.put("orderList", value);
//			System.out.println(params);
//
//			String dstPath = "/aaa/bbb/ccc/ddd/";
//			String backPath = dstPath.substring(0, dstPath.lastIndexOf("/"));
//			backPath = backPath.substring(0, backPath.lastIndexOf("/")) + "/";
//			System.out.println(backPath);
//		} catch(Exception ex) {
//			System.out.println(ex);
//		}
	}

	/**
	 * 从输入流中获取字节数组
	 * 
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	public static byte[] readInputStream(InputStream inputStream) throws IOException {
		byte[] buffer = new byte[1024];
		int len = 0;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		while((len = inputStream.read(buffer)) != -1) {
			bos.write(buffer, 0, len);
		}
		bos.close();
		return bos.toByteArray();
	}

}
