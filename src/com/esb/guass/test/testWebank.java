package com.esb.guass.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.esb.jinying.thirdcompany.webank.https.HttpClientUtils;
import com.esb.jinying.thirdcompany.webank.https.HttpConstants;
import com.esb.jinying.thirdcompany.webank.https.SignUtils;

public class testWebank {

	public static void main(String[] args) {
		HttpClient client = HttpClientUtils.createHttpClientWithCert(HttpConstants.KEYSTORE, HttpConstants.CLIENT_KEYSTORE_PASSWORD, HttpConstants.TRUSTSTORE, 200, 5, 1000, 3000, "", 8080, "", "");
		String bodyStr = "{\"orderid\":\"10001\",\"code\":\"9800000001\",\"balance\":0,\"transTime\":" + System.currentTimeMillis() + "}";
		String[] paramValues = {HttpConstants.VERSION, HttpConstants.APPID};
		String sign = SignUtils.getSign(paramValues, bodyStr);
		HttpPost httpPost = new HttpPost(HttpConstants.CONSUME_URL + "?version=" + HttpConstants.VERSION + "&appid=" + HttpConstants.APPID + "&sign=" + sign);
		StringEntity data = new StringEntity(bodyStr, Charset.forName("UTF-8"));
		data.setContentType("application/json;charset=UTF-8");
		httpPost.setEntity(data);
		System.out.println(client);
		System.out.println(JSON.toJSONString(httpPost));
		System.out.println(bodyStr);
		HttpResponse response;
		try {
			response = client.execute(httpPost);
			System.out.println("-------------------------------------");
			System.out.println(JSON.toJSONString(response));
			System.out.println("-------------------------------------");
			System.out.println(response.getEntity());
			InputStream is = response.getEntity().getContent();
			// 读取输入流，即返回文本内容
			StringBuffer sb = new StringBuffer();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line = "";
			while((line = br.readLine()) != null) {
				sb.append(line);
			}
			System.out.println(sb.toString());
			JSONObject result = JSON.parseObject(sb.toString());
			System.out.println(result.getString("code"));
			System.out.println(result.getString("msg"));
		} catch(ClientProtocolException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		}

	}

}
