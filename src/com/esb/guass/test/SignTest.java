package com.esb.guass.test;

import java.util.HashMap;
import java.util.Map;

import com.esb.guass.auth.service.SignValidateService;
import com.esb.guass.dispatcher.entity.RequestEntity;

public class SignTest {

	public static void main(String[] args) {
		RequestEntity entity = new RequestEntity();
		Map<String, String> params = new HashMap<>();
		params.put("glideNo", "1512718231245215926");
		params.put("posNo", "0511");
		params.put("companyNo", "0120");
		params.put("refundFlag", "0");
		params.put("scrdList", "[{\"consSum\":0.03,\"scrdNo\":\";85718089980031313958:?\"}]");
		entity.setParams(params);
		entity.setAppId("TCABCBANK000001");
		entity.setServiceCode("scrdUseFlow");
		

		System.out.println(SignValidateService.getSignValidate(entity, "1b97a4bd945646e489c2810dd8b5bfe4"));
	}

}
