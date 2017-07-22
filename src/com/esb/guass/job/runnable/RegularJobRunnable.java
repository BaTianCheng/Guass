package com.esb.guass.job.runnable;

import java.lang.reflect.Method;
import java.util.Map;

import com.esb.guass.common.util.LogUtils;
import com.esb.guass.dispatcher.entity.ResultEntity;
import com.esb.guass.dispatcher.entity.ServiceEntity;
import com.esb.guass.dispatcher.service.ServiceMangerService;
import com.esb.guass.job.entity.RegularJobEntity;
import com.esb.guass.job.service.RegularJobService;

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
			LogUtils.error("定时任务无法启动"+serviceEntity.getServiceName(), ex);
		}
	}
	
	@Override
	public void run() {
		while(true){
			try{
				if(RegularJobService.find(regularJobEntity.getJobCode())!=null
						&&RegularJobService.find(regularJobEntity.getJobCode()).getStatus() == 1){
			        ResultEntity result = (ResultEntity) method.invoke(clazz.newInstance(),null, null);
			        LogUtils.info(regularJobEntity.getJobName()+"执行完成："+result);
				}
			}
			catch(Exception ex){
				LogUtils.error("定时任务执行失败", ex);
			}
			
			try {
				Thread.sleep(regularJobEntity.getIntervalSecond()*1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
