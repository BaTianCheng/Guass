package com.esb.guass.dispatcher.service;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.esb.guass.common.dao.mongo.MongoDAO;
import com.esb.guass.dispatcher.entity.TrackEntity;

/**
 * 轨迹服务类
 * 
 * @author wicks
 */
public class TrackService {

	private static final String dbName = "db_esb";

	private static final String collectionName = "tb_track";

//  定期写入数据库，暂不启用
//	private static List<Document> documents;
//	
//	static{
//		documents = new ArrayList<>();
//		
//		// 每隔五秒插入一次轨迹数据
//		Thread thread = new Thread(new Runnable() {
//			@Override
//			public void run() {
//				while(true){
//					try {
//						Thread.sleep(5000);
//						synchronized(TrackService.class) {
//							List<Document> insertDocuments = new ArrayList<>();
//							insertDocuments.addAll(documents);
//							
//						}
//					} catch(InterruptedException e) {
//					}
//				}
//			}
//		});
//		
//		thread.start();
//	}

	/**
	 * 记录轨迹
	 * 
	 * @param questId
	 * @param message
	 */
	public static void record(String questId, String message) {
		try {
			TrackEntity entity = new TrackEntity();
			entity.setQuestId(questId);
			entity.setTime(System.currentTimeMillis());
			entity.setMessage(message);
			insert(entity);
		} catch(Exception ex) {

		}
	}

	/**
	 * 记录信息类轨迹
	 * 
	 * @param questId
	 * @param message
	 */
	public static void recordInfo(String questId, String message) {
		try {
			TrackEntity entity = new TrackEntity();
			entity.setQuestId(questId);
			entity.setTime(System.currentTimeMillis());
			entity.setMessage(message);
			entity.setNo(150);
			insert(entity);
		} catch(Exception ex) {

		}
	}

	/**
	 * 记录最后一条轨迹
	 * 
	 * @param questId
	 * @param message
	 */
	public static void endRecord(String questId, String message) {
		try {
			TrackEntity entity = new TrackEntity();
			entity.setQuestId(questId);
			entity.setTime(System.currentTimeMillis());
			entity.setMessage(message);
			entity.setNo(200);
			insert(entity);
		} catch(Exception ex) {

		}
	}

	/**
	 * 插入数据
	 * 
	 * @param entity
	 */
	public static void insert(TrackEntity entity) {
		Document doc = new Document(JSONObject.parseObject(entity.toString()));
		MongoDAO.getInstance().secondaryInsert(dbName, collectionName, doc);
	}

	/**
	 * 查询
	 * 
	 * @param questId
	 */
	public static List<TrackEntity> find(String questId) {
		Document filter = new Document();
		filter.append("questId", questId);
		Document sort = new Document();
		sort.append("time", 1);
		List<Document> docs = MongoDAO.getInstance().findBy(dbName, collectionName, filter, sort);
		return JSONArray.parseArray(JSON.toJSONString(docs), TrackEntity.class);
	}
}
