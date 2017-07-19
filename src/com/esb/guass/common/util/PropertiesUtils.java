package com.esb.guass.common.util;

import java.io.FileInputStream;
import java.util.Properties;

/**
 * 属性文件工具类
 * @author wicks
 */
public class PropertiesUtils {

	/**
	 * 读取属性文件
	 * @param path
	 * @return
	 */
	public static Properties getProperties(String path){
		Properties pro = new Properties();
		try{
			FileInputStream in = new FileInputStream(path);
			pro.load(in);
			in.close();
		}
		catch(Exception ex){
			LogUtils.error("读取属性文件失败", ex);
		}
		return pro;
	}
}