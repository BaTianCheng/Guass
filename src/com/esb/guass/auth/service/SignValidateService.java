package com.esb.guass.auth.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.esb.guass.common.constant.ParamConstants;
import com.esb.guass.common.util.EncryptionUtils;
import com.esb.guass.dispatcher.entity.RequestEntity;
import com.google.common.base.Strings;

/**
 * 签名校验
 * 
 * @author wicks
 */
public class SignValidateService {

	/**
	 * 签名校验
	 * 
	 * @return
	 */
	public static boolean validate(RequestEntity entity, String appKey) {
		String signValidate = getSignValidate(entity, appKey);
		String signStr = entity.getSign();
		if(!Strings.isNullOrEmpty(signStr) && signStr.equals(signValidate)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获取验证字符串
	 * 
	 * @param entity
	 * @param appKey
	 * @return
	 */
	public static String getSignValidate(RequestEntity entity, String appKey) {
		Map<String, String> params = entity.getParams();
		List<String> paramNamesList = new ArrayList<>();
		for(Entry<String, String> param : params.entrySet()) {
			paramNamesList.add(param.getKey());
		}
		paramNamesList.add(ParamConstants.PARAM_APPID);
		paramNamesList.add(ParamConstants.PARAM_SERVICECODE);

		String[] paramNames = paramNamesList.toArray(new String[0]);
		Arrays.sort(paramNames);
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < paramNames.length; i++) {
			if(paramNames[i].equals(ParamConstants.PARAM_APPID)) {
				sb.append(paramNames[i] + "=" + entity.getAppId());
			} else if(paramNames[i].equals(ParamConstants.PARAM_SERVICECODE)) {
				sb.append(paramNames[i] + "=" + entity.getServiceCode());
			} else {
				sb.append(paramNames[i] + "=" + params.get(paramNames[i]));
			}
			sb.append("&");
		}
		sb.append("APPKEY=" + appKey);
		String signValidate = sb.toString();
		signValidate = EncryptionUtils.EncoderByMd5(signValidate);
		signValidate = EncryptionUtils.SHA256(signValidate);
		return signValidate;
	}

}
