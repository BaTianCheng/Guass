package com.esb.guass.server.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.redkale.net.http.HttpRequest;
import org.redkale.net.http.HttpResponse;
import org.redkale.net.http.WebServlet;

import com.alibaba.fastjson.JSON;
import com.esb.guass.common.constant.ConfigConstant;
import com.esb.guass.common.constant.StatusConstant;
import com.esb.guass.common.util.LogUtils;
import com.esb.guass.dispatcher.entity.RequestEntity;
import com.esb.guass.dispatcher.entity.RequestOption;
import com.esb.guass.dispatcher.service.RequestQueue;
import com.esb.guass.dispatcher.service.RequestService;
import com.esb.guass.server.base.BaseSerlvet;
import com.google.common.base.Strings;

/**
 * Htpp代理服务
 * @author wicks
 */
@WebServlet(value = {"/proxys/*"}, comment = "Htpp代理服务")
public class HttpProxyServlet extends BaseSerlvet {

    /**
     * HTTP代理发送请求服务
     * @param req
     * @param resp
     * @throws IOException
     */
    @AuthIgnore
    @WebMapping(url = "/proxys/request", comment = "HTTP代理发送请求服务")
    @WebParam(name = "url", type = String.class, comment = "请求转发的URL")
    public void request(HttpRequest req, HttpResponse resp) throws IOException {
    	if(Strings.isNullOrEmpty(req.getParameter("url"))){
    		this.writeErrorResult(resp, StatusConstant.CODE_400, StatusConstant.CODE_400_MSG, null);
    	} else {
    		//参数解析
    		RequestEntity requestEntity = new RequestEntity();
    		try{
	    		requestEntity.setQuestId(UUID.randomUUID().toString());
	    		requestEntity.setModule(ConfigConstant.MODULE_PROXY);
	    		requestEntity.setUrl(req.getParameter("url"));
	    		requestEntity.setIdentification(req.getParameter("identification"));
	    		requestEntity.setRequestOption(parseRequestOption(req));
	    		
	        	if(!Strings.isNullOrEmpty(req.getParameter("data"))){
	        		try{
		    			@SuppressWarnings("unchecked")
		    			Map<String, String> params = JSON.parseObject(req.getParameter("data"), HashMap.class);
		        		requestEntity.setParams(params);
	        		}
	    			catch(Exception ex){
	    				this.writeErrorResult(resp, StatusConstant.CODE_400, StatusConstant.CODE_400_MSG, null);
	    			}
	    		}
	        	if(!Strings.isNullOrEmpty(req.getParameter("getResultData"))){
	        		requestEntity.setAsync(Boolean.valueOf(req.getParameter("getResultData")));
	        	}
	    		if(!Strings.isNullOrEmpty(req.getParameter("head"))){
	    			@SuppressWarnings("unchecked")
	    			Map<String, String> head = JSON.parseObject(req.getParameter("head"), HashMap.class);
	    			requestEntity.setHead(head);
	    		} else {
	    			Map<String, String> head = new HashMap<>();
	    			if(req.getHeaderNames() != null){
	    				for(String headName : req.getHeaderNames()){
	    					head.put(headName, req.getHeader(headName));
	    				}
	    			}
	    			requestEntity.setHead(head);
	    		}
	    		if(Strings.isNullOrEmpty(req.getBodyUTF8())){
	    			requestEntity.setPostBody(req.getBodyUTF8());
	    		}
	    		
	    		requestEntity.setAsync(req.getBooleanParameter("async", true));
	    		requestEntity.setRequestIP(req.getRemoteAddr());
	    		requestEntity.setRequestTime(req.getCreatetime());
	    		requestEntity.setServiceName("HTTP代理");
	    		requestEntity.setStatus(StatusConstant.CODE_1201);
    		}
    		catch(Exception ex){
    			LogUtils.error("请求时发生异常", ex);
    			this.writeErrorResult(resp, StatusConstant.CODE_400, StatusConstant.CODE_400_MSG, ex.toString());
    		}
    		RequestService.insert(requestEntity);
    		RequestQueue.add(requestEntity);
    		
    		this.writeRequestResult(requestEntity, resp);
    	}
    }
    
    /**
     * 解析请求选项
     * @param req
     * @return
     */
    public RequestOption parseRequestOption(HttpRequest req)
    {
    	RequestOption option = new RequestOption();
		
		if(!Strings.isNullOrEmpty(req.getParameter("businessId"))){
			option.setBusinessId(req.getParameter("businessId"));
		} 
		
		if(!Strings.isNullOrEmpty(req.getParameter("charset"))){
			option.setCharset(req.getParameter("charset"));
		}
		
		if(!Strings.isNullOrEmpty(req.getParameter("method"))){
			option.setMethod(req.getParameter("method"));
		} else {
			option.setMethod(req.getMethod());
		}
		
		if(!Strings.isNullOrEmpty(req.getParameter("isbody"))){
			try{
			option.setBody(Boolean.valueOf(req.getParameter("isbody")));
			}
			catch(Exception ex){}
		}

		if(!Strings.isNullOrEmpty(req.getParameter("id"))){
			option.setBusinessId(req.getParameter("id"));
		}
		
		return option;
    }
    
}
