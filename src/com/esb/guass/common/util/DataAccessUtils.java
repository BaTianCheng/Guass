package com.esb.guass.common.util;

import java.util.List;
import java.util.Map;

import org.redkale.source.DataSource;

/**
 * 数据访问操作工具类
 * @author wicks
 */
public class DataAccessUtils {
	
	/**
	 * 判断是否存在
	 * @param ds
	 * @param sql
	 * @return
	 */
	public static boolean isExist(DataSource ds, String sql){
		List<Map<String, Object>> list = ds.directQuery(sql);
		if(list.size() > 0){
			return true;
		} else {
			return false;
		}
	}
	
}
