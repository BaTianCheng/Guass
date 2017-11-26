package com.esb.guass.auth.service;

import java.util.List;

import org.bson.Document;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.esb.guass.common.cache.ehcache.EhCacheService;
import com.esb.guass.common.dao.mongo.MongoDAO;
import com.esb.guass.auth.entity.VisitorEntity;

/**
 * 访问者服务
 * 
 * @author wicks
 */
public class VisitorService {

	private static final String dbName = "db_esb";

	private static final String collectionName = "tb_visitor";

	/**
	 * 插入数据
	 * 
	 * @param entity
	 */
	public static void insert(VisitorEntity entity) {
		Document doc = new Document(JSONObject.parseObject(entity.toString()));
		MongoDAO.getInstance().insert(dbName, collectionName, doc);

		// 更新缓存
		EhCacheService.setVisitorCache(entity);
	}

	/**
	 * 更新数据
	 * 
	 * @param entity
	 */
	public static void update(VisitorEntity entity) {
		Document doc = new Document(JSONObject.parseObject(entity.toString()));
		Document filter = new Document();
		filter.append("appId", entity.getAppId());
		MongoDAO.getInstance().update(dbName, collectionName, filter, doc);

		// 更新缓存
		EhCacheService.setVisitorCache(entity);
	}

	/**
	 * 查询
	 * 
	 * @param serviceCode
	 */
	public static VisitorEntity find(String appId) {
		// 判断是否读取缓存
		if(EhCacheService.getServiceCache(appId) != null) {
			return EhCacheService.getVisitorCache(appId);
		}

		Document filter = new Document();
		filter.append("appId", appId);
		List<Document> docs = MongoDAO.getInstance().findBy(dbName, collectionName, filter);
		if(docs.size() > 0) {
			return JSONObject.toJavaObject(JSONObject.parseObject(JSON.toJSONString(docs.get(0))), VisitorEntity.class);
		}
		return null;
	}

	/**
	 * 查询全部
	 */
	public static List<VisitorEntity> findAll() {
		List<Document> docs = MongoDAO.getInstance().findAll(dbName, collectionName);
		return JSONArray.parseArray(JSON.toJSONString(docs), VisitorEntity.class);
	}

}
