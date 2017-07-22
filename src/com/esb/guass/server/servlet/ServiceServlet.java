package com.esb.guass.server.servlet;

import java.io.IOException;

import org.redkale.net.http.HttpRequest;
import org.redkale.net.http.HttpResponse;
import org.redkale.net.http.WebServlet;

import com.esb.guass.auth.AuthAPI;
import com.esb.guass.common.constant.ParamConstants;
import com.esb.guass.common.constant.StatusConstant;
import com.esb.guass.common.util.LogUtils;
import com.esb.guass.dispatcher.entity.RequestEntity;
import com.esb.guass.dispatcher.exception.ParseException;
import com.esb.guass.dispatcher.exception.UnfindServiceException;
import com.esb.guass.dispatcher.service.RequestService;
import com.esb.guass.dispatcher.service.ServiceMangerService;
import com.esb.guass.server.base.BaseSerlvet;
import com.google.common.base.Strings;

@WebServlet(value = {"/services/*"}, comment = "ESB服务管理")
public class ServiceServlet extends BaseSerlvet {

	/**
     * ESB服务请求
     * @param req
     * @param resp
     * @throws IOException
     */
    @AuthIgnore
    @WebMapping(url = "/services/get", comment = "获取ESB服务项")
    public void getService(HttpRequest req, HttpResponse resp) throws IOException {
    	if(Strings.isNullOrEmpty(req.getParameter(ParamConstants.PARAM_SERVICECODE))){
    		this.writeErrorResult(resp, StatusConstant.CODE_400, StatusConstant.CODE_400_MSG, null);
    	} else {
    		try{
	    		RequestEntity requestEntity = RequestService.getRequestEntuty(req);
	    		requestEntity = ServiceMangerService.getRequestByService(requestEntity, req);
	    		String authCode = AuthAPI.authCheck(requestEntity);
	    		if(authCode.equals(StatusConstant.CODE_300)){
	    			this.writeErrorResult(resp, StatusConstant.CODE_300, StatusConstant.CODE_300_MSG, requestEntity);
	    		} else if(authCode.equals(StatusConstant.CODE_301)){
	    			this.writeErrorResult(resp, StatusConstant.CODE_301, StatusConstant.CODE_301_MSG, requestEntity);
	    		} else {
		    		requestEntity = ServiceMangerService.sendService(requestEntity);
					this.writeRequestResult(requestEntity, resp);
	    		}
    		}
    		catch(Exception ex){
    			//出现异常，先判断是否可以获取自定义异常
    			RequestEntity requestEntity = ServiceMangerService.getErrRequest(req);
    			if(ex instanceof ParseException){
    				this.writeErrorResult(resp, StatusConstant.CODE_400, ex.getMessage(), requestEntity);
    			} else if(ex instanceof UnfindServiceException){
    				this.writeErrorResult(resp, StatusConstant.CODE_310, StatusConstant.CODE_310_MSG, requestEntity);
    			} else {
    				this.writeErrorResult(resp, StatusConstant.CODE_500, StatusConstant.CODE_500_MSG, requestEntity);
    				LogUtils.error("请求时发生异常", ex);
    			}
    		}
    	}
    }
    
    /**
     * ESB服务请求(自动路径，将serviceCode作为路径的的一部分)
     * @param req
     * @param resp
     * @throws IOException
     */
    @AuthIgnore
    @WebMapping(url = "/services/auto/", comment = "获取ESB服务项")
    public void getServiceByPath(HttpRequest req, HttpResponse resp) throws IOException {
    	String[] pathSplits = req.getRequestURI().split("/services/auto");
    	if(pathSplits!=null && pathSplits.length>=2){
    		String serviceCode = pathSplits[1];
    		try{
	    		RequestEntity requestEntity = RequestService.getRequestEntuty(req);
	    		requestEntity.setServiceCode(serviceCode);
	    		requestEntity = ServiceMangerService.getRequestByService(requestEntity, req);
	    		String authCode = AuthAPI.authCheck(requestEntity);
	    		if(authCode.equals(StatusConstant.CODE_300)){
	    			this.writeErrorResult(resp, StatusConstant.CODE_300, StatusConstant.CODE_300_MSG, requestEntity);
	    		} else if(authCode.equals(StatusConstant.CODE_301)){
	    			this.writeErrorResult(resp, StatusConstant.CODE_301, StatusConstant.CODE_301_MSG, requestEntity);
	    		} else {
		    		requestEntity = ServiceMangerService.sendService(requestEntity);
					this.writeRequestResult(requestEntity, resp);
	    		}
    	}
		catch(Exception ex){
			//出现异常，先判断是否可以获取自定义异常
			RequestEntity requestEntity = ServiceMangerService.getErrRequest(req);
			if(ex instanceof ParseException){
				this.writeErrorResult(resp, StatusConstant.CODE_400, ex.getMessage(), requestEntity);
			} else if(ex instanceof UnfindServiceException){
				this.writeErrorResult(resp, StatusConstant.CODE_310, StatusConstant.CODE_310_MSG, requestEntity);
			} else {
				this.writeErrorResult(resp, StatusConstant.CODE_500, StatusConstant.CODE_500_MSG, requestEntity);
			}
			LogUtils.error("请求时发送异常", ex);
		}
    	} else {
    		this.writeErrorResult(resp, StatusConstant.CODE_310, StatusConstant.CODE_310_MSG, null);
    	}
    }
    
}
