package com.esb.guass.dispatcher.entity;

import java.io.Serializable;

import com.alibaba.fastjson.JSON;

public class ModuleEntity implements Serializable{

	private static final long serialVersionUID = -2087064044723262702L;

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
	public String toString() {
		return JSON.toJSONString(this);
	}

}
