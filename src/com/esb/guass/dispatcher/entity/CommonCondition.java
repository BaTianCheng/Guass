package com.esb.guass.dispatcher.entity;

import java.io.Serializable;

import com.alibaba.fastjson.JSON;

/**
 * 查询请求的条件
 * 
 * @author wicks
 */
public class CommonCondition implements Serializable{

	private static final long serialVersionUID = -7398439072729532788L;

	/**
	 * 任务编号
	 */
	private String questId;

	/**
	 * 请求的服务编码
	 */
	private String serviceCode;

	/**
	 * 身份标记
	 */
	private String identification;

	/**
	 * 状态
	 */
	private String status;

	/**
	 * 请求开始时间
	 */
	private long beginTime;

	/**
	 * 请求结束时间
	 */
	private long endTime;

	/**
	 * 所属模块
	 */
	private String module;

	/**
	 * 页码
	 */
	private int pageNum;

	/**
	 * 每页数目
	 */
	private int pageSize;
	
	/**
	 * 参数key
	 */
	private String paramKey;
	
	/**
	 * 参数value
	 */
	private String paramValue;

	public String getQuestId() {
		return questId;
	}

	public void setQuestId(String questId) {
		this.questId = questId;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getIdentification() {
		return identification;
	}

	public void setIdentification(String identification) {
		this.identification = identification;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public long getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(long beginTime) {
		this.beginTime = beginTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getParamKey() {
		return paramKey;
	}

	public void setParamKey(String paramKey) {
		this.paramKey = paramKey;
	}

	public String getParamValue() {
		return paramValue;
	}

	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

}
