package com.esb.guass.common.constant;

import java.util.Properties;

import org.redkale.source.Encode;

import com.esb.guass.common.util.PropertiesUtils;
import com.google.common.base.Strings;

/**
 * Mongo常量类
 * 
 * @author wicks
 */
public class MongoConstant {

	/**
	 * 配置文件路径
	 */
	private static String XMLPATH = "conf/mongo.properties";

	// 初始化属性
	static {
		String userDir = System.getProperty("user.dir");
		Properties properties = PropertiesUtils.getProperties(userDir + "/" + XMLPATH);
		IP = properties.getProperty("IP");
		PORT = Integer.valueOf(properties.getProperty("PORT"));
		USERNAME = properties.getProperty("USERNAME");
		PASSWORD = properties.getProperty("PASSWORD");
		if(!Strings.isNullOrEmpty(properties.getProperty("ENCRYPT"))) {
			Encode encode = new Encode("hhxxttxs");
			PASSWORD = encode.DecryptionStringData(MongoConstant.PASSWORD);
		}
	}

	/**
	 * IP地址
	 */
	public static String IP;

	/**
	 * 端口号
	 */
	public static int PORT;

	/**
	 * 用户名
	 */
	public static String USERNAME;

	/**
	 * 密码
	 */
	public static String PASSWORD;

	/**
	 * 最大连接数
	 */
	public static int MAX_CONNECTIONS = 50;

	/**
	 * 次要客户端最大连接数
	 */
	public static int SECONDARY_MAX_CONNECTIONS = 20;

	/**
	 * 连接超时
	 */
	public static int CONNECTION_TIMEOUT = 1000 * 60 * 20;

	/**
	 * 最大线程等待时间
	 */
	public static int THREAD_MAX_WAITTIME = 1000 * 60 * 5;

	/**
	 * 最大线程等待数
	 */
	public static int THREAD_MAX_WAITNUM = 100;

}
