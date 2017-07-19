package com.esb.guass.dispatcher.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.esb.guass.common.cache.ehcache.EhCacheService;
import com.esb.guass.common.constant.StatusConstant;
import com.esb.guass.common.dao.mongo.MongoDAO;
import com.esb.guass.dispatcher.entity.RequestCondition;
import com.esb.guass.dispatcher.entity.RequestEntity;
import com.google.common.base.Strings;
import com.mongodb.BasicDBObject;

/**
 * 请求服务
 * @author wicks
 */
public class RequestService {

	private static final String dbName = "db_esb";
	
	private static final String collectionName = "tb_request";
	
	/**
	 * 插入数据
	 * @param entity
	 */
	public static void insert(RequestEntity entity){
		Document doc = new Document(JSONObject.parseObject(entity.toString())) ;
		MongoDAO.getInstance().insert(dbName, collectionName, doc);
		TrackService.record(entity.getQuestId(), StatusConstant.CODE_1201_MSG);
		
		//更新缓存
		EhCacheService.setResultCache(entity);
	}
	
	/**
	 * 更新数据
	 * @param entity
	 */
	public static void update(RequestEntity entity){
		Document doc = new Document(JSONObject.parseObject(entity.toString())) ;
		Document filter = new Document();  
    	filter.append("questId", entity.getQuestId());  
		MongoDAO.getInstance().update(dbName, collectionName, filter, doc);
		
		//更新缓存
		EhCacheService.setResultCache(entity);
	}
	
	/**
	 * 查询
	 * @param questId
	 */
	public static RequestEntity find(String questId){
		//判断是否读取缓存
		if(EhCacheService.getResultCache(questId) != null){
			return EhCacheService.getResultCache(questId);
		}
		
		//无缓存时从数据库中查找
		Document filter = new Document();  
    	filter.append("questId", questId);  
    	List<Document> docs = MongoDAO.getInstance().findBy(dbName, collectionName, filter);
    	if(docs.size() > 0){
    		return JSONObject.toJavaObject(JSONObject.parseObject(JSON.toJSONString(docs.get(0))), RequestEntity.class);
    	}
    	return null;
	}
	
	/**
	 * 查询全部
	 */
	public static List<RequestEntity> findAll(){
		List<Document> docs = MongoDAO.getInstance().findAll(dbName, collectionName);
		return JSONArray.parseArray(JSON.toJSONString(docs), RequestEntity.class);
	}
	
	/**
	 * 按照条件进行查询
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public static  Map<String, Object> findPages(RequestCondition condition){
		List<Bson> conditions = new ArrayList<>();
		if(condition.getBeginTime() > 0){
			conditions.add(new BasicDBObject("requestTime", new BasicDBObject("$gte", condition.getBeginTime())));	
		}
		if(condition.getEndTime() > 0){
			conditions.add(new BasicDBObject("requestTime", new BasicDBObject("$lte", condition.getEndTime() > 0)));	
		}
		if(!Strings.isNullOrEmpty(condition.getQuestId())){
			conditions.add(new BasicDBObject("questId", condition.getQuestId()));	
		}
		if(!Strings.isNullOrEmpty(condition.getServiceCode())){
			conditions.add(new BasicDBObject("serviceCode", condition.getServiceCode()));	
		}
		if(!Strings.isNullOrEmpty(condition.getStatus())){
			//状态特殊处理，1XXX表示小于XXX的状态
			if(condition.getStatus().length() >= 5){
				conditions.add(new BasicDBObject("status", new BasicDBObject("$lte", condition.getStatus().substring(1))));	
			} else {
				conditions.add(new BasicDBObject("status", condition.getStatus()));
			}
		}
		if(!Strings.isNullOrEmpty(condition.getIdentification())){
			conditions.add(new BasicDBObject("identification", condition.getIdentification()));	
		}
		
		Bson filter = new BasicDBObject();
		if(conditions.size() >0 ){
			filter = new BasicDBObject("$and", conditions);
		}
		Document sort = new Document();
    	sort.append("requestTime", -1);
		List<Document> docs = MongoDAO.getInstance().findBy(dbName, collectionName, filter, sort, condition.getPageNum(), condition.getPageSize());
		
		//装配分页信息
		Map<String, Object> pages = new HashMap<>();
		long count =  MongoDAO.getInstance().count(dbName, collectionName, filter);
		pages.put("list", JSONArray.parseArray(JSON.toJSONString(docs), RequestEntity.class));
		pages.put("total",count);
		pages.put("pageNum", condition.getPageNum());
		if(condition.getPageSize() > 0){
			pages.put("pages", (count / condition.getPageSize() + 1));
		}
		
		return pages;
	}
	
}
