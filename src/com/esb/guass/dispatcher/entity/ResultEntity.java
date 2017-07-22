package com.esb.guass.dispatcher.entity;

import com.alibaba.fastjson.JSON;

/**
 * 结果封装实体类
 * @author wicks
 */
public class ResultEntity {
	
	private String statusCode;
	
	private String message;

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public String toString(){
		return JSON.toJSONString(this);
	}

}
