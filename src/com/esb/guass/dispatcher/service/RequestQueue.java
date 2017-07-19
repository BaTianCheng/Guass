package com.esb.guass.dispatcher.service;

import java.util.concurrent.ConcurrentLinkedQueue;

import com.esb.guass.dispatcher.entity.RequestEntity;

/**
 * 请求队列
 * @author wicks
 */
public class RequestQueue {

	/**
	 * 线程安全有序队列
	 */
	private static ConcurrentLinkedQueue<RequestEntity> queue;
	
	static{
		queue = new ConcurrentLinkedQueue<RequestEntity>();
	}
	
	/**
	 * 入列
	 * @param requestEntity
	 */
	public static void add(RequestEntity requestEntity){
		queue.add(requestEntity);
	}
	
	/**
	 * 出队
	 * @return
	 */
	public static RequestEntity poll(){
		if(queue.size() > 0){
			return queue.poll();
		} else {
			return null;
		}
	}
	
	/**
	 * 获取队列长度
	 * @return
	 */
	public static int getSize(){
		return queue.size();
	}
	
}
