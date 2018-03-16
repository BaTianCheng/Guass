package com.esb.guass.test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class SoapTest {
	static int socketTimeout = 30000;// 请求超时时间
	static int connectTimeout = 30000;// 传输超时时间

	/**
	 * 使用SOAP1.1发送消息
	 * 
	 * @param postUrl
	 * @param soapXml
	 * @param soapAction
	 * @return
	 */
	public static String doPostSoap1_1(String postUrl, String soapXml, String soapAction) {
		String retStr = "";
		// 创建HttpClientBuilder
		HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
		// HttpClient
		CloseableHttpClient closeableHttpClient = httpClientBuilder.build();
		HttpPost httpPost = new HttpPost(postUrl);
		// 设置请求和传输超时时间
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(socketTimeout).setConnectTimeout(connectTimeout).build();
		httpPost.setConfig(requestConfig);
		try {
			httpPost.setHeader("Content-Type", "text/xml;charset=UTF-8");
			httpPost.setHeader("SOAPAction", soapAction);
			StringEntity data = new StringEntity(soapXml, Charset.forName("UTF-8"));
			httpPost.setHeader("Authorization", "Basic aW50X3BvczpCYXNpc2d4cDA=");
			httpPost.setEntity(data);
			CloseableHttpResponse response = closeableHttpClient.execute(httpPost);
			HttpEntity httpEntity = response.getEntity();
			if(httpEntity != null) {
				// 打印响应内容
				retStr = EntityUtils.toString(httpEntity, "UTF-8");
				System.out.println("response:" + retStr);
			}
			// 释放资源
			closeableHttpClient.close();
		} catch(Exception e) {
			// logger.error("exception in doPostSoap1_1", e);
		}
		return retStr;
	}

	public static void main(String[] args) throws HttpException, IOException {
		/*
		 * String orderSoapXml = "<?xml version = \"1.0\" ?>" +
		 * "<soapenv:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:web=\"http://webservices.b.com\">"
		 * + "   <soapenv:Header/>" + "   <soapenv:Body>" +
		 * "      <web:order soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">"
		 * + "         <in0 xsi:type=\"web:OrderRequest\">" +
		 * "            <mobile xsi:type=\"soapenc:string\" xmlns:soapenc=\"http://schemas.xmlsoap.org/soap/encoding/\">?</mobile>"
		 * + "            <orderStatus xsi:type=\"xsd:int\">?</orderStatus>" +
		 * "            <productCode xsi:type=\"soapenc:string\" xmlns:soapenc=\"http://schemas.xmlsoap.org/soap/encoding/\">?</productCode>"
		 * + "         </in0>" + "      </web:order>" + "   </soapenv:Body>" +
		 * "</soapenv:Envelope>";
		 * 
		 * String querySoapXml = "<?xml version = \"1.0\" ?>" +
		 * "<soapenv:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:web=\"http://webservices.b.com\">"
		 * + "   <soapenv:Header/>" + "   <soapenv:Body>" +
		 * "      <web:query soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">"
		 * + "         <in0 xsi:type=\"web:QueryRequest\">" +
		 * "            <endTime xsi:type=\"xsd:dateTime\">?</endTime>" +
		 * "            <mobile xsi:type=\"soapenc:string\" xmlns:soapenc=\"http://schemas.xmlsoap.org/soap/encoding/\">?</mobile>"
		 * + "            <startTime xsi:type=\"xsd:dateTime\">?</startTime>" +
		 * "         </in0>" + "      </web:query>" + "   </soapenv:Body>" +
		 * "</soapenv:Envelope>";
		 */
		String postUrl = "http://srvgxp.goodee.cn/XISOAPAdapter/MessageServlet?senderParty=&senderService=BS_RF&receiverParty=&receiverService=&interface=SIO_NET&interfaceNamespace=urn%3Anet%3Adealup";
		// 采用SOAP1.1调用服务端，这种方式能调用服务端为soap1.1和soap1.2的服务
		// doPostSoap1_1(postUrl, "", "");
		// doPostSoap1_1(postUrl, querySoapXml, "");

		// 采用SOAP1.2调用服务端，这种方式只能调用服务端为soap1.2的服务
		// doPostSoap1_2(postUrl, orderSoapXml, "order");
		// doPostSoap1_2(postUrl, querySoapXml, "query");

		String soapRequestData = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:urn=\"urn:sap-com:document:sap:rfc:functions\">"
				+ "<soapenv:Header/><soapenv:Body><urn:ZPP002><!--You may enter the following 2 items in any order--><I_DATE>20170707</I_DATE><O_TAB>"
				+ "<!--Zero or more repetitions:--><item><!--Optional:--><BUKRS>?</BUKRS><!--Optional:--><BRAND_NO>?</BRAND_NO><!--Optional:-->"
				+ "<SUPPLY_TYPE_NO>?</SUPPLY_TYPE_NO></item></O_TAB></urn:ZPP002></soapenv:Body></soapenv:Envelope>";

		// System.out.println(soapRequestData);

		PostMethod postMethod = new PostMethod(postUrl);

		// 然后把Soap请求数据添加到PostMethod中
		byte[] b = soapRequestData.getBytes("utf-8");
		InputStream is = new ByteArrayInputStream(b, 0, b.length);
		RequestEntity re = new InputStreamRequestEntity(is, b.length, "application/soap+xml; charset=utf-8");
		postMethod.setRequestEntity(re);
		postMethod.setRequestHeader("Authorization", "Basic aW50X3BvczpCYXNpc2d4cDA=");
		// 最后生成一个HttpClient对象，并发出postMethod请求
		HttpClient httpClient = new HttpClient();
		int statusCode = httpClient.executeMethod(postMethod);
		if(statusCode == 200) {
			System.out.println("调用成功！");
			String soapResponseData = postMethod.getResponseBodyAsString();
			System.out.println(soapResponseData);
		} else {
			System.out.println("调用失败！错误码：" + statusCode);
		}

	}

}