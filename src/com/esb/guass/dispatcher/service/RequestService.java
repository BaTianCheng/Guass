package com.esb.guass.dispatcher.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.redkale.net.http.HttpRequest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.esb.guass.common.cache.ehcache.EhCacheService;
import com.esb.guass.common.constant.ParamConstants;
import com.esb.guass.common.constant.StatusConstant;
import com.esb.guass.common.dao.mongo.MongoDAO;
import com.esb.guass.dispatcher.entity.CommonCondition;
import com.esb.guass.dispatcher.entity.RequestEntity;
import com.esb.guass.dispatcher.exception.ParseException;
import com.google.common.base.Strings;
import com.mongodb.BasicDBObject;

/**
 * 请求服务
 * 
 * @author wicks
 */
public class RequestService {

	private static final String dbName = "db_esb";

	private static final String collectionName = "tb_request";

	/**
	 * 生成请求实体类
	 * 
	 * @param req
	 * @return
	 */
	public static RequestEntity getRequestEntuty(HttpRequest req) {
		RequestEntity requestEntity = new RequestEntity();
		requestEntity.setQuestId(UUID.randomUUID().toString());
		requestEntity.setRequestTime(System.currentTimeMillis());
		requestEntity.setStatus(StatusConstant.CODE_1201);
		requestEntity.setRequestIP(req.getRemoteAddr());

		// 参数解析
		Map<String, String> params = new HashMap<>();
		for(String paramName : req.getParameterNames()) {
			if(params.containsKey(paramName))
				continue;

			switch(paramName) {
				case ParamConstants.PARAM_SERVICECODE:
					requestEntity.setServiceCode(req.getParameter(paramName));
					break;
				case ParamConstants.PARAM_APPID:
					requestEntity.setAppId(req.getParameter(paramName));
					break;
				case ParamConstants.PARAM_SIGN:
					requestEntity.setSign(req.getParameter(paramName));
					break;
				case ParamConstants.PARAM_IDENTIFICATION:
					requestEntity.setIdentification(req.getParameter(paramName));
					break;
				case ParamConstants.PARAM_ASYNC:
					requestEntity.setAsync(req.getBooleanParameter(paramName, true));
					break;
				case ParamConstants.PARAM_DATA:
					break;
				default:
					params.put(paramName, req.getParameter(paramName));
					break;
			}
		}

		// 解析DATA参数
		if(!Strings.isNullOrEmpty(req.getParameter(ParamConstants.PARAM_DATA))) {
			try {
				@SuppressWarnings("unchecked")
				Map<String, Object> paramDatas = JSON.parseObject(req.getParameter(ParamConstants.PARAM_DATA), HashMap.class);
				if(paramDatas != null) {
					for(Entry<String, Object> paramData : paramDatas.entrySet()) {
						if(paramData.getValue() instanceof String) {
							params.put(paramData.getKey(), String.valueOf(paramData.getValue()));
						} else if(paramData.getValue() instanceof JSONObject || paramData.getValue() instanceof JSONArray) {
							params.put(paramData.getKey(), JSON.toJSONString(paramData.getValue()));
						} else {
							params.put(paramData.getKey(), String.valueOf((paramData.getValue())));
						}
					}
				}
			} catch(ParseException ex) {
				throw ex;
			} catch(Exception ex) {
				throw new ParseException("data参数解析失败，请确保是k-v形式的json字符串，同时属性值仅限字符串和数字类型");
			}
		} else if(req.getMethod().equals("POST") && !Strings.isNullOrEmpty(req.getBodyUTF8())){
			//如果没有data参数，取body中的数据为data参数
			try {
				@SuppressWarnings("unchecked")
				Map<String, Object> paramDatas = JSON.parseObject(req.getBodyUTF8(), HashMap.class);
				if(paramDatas != null) {
					for(Entry<String, Object> paramData : paramDatas.entrySet()) {
						if(paramData.getValue() instanceof String) {
							params.put(paramData.getKey(), String.valueOf(paramData.getValue()));
						} else if(paramData.getValue() instanceof JSONObject || paramData.getValue() instanceof JSONArray) {
							params.put(paramData.getKey(), JSON.toJSONString(paramData.getValue()));
						} else {
							params.put(paramData.getKey(), String.valueOf((paramData.getValue())));
						}
					}
				}
			} catch(ParseException ex) {
				throw ex;
			} catch(Exception ex) {
				throw new ParseException("body解析失败，请确保是k-v形式的json字符串，同时属性值仅限字符串和数字类型");
			}
		}
		requestEntity.setParams(params);

		// 解析头部
		Map<String, String> head = new HashMap<>();
		head.put("content-type", req.getContentType());
		if(req.getHeaderNames() != null) {
			for(String headName : req.getHeaderNames()) {
				head.put(headName, req.getHeader(headName));
			}
		}

		// 解析体
		requestEntity.setPostBody(req.getBodyUTF8());

		return requestEntity;
	}

	/**
	 * 插入数据
	 * 
	 * @param entity
	 */
	public static void insert(RequestEntity entity) {
		Document doc = new Document(JSONObject.parseObject(entity.toString()));
		MongoDAO.getInstance().insert(dbName, collectionName, doc);
		TrackService.record(entity.getQuestId(), StatusConstant.CODE_1201_MSG);

		// 更新缓存
		EhCacheService.setResultCache(entity);
	}

	/**
	 * 更新数据
	 * 
	 * @param entity
	 */
	public static void update(RequestEntity entity) {
		Document doc = new Document(JSONObject.parseObject(entity.toString()));
		Document filter = new Document();
		filter.append("questId", entity.getQuestId());
		MongoDAO.getInstance().update(dbName, collectionName, filter, doc);

		// 更新缓存
		EhCacheService.setResultCache(entity);
	}

	/**
	 * 查询
	 * 
	 * @param questId
	 */
	public static RequestEntity find(String questId) {
		// 判断是否读取缓存
		if(EhCacheService.getResultCache(questId) != null) {
			return EhCacheService.getResultCache(questId);
		}

		// 无缓存时从数据库中查找
		Document filter = new Document();
		filter.append("questId", questId);
		List<Document> docs = MongoDAO.getInstance().findBy(dbName, collectionName, filter);
		if(docs.size() > 0) {
			return JSONObject.toJavaObject(JSONObject.parseObject(JSON.toJSONString(docs.get(0))), RequestEntity.class);
		}
		return null;
	}

	/**
	 * 根据业务编号查询
	 * 
	 * @param questId
	 */
	public static RequestEntity findByBusinessId(String businessId) {
		// 从数据库中查找
		Document filter = new Document();
		filter.append("requestOption.businessId", businessId);
		List<Document> docs = MongoDAO.getInstance().findBy(dbName, collectionName, filter);
		if(docs.size() > 0) {
			return JSONObject.toJavaObject(JSONObject.parseObject(JSON.toJSONString(docs.get(0))), RequestEntity.class);
		}
		return null;
	}

	/**
	 * 查询全部
	 */
	public static List<RequestEntity> findAll() {
		List<Document> docs = MongoDAO.getInstance().findAll(dbName, collectionName);
		return JSONArray.parseArray(JSON.toJSONString(docs), RequestEntity.class);
	}

	/**
	 * 根据状态查询
	 */
	public static List<RequestEntity> findByStatus(String status) {
		Document filter = new Document();
		filter.append("status", status);
		List<Document> docs = MongoDAO.getInstance().findBy(dbName, collectionName, filter);
		return JSONArray.parseArray(JSON.toJSONString(docs), RequestEntity.class);
	}

	/**
	 * 按照条件进行查询
	 * 
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	public static Map<String, Object> findPages(CommonCondition condition) {
		List<Bson> conditions = new ArrayList<>();
		if(condition.getBeginTime() > 0) {
			conditions.add(new BasicDBObject("requestTime", new BasicDBObject("$gte", condition.getBeginTime())));
		}
		if(condition.getEndTime() > 0) {
			conditions.add(new BasicDBObject("requestTime", new BasicDBObject("$lte", condition.getEndTime())));
		}
		if(!Strings.isNullOrEmpty(condition.getQuestId())) {
			conditions.add(new BasicDBObject("questId", condition.getQuestId()));
		}
		if(!Strings.isNullOrEmpty(condition.getServiceCode())) {
			conditions.add(new BasicDBObject("serviceCode", condition.getServiceCode()));
		}
		if(!Strings.isNullOrEmpty(condition.getModule())) {
			conditions.add(new BasicDBObject("module", condition.getModule()));
		}
		if(!Strings.isNullOrEmpty(condition.getStatus())) {
			// 状态特殊处理，1XXXX表示小于等于XXXX的状态，0表示不等于，2表示大于等于
			if(condition.getStatus().length() >= 5) {
				switch(condition.getStatus().charAt(0)) {
					case '0':
						conditions.add(new BasicDBObject("status", new BasicDBObject("$ne", condition.getStatus().substring(1))));
						break;
					case '1':
						conditions.add(new BasicDBObject("status", new BasicDBObject("$lte", condition.getStatus().substring(1))));
						break;
					case '2':
						conditions.add(new BasicDBObject("status", new BasicDBObject("$gte", condition.getStatus().substring(1))));
						break;
					default:
						break;
				}
			} else {
				conditions.add(new BasicDBObject("status", condition.getStatus()));
			}
		}
		if(!Strings.isNullOrEmpty(condition.getIdentification())) {
			conditions.add(new BasicDBObject("identification", condition.getIdentification()));
		}
		
		if(!Strings.isNullOrEmpty(condition.getParamKey()) && !Strings.isNullOrEmpty(condition.getParamValue())) {
			conditions.add(new BasicDBObject(condition.getParamKey(), condition.getParamValue()));
		}

		Bson filter = new BasicDBObject();
		if(conditions.size() > 0) {
			filter = new BasicDBObject("$and", conditions);
		}
		Document sort = new Document();
		sort.append("requestTime", -1);
		List<Document> docs = MongoDAO.getInstance().findBy(dbName, collectionName, filter, sort, condition.getPageNum(), condition.getPageSize());

		// 装配分页信息
		Map<String, Object> pages = new HashMap<>();
		long count = MongoDAO.getInstance().count(dbName, collectionName, filter);
		pages.put("list", JSONArray.parseArray(JSON.toJSONString(docs), RequestEntity.class));
		pages.put("total", count);
		pages.put("pageNum", condition.getPageNum());
		if(condition.getPageSize() > 0) {
			if(count % condition.getPageSize() == 0) {
				pages.put("pages", (count / condition.getPageSize()));
			} else {
				pages.put("pages", (count / condition.getPageSize() + 1));
			}
		}

		return pages;
	}

}
