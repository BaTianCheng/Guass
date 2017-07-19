package com.esb.guass.dispatcher.runnable;

import com.esb.guass.common.constant.ConfigConstant;
import com.esb.guass.dispatcher.service.RequestQueue;
import com.esb.guass.dispatcher.service.ThreadPoolService;

/**
 * 线程池监视器
 * @author wicks
 */
public class MonitorRunnable implements Runnable{

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
