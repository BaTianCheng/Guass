package com.esb.guass.server;

import org.redkale.boot.Application;

import com.esb.guass.common.cache.ehcache.EhCacheService;
import com.esb.guass.dispatcher.runnable.MonitorRunnable;
import com.esb.guass.job.service.JobMangerService;

/**
 * 服务主程序
 * @author wicks
 */
public class Server {

	public static void main(String[] args) {
		 try {
	        //启动缓存
	        EhCacheService.init();
	        
			//启动监控线程
	        Thread monitor = new Thread(new MonitorRunnable());
	        monitor.start();
	        
	        //启动计划任务线程
	        JobMangerService.initJobThread();
	            
			 //主程序启动
			Application.main(args);
			final Application application = Application.create(true);
            application.init();
            application.start();
            
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
