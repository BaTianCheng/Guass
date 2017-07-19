package com.esb.guass.server.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.redkale.net.http.HttpRequest;
import org.redkale.net.http.HttpResponse;
import org.redkale.net.http.WebServlet;

import com.esb.guass.common.constant.StatusConstant;
import com.esb.guass.dispatcher.entity.RequestCondition;
import com.esb.guass.dispatcher.entity.ServiceEntity;
import com.esb.guass.dispatcher.service.RequestQueue;
import com.esb.guass.dispatcher.service.RequestService;
import com.esb.guass.dispatcher.service.ServiceMangerService;
import com.esb.guass.server.base.BaseSerlvet;
import com.google.common.base.Strings;

@WebServlet(value = {"/manger/*"}, comment = "ESB管理")
public class MangerServlert extends BaseSerlvet {
	
    /**
     * 获取服务状态
     * @param req
     * @param resp
     * @throws IOException
     */
    @AuthIgnore
    @WebMapping(url = "/manger/status/get", comment = "管理服务状态获取")
    public void getStatus(HttpRequest req, HttpResponse resp) throws IOException {
    	Map<String, Integer> map = new HashMap<>();
    	map.put("queueSize", RequestQueue.getSize());
    	this.writeSuccessResult(resp, map);
    }
    
    /**
     * 获取已注册的服务列表
     * @param req
     * @param resp
     * @throws IOException
     */
    @AuthIgnore
    @WebMapping(url = "/manger/services/list", comment = "获取已注册的服务列表")
    public void getServices(HttpRequest req, HttpResponse resp) throws IOException {
    	if(Strings.isNullOrEmpty(req.getParameter("module"))){
    		this.writeErrorResult(resp, StatusConstant.CODE_400, StatusConstant.CODE_400_MSG, null);
    	} else {
    		List<ServiceEntity> entitys = ServiceMangerService.findByModule(req.getParameter("module"));
    		this.writeSuccessResult(resp, entitys);	
    	}
    }
    
    /**
     * 获取请求列表
     * @param req
     * @param resp
     * @throws IOException
     */
    @AuthIgnore
    @WebMapping(url = "/manger/requests/list", comment = "获取请求列表")
    public void getRequests(HttpRequest req, HttpResponse resp) throws IOException {
    	RequestCondition condition = req.getJsonParameter(RequestCondition.class, "condition"); //获取参数
    	if(!Strings.isNullOrEmpty(req.getParameter("pageNum"))){
    		condition.setPageNum(req.getIntParameter("pageNum", 1));
    	}
    	if(!Strings.isNullOrEmpty(req.getParameter("pageSize"))){
    		condition.setPageSize(req.getIntParameter("pageSize", 0));
    	}
    	
    	this.writeSuccessResult(resp, RequestService.findPages(condition));	
    }

}
