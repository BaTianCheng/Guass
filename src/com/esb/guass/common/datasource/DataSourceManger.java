package com.esb.guass.common.datasource;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.redkale.source.DataSource;
import org.redkale.source.DataSources;

/**
 * 数据源管理器
 * @author wicks
 */
public class DataSourceManger {
	
	public static Map<String, DataSource> dataSources = new HashMap<>();
	
	//设置数据库文件读取路径
	static{
		System.setProperty(DataSources.DATASOURCE_CONFPATH,"conf/persistence.xml");
	}
	
	/**
	 * 获取数据源
	 * @param sourceName
	 * @return
	 * @throws IOException 
	 */
	public static DataSource getDataSource(String sourceName) throws IOException{
		if(!dataSources.containsKey(sourceName)){
			DataSource ds = DataSources.createDataSource(sourceName);
			dataSources.put(sourceName, ds);
		}
		return dataSources.get(sourceName);
	}

}
