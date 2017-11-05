package com.esb.guass.server.servlet;

import java.io.IOException;

import org.redkale.convert.ConvertException;
import org.redkale.net.http.HttpRequest;
import org.redkale.net.http.HttpResponse;
import org.redkale.net.http.WebServlet;

import com.esb.guass.common.constant.ConfigConstant;
import com.esb.guass.common.constant.StatusConstant;
import com.esb.guass.common.util.LogUtils;
import com.esb.guass.dispatcher.entity.CommonCondition;
import com.esb.guass.dispatcher.service.RequestService;
import com.esb.guass.job.entity.PlanningJobEntity;
import com.esb.guass.job.entity.RegularJobEntity;
import com.esb.guass.job.service.PlanningJobService;
import com.esb.guass.job.service.RegularJobService;
import com.esb.guass.server.base.BaseSerlvet;
import com.google.common.base.Strings;

/**
 * 计划任务管理
 * @author wicks
 */
@WebServlet(value = {"/jobs/*"}, comment = "计划任务管理")
public class JobServlet extends BaseSerlvet {

    /**
     * 获取实时任务列表
     * @param req
     * @param resp
     * @throws IOException
     */
    @AuthIgnore
    @WebMapping(url = "/jobs/regularJobs/list")
    public void getRegularJobs(HttpRequest req, HttpResponse resp) throws IOException {
    	try{
	    	CommonCondition condition = req.getJsonParameter(CommonCondition.class, "condition");
	    	if(!Strings.isNullOrEmpty(req.getParameter("pageNum"))){
	    		condition.setPageNum(req.getIntParameter("pageNum", 1));
	    	}
	    	if(!Strings.isNullOrEmpty(req.getParameter("pageSize"))){
	    		condition.setPageSize(req.getIntParameter("pageSize", 0));
	    	}
	    	this.writeSuccessResult(resp, RegularJobService.findPages(condition));
    	}
    	catch(ConvertException ex){
    		this.writeErrorResult(resp, StatusConstant.CODE_400, StatusConstant.CODE_400_MSG, null);
    	}
    	catch(Exception ex){
    		LogUtils.error("请求时发生异常", ex);
    		this.writeErrorResult(resp, StatusConstant.CODE_500, StatusConstant.CODE_500_MSG, null);
    	}
    }
    
    /**
     * 获取计划任务列表
     * @param req
     * @param resp
     * @throws IOException
     */
    @AuthIgnore
    @WebMapping(url = "/jobs/planningJobs/list")
    public void getPlanningJobs(HttpRequest req, HttpResponse resp) throws IOException {
    	try{
	    	CommonCondition condition = req.getJsonParameter(CommonCondition.class, "condition");
	    	if(!Strings.isNullOrEmpty(req.getParameter("pageNum"))){
	    		condition.setPageNum(req.getIntParameter("pageNum", 1));
	    	}
	    	if(!Strings.isNullOrEmpty(req.getParameter("pageSize"))){
	    		condition.setPageSize(req.getIntParameter("pageSize", 0));
	    	}
	    	this.writeSuccessResult(resp, PlanningJobService.findPages(condition));
    	}
    	catch(ConvertException ex){
    		this.writeErrorResult(resp, StatusConstant.CODE_400, StatusConstant.CODE_400_MSG, null);
    	}
    	catch(Exception ex){
    		LogUtils.error("请求时发生异常", ex);
    		this.writeErrorResult(resp, StatusConstant.CODE_500, StatusConstant.CODE_500_MSG, null);
    	}
    }
    
    /**
     * 获取实时任务
     * @param req
     * @param resp
     * @throws IOException
     */
    @AuthIgnore
    @WebMapping(url = "/jobs/regularJobs/get")
    public void getRegularJob(HttpRequest req, HttpResponse resp) {
    	if(Strings.isNullOrEmpty(req.getParameter("jobCode"))){
    		this.writeErrorResult(resp, StatusConstant.CODE_400, StatusConstant.CODE_400_MSG, null);
    	} else {
    		this.writeSuccessResult(resp, RegularJobService.find(req.getParameter("jobCode")));
    	}
    }
    
    /**
     * 获取计划任务
     * @param req
     * @param resp
     * @throws IOException
     */
    @AuthIgnore
    @WebMapping(url = "/jobs/planningJobs/get")
    public void getPlanningJob(HttpRequest req, HttpResponse resp) {
    	if(Strings.isNullOrEmpty(req.getParameter("jobCode"))){
    		this.writeErrorResult(resp, StatusConstant.CODE_400, StatusConstant.CODE_400_MSG, null);
    	} else {
    		this.writeSuccessResult(resp, PlanningJobService.find(req.getParameter("jobCode")));
    	}
    }
    
    /**
     * 获取计划任务请求列表
     * @param req
     * @param resp
     * @throws IOException
     */
    @AuthIgnore
    @WebMapping(url = "/jobs/planningJob/requests/list", comment = "获取已注册的服务列表")
    public void getServices(HttpRequest req, HttpResponse resp) throws IOException {
    	if(Strings.isNullOrEmpty(req.getParameter("jobCode"))){
    		this.writeErrorResult(resp, StatusConstant.CODE_400, StatusConstant.CODE_400_MSG, null);
    	} else {
        	try{
	    		CommonCondition condition = req.getJsonParameter(CommonCondition.class, "condition");
	        	if(!Strings.isNullOrEmpty(req.getParameter("pageNum"))){
	        		condition.setPageNum(req.getIntParameter("pageNum", 1));
	        	}
	        	if(!Strings.isNullOrEmpty(req.getParameter("pageSize"))){
	        		condition.setPageSize(req.getIntParameter("pageSize", 0));
	        	}
	    		PlanningJobEntity entity = PlanningJobService.find(req.getParameter("jobCode"));
	    		condition.setIdentification(ConfigConstant.IDENTIFICATION_SYS_JOB);
	    		condition.setServiceCode(entity.getServiceCode());
	    		this.writeSuccessResult(resp, RequestService.findPages(condition));
        	}
        	catch(ConvertException ex){
        		this.writeErrorResult(resp, StatusConstant.CODE_400, StatusConstant.CODE_400_MSG, null);
        	}
        	catch(Exception ex){
        		LogUtils.error("请求时发生异常", ex);
        		this.writeErrorResult(resp, StatusConstant.CODE_500, StatusConstant.CODE_500_MSG, null);
        	}
    	}
    }
    
    /**
     * 添加实时任务
     * @param req
     * @param resp
     * @throws IOException
     */
    @AuthIgnore
    @WebMapping(url = "/jobs/regularJobs/insert")
    public void insertRegularJobs(HttpRequest req, HttpResponse resp) throws IOException {
    	try{
	    	RegularJobEntity entity = req.getJsonParameter(RegularJobEntity.class, "entity");
	    	if(Strings.isNullOrEmpty(entity.getJobCode())){
	    		this.writeErrorResult(resp, StatusConstant.CODE_400, "jobCode不能为空", null);
	    	} else {
	    		if(RegularJobService.find(entity.getJobCode())==null){
	    			RegularJobService.insert(entity);
	    			this.writeSuccessResult(resp, entity);
	    		} else {
	    			this.writeErrorResult(resp, StatusConstant.CODE_400, "jobCode重复", null);
	    		}
	    	}
    	}
    	catch(ConvertException ex){
    		this.writeErrorResult(resp, StatusConstant.CODE_400, StatusConstant.CODE_400_MSG, null);
    	}
    	catch(Exception ex){
    		LogUtils.error("请求时发生异常", ex);
    		this.writeErrorResult(resp, StatusConstant.CODE_500, StatusConstant.CODE_500_MSG, null);
    	}
    }
    
    /**
     * 修改实时任务
     * @param req
     * @param resp
     * @throws IOException
     */
    @AuthIgnore
    @WebMapping(url = "/jobs/regularJobs/update")
    public void updateRegularJobs(HttpRequest req, HttpResponse resp) throws IOException {
    	try{
	    	RegularJobEntity entity = req.getJsonParameter(RegularJobEntity.class, "entity");
	    	if(Strings.isNullOrEmpty(entity.getJobCode())){
	    		this.writeErrorResult(resp, StatusConstant.CODE_400, "jobCode不能为空", null);
	    	} else {
	    		RegularJobService.update(entity);
    			this.writeSuccessResult(resp, entity);
	    	}
    	}
    	catch(ConvertException ex){
    		this.writeErrorResult(resp, StatusConstant.CODE_400, StatusConstant.CODE_400_MSG, null);
    	}
    	catch(Exception ex){
    		LogUtils.error("请求时发生异常", ex);
    		this.writeErrorResult(resp, StatusConstant.CODE_500, StatusConstant.CODE_500_MSG, null);
    	}
    }
    
    /**
     * 删除实时任务
     * @param req
     * @param resp
     * @throws IOException
     */
    @AuthIgnore
    @WebMapping(url = "/jobs/regularJobs/delete")
    public void deleteRegularJobs(HttpRequest req, HttpResponse resp) throws IOException {
    	try{
	    	if(Strings.isNullOrEmpty(req.getParameter("jobCode"))){
	    		this.writeErrorResult(resp, StatusConstant.CODE_400, "jobCode不能为空", null);
	    	} else {
	    		RegularJobService.delete(req.getParameter("jobCode"));
    			this.writeSuccessResult(resp, req.getParameter("jobCode"));
	    	}
    	}
    	catch(ConvertException ex){
    		this.writeErrorResult(resp, StatusConstant.CODE_400, StatusConstant.CODE_400_MSG, null);
    	}
    	catch(Exception ex){
    		LogUtils.error("请求时发生异常", ex);
    		this.writeErrorResult(resp, StatusConstant.CODE_500, StatusConstant.CODE_500_MSG, null);
    	}
    }
    
    /**
     * 添加计划任务
     * @param req
     * @param resp
     * @throws IOException
     */
    @AuthIgnore
    @WebMapping(url = "/jobs/planningJobs/insert")
    public void insertPlanningJobs(HttpRequest req, HttpResponse resp) throws IOException {
    	try{
	    	PlanningJobEntity entity = req.getJsonParameter(PlanningJobEntity.class, "entity");
	    	if(Strings.isNullOrEmpty(entity.getJobCode())){
	    		this.writeErrorResult(resp, StatusConstant.CODE_400, "jobCode不能为空", null);
	    	} else {
	    		if(PlanningJobService.find(entity.getJobCode())==null){
	    			PlanningJobService.insert(entity);
	    			this.writeSuccessResult(resp, entity);
	    		} else {
	    			this.writeErrorResult(resp, StatusConstant.CODE_400, "jobCode重复", null);
	    		}
	    	}
    	}
    	catch(ConvertException ex){
    		this.writeErrorResult(resp, StatusConstant.CODE_400, StatusConstant.CODE_400_MSG, null);
    	}
    	catch(Exception ex){
    		LogUtils.error("请求时发生异常", ex);
    		this.writeErrorResult(resp, StatusConstant.CODE_500, StatusConstant.CODE_500_MSG, null);
    	}
    }
    
    /**
     * 修改计划任务
     * @param req
     * @param resp
     * @throws IOException
     */
    @AuthIgnore
    @WebMapping(url = "/jobs/planningJobs/update")
    public void updatePlanningJobs(HttpRequest req, HttpResponse resp) throws IOException {
    	try{
    		PlanningJobEntity entity = req.getJsonParameter(PlanningJobEntity.class, "entity");
	    	if(Strings.isNullOrEmpty(entity.getJobCode())){
	    		this.writeErrorResult(resp, StatusConstant.CODE_400, "jobCode不能为空", null);
	    	} else {
	    		PlanningJobService.update(entity);
    			this.writeSuccessResult(resp, entity);
	    	}
    	}
    	catch(ConvertException ex){
    		this.writeErrorResult(resp, StatusConstant.CODE_400, StatusConstant.CODE_400_MSG, null);
    	}
    	catch(Exception ex){
    		LogUtils.error("请求时发生异常", ex);
    		this.writeErrorResult(resp, StatusConstant.CODE_500, StatusConstant.CODE_500_MSG, null);
    	}
    }
    
    /**
     * 删除计划任务
     * @param req
     * @param resp
     * @throws IOException
     */
    @AuthIgnore
    @WebMapping(url = "/jobs/planningJobs/delete")
    public void deletePlanningJobs(HttpRequest req, HttpResponse resp) throws IOException {
    	try{
	    	if(Strings.isNullOrEmpty(req.getParameter("jobCode"))){
	    		this.writeErrorResult(resp, StatusConstant.CODE_400, "jobCode不能为空", null);
	    	} else {
	    		PlanningJobService.delete(req.getParameter("jobCode"));
    			this.writeSuccessResult(resp, req.getParameter("jobCode"));
	    	}
    	}
    	catch(ConvertException ex){
    		this.writeErrorResult(resp, StatusConstant.CODE_400, StatusConstant.CODE_400_MSG, null);
    	}
    	catch(Exception ex){
    		LogUtils.error("请求时发生异常", ex);
    		this.writeErrorResult(resp, StatusConstant.CODE_500, StatusConstant.CODE_500_MSG, null);
    	}
    }
	
}
