package com.esb.guass.common.cache.ehcache;

import java.util.List;

import com.esb.guass.auth.entity.VisitorEntity;
import com.esb.guass.auth.service.VisitorService;
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
	 * 访问者缓存名
	 */
	private static String VISITORCACHE = "VisitorCache";
	
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
		
		//加载所有访问者入缓存
		List<VisitorEntity> visitorEntities = VisitorService.findAll();
		if(serviceEntities != null){
			for(VisitorEntity entity : visitorEntities){
				setVisitorCache(entity);
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
	
	/**
	 * 设置访问者缓存
	 */
	public static void setVisitorCache(VisitorEntity entity){
		Cache cache = manager.getCache(VISITORCACHE);
		cache.put(new Element(entity.getAppId(), entity));
	}
	
	/**
	 * 获取访问者缓存
	 */
	public static VisitorEntity getVisitorCache(String appId){
		Cache cache = manager.getCache(VISITORCACHE);
		if(cache.isKeyInCache(appId)){
			return (VisitorEntity)cache.get(appId).getObjectValue();
		} else {
			return null;
		}
	}
	
}
