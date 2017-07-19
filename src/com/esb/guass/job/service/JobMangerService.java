package com.esb.guass.job.service;

import com.esb.guass.common.constant.ConfigConstant;
import com.esb.guass.common.constant.StatusConstant;
import com.esb.guass.common.util.LogUtils;
import com.esb.guass.dispatcher.entity.RequestEntity;
import com.esb.guass.dispatcher.service.RequestService;
import com.esb.guass.dispatcher.service.ServiceMangerService;
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
        Thread regularJobThread = new Thread(new RegularJobRunnable());
        regularJobThread.start();
        
        
        //设置尚未执行完的任务为程序异常状态
        
	}
	
	/**
	 * 执行任务（定时任务）
	 * @param entity
	 */
	public static void excute(RegularJobEntity entity){
		RequestEntity lastRequestEntity = RequestService.find(entity.getLastQuestId());
		if(lastRequestEntity.getStatus().equals(StatusConstant.CODE_1201)){
			LogUtils.info(entity.getJobName()+entity.getLastQuestId()+"还在排队中，本次不执行");
		} else if(lastRequestEntity.getStatus().equals(StatusConstant.CODE_1202)){
			LogUtils.info(entity.getJobName()+entity.getLastQuestId()+"还在执行中，本次不执行");
		} else {
			RequestEntity requestEntity = ServiceMangerService.sendService(
					entity.getServiceCode(),ConfigConstant.IDENTIFICATION_SYS_JOB,null,null,null);
			entity.setLastQuestId(requestEntity.getQuestId());
			entity.setLastTime(requestEntity.getRequestTime());
			RegularJobService.update(entity);
		}
	}
	
}
