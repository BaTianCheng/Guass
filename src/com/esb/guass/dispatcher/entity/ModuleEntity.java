package com.esb.guass.dispatcher.entity;

import com.alibaba.fastjson.JSON;

public class ModuleEntity {
	
	private String module;
	
	private String name;
	
	private int sortNum;

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSortNum() {
		return sortNum;
	}

	public void setSortNum(int sortNum) {
		this.sortNum = sortNum;
	}
	
	@Override
	public String toString(){
		return JSON.toJSONString(this);
	}

}
