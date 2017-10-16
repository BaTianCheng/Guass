package com.esb.guass.test;

import java.util.HashMap;
import java.util.Map;

import com.esb.guass.auth.service.SignValidateService;
import com.esb.guass.dispatcher.entity.RequestEntity;

public class SignTest {

	public static void main(String[] args) {
		RequestEntity entity = new RequestEntity();
		Map<String, String> params = new HashMap<>();
		params.put("vipNo", "aaa");
		params.put("activityNo", "bbb");
		entity.setParams(params);
		entity.setAppId("TCWEBANK000001");
		entity.setServiceCode("getDiscountTicket");
		
		System.out.println(SignValidateService.getSignValidate(entity, "ff829b8f76d34cec8a173ef7d9cc676a"));
	}

}
