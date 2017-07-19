package com.esb.guass.common.constant;

/**
 * HTTP常量类
 * @author wicks
 */
public class HttpConstant {

	/**
	 * 默认字符集
	 */
	public static String DEFAULT_CHARSET = "UTF8";
	
	/**
	 * 默认方法
	 */
	public static String DEFAULT_METHOD = "POST";

	/**
	 * 连接超时时间（8000）
	 */
	public static int defaultConnectionTimeout = 8000;

	/**
	 * 响应超时时间（60000）
	 */
	public static int defaultSoTimeout = 60000;

	/**
	 * 闲置超时时间（60000）
	 */
	public static int defaultIdleConnTimeout = 60000;

	/**
	 * 路由最大连接数
	 */
	public static int defaultMaxConnPerHost = 512;

	/**
	 * 连接池数量
	 */
	public static int defaultMaxTotalConn = 1024;

	/**
	 * 等待HttpConnectionManager返回连接超时（只有在达到最大连接数时起作用）：3秒
	 */
	public static final long defaultHttpConnectionManagerTimeout = 3 * 1000;

}
