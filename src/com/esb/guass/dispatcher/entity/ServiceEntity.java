package com.esb.guass.dispatcher.entity;

import java.util.List;

import com.alibaba.fastjson.JSON;

/**
 * 服务实体类
 * @author wicks
 */
/**
 * 
 * @author wicks
 */
public class ServiceEntity {
	
	/**
	 * 服务编码
	 */
	private String serviceCode;
	
	/**
	 * 服务名称
	 */
	private String serviceName;
	
	/**
	 * 所属模块
	 */
	private String module;
	
	/**
	 * 请求类型
	 */
	private String requestType;
	
	/**
	 * 映射路径
	 */
	private String mapUrl;
	
	/**
	 * 异步标志
	 */
	private boolean async = true;
	
	/**
	 * 是否直接返回结果（同步时无效）
	 */
	private boolean directReturn = false;
	
	/**
	 * 请求选项
	 */
	private RequestOption requestOption;
	
	/**
	 * 参数列表
	 */
	private List<String> params;
	
	/**
	 * 必填参数列表
	 */
	private List<String> requiredParams;
	
	/**
	 * 头部参数列表
	 */
	private List<String> headParams;
	
	/**
	 * 返回的内容格式（仅限直接返回）
	 */
	private String responseContentType;
	
	/**
	 * 返回的错误信息（仅限直接返回）
	 */
	private String responseErrorMsg;
	

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

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public String getMapUrl() {
		return mapUrl;
	}

	public void setMapUrl(String mapUrl) {
		this.mapUrl = mapUrl;
	}

	public boolean isAsync() {
		return async;
	}

	public void setAsync(boolean async) {
		this.async = async;
	}

	public boolean isDirectReturn() {
		return directReturn;
	}

	public void setDirectReturn(boolean directReturn) {
		this.directReturn = directReturn;
	}

	public RequestOption getRequestOption() {
		return requestOption;
	}

	public void setRequestOption(RequestOption requestOption) {
		this.requestOption = requestOption;
	}

	public List<String> getParams() {
		return params;
	}

	public void setParams(List<String> params) {
		this.params = params;
	}

	public List<String> getRequiredParams() {
		return requiredParams;
	}

	public void setRequiredParams(List<String> requiredParams) {
		this.requiredParams = requiredParams;
	}
	
	public List<String> getHeadParams() {
		return headParams;
	}

	public void setHeadParams(List<String> headParams) {
		this.headParams = headParams;
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
