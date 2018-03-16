package com.esb.guass.common.base;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Resource;

import org.apache.commons.httpclient.Header;
import org.redkale.convert.json.JsonConvert;
import org.redkale.convert.json.JsonFactory;
import org.redkale.net.http.HttpRequest;
import org.redkale.net.http.HttpResponse;
import org.redkale.net.http.HttpServlet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.esb.guass.common.constant.ConfigConstant;
import com.esb.guass.common.constant.StatusConstant;
import com.esb.guass.common.util.LogUtils;
import com.esb.guass.dispatcher.entity.RequestEntity;
import com.esb.guass.dispatcher.service.RequestService;
import com.esb.guass.server.entity.ResponseResult;
import com.google.common.base.Strings;

/**
 * Serlvet基类
 * 
 * @author wicks
 */
public class BaseSerlvet extends org.redkale.net.http.HttpBaseServlet {

	protected final Logger logger = Logger.getLogger(this.getClass().getSimpleName());

	protected final boolean info = logger.isLoggable(Level.INFO);

	@Resource(name = "APP_TIME")
	protected long serverCreateTime;

	@Resource
	protected JsonConvert jsonConvert;

	@Resource
	protected JsonFactory jsonFactory;

	// 当前进程的根目录
	@Resource(name = "APP_HOME")
	protected File home;

	// 当前Http Server的web页面的根目录
	@Resource(name = "SERVER_ROOT")
	protected File webroot;

	/**
	 * 前置拦截器
	 */
	@Override
	public void preExecute(final HttpRequest request, final HttpResponse response, HttpServlet next) throws IOException {
		// 记录所有请求
		if(info)
			response.setRecycleListener((req, resp) -> {
				if(!req.getRequestURI().contains("/results/tracks/get") && !req.getRequestURI().contains("/results/list")) {
					long e = System.currentTimeMillis() - request.getCreatetime();
					if(req.getRequestURI().contains("/services/get")) {
						logger.info("[" + request.getCreatetime() + "]请求耗时" + e + " 毫秒. 请求为: " + req + "\r\n响应为: " + resp.getOutput());
					} else {
						logger.info("[" + request.getCreatetime() + "]请求耗时" + e + " 毫秒. 请求为: " + req);
					}
				}
			});
		next.execute(request, response);
	}

	/**
	 * 鉴权方法，目前不鉴权
	 */
	@Override
	public void authenticate(int moduleid, int actionid, HttpRequest request, HttpResponse response, HttpServlet next) throws IOException {
		next.execute(request, response);
	}

	/**
	 * 成功输出
	 * 
	 * @param resp
	 * @param data
	 */
	public void writeSuccessResult(HttpResponse resp, Object data) {
		ResponseResult result = new ResponseResult();
		result.setStatusCode(StatusConstant.CODE_200);
		result.setMessage(StatusConstant.CODE_200_MSG);
		result.setData(data);
		resp.addHeader("Access-Control-Allow-Origin", "*");
		resp.finishJson(result);
	}

	/**
	 * 成功输出
	 * 
	 * @param resp
	 * @param data
	 * @param msg
	 * @param questId
	 */
	public void writeSuccessResult(HttpResponse resp, Object data, String msg, String questId) {
		ResponseResult result = new ResponseResult();
		result.setStatusCode(StatusConstant.CODE_200);
		result.setMessage(msg);
		result.setQuestId(questId);
		// 暂不输出响应头
		if(data instanceof RequestEntity) {
			((RequestEntity) data).setResponseHeaders(null);
		}
		result.setData(data);
		resp.addHeader("Access-Control-Allow-Origin", "*");
		resp.finishJson(result);
	}

	/**
	 * 失败输出
	 * 
	 * @param resp
	 * @param code
	 * @param msg
	 * @param data
	 */
	public void writeErrorResult(HttpResponse resp, String code, String msg, Object data) {
		ResponseResult result = new ResponseResult();
		result.setStatusCode(code);
		result.setMessage(msg);
		result.setData(data);
		resp.addHeader("Access-Control-Allow-Origin", "*");
		resp.finishJson(result);
	}

	/**
	 * 失败输出
	 * 
	 * @param resp
	 * @param code
	 * @param msg
	 */
	public void writeErrorResult(HttpResponse resp, String code, String msg) {
		ResponseResult result = new ResponseResult();
		result.setStatusCode(code);
		result.setMessage(msg);
		resp.addHeader("Access-Control-Allow-Origin", "*");
		resp.finishJson(result);
	}

	/**
	 * 失败输出(判断是否有指定输出格式)
	 * 
	 * @param resp
	 * @param code
	 * @param msg
	 * @param data
	 */
	public void writeErrorResult(HttpResponse resp, String code, String msg, RequestEntity entity) {
		if(entity == null) {
			writeErrorResult(resp, code, msg);
		} else if(Strings.isNullOrEmpty(entity.getResponseErrorMsg())) {
			try {
				JSONObject jsonObj = JSON.parseObject(entity.getResult());
				writeErrorResult(resp, code, msg, jsonObj);
			}
			catch(Exception ex){
				writeErrorResult(resp, code, msg, entity.getResult());
			}
		} else {
			try {
				JSONObject jsonObj = JSON.parseObject(entity.getResponseErrorMsg());
				Map<String, String> map = new HashMap<>();
				if(jsonObj.containsKey("code")) {
					map.put(jsonObj.getString("statusCode"), jsonObj.getString("code"));
				} else {
					map.put(jsonObj.getString("statusCode"), code);
				}
				map.put(jsonObj.getString("message"), msg);
				resp.addHeader("Access-Control-Allow-Origin", "*");
				resp.finishJson(map);
			} catch(Exception ex) {
				LogUtils.error("自定义错误码解析错误", ex);
				writeErrorResult(resp, code, msg, entity.getResult());
			}
		}
	}

	/**
	 * 直接返回结果
	 * 
	 * @param resp
	 * @param data
	 */
	public void writeText(HttpResponse resp, String data, Header[] headers) {
		if(headers != null) {
			for(Header header : headers) {
				if(header.getName().toLowerCase().equals("content-type")) {
					resp.setContentType(header.getValue());
				} else if(header.getName().toLowerCase().equals("content-length") || header.getName().toLowerCase().equals("date")) {
					continue;
				} else {
					continue;
					// resp.addHeader(header.getName(), header.getValue());
				}
			}
		} else {
			resp.setContentType("application/json; charset=utf-8");
		}
		resp.finish(data);
	}

	/**
	 * 同步获取结果
	 * 
	 * @param requestEntity
	 * @return
	 */
	public RequestEntity getSyncResult(String questId) {
		RequestEntity entity = null;
		long beiginTime = System.currentTimeMillis();
		while((System.currentTimeMillis() - beiginTime) < ConfigConstant.RETURNRESULTDATA_MAXTIME) {
			try {
				Thread.sleep(ConfigConstant.RETURNRESULTDATA_INTERVAL);
			} catch(InterruptedException e) {
				break;
			}
			entity = RequestService.find(questId);
			if((!entity.getStatus().equals(StatusConstant.CODE_1201)) && (!entity.getStatus().equals(StatusConstant.CODE_1202))) {
				return entity;
			}
		}
		return entity;
	}

	/**
	 * 结果输出
	 * 
	 * @param requestEntity
	 * @param resp
	 */
	public void writeRequestResult(RequestEntity requestEntity, HttpResponse resp) {

		// 判断是否为空
		if(requestEntity == null) {
			this.writeErrorResult(resp, StatusConstant.CODE_310, StatusConstant.CODE_310_MSG, requestEntity);
			return;
		}

		// 判断是否异步
		if(requestEntity.isAsync()) {
			this.writeSuccessResult(resp, null, StatusConstant.CODE_200_MSG, requestEntity.getQuestId());
		} else {
			RequestEntity entity = getSyncResult(requestEntity.getQuestId());
			if(entity == null) {
				this.writeErrorResult(resp, StatusConstant.CODE_500, StatusConstant.CODE_500_MSG, requestEntity);
			} else if(entity.getStatus().equals(StatusConstant.CODE_1203)) {
				this.writeText(resp, entity.getResult(), entity.getResponseHeaders());
			} else {
				if(Strings.isNullOrEmpty(entity.getResponseErrorMsg())) {
					this.writeErrorResult(
						resp, 
						Strings.isNullOrEmpty(requestEntity.getSubStatus()) ? requestEntity.getStatus() : requestEntity.getSubStatus(), 
						requestEntity.getMessage(), 
						requestEntity);
				} else {
					if(Strings.isNullOrEmpty(entity.getResult())){
						this.writeText(resp, entity.getMessage(), entity.getResponseHeaders());
					} else {
						this.writeText(resp, entity.getResult(), entity.getResponseHeaders());
					}
				}
			}
		}
	}

}
