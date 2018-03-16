package com.esb.guass.test;

import com.esb.guass.dispatcher.entity.RequestEntity;
import com.esb.guass.dispatcher.runnable.MonitorRunnable;
import com.esb.guass.dispatcher.service.RequestQueue;

public class ThreadPoolTest {

	// http://localhost:16060/esb/proxys/request?url=http://localhost:16060/esb/manger/status/get&data={}&identification=a&getResultData=1

	public static void main(String[] args) {
		/*
		 * for(int i=0;i<10;i++){ ThreadPoolService.execute(new Runnable() {
		 * 
		 * @Override public void run() { try {
		 * System.out.println(ThreadPoolService.getActiveCount());
		 * Thread.sleep(2000); System.out.println("END");
		 * System.out.println(ThreadPoolService.getActiveCount()); } catch
		 * (InterruptedException e) { e.printStackTrace(); } } }); }
		 */

		for(int i = 0; i < 20; i++) {
			RequestQueue.add(new RequestEntity());
		}

		Thread thread = new Thread(new MonitorRunnable());
		thread.start();
	}
}
