package com.esb.guass.test;

import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.esb.guass.common.base.BaseHandler;
import com.esb.guass.dispatcher.entity.ResultEntity;

public class TestHandler implements BaseHandler{

	@Override
	public ResultEntity HandlerRequest(String questId, Map<String, String> params) {
		
		ResultEntity resultEntity = new ResultEntity();
		resultEntity.setStatusCode("200");
		resultEntity.setMessage("sucess");
		resultEntity.setData(JSON.toJSONString(params));
		
		return resultEntity;
	}

}
