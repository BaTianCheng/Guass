package com.esb.guass.job.service;

import java.util.List;

import com.esb.guass.job.entity.RegularJobEntity;
import com.esb.guass.job.runnable.PlanningJobRunnable;
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
		//定时任务
		List<RegularJobEntity> RegularJobEntities = RegularJobService.findAll();
		for(RegularJobEntity regularJobEntity : RegularJobEntities){
			Thread thread = new Thread(new RegularJobRunnable(regularJobEntity));
			thread.start();
		}
		
		//计划任务
		Thread thread = new Thread(new PlanningJobRunnable());
		thread.start();
	}
}
