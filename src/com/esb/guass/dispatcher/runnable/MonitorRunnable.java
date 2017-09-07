package com.esb.guass.dispatcher.runnable;

import java.util.List;

import com.esb.guass.common.constant.ConfigConstant;
import com.esb.guass.common.constant.StatusConstant;
import com.esb.guass.dispatcher.entity.RequestEntity;
import com.esb.guass.dispatcher.service.RequestQueue;
import com.esb.guass.dispatcher.service.RequestService;
import com.esb.guass.dispatcher.service.ThreadPoolService;

/**
 * 线程池监视器
 * @author wicks
 */
public class MonitorRunnable implements Runnable{
	
	/**
	 * 构造函数
	 */
	public MonitorRunnable(){
		super();
		
		//监控程序启动时，将正在队列的设为异常
		List<RequestEntity> entities1201 = RequestService.findByStatus(StatusConstant.CODE_1201);
		for(RequestEntity entity : entities1201){
			entity.setStatus(StatusConstant.CODE_1301);
			RequestService.update(entity);
		}
		
		//监控程序启动时，将正在执行的设为异常
		List<RequestEntity> entities1202 = RequestService.findByStatus(StatusConstant.CODE_1202);
		for(RequestEntity entity : entities1202){
			entity.setStatus(StatusConstant.CODE_1301);
			RequestService.update(entity);
		}
		
	}

	@Override
	public void run() {
		while(true){
			//使用空余线程执行请求
			if(ThreadPoolService.getActiveCount()<ConfigConstant.THREADS_MAX_NUM && RequestQueue.getSize() > 0){
				int freeThreadNum = ConfigConstant.THREADS_MAX_NUM - ThreadPoolService.getActiveCount();
				int enqueueNum = freeThreadNum>RequestQueue.getSize() ? RequestQueue.getSize() : freeThreadNum;
				while(enqueueNum>0){
					ThreadPoolService.execute(new RequsetRunnable());
					enqueueNum--;
				}
			}
			
			try {
				Thread.sleep(ConfigConstant.MONITOR_INTERVAL);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}

}
