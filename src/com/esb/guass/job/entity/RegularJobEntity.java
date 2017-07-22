package com.esb.guass.job.entity;

/**
 * 定时任务实体类
 * @author wicks
 */
public class RegularJobEntity {

	/**
	 * 编号
	 */
	private String jobCode;
	
	/**
	 * 名称
	 */
	private String jobName;
	
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
	 * 间隔时间（秒）
	 */
	private Integer intervalSecond;

	/**
	 * 开始时间（分，如480表示8点）
	 */
	private Integer beiginTime;
	
	/**
	 * 结束时间（分，如600表示10点）
	 */
	private Integer endTime;
	
	/**
	 * 最近执行时间
	 */
	private long lastTime;
	
	/**
	 * 最近请求任务编号
	 */
	private String lastQuestId;
	
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

	public Integer getIntervalSecond() {
		return intervalSecond;
	}

	public void setIntervalSecond(Integer intervalSecond) {
		this.intervalSecond = intervalSecond;
	}

	public Integer getBeiginTime() {
		return beiginTime;
	}

	public void setBeiginTime(Integer beiginTime) {
		this.beiginTime = beiginTime;
	}

	public Integer getEndTime() {
		return endTime;
	}

	public void setEndTime(Integer endTime) {
		this.endTime = endTime;
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

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
}
