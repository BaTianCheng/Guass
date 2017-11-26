package com.esb.guass.client.service;

import java.util.Map;

import com.esb.guass.client.entity.HttpRequest;
import com.esb.guass.client.entity.HttpResponse;
import com.esb.guass.client.entity.HttpResultType;
import com.esb.guass.common.util.StringUtils;
import com.esb.guass.dispatcher.entity.RequestEntity;

/**
 * HTTP操作服务类
 * 
 * @author wicks
 */
public class HttpOperatorService {

	/**
	 * 构建HTTP请求
	 * 
	 * @param requestEntity
	 * @return
	 */
	public static HttpResponse buildRequest(RequestEntity requestEntity) throws Exception {
		return buildRequest(requestEntity.getParams(), requestEntity.getHead(), requestEntity.getUrl(), requestEntity.getRequestOption().getMethod(), requestEntity.getRequestOption().getCharset(),
				requestEntity.getPostBody());
	}

	/**
	 * 构建HTTP请求
	 * 
	 * @param sPara
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public static HttpResponse buildRequest(Map<String, String> sPara, Map<String, String> headers, String url, String method, String charset, String strBody) throws Exception {

		HttpProtocolHandler httpProtocolHandler = HttpProtocolHandler.getInstance();

		HttpRequest request = new HttpRequest(HttpResultType.BYTES);

		// 设置请求信息
		request.setCharset(charset);
		if(sPara != null) {
			request.setParameters(StringUtils.generatNameValuePair(sPara));
		}
		request.setMethod(method);
		request.setUrl(url);
		request.setHeaders(headers);

		HttpResponse response = httpProtocolHandler.execute(request, "", "", strBody);

		return response;
	}

}
