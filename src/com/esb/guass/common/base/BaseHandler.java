package com.esb.guass.common.base;

import java.util.Map;

import com.esb.guass.dispatcher.entity.ResultEntity;

/**
 * 处理类接口
 * 
 * @author wicks
 */
public interface BaseHandler {

	public ResultEntity HandlerRequest(String questId, Map<String, String> params);
}
