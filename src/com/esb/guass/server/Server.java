package com.esb.guass.server;

import java.io.File;

import org.redkale.boot.Application;
import org.slf4j.LoggerFactory;

import com.esb.guass.common.cache.ehcache.EhCacheService;
import com.esb.guass.dispatcher.runnable.MonitorRunnable;
import com.esb.guass.job.service.JobMangerService;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;

/**
 * 服务主程序
 * 
 * @author wicks
 */
public class Server {

	public static void main(String[] args) {
		try {

			// 读取Logback配置文件
			File logbackFile = new File("./conf/logback.xml");
			if(logbackFile.exists()) {
				LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
				JoranConfigurator configurator = new JoranConfigurator();
				configurator.setContext(lc);
				lc.reset();
				configurator.doConfigure(logbackFile);
			}
			
			// 启动缓存
			EhCacheService.init();

			// 启动监控线程
			Thread monitor = new Thread(new MonitorRunnable());
			monitor.start();

			// 启动计划任务线程
			JobMangerService.initJobThread();

			// 主程序启动
			Application.main(args);
			final Application application = Application.create(true);
			application.init();
			application.start();

		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
