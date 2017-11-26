package com.esb.guass.dispatcher.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;

/**
 * 结果封装实体类
 * 
 * @author wicks
 */
public class ResultEntity implements Serializable{

	private static final long serialVersionUID = -3589264449129846839L;

	private String statusCode;

	private String message;
	
	private String data;

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
	
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public void buildMessage(String message, Map<String, Object> data){
		Map<String, Object> resultData = new HashMap<>();
		resultData.put("statusCode", this.statusCode);
		resultData.put("message", message);
		resultData.put("data", data);
		setMessage(JSON.toJSONString(resultData));
	}
	
	public void buildMessage(String message, List<Map<String, Object>> data){
		Map<String, Object> resultData = new HashMap<>();
		resultData.put("statusCode", this.statusCode);
		resultData.put("message", message);
		resultData.put("data", data);
		setMessage(JSON.toJSONString(resultData));
	}

	public String toString() {
		return JSON.toJSONString(this);
	}

}
