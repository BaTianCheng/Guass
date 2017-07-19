package com.esb.guass.dispatcher.entity;

import com.alibaba.fastjson.JSON;

/**
 * 轨迹实体类
 * @author wicks
 */
public class TrackEntity {

	private String questId;
	
	private int no;
	
	private long time;
	
	private String message;

	public String getQuestId() {
		return questId;
	}

	public void setQuestId(String questId) {
		this.questId = questId;
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	@Override
	public String toString(){
		return JSON.toJSONString(this);
	}
	
}
