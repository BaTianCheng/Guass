package com.esb.guass.job.service;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.bson.Document;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.esb.guass.common.dao.mongo.MongoDAO;
import com.esb.guass.dispatcher.entity.CommonCondition;
import com.esb.guass.job.entity.PlanningJobEntity;
import com.google.common.base.Strings;

/**
 * 计划任务服务类
 * @author wicks
 */
public class PlanningJobService {

	private static final String dbName = "db_esb";
	
	private static final String collectionName = "tb_planningjob";
	
	/**
	 * 获取下次执行时间
	 * @param entity
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static long getNextTime(PlanningJobEntity entity){
		long nextTime = 0;
		Date nowDate = new Date();
		long nowMintute = nowDate.getTime()/1000/60;
		//计算今天0点
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());  
        calendar.setTimeInMillis(nowDate.getTime());  
        calendar.set(Calendar.HOUR_OF_DAY, 0);  
        calendar.set(Calendar.MINUTE, 0);  
        calendar.set(Calendar.SECOND, 0);  
        calendar.set(Calendar.MILLISECOND, 0);  
        long today0 = calendar.getTimeInMillis()/1000/60;
		
		
		switch (entity.getJobRule()) {
		case 1:
			//每日
			if(entity.getJobRuleValues() != null &&  entity.getJobRuleValues().size() > 0){
				//今天
				long today = today0;
				for(Integer nextMintute : entity.getJobRuleValues()){
					if((today+nextMintute) > nowMintute){
						return (today+nextMintute)*60*1000;
					}
				}
				//明天
				long  tomorrow = today + 24*60;
				return (tomorrow + entity.getJobRuleValues().get(0))*60*1000;
			}
			break;
		case 2:
			//每周
			if(entity.getJobRuleValues() != null &&  entity.getJobRuleValues().size() > 0){
				//本周一
				int day = nowDate.getDay();
				long thisWeek = today0;
				if(day ==0 ){
					thisWeek = thisWeek - 6*24*60;
				} else {
					thisWeek = thisWeek - (day-1)*24*60;
				}
				for(Integer nextDay : entity.getJobRuleValues()){
					if((thisWeek+(nextDay-1)*24*60+entity.getJobRuleTime()) > nowMintute){
						return (thisWeek+(nextDay-1)*24*60+entity.getJobRuleTime())*60*1000;
					}
				}
				//下周
				long  nextWeek = thisWeek + 7*24*60;
				return (nextWeek + (entity.getJobRuleValues().get(0)-1)*24*60+entity.getJobRuleTime())*60*1000;
			}
			break;
		case 3:
			//每月
			if(entity.getJobRuleValues() != null &&  entity.getJobRuleValues().size() > 0){
				//本月一号
				int date = nowDate.getDate();
				long thisMonth = today0 - (date-1)*24*60;
				for(Integer nextDate : entity.getJobRuleValues()){
					if((thisMonth+(nextDate-1)*24*60+entity.getJobRuleTime()) > nowMintute){
						return (thisMonth+(nextDate-1)*24*60+entity.getJobRuleTime())*60*1000;
					}
				}
				//下月一号
		        calendar.set(Calendar.DAY_OF_MONTH, 1);
		        calendar.set(Calendar.HOUR_OF_DAY, 0);
		        calendar.set(Calendar.MINUTE, 0);
		        calendar.set(Calendar.SECOND, 0);
		        calendar.add(Calendar.MONTH, 1);
		        long nextMonth = calendar.getTime().getTime()/60/1000;
				return (nextMonth + (entity.getJobRuleValues().get(0)-1)*24*60+entity.getJobRuleTime())*60*1000;
			}
			break;
		default:
			break;
		}
		
		return nextTime;
	}
	
	/**
	 * 插入数据
	 * @param entity
	 */
	public static void insert(PlanningJobEntity entity){
		Document doc = new Document(JSONObject.parseObject(entity.toString())) ;
		MongoDAO.getInstance().insert(dbName, collectionName, doc);
	}
	
	/**
	 * 更新数据
	 * @param entity
	 */
	public static void update(PlanningJobEntity entity){
		PlanningJobEntity originalEntity = find(entity.getJobCode());
		if(entity.getLastTime()==0){
			entity.setLastTime(originalEntity.getLastTime());
		}
		entity.setLastQuestId(originalEntity.getLastQuestId());
		if(entity.getStatus()==1){
			entity.setNextTime(getNextTime(entity));
		} else {
			entity.setNextTime(0);
		}
		
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
	 * @param jobCode
	 */
	public static PlanningJobEntity find(String jobCode){
		Document filter = new Document();  
    	filter.append("jobCode", jobCode);  
    	List<Document> docs = MongoDAO.getInstance().findBy(dbName, collectionName, filter);
    	if(docs.size() > 0){
    		return JSONObject.toJavaObject(JSONObject.parseObject(JSON.toJSONString(docs.get(0))), PlanningJobEntity.class);
    	}
    	return null;
	}
	
	/**
	 * 查询全部
	 */
	public static List<PlanningJobEntity> findAll(){
		List<Document> docs = MongoDAO.getInstance().findAll(dbName, collectionName);
		return JSONArray.parseArray(JSON.toJSONString(docs), PlanningJobEntity.class);
	}
	
	/**
	 * 查询全部可用
	 */
	public static List<PlanningJobEntity> findAllEnable(){
		Document filter = new Document();  
    	filter.append("status", 1);
    	List<Document> docs = MongoDAO.getInstance().findBy(dbName, collectionName, filter);
		return JSONArray.parseArray(JSON.toJSONString(docs), PlanningJobEntity.class);
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
		pages.put("list", JSONArray.parseArray(JSON.toJSONString(docs), PlanningJobEntity.class));
		pages.put("total",count);
		pages.put("pageNum", condition.getPageNum());
		if(condition.getPageSize() > 0){
			if(count % condition.getPageSize() == 0){
				pages.put("pages", (count / condition.getPageSize()));
			} else {
				pages.put("pages", (count / condition.getPageSize() + 1));
			}
		}
		
		return pages;
	}
}
