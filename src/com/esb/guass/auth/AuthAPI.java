package com.esb.guass.auth;

import com.esb.guass.auth.entity.VisitorEntity;
import com.esb.guass.auth.service.SignValidateService;
import com.esb.guass.auth.service.VisitorService;
import com.esb.guass.auth.service.WhiteListService;
import com.esb.guass.common.constant.StatusConstant;
import com.esb.guass.common.util.LogUtils;
import com.esb.guass.dispatcher.entity.RequestEntity;

/**
 * 权限API
 * 
 * @author wicks
 */
public class AuthAPI {

	/**
	 * 权限校验
	 * 
	 * @param entity
	 * @return
	 */
	public static String authCheck(RequestEntity entity) {
		try {
			if(entity.isAuthValidate() && !WhiteListService.isWhiteList(entity.getRequestIP())) {
				VisitorEntity visitor = VisitorService.find(entity.getAppId());
				if(visitor == null) {
					// 用户不存在，权限不足
					return StatusConstant.CODE_300;
				}
				if(visitor.getServiceCodes() != null && visitor.getServiceCodes().contains(entity.getServiceCode())) {
					if(!SignValidateService.validate(entity, visitor.getAppKey())) {
						// 签名校验失败
						return StatusConstant.CODE_301;
					}
				} else {
					// 用户无权访问，权限不足
					return StatusConstant.CODE_300;
				}
			}

			return StatusConstant.CODE_200;
		} catch(Exception ex) {
			LogUtils.error("权限校验失败", ex);
			return StatusConstant.CODE_300;
		}
	}

}
