package com.esb.guass.common.base;

import java.io.Serializable;

import com.alibaba.fastjson.JSON;

/**
 * 实体类基类
 * @author wicks
 */
public class BaseEntity implements Serializable{
	
	private static final long serialVersionUID = 1L;

	/**
	 * 输出字符串
	 * @return
	 */
	@Override
	public String toString(){
		return JSON.toJSONString(this);
	}

}
