package com.esb.guass.job.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.esb.guass.common.dao.mongo.MongoDAO;
import com.esb.guass.dispatcher.entity.CommonCondition;
import com.esb.guass.job.entity.RegularJobEntity;
import com.google.common.base.Strings;

/**
 * 实时任务服务类
 * @author wicks
 */
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
    	filter.append("jobCode", entity.getJobCode());  
		MongoDAO.getInstance().update(dbName, collectionName, filter, doc);
	}
	
	/**
	 * 删除数据
	 * @param entity
	 */
	public static void delete(String jobCode){
		Document filter = new Document();  
    	filter.append("jobCode", jobCode);  
		MongoDAO.getInstance().delete(dbName, collectionName, filter);
	}
	
	/**
	 * 查询
	 * @param questId
	 */
	public static RegularJobEntity find(String jobCode){
		Document filter = new Document();  
    	filter.append("jobCode", jobCode);  
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
	
	/**
	 * 按模块查询
	 */
	public static Map<String, Object> findPages(CommonCondition condition){
		Document filter = new Document();
		if(!Strings.isNullOrEmpty(condition.getModule())){
			filter.append("module", condition.getModule());  
		}
		List<Document> docs = MongoDAO.getInstance().findBy(dbName, collectionName, filter, condition.getPageNum(), condition.getPageSize());
		
		//装配分页信息
		Map<String, Object> pages = new HashMap<>();
		long count =  MongoDAO.getInstance().count(dbName, collectionName, filter);
		pages.put("list", JSONArray.parseArray(JSON.toJSONString(docs), RegularJobEntity.class));
		pages.put("total",count);
		pages.put("pageNum", condition.getPageNum());
		if(condition.getPageSize() > 0){
			pages.put("pages", (count / condition.getPageSize() + 1));
		}
		
		return pages;
	}
}
