package com.esb.guass.dispatcher.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import com.esb.guass.common.constant.ConfigConstant;

/**
 * 线程池服务类
 * @author wicks
 */
public class ThreadPoolService {
	
	private static ExecutorService fixedThreadPool;
	
	static{
		fixedThreadPool = Executors.newFixedThreadPool(ConfigConstant.THREADS_MAX_NUM);  
	}
	
	/**
	 * 执行线程
	 * @param runnable
	 */
	public static void execute(Runnable runnable){
		fixedThreadPool.execute(runnable);
	}
	
	/**
	 * 获取正在执行数
	 * @return
	 */
	public static int getActiveCount(){
		return ((ThreadPoolExecutor)fixedThreadPool).getActiveCount();
	}

}
