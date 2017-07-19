package com.esb.guass.job.service;

import java.util.List;

import org.bson.Document;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.esb.guass.common.dao.mongo.MongoDAO;
import com.esb.guass.job.entity.RegularJobEntity;

public class RegularJobService {

	private static final String dbName = "db_esb";
	
	private static final String collectionName = "tb_regularjob";
	
	/**
	 * 插入数据
	 * @param entity
	 */
	public static void insert(RegularJobEntity entity){
		Document doc = new Document(JSONObject.parseObject(entity.toString())) ;
		MongoDAO.getInstance().insert(dbName, collectionName, doc);
	}
	
	/**
	 * 更新数据
	 * @param entity
	 */
	public static void update(RegularJobEntity entity){
		Document doc = new Document(JSONObject.parseObject(entity.toString())) ;
		Document filter = new Document();  
    	filter.append("jobId", entity.getJobId());  
		MongoDAO.getInstance().update(dbName, collectionName, filter, doc);
	}
	
	/**
	 * 查询
	 * @param questId
	 */
	public static RegularJobEntity find(String jobId){
		Document filter = new Document();  
    	filter.append("jobId", jobId);  
    	List<Document> docs = MongoDAO.getInstance().findBy(dbName, collectionName, filter);
    	if(docs.size() > 0){
    		return JSONObject.toJavaObject(JSONObject.parseObject(JSON.toJSONString(docs.get(0))), RegularJobEntity.class);
    	}
    	return null;
	}
	
	/**
	 * 查询全部
	 */
	public static List<RegularJobEntity> findAll(){
		List<Document> docs = MongoDAO.getInstance().findAll(dbName, collectionName);
		return JSONArray.parseArray(JSON.toJSONString(docs), RegularJobEntity.class);
	}
}
