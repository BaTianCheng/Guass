package com.esb.guass.common.cache.ehcache;

import java.util.List;

import com.esb.guass.dispatcher.entity.RequestEntity;
import com.esb.guass.dispatcher.entity.ServiceEntity;
import com.esb.guass.dispatcher.service.ServiceMangerService;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * Ehcache缓存服务类
 * @author wicks
 */
public class EhCacheService {
	
	/**
	 * 缓存管理器
	 */
	private static CacheManager manager;
	
	/**
	 * 配置文件路径
	 */
	private static String XMLPATH = "conf/ehcache.xml";
	
	/**
	 * 结果缓存名
	 */
	private static String RESULTCACHE = "ResultCache";
	
	/**
	 * 结果缓存名
	 */
	private static String SERVICECACHE = "ServiceCache";
	
	/**
	 * 缓存初始化
	 */
	public static void init(){
		String userDir = System.getProperty("user.dir");
		manager = CacheManager.create(userDir+"/"+XMLPATH);
		
		//加载所有服务入缓存
		List<ServiceEntity> serviceEntities = ServiceMangerService.findAll();
		if(serviceEntities != null){
			for(ServiceEntity entity : serviceEntities){
				setServiceCache(entity);
			}
		}
	}
	
	/**
	 * 设置结果缓存
	 */
	public static void setResultCache(RequestEntity entity){
		Cache cache = manager.getCache(RESULTCACHE);
		cache.put(new Element(entity.getQuestId(), entity));
	}
	
	/**
	 * 获取结果缓存
	 */
	public static RequestEntity getResultCache(String questId){
		Cache cache = manager.getCache(RESULTCACHE);
		if(cache.isKeyInCache(questId)){
			return (RequestEntity)cache.get(questId).getObjectValue();
		} else {
			return null;
		}
	}
	
	/**
	 * 设置服务缓存
	 */
	public static void setServiceCache(ServiceEntity entity){
		Cache cache = manager.getCache(SERVICECACHE);
		cache.put(new Element(entity.getServiceName(), entity));
	}
	
	/**
	 * 获取服务缓存
	 */
	public static ServiceEntity getServiceCache(String serviceName){
		Cache cache = manager.getCache(SERVICECACHE);
		if(cache.isKeyInCache(serviceName)){
			return (ServiceEntity)cache.get(serviceName).getObjectValue();
		} else {
			return null;
		}
	}
	
}
