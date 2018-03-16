package com.esb.guass.job.runnable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.esb.guass.common.constant.ConfigConstant;
import com.esb.guass.common.constant.StatusConstant;
import com.esb.guass.common.util.LogUtils;
import com.esb.guass.dispatcher.entity.RequestEntity;
import com.esb.guass.dispatcher.entity.RequestOption;
import com.esb.guass.dispatcher.service.ServiceMangerService;
import com.esb.guass.job.entity.PlanningJobEntity;
import com.esb.guass.job.service.PlanningJobService;
import com.google.common.base.Strings;

/**
 * 定时任务线程
 * 
 * @author wicks
 */
public class PlanningJobRunnable implements Runnable {

	@Override
	public void run() {
		while(true) {
			try {
				long nowTime = System.currentTimeMillis();
				List<PlanningJobEntity> jobEntities = PlanningJobService.findAllEnable();
				for(PlanningJobEntity jobEntity : jobEntities) {
					if(jobEntity.getNextTime() > 0 && jobEntity.getNextTime() < nowTime) {
						// 执行计划任务
						RequestEntity requestEntity = new RequestEntity();
						requestEntity.setQuestId(UUID.randomUUID().toString());
						requestEntity.setRequestTime(nowTime);
						requestEntity.setStatus(StatusConstant.CODE_1201);
						requestEntity.setIdentification(ConfigConstant.IDENTIFICATION_SYS_JOB);
						requestEntity.setServiceCode(jobEntity.getServiceCode());
						requestEntity.setServiceName(jobEntity.getServiceName());
						requestEntity.setAuthValidate(false);
						requestEntity.setRequestOption(new RequestOption());

						// 判断是否有参数
						if(!Strings.isNullOrEmpty(jobEntity.getRemarks())) {
							try {
								JSONObject jsonObject = JSON.parseObject(jobEntity.getRemarks());
								if(jsonObject != null) {
									Map<String, String> params = new HashMap<>();
									for(String key : jsonObject.keySet()) {
										params.put(key, jsonObject.getString(key));
									}
									requestEntity.setParams(params);
								}
							} catch(Exception ex) {
								LogUtils.error(jobEntity.getJobName() + "参数无法转换", ex);
							}
						}

						// 更新计划任务内容
						LogUtils.info("执行计划任务：" + jobEntity.getJobName() + "->" + requestEntity.getQuestId());
						jobEntity.setLastTime(nowTime);
						jobEntity.setLastQuestId(requestEntity.getQuestId());
						jobEntity.setNextTime(PlanningJobService.getNextTime(jobEntity));
						PlanningJobService.update(jobEntity);
						ServiceMangerService.getRequestByService(requestEntity, null);
						ServiceMangerService.sendPriorityService(requestEntity);
					} else if(jobEntity.getNextTime() == 0) {
						// 计划任务第一次执行
						jobEntity.setNextTime(PlanningJobService.getNextTime(jobEntity));
						PlanningJobService.update(jobEntity);
					}
				}
			} catch(Throwable ex) {
				LogUtils.error("定时任务执行失败", ex);
			}

			try {
				Thread.sleep(ConfigConstant.PLANNINGJOB_INTERVAL);
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
