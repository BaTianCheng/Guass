package com.esb.guass.job.service;

import java.util.List;

import com.esb.guass.job.entity.RegularJobEntity;
import com.esb.guass.job.runnable.RegularJobRunnable;

/**
 * 任务管理服务类
 * @author wicks
 */
public class JobMangerService {
	
	/**
	 * 初始化任务线程
	 */
	public static void initJobThread(){
		List<RegularJobEntity> RegularJobEntities = RegularJobService.findAll();
		for(RegularJobEntity regularJobEntity : RegularJobEntities){
			Thread thread = new Thread(new RegularJobRunnable(regularJobEntity));
			thread.start();
		}        
	}
	
//	/**
//	 * 执行任务（定时任务）
//	 * @param entity
//	 */
//	public static void excute(RegularJobEntity entity){
//		RequestEntity lastRequestEntity = RequestService.find(entity.getLastQuestId());
//		if(lastRequestEntity.getStatus().equals(StatusConstant.CODE_1201)){
//			LogUtils.info(entity.getJobName()+entity.getLastQuestId()+"还在排队中，本次不执行");
//		} else if(lastRequestEntity.getStatus().equals(StatusConstant.CODE_1202)){
//			LogUtils.info(entity.getJobName()+entity.getLastQuestId()+"还在执行中，本次不执行");
//		} else {
//			RequestEntity requestEntity = new RequestEntity();
//			//等待处理
////			RequestEntity requestEntity = ServiceMangerService.sendService(
////					entity.getServiceCode(),ConfigConstant.IDENTIFICATION_SYS_JOB,null,null,null);
//			entity.setLastQuestId(requestEntity.getQuestId());
//			entity.setLastTime(requestEntity.getRequestTime());
//			RegularJobService.update(entity);
//		}
//	}
	
}
