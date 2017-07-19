package com.esb.guass.job.runnable;

import java.util.List;

import com.esb.guass.job.entity.RegularJobEntity;
import com.esb.guass.job.service.JobMangerService;
import com.esb.guass.job.service.RegularJobService;

/**
 * 定时任务线程
 * @author wicks
 */
public class RegularJobRunnable implements Runnable{

	@Override
	public void run() {
		//每隔一分钟判断是否有定时任务需要执行
		while(true){
			long nowTime = System.currentTimeMillis();
			List<RegularJobEntity> entities = RegularJobService.findAll();
			for(RegularJobEntity entity : entities){
				if((nowTime - entity.getLastTime()) > entity.getIntervalMinute()*60*1000){
					JobMangerService.excute(entity);
				}
			}
			
			try {
				Thread.sleep(60*1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
