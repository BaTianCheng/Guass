package com.esb.guass.job.entity;

import java.util.List;

import com.alibaba.fastjson.JSON;

/**
 * 计划任务实体类
 * 
 * @author wicks
 */
public class PlanningJobEntity {

	/**
	 * 编号
	 */
	private String jobCode;

	/**
	 * 名称
	 */
	private String jobName;

	/**
	 * 模块
	 */
	private String module;

	/**
	 * 服务编码
	 */
	private String serviceCode;

	/**
	 * 服务名称
	 */
	private String serviceName;

	/**
	 * 创建者
	 */
	private Integer creator;

	/**
	 * 状态（1可用，0不可用）
	 */
	private Integer status;

	/**
	 * 规则（1每天，2每周，3每月）
	 */
	private Integer jobRule;

	/**
	 * 规则的值（每天哪一时间，每周周一，每月哪一天）
	 */
	private List<Integer> jobRuleValues;

	/**
	 * 规则对应的时间（分）
	 */
	private Integer jobRuleTime;

	/**
	 * 最近执行时间
	 */
	private long lastTime;

	/**
	 * 最近请求任务编号
	 */
	private String lastQuestId;

	/**
	 * 下次执行时间
	 */
	private long nextTime;

	/**
	 * 备注
	 */
	private String remarks;

	public String getJobCode() {
		return jobCode;
	}

	public void setJobCode(String jobCode) {
		this.jobCode = jobCode;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
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

	public Integer getCreator() {
		return creator;
	}

	public void setCreator(Integer creator) {
		this.creator = creator;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getJobRule() {
		return jobRule;
	}

	public void setJobRule(Integer jobRule) {
		this.jobRule = jobRule;
	}

	public List<Integer> getJobRuleValues() {
		return jobRuleValues;
	}

	public void setJobRuleValues(List<Integer> jobRuleValues) {
		this.jobRuleValues = jobRuleValues;
	}

	public Integer getJobRuleTime() {
		return jobRuleTime;
	}

	public void setJobRuleTime(Integer jobRuleTime) {
		this.jobRuleTime = jobRuleTime;
	}

	public long getLastTime() {
		return lastTime;
	}

	public void setLastTime(long lastTime) {
		this.lastTime = lastTime;
	}

	public String getLastQuestId() {
		return lastQuestId;
	}

	public void setLastQuestId(String lastQuestId) {
		this.lastQuestId = lastQuestId;
	}

	public long getNextTime() {
		return nextTime;
	}

	public void setNextTime(long nextTime) {
		this.nextTime = nextTime;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

}
