package com.esb.guass.auth.service;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.esb.guass.common.dao.mongo.MongoDAO;

/**
 * 白名单服务
 * 
 * @author wicks
 */
public class WhiteListService {

	private static List<String> whiteLists;

	private static final String dbName = "db_esb";

	private static final String collectionName = "tb_whitelist";

	/**
	 * 加载白名单
	 */
	public static void loadWhiteLists() {
		whiteLists = new ArrayList<String>();
		List<Document> docs = MongoDAO.getInstance().findAll(dbName, collectionName);
		if(docs != null) {
			for(Document doc : docs) {
				if(doc.getInteger("status") == 1) {
					if(doc.getString("ip").contains("*")){
						for(int i=1; i<255; i++){
							whiteLists.add(doc.getString("ip").replace("*", String.valueOf(i)));
						}
					} else {
						whiteLists.add(doc.getString("ip"));
					}
				}
			}
		}
	}

	/**
	 * 是否属于白名单之内
	 * 
	 * @param ip
	 * @return
	 */
	public static boolean isWhiteList(String ip) {
		return whiteLists.contains(ip);
	}

}
