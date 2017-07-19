package com.esb.guass.common.constant;

public class ConfigConstant {
	
	//最佳线程数目 = （（线程等待时间+线程CPU时间）/线程CPU时间 ）* CPU数目
	public static int THREADS_MAX_NUM = 2046;
	
	//监视程序监视间隔
	public static int MONITOR_INTERVAL = 100;
	
	//请求时如果需要返回结果限时和间隔
	public static int RETURNRESULTDATA_MAXTIME = 60 * 1000;
	public static int RETURNRESULTDATA_INTERVAL = 50;
	
	//请求类型
	public static String HTTP = "HTTP";
	public static String API = "API";
	
	//权限凭据
	public static String IDENTIFICATION_SYS_JOB = "SYS_JOB";
}
