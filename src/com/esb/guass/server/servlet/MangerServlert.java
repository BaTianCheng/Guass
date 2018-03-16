package com.esb.guass.server.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.redkale.net.http.HttpRequest;
import org.redkale.net.http.HttpResponse;
import org.redkale.net.http.WebServlet;

import com.esb.guass.common.base.BaseSerlvet;
import com.esb.guass.common.cache.ehcache.EhCacheService;
import com.esb.guass.common.constant.StatusConstant;
import com.esb.guass.common.util.LogUtils;
import com.esb.guass.dispatcher.entity.CommonCondition;
import com.esb.guass.dispatcher.entity.ServiceEntity;
import com.esb.guass.dispatcher.service.ModuleService;
import com.esb.guass.dispatcher.service.RequestQueue;
import com.esb.guass.dispatcher.service.RequestService;
import com.esb.guass.dispatcher.service.ServiceMangerService;
import com.google.common.base.Strings;

@WebServlet(value = {"/manger/*"}, comment = "ESB管理")
public class MangerServlert extends BaseSerlvet {

	/**
	 * 获取服务状态
	 * 
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
	 * 刷新缓存
	 * 
	 * @param req
	 * @param resp
	 * @throws IOException
	 */
	@AuthIgnore
	@WebMapping(url = "/manger/caches/update", comment = "更新缓存")
	public void updateCache(HttpRequest req, HttpResponse resp) throws IOException {
		try {
			EhCacheService.init();
			System.gc();
			LogUtils.info("更新缓存成功");
			this.writeSuccessResult(resp, "更新成功");
		} catch(Exception ex) {
			LogUtils.error("更新缓存失败", ex);
			this.writeErrorResult(resp, StatusConstant.CODE_500, StatusConstant.CODE_500_MSG, null);
		}
	}

	/**
	 * 获取所有模块
	 * 
	 * @param req
	 * @param resp
	 * @throws IOException
	 */
	@AuthIgnore
	@WebMapping(url = "/manger/modules/list", comment = "获取所有模块列表")
	public void getModules(HttpRequest req, HttpResponse resp) throws IOException {
		this.writeSuccessResult(resp, ModuleService.findAll());
	}

	/**
	 * 获取已注册的服务列表
	 * 
	 * @param req
	 * @param resp
	 * @throws IOException
	 */
	@AuthIgnore
	@WebMapping(url = "/manger/services/list", comment = "获取已注册的服务列表")
	public void getServices(HttpRequest req, HttpResponse resp) throws IOException {
		if(Strings.isNullOrEmpty(req.getParameter("module"))) {
			List<ServiceEntity> entitys = ServiceMangerService.findAll();
			this.writeSuccessResult(resp, entitys);
		} else {
			List<ServiceEntity> entitys = ServiceMangerService.findByModule(req.getParameter("module"));
			this.writeSuccessResult(resp, entitys);
		}
	}

	/**
	 * 获取请求列表
	 * 
	 * @param req
	 * @param resp
	 * @throws IOException
	 */
	@AuthIgnore
	@WebMapping(url = "/manger/requests/list", comment = "获取请求列表")
	public void getRequests(HttpRequest req, HttpResponse resp) throws IOException {
		CommonCondition condition = req.getJsonParameter(CommonCondition.class, "condition"); // 获取参数
		if(!Strings.isNullOrEmpty(req.getParameter("pageNum"))) {
			condition.setPageNum(req.getIntParameter("pageNum", 1));
		}
		if(!Strings.isNullOrEmpty(req.getParameter("pageSize"))) {
			condition.setPageSize(req.getIntParameter("pageSize", 0));
		}

		this.writeSuccessResult(resp, RequestService.findPages(condition));
	}

}
