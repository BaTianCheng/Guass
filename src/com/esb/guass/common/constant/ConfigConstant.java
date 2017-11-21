package com.esb.guass.common.constant;

import java.util.Properties;

import com.esb.guass.common.util.LogUtils;
import com.esb.guass.common.util.PropertiesUtils;

public class ConfigConstant {

	// 最佳线程数目 = （（线程等待时间+线程CPU时间）/线程CPU时间 ）* CPU数目
	public static int THREADS_MAX_NUM = 2046;

	// 监视程序监视间隔
	public static int MONITOR_INTERVAL = 100;

	// 请求时如果需要返回结果限时和间隔
	public static int RETURNRESULTDATA_MAXTIME = 2 * 60 * 1000;
	public static int RETURNRESULTDATA_INTERVAL = 50;

	// 请求类型
	public static String HTTP = "HTTP";
	public static String API = "API";

	// 代理模块名
	public static String MODULE_PROXY = "proxy";

	// 权限凭据--计划任务
	public static String IDENTIFICATION_SYS_JOB = "SYS_JOB";
	// 计划任务扫描间隔
	public static int PLANNINGJOB_INTERVAL = 30 * 1000;

	// 数据库加密KEY
	public static String ENCODE_KEY = "hhxxttxs";

	// 是否启动计划任务
	public static boolean PLANJOB_ENABLED = true;
	
	// 是否启动实时任务
	public static boolean REGULARJOB_ENABLED = true;

	static {
		String XMLPATH = "conf/server.properties";
		String userDir = System.getProperty("user.dir");
		Properties properties = PropertiesUtils.getProperties(userDir + "/" + XMLPATH);
		try {
			PLANJOB_ENABLED = Boolean.parseBoolean(properties.getProperty("PLANJOB_ENABLED"));
			REGULARJOB_ENABLED = Boolean.parseBoolean(properties.getProperty("REGULARJOB_ENABLED"));
		} catch(Exception ex) {
			LogUtils.error("配置文件读取失败，请确定格式正确", ex);
		}
	}

}
