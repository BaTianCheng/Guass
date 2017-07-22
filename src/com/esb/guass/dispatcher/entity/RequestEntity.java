package com.esb.guass.dispatcher.entity;

import java.util.Map;

import org.apache.commons.httpclient.Header;

import com.alibaba.fastjson.JSON;
import com.esb.guass.common.constant.ConfigConstant;
import com.esb.guass.common.constant.HttpConstant;

/**
 * 请求业务实例
 * @author wicks
 */
public class RequestEntity {
	
	/**
	 * 任务编号
	 */
	private String questId;
	
	/**
	 * 请求类型
	 */
	private String requestType = ConfigConstant.HTTP;
	
	/**
	 * 请求的服务编码
	 */
	private String serviceCode;
	
	/**
	 * 请求的服务名称
	 */
	private String serviceName;
	
	/**
	 * 请求路径
	 */
	private String url;
	
	/**
	 * AppId标记
	 */
	private String appId;
	
	/**
	 * 签名
	 */
	private String sign;

	/**
	 * 身份标记
	 */
	private String identification;
	
	/**
	 * 异步标志
	 */
	private boolean async = true;
	
	/**
	 * 权限认证标志
	 */
	private boolean authValidate = false;
	
	/**
	 * 是否直接返回结果（同步时无效）
	 */
	private boolean directReturn = false;
	
	/**
	 * 状态
	 */
	private String status;
	
	/**
	 * 请求选项
	 */
	private RequestOption requestOption;
	
	/**
	 * 头部数据
	 */
	private Map<String, String> head;
	
	/**
	 * 参数
	 */
	private Map<String, String> params;
	
	/**
	 * POST体数据
	 */
	private String postBody;
	
	/**
	 * 请求时间
	 */
	private long requestTime;
	
	/**
	 * 执行时间
	 */
	private long excuteTime;
	
	/**
	 * 响应时间
	 */
	private long responseTime;
	
	/**
	 * 请求IP
	 */
	private String requestIP;
	
	/**
	 * 结果字符串
	 */
	private String result;
	
	/**
	 * 响应字符集
	 */
	private String responseCharset = HttpConstant.DEFAULT_CHARSET;
	
	/**
	 * 响应内容格式
	 */
	private Header[] responseHeaders;
	
	/**
	 * 返回的内容格式（仅限直接返回）
	 */
	private String responseContentType;
	
	/**
	 * 返回的错误信息（仅限直接返回）
	 */
	private String responseErrorMsg;
	
	public String getQuestId() {
		return questId;
	}

	public void setQuestId(String questId) {
		this.questId = questId;
	}

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}
	
	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}
	
	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getIdentification() {
		return identification;
	}

	public void setIdentification(String identification) {
		this.identification = identification;
	}

	public boolean isAsync() {
		return async;
	}

	public void setAsync(boolean async) {
		this.async = async;
	}

	public boolean isAuthValidate() {
		return authValidate;
	}

	public void setAuthValidate(boolean authValidate) {
		this.authValidate = authValidate;
	}

	public boolean isDirectReturn() {
		return directReturn;
	}

	public void setDirectReturn(boolean directReturn) {
		this.directReturn = directReturn;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public RequestOption getRequestOption() {
		return requestOption;
	}

	public void setRequestOption(RequestOption requestOption) {
		this.requestOption = requestOption;
	}

	public Map<String, String> getHead() {
		return head;
	}

	public void setHead(Map<String, String> head) {
		this.head = head;
	}

	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	public String getPostBody() {
		if(postBody==null && requestOption != null && requestOption.isBody() == true){
            if(getParams() != null){
            	return JSON.toJSONString(getParams());
            }
		}
		return postBody;
	}

	public void setPostBody(String postBody) {
		this.postBody = postBody;
	}

	public long getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(long requestTime) {
		this.requestTime = requestTime;
	}

	public long getExcuteTime() {
		return excuteTime;
	}

	public void setExcuteTime(long excuteTime) {
		this.excuteTime = excuteTime;
	}

	public long getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(long responseTime) {
		this.responseTime = responseTime;
	}

	public String getRequestIP() {
		return requestIP;
	}

	public void setRequestIP(String requestIP) {
		this.requestIP = requestIP;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getResponseCharset() {
		return responseCharset;
	}

	public void setResponseCharset(String responseCharset) {
		this.responseCharset = responseCharset;
	}

	public Header[] getResponseHeaders() {
		return responseHeaders;
	}

	public void setResponseHeaders(Header[] responseHeaders) {
		this.responseHeaders = responseHeaders;
	}

	public String getResponseContentType() {
		return responseContentType;
	}

	public void setResponseContentType(String responseContentType) {
		this.responseContentType = responseContentType;
	}

	public String getResponseErrorMsg() {
		return responseErrorMsg;
	}

	public void setResponseErrorMsg(String responseErrorMsg) {
		this.responseErrorMsg = responseErrorMsg;
	}

	@Override
	public String toString(){
		return JSON.toJSONString(this);
	}

}
