package com.esb.guass.dispatcher.service;

import java.util.concurrent.ConcurrentLinkedQueue;

import com.esb.guass.dispatcher.entity.RequestEntity;

/**
 * 请求队列
 * 
 * @author wicks
 */
public class RequestQueue {

	/**
	 * 线程安全有序队列
	 */
	private static ConcurrentLinkedQueue<RequestEntity> queue;
	private static ConcurrentLinkedQueue<RequestEntity> priorityQueue;

	static {
		queue = new ConcurrentLinkedQueue<RequestEntity>();
		priorityQueue = new ConcurrentLinkedQueue<RequestEntity>();
	}

	/**
	 * 入普通队列
	 * 
	 * @param requestEntity
	 */
	public static void add(RequestEntity requestEntity) {
		queue.add(requestEntity);
	}

	/**
	 * 入优先队列
	 * 
	 * @param requestEntity
	 */
	public static void addPriority(RequestEntity requestEntity) {
		priorityQueue.add(requestEntity);
	}

	/**
	 * 出队
	 * 
	 * @return
	 */
	public static RequestEntity poll() {
		// 优先队列享有优先出队权利
		if(priorityQueue.size() > 0) {
			return priorityQueue.poll();
		} else if(queue.size() > 0) {
			return queue.poll();
		} else {
			return null;
		}
	}

	/**
	 * 获取队列长度
	 * 
	 * @return
	 */
	public static int getSize() {
		return priorityQueue.size() + queue.size();
	}

}
