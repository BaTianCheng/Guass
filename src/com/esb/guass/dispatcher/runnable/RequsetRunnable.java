package com.esb.guass.dispatcher.runnable;

import java.lang.reflect.Method;
import java.util.Map;

import com.esb.guass.client.entity.HttpResponse;
import com.esb.guass.client.service.HttpOperatorService;
import com.esb.guass.common.cache.ehcache.EhCacheService;
import com.esb.guass.common.constant.ConfigConstant;
import com.esb.guass.common.constant.StatusConstant;
import com.esb.guass.common.util.LogUtils;
import com.esb.guass.dispatcher.entity.RequestEntity;
import com.esb.guass.dispatcher.entity.ResultEntity;
import com.esb.guass.dispatcher.service.RequestQueue;
import com.esb.guass.dispatcher.service.RequestService;
import com.esb.guass.dispatcher.service.TrackService;

/**
 * 请求执行线程
 * 
 * @author wicks
 */
public class RequsetRunnable implements Runnable {

	@Override
	public void run() {
		RequestEntity requestEntity = RequestQueue.poll();
		requestEntity.setExcuteTime(System.currentTimeMillis());
		requestEntity.setStatus(StatusConstant.CODE_1202);
		TrackService.record(requestEntity.getQuestId(), StatusConstant.CODE_1202_MSG);
		RequestService.update(requestEntity);
		EhCacheService.setResultCache(requestEntity);

		// 进行处理程序
		if(requestEntity != null) {
			if(requestEntity.getRequestType().equals(ConfigConstant.API)) {
				// API形式的处理程序
				try {
					Class<?> clazz = Class.forName(requestEntity.getUrl());
					Method method = clazz.getMethod("HandlerRequest", String.class, Map.class);
					ResultEntity result = (ResultEntity) method.invoke(clazz.newInstance(), requestEntity.getQuestId(), requestEntity.getParams());
					requestEntity.setResponseTime(System.currentTimeMillis());
					
					// 执行处理程序成功
					if(result.getStatusCode().equals(StatusConstant.CODE_200)) {
						requestEntity.setStatus(StatusConstant.CODE_1203);
						requestEntity.setResult(result.getData());
						RequestService.update(requestEntity);
						TrackService.endRecord(requestEntity.getQuestId(), StatusConstant.CODE_1203_MSG);
					} else {
						requestEntity.setStatus(StatusConstant.CODE_1301);
						requestEntity.setSubStatus(result.getStatusCode());
						requestEntity.setResult(result.getData());
						requestEntity.setMessage(result.getMessage());
						RequestService.update(requestEntity);
						TrackService.endRecord(requestEntity.getQuestId(), StatusConstant.CODE_1301_MSG);
					}

					// 回收资源
					clazz = null;
					method = null;

				} catch(Throwable thrown) {
					requestEntity.setResponseTime(System.currentTimeMillis());
					requestEntity.setStatus(StatusConstant.CODE_1400);
					RequestService.update(requestEntity);
					TrackService.endRecord(requestEntity.getQuestId(), StatusConstant.CODE_1400_MSG);
					LogUtils.error("API请求发送异常：" + requestEntity.getQuestId(), thrown);
				}
			} else {
				// HTTP形式的处理程序
				try {
					HttpResponse response = HttpOperatorService.buildRequest(requestEntity);
					if(response == null) {
						throw new Exception();
					}
					requestEntity.setResponseTime(System.currentTimeMillis());
					requestEntity.setStatus(StatusConstant.CODE_1203);
					requestEntity.setResult(response.getStringResult());
					requestEntity.setResponseCharset(response.getCharset());
					requestEntity.setResponseHeaders(response.getResponseHeaders());
					RequestService.update(requestEntity);
					TrackService.endRecord(requestEntity.getQuestId(), StatusConstant.CODE_1203_MSG);
				} catch(Throwable thrown) {
					requestEntity.setResponseTime(System.currentTimeMillis());
					requestEntity.setStatus(StatusConstant.CODE_1400);
					RequestService.update(requestEntity);
					TrackService.endRecord(requestEntity.getQuestId(), StatusConstant.CODE_1400_MSG);
					LogUtils.error("Http请求发送异常：" + requestEntity.getQuestId(), thrown);
				}
			}
		}
	}
}
