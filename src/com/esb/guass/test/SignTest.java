package com.esb.guass.test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import com.esb.guass.auth.service.SignValidateService;
import com.esb.guass.dispatcher.entity.RequestEntity;

public class SignTest {

	public static void main(String[] args) {
		RequestEntity entity = new RequestEntity();
		Map<String, String> params = new HashMap<>();
		params.put("from", "01");
		params.put("giftCardNo", "9800005041");
		params.put("cardId", "pS9qRtwrGyuC8rQRB2E_gAuP-TOw");
		entity.setParams(params);
		entity.setAppId("TCWEBANK000001");
		entity.setServiceCode("saleGECard");
		System.out.println(SignValidateService.getSignValidate(entity, "ff829b8f76d34cec8a173ef7d9cc676a"));
	}

}
