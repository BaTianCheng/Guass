package com.esb.guass.dispatcher.service;

import java.util.List;

import org.bson.Document;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.esb.guass.common.dao.mongo.MongoDAO;
import com.esb.guass.dispatcher.entity.ModuleEntity;

/**
 * 轨迹服务类
 * @author wicks
 */
public class ModuleService {

private static final String dbName = "db_esb";
	
	private static final String collectionName = "tb_module";
	
	/**
	 * 插入数据
	 * @param entity
	 */
	public static void insert(ModuleEntity entity){
		Document doc = new Document(JSONObject.parseObject(entity.toString())) ;
		MongoDAO.getInstance().secondaryInsert(dbName, collectionName, doc);
	}
	
	/**
	 * 更新数据
	 * @param entity
	 */
	public static void update(ModuleEntity entity){
		Document doc = new Document(JSONObject.parseObject(entity.toString())) ;
		Document filter = new Document();  
    	filter.append("module", entity.getModule());  
		MongoDAO.getInstance().update(dbName, collectionName, filter, doc);
	}
	
	/**
	 * 查询全部
	 */
	public static List<ModuleEntity> findAll(){
		Document sort = new Document();
    	sort.append("sortNum", 1);
    	List<Document> docs = MongoDAO.getInstance().findAll(dbName, collectionName, sort);
    	return JSONArray.parseArray(JSON.toJSONString(docs), ModuleEntity.class);
	}
}
