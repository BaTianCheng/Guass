package com.esb.guass.dispatcher.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.redkale.net.http.HttpRequest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.esb.guass.common.cache.ehcache.EhCacheService;
import com.esb.guass.common.constant.ParamConstants;
import com.esb.guass.common.dao.mongo.MongoDAO;
import com.esb.guass.dispatcher.entity.RequestEntity;
import com.esb.guass.dispatcher.entity.RequestOption;
import com.esb.guass.dispatcher.entity.ServiceEntity;
import com.esb.guass.dispatcher.exception.UnfindServiceException;
import com.google.common.base.Strings;

/**
 * 服务管理服务类
 * 
 * @author wicks
 */
public class ServiceMangerService {

	private static final String dbName = "db_esb";

	private static final String collectionName = "tb_service";

	/**
	 * 从服务组装请求
	 * 
	 * @param serviceEntity
	 * @return
	 */
	public static RequestEntity getRequestByService(RequestEntity requestEntity, HttpRequest req) {
		ServiceEntity serviceEntity = find(requestEntity.getServiceCode());
		if(serviceEntity != null) {
			requestEntity.setModule(serviceEntity.getModule());
			requestEntity.setServiceName(serviceEntity.getServiceName());
			requestEntity.setRequestType(serviceEntity.getRequestType());
			requestEntity.setUrl(serviceEntity.getMapUrl());
			requestEntity.setResponseContentType(serviceEntity.getResponseContentType());
			requestEntity.setResponseErrorMsg(serviceEntity.getResponseErrorMsg());
			requestEntity.setDirectReturn(serviceEntity.isDirectReturn());
			requestEntity.setAuthValidate(serviceEntity.isAuthValidate());
			requestEntity.setRecordRequest(serviceEntity.isRecordRequest());
			if(serviceEntity.getRequestOption() == null) {
				requestEntity.setRequestOption(new RequestOption());
			} else {
				requestEntity.setRequestOption(serviceEntity.getRequestOption());
			}

			if(serviceEntity.getHeadParams() != null && serviceEntity.getHeadParams().size() > 0) {
				Map<String, String> headers = new HashMap<>();
				for(String headParam : serviceEntity.getHeadParams()) {
					if(requestEntity.getParams().containsKey(headParam)) {
						headers.put(headParam, requestEntity.getParams().get(headParam));
					}
				}
				requestEntity.setHead(headers);
			}

			if(req != null) {
				requestEntity.setAsync(req.getBooleanParameter(ParamConstants.PARAM_ASYNC, serviceEntity.isAsync()));
			}
			return requestEntity;
		} else {
			throw new UnfindServiceException("无法获得服务，服务不存在");
		}
	}

	/**
	 * 获取自定义异常
	 * 
	 * @param serviceEntity
	 * @return
	 */
	public static RequestEntity getErrRequest(HttpRequest req) {
		RequestEntity requestEntity = new RequestEntity();
		if(!Strings.isNullOrEmpty(req.getParameter(ParamConstants.PARAM_SERVICECODE))) {
			requestEntity.setServiceCode(req.getParameter(ParamConstants.PARAM_SERVICECODE));
			ServiceEntity serviceEntity = find(requestEntity.getServiceCode());
			if(serviceEntity != null) {
				requestEntity.setServiceName(serviceEntity.getServiceName());
				requestEntity.setResponseErrorMsg(serviceEntity.getResponseErrorMsg());
			}
		}
		return requestEntity;
	}

	/**
	 * 发送请求
	 * 
	 * @param serviceEntity
	 * @return
	 */
	public static RequestEntity sendService(RequestEntity requestEntity) {
		RequestService.insert(requestEntity);
		RequestQueue.add(requestEntity);
		return requestEntity;
	}

	/**
	 * 发送优先请求
	 * 
	 * @param serviceEntity
	 * @return
	 */
	public static RequestEntity sendPriorityService(RequestEntity requestEntity) {
		RequestService.insert(requestEntity);
		RequestQueue.addPriority(requestEntity);
		return requestEntity;
	}

	/**
	 * 插入数据
	 * 
	 * @param entity
	 */
	public static void insert(ServiceEntity entity) {
		Document doc = new Document(JSONObject.parseObject(entity.toString()));
		MongoDAO.getInstance().insert(dbName, collectionName, doc);

		// 更新缓存
		EhCacheService.setServiceCache(entity);
	}

	/**
	 * 更新数据
	 * 
	 * @param entity
	 */
	public static void update(ServiceEntity entity) {
		Document doc = new Document(JSONObject.parseObject(entity.toString()));
		Document filter = new Document();
		filter.append("serviceName", entity.getServiceName());
		MongoDAO.getInstance().update(dbName, collectionName, filter, doc);

		// 更新缓存
		EhCacheService.setServiceCache(entity);
	}

	/**
	 * 查询
	 * 
	 * @param serviceCode
	 */
	public static ServiceEntity find(String serviceCode) {
		// 判断是否读取缓存
		if(EhCacheService.getServiceCache(serviceCode) != null) {
			return EhCacheService.getServiceCache(serviceCode);
		}

		Document filter = new Document();
		filter.append("serviceCode", serviceCode);
		List<Document> docs = MongoDAO.getInstance().findBy(dbName, collectionName, filter);
		if(docs.size() > 0) {
			return JSONObject.toJavaObject(JSONObject.parseObject(JSON.toJSONString(docs.get(0))), ServiceEntity.class);
		}
		return null;
	}

	/**
	 * 查询全部
	 */
	public static List<ServiceEntity> findAll() {
		List<Document> docs = MongoDAO.getInstance().findAll(dbName, collectionName);
		return JSONArray.parseArray(JSON.toJSONString(docs), ServiceEntity.class);
	}

	/**
	 * 根据模块查询
	 */
	public static List<ServiceEntity> findByModule(String module) {
		Document filter = new Document();
		filter.append("module", module);
		List<Document> docs = MongoDAO.getInstance().findBy(dbName, collectionName, filter);
		return JSONArray.parseArray(JSON.toJSONString(docs), ServiceEntity.class);
	}
}
