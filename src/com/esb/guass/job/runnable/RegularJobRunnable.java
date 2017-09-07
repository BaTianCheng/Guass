package com.esb.guass.job.runnable;

import java.lang.reflect.Method;
import java.util.Map;

import com.esb.guass.dispatcher.entity.ResultEntity;
import com.esb.guass.dispatcher.entity.ServiceEntity;
import com.esb.guass.dispatcher.service.ServiceMangerService;
import com.esb.guass.job.entity.RegularJobEntity;
import com.esb.guass.job.service.JobLogService;
import com.esb.guass.job.service.RegularJobService;
import com.google.common.base.Strings;

/**
 * 定时任务线程
 * @author wicks
 */
public class RegularJobRunnable implements Runnable{

	private RegularJobEntity regularJobEntity;
	private ServiceEntity serviceEntity;
	private Method method;
	private Class<?> clazz;
	
	/**
	 * 构造函数
	 * @param regularJobEntity
	 */
	public RegularJobRunnable(RegularJobEntity regularJobEntity){
		this.regularJobEntity = regularJobEntity;
		this.serviceEntity = ServiceMangerService.find(regularJobEntity.getServiceCode());
		try{
			clazz = Class.forName(serviceEntity.getMapUrl());
			method = clazz.getMethod("HandlerRequest",String.class, Map.class);
		}
		catch(Exception ex){
			JobLogService.error("定时任务无法启动"+serviceEntity.getServiceName(), ex);
		}
	}
	
	@Override
	public void run() {
		long logTime = 0;
		while(true){
			try{
				if(RegularJobService.find(regularJobEntity.getJobCode())!=null
						&&RegularJobService.find(regularJobEntity.getJobCode()).getStatus() == 1){
			        ResultEntity result = (ResultEntity) method.invoke(clazz.newInstance(),null, null);
			        regularJobEntity.setLastTime(System.currentTimeMillis());
		        	RegularJobService.update(regularJobEntity);
		        	
			        if(Strings.isNullOrEmpty(result.getMessage())){
				        if(logTime==0 || (System.currentTimeMillis() - logTime) >= 10*60*1000){
				        	//每10分钟记录一次日志
				        	JobLogService.info(regularJobEntity.getJobName()+"执行完成："+result);
				        	logTime = System.currentTimeMillis();
				        }
			        } else {
			        	JobLogService.info(regularJobEntity.getJobName()+"执行完成："+result);
			        }
				}
			}
			catch(Exception ex){
				JobLogService.error("定时任务执行失败", ex);
			}
			
			try {
				Thread.sleep(regularJobEntity.getIntervalSecond()*1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
