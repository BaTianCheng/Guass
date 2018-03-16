package com.esb.guass.job.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 任务日志
 * 
 * @author wicks
 */
public class JobLogService {

	private static Logger logger = LoggerFactory.getLogger("com.esb.guass.job");

	public static void info(String info) {
		logger.info(info);
	}

	/**
	 * 输出错误
	 * 
	 * @param msg
	 * @param thrown
	 */
	public static void error(String msg, Throwable thrown) {
		logger.error(msg, thrown);
	}

}
