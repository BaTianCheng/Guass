package com.esb.guass.dispatcher.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bson.Document;
import org.redkale.net.http.HttpRequest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.esb.guass.common.cache.ehcache.EhCacheService;
import com.esb.guass.common.constant.StatusConstant;
import com.esb.guass.common.dao.mongo.MongoDAO;
import com.esb.guass.dispatcher.entity.RequestEntity;
import com.esb.guass.dispatcher.entity.RequestOption;
import com.esb.guass.dispatcher.entity.ServiceEntity;
import com.google.common.base.Strings;

/**
 * 服务管理服务类
 * @author wicks
 */
public class ServiceMangerService {

	private static final String dbName = "db_esb";
	
	private static final String collectionName = "tb_service";
	
	/**
	 * 发送服务
	 * @param serviceEntity
	 * @return
	 */
	public static RequestEntity sendService(String serviceCode, String identification, Map<String, String> params, Map<String, String> headers, HttpRequest req){
		ServiceEntity serviceEntity = find(serviceCode);
		if(serviceEntity != null){
			RequestEntity requestEntity = new RequestEntity();
			requestEntity.setQuestId(UUID.randomUUID().toString());
			requestEntity.setServiceCode(serviceCode);
			requestEntity.setServiceName(serviceEntity.getServiceName());
    		requestEntity.setRequestTime(System.currentTimeMillis());
			requestEntity.setRequestType(serviceEntity.getRequestType());
    		requestEntity.setUrl(serviceEntity.getMapUrl());
    		requestEntity.setIdentification(identification);
    		requestEntity.setStatus(StatusConstant.CODE_1201);
    		requestEntity.setResponseContentType(serviceEntity.getResponseContentType());
    		requestEntity.setResponseErrorMsg(serviceEntity.getResponseErrorMsg());
    		requestEntity.setDirectReturn(serviceEntity.isDirectReturn());
    		if(serviceEntity.getRequestOption() == null){
    			requestEntity.setRequestOption(new RequestOption());
    		} else {
    			requestEntity.setRequestOption(serviceEntity.getRequestOption());
    		}
    		requestEntity.setParams(params);
    		if(serviceEntity.getHeadParams()!=null && serviceEntity.getHeadParams().size() > 0 ){
    			headers = new HashMap<>();
    			for(String headParam : serviceEntity.getHeadParams()){
    				if(params.containsKey(headParam)){
    					headers.put(headParam, params.get(params));
    				}
    			}
    		} 
    		requestEntity.setHead(headers);
    		if(req != null){
	    		requestEntity.setAsync(req.getBooleanParameter("async", serviceEntity.isAsync()));
	    		if(!Strings.isNullOrEmpty(req.getBodyUTF8())){
	    			requestEntity.setPostBody(req.getBodyUTF8());
	    		}
	    		requestEntity.setRequestIP(req.getRemoteAddr());
    		}
    		RequestService.insert(requestEntity);
    		RequestQueue.add(requestEntity);
    		return requestEntity;
		}

		return null;
	}
	
	/**
	 * 插入数据
	 * @param entity
	 */
	public static void insert(ServiceEntity entity){
		Document doc = new Document(JSONObject.parseObject(entity.toString())) ;
		MongoDAO.getInstance().insert(dbName, collectionName, doc);
		
		//更新缓存
		EhCacheService.setServiceCache(entity);
	}
	
	/**
	 * 更新数据
	 * @param entity
	 */
	public static void update(ServiceEntity entity){
		Document doc = new Document(JSONObject.parseObject(entity.toString())) ;
		Document filter = new Document();  
    	filter.append("serviceName", entity.getServiceName());  
		MongoDAO.getInstance().update(dbName, collectionName, filter, doc);
		
		//更新缓存
		EhCacheService.setServiceCache(entity);
	}
	
	/**
	 * 查询
	 * @param serviceCode
	 */
	public static ServiceEntity find(String serviceCode){
		//判断是否读取缓存
		if(EhCacheService.getServiceCache(serviceCode) != null){
			return EhCacheService.getServiceCache(serviceCode);
		}

		Document filter = new Document();  
    	filter.append("serviceCode", serviceCode);  
    	List<Document> docs = MongoDAO.getInstance().findBy(dbName, collectionName, filter);
    	if(docs.size() > 0){
    		return JSONObject.toJavaObject(JSONObject.parseObject(JSON.toJSONString(docs.get(0))), ServiceEntity.class);
    	}
    	return null;
	}
	
	/**
	 * 查询全部
	 */
	public static List<ServiceEntity> findAll(){
		List<Document> docs = MongoDAO.getInstance().findAll(dbName, collectionName);
		return JSONArray.parseArray(JSON.toJSONString(docs), ServiceEntity.class);
	}
	
	/**
	 * 根据模块查询
	 */
	public static List<ServiceEntity> findByModule(String module){
		Document filter = new Document();  
    	filter.append("module", module);  
		List<Document> docs = MongoDAO.getInstance().findBy(dbName, collectionName, filter);
		return JSONArray.parseArray(JSON.toJSONString(docs), ServiceEntity.class);
	}
}
