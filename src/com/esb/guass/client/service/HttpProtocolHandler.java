package com.esb.guass.client.service;

import org.apache.commons.httpclient.HttpException;
import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.util.IdleConnectionTimeoutThread;

import com.esb.guass.client.entity.HttpRequest;
import com.esb.guass.client.entity.HttpResponse;
import com.esb.guass.client.entity.HttpResultType;
import com.esb.guass.client.security.MySecureProtocolSocketFactory;
import com.esb.guass.common.constant.HttpConstant;
import com.google.common.base.Strings;

import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.FilePartSource;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

/**
 * HttpClient方式访问 (目前客户端访问方式混乱，主要是由于request,reponse使用的类型不同造成，后期再改)
 * 
 * @author wicks
 */
public class HttpProtocolHandler {

	/**
	 * HTTP连接管理器，该连接管理器必须是线程安全的.
	 */
	private HttpConnectionManager connectionManager;

	private static HttpProtocolHandler httpProtocolHandler = new HttpProtocolHandler();

	/**
	 * 工厂方法
	 * 
	 * @return
	 */
	public static HttpProtocolHandler getInstance() {
		return httpProtocolHandler;
	}

	/**
	 * 私有的构造方法
	 */
	private HttpProtocolHandler() {
		// 创建一个线程安全的HTTP连接池
		connectionManager = new MultiThreadedHttpConnectionManager();
		connectionManager.getParams().setDefaultMaxConnectionsPerHost(HttpConstant.defaultMaxConnPerHost);
		connectionManager.getParams().setMaxTotalConnections(HttpConstant.defaultMaxTotalConn);

		IdleConnectionTimeoutThread ict = new IdleConnectionTimeoutThread();
		ict.addConnectionManager(connectionManager);
		ict.setConnectionTimeout(HttpConstant.defaultIdleConnTimeout);

		ict.start();
	}

	/**
	 * 执行Http请求/执行Https请求(根据协议头自动识别)
	 * 
	 * @param request
	 *            请求
	 * @param strParaFileName
	 *            文件类型参数
	 * @param strFilePath
	 *            文件路径
	 * @param strBody
	 *            体数据
	 * @return 响应
	 * @throws HttpException
	 * @throws IOException
	 */
	public HttpResponse execute(HttpRequest request, String strParaFileName, String strFilePath, String strBody) throws HttpException, IOException {
		HttpClient httpclient;

		// https协议加载
		if(request.getUrl().startsWith("https")) {
			ProtocolSocketFactory fcty = new MySecureProtocolSocketFactory();
			Protocol.registerProtocol("https", new Protocol("https", fcty, 443));
		}

		httpclient = new HttpClient(connectionManager);

		// 设置连接超时
		int connectionTimeout = HttpConstant.defaultConnectionTimeout;
		if(request.getConnectionTimeout() > 0) {
			connectionTimeout = request.getConnectionTimeout();
		}
		httpclient.getHttpConnectionManager().getParams().setConnectionTimeout(connectionTimeout);

		// 设置回应超时
		int soTimeout = HttpConstant.defaultSoTimeout;
		if(request.getTimeout() > 0) {
			soTimeout = request.getTimeout();
		}
		httpclient.getHttpConnectionManager().getParams().setSoTimeout(soTimeout);

		// 设置等待ConnectionManager释放connection的时间
		httpclient.getParams().setConnectionManagerTimeout(HttpConstant.defaultHttpConnectionManagerTimeout);

		// 设置字符集
		String charset = request.getCharset();
		charset = charset == null ? HttpConstant.DEFAULT_CHARSET : charset;
		HttpMethod method = null;

		// get模式
		// parseNotifyConfig会保证使用GET方法时，request一定使用QueryString
		if(request.getMethod().equals(HttpRequest.METHOD_GET)) {
			method = new GetMethod(request.getUrl());
			method.getParams().setCredentialCharset(charset);
			if(request.getQueryString() != null) {
				method.setQueryString(request.getQueryString());
			} else if(request.getParameters() != null) {
				StringBuilder sb = new StringBuilder();
				for(NameValuePair pair : request.getParameters()) {
					sb.append(pair.getName() + "=" + pair.getValue() + "&");
				}
				method.setQueryString(sb.toString());
			}

		} else if(Strings.isNullOrEmpty(strParaFileName) && Strings.isNullOrEmpty(strFilePath)) {
			// post模式
			method = new PostMethod(request.getUrl());
			if(!Strings.isNullOrEmpty(strBody)) {
				byte[] b = strBody.getBytes("utf-8");
				InputStream is = new ByteArrayInputStream(b, 0, b.length);
				RequestEntity re = new InputStreamRequestEntity(is, b.length, "application/soap+xml; charset=utf-8");
				((PostMethod) method).setRequestEntity(re);

				StringBuilder sb = new StringBuilder();
				for(NameValuePair pair : request.getParameters()) {
					sb.append(pair.getName() + "=" + pair.getValue() + "&");
				}
				method.setQueryString(sb.toString());

			} else {
				((PostMethod) method).addParameters(request.getParameters());
			}

			method.setRequestHeader("Content-Type", "application/json; text/html; charset=" + charset);
		} else {
			// post模式且带上传文件
			method = new PostMethod(request.getUrl());
			List<Part> parts = new ArrayList<Part>();
			for(int i = 0; i < request.getParameters().length; i++) {
				parts.add(new StringPart(request.getParameters()[i].getName(), request.getParameters()[i].getValue(), charset));
			}
			parts.add(new FilePart(strParaFileName, new FilePartSource(new File(strFilePath))));
			((PostMethod) method).setRequestEntity(new MultipartRequestEntity(parts.toArray(new Part[0]), new HttpMethodParams()));
		}

		// 设置Http Header中的User-Agent属性
		method.setRequestHeader("User-Agent", "Mozilla/4.0");
		if(request.getHeaders() != null) {
			for(Entry<String, String> head : request.getHeaders().entrySet()) {
				// 编码格式处理，导致gzip无法入库，不做压缩
				if(!head.getKey().toLowerCase().equals("accept-encoding")) {
					method.setRequestHeader(head.getKey(), head.getValue());
				}
			}
		}
		HttpResponse response = new HttpResponse();

		try {
			httpclient.executeMethod(method);
			if(request.getResultType().equals(HttpResultType.STRING)) {
				response.setStringResult(method.getResponseBodyAsString());
			} else if(request.getResultType().equals(HttpResultType.BYTES)) {
				InputStream input = method.getResponseBodyAsStream();
				ByteArrayOutputStream output = new ByteArrayOutputStream();
				byte[] buffer = new byte[4096];
				int n = 0;
				while(-1 != (n = input.read(buffer))) {
					output.write(buffer, 0, n);
				}
				response.setByteResult(output.toByteArray());
			}
			response.setResponseHeaders(method.getResponseHeaders());
		} catch(UnknownHostException ex) {

			return null;
		} catch(IOException ex) {

			return null;
		} catch(Exception ex) {

			return null;
		} finally {
			method.releaseConnection();
		}

		return response;
	}

}
