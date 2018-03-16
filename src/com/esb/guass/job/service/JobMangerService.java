package com.esb.guass.job.service;

import java.util.List;

import com.esb.guass.common.constant.ConfigConstant;
import com.esb.guass.job.entity.RegularJobEntity;
import com.esb.guass.job.runnable.PlanningJobRunnable;
import com.esb.guass.job.runnable.RegularJobRunnable;

/**
 * 任务管理服务类
 * 
 * @author wicks
 */
public class JobMangerService {

	/**
	 * 初始化任务线程
	 */
	public static void initJobThread() {
		// 定时任务
		if(ConfigConstant.REGULARJOB_ENABLED == true) {
			List<RegularJobEntity> RegularJobEntities = RegularJobService.findAll();
			JobLogService.info("定时任务启动开始");
			for(RegularJobEntity regularJobEntity : RegularJobEntities) {
				Thread thread = new Thread(new RegularJobRunnable(regularJobEntity));
				thread.start();
			}
		}

		// 计划任务
		if(ConfigConstant.PLANJOB_ENABLED == true) {
			Thread thread = new Thread(new PlanningJobRunnable());
			thread.start();
		}
	}
}
