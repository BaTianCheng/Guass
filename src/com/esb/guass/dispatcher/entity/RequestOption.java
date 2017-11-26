package com.esb.guass.dispatcher.entity;

import java.io.Serializable;

import com.alibaba.fastjson.JSON;
import com.esb.guass.common.constant.HttpConstant;

/**
 * 请求选项
 * 
 * @author wicks
 */
public class RequestOption implements Serializable{

	private static final long serialVersionUID = 4256827578161846802L;

	/**
	 * 字符集
	 */
	private String charset = HttpConstant.DEFAULT_CHARSET;

	/**
	 * 方法
	 */
	private String method = HttpConstant.DEFAULT_METHOD;

	/**
	 * 是否将参数以体的形式传递
	 */
	private boolean isBody = false;

	/**
	 * 超时时间
	 */
	private Integer timeOut = HttpConstant.defaultSoTimeout;

	/**
	 * 业务编号
	 */
	private String businessId;

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public boolean isBody() {
		return isBody;
	}

	public void setBody(boolean isBody) {
		this.isBody = isBody;
	}

	public Integer getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(Integer timeOut) {
		this.timeOut = timeOut;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

}
