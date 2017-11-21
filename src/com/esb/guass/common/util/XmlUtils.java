package com.esb.guass.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.util.JSON;

/**
 * XML工具类，体现在在与JSON互相转换
 * 
 * @author wicks
 */
public class XmlUtils {

	/**
	 * XML字符串转JSONObject
	 * 
	 * @param xml
	 * @return
	 * @throws DocumentException
	 */
	public static JSONObject documentToJSONObject(String xml) throws DocumentException {
		return elementToJSONObject(strToDocument(xml).getRootElement());
	}

	/**
	 * JSON字符串转XML字符串
	 * 
	 * @param jsonStr
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String jsonStrToXmlStr(String jsonStr) {
		HashMap<String, Object> map = (HashMap<String, Object>) JSON.parse(jsonStr);
		return mapToXmlStr(map);
	}

	/**
	 * Map转XML字符串
	 * 
	 * @param map
	 * @return
	 */
	public static String mapToXmlStr(HashMap<String, Object> map) {
		StringBuffer sb = new StringBuffer();
		mapToXML(map, sb);
		return sb.toString();
	}

	/**
	 * String 转 Document
	 * 
	 * @param xml
	 * @return
	 * @throws DocumentException
	 */
	public static Document strToDocument(String xml) throws DocumentException {
		return DocumentHelper.parseText(xml);
	}

	/**
	 * Element 转 JSONObject
	 * 
	 * @param node
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static JSONObject elementToJSONObject(Element node) {
		JSONObject result = new JSONObject();
		// 当前节点的名称、文本内容和属性
		List<Attribute> listAttr = node.attributes();// 当前节点的所有属性的list
		for(Attribute attr : listAttr) {// 遍历当前节点的所有属性
			result.put(attr.getName(), attr.getValue());
		}
		// 递归遍历当前节点所有的子节点
		List<Element> listElement = node.elements();// 所有一级子节点的list
		if(!listElement.isEmpty()) {
			for(Element e : listElement) {// 遍历所有一级子节点
				if(e.attributes().isEmpty() && e.elements().isEmpty()) // 判断一级节点是否有属性和子节点
					result.put(e.getName(), e.getTextTrim());// 沒有则将当前节点作为上级节点的属性对待
				else {
					if(!result.containsKey(e.getName())) // 判断父节点是否存在该一级节点名称的属性
						result.put(e.getName(), new JSONArray());// 没有则创建
					((JSONArray) result.get(e.getName())).add(elementToJSONObject(e));// 将该一级节点放入该节点名称的属性对应的值中
				}
			}
		}
		return result;
	}

	/**
	 * map转XML字符串
	 * 
	 * @param map
	 * @param sb
	 */
	@SuppressWarnings("rawtypes")
	private static void mapToXML(Map map, StringBuffer sb) {
		Set set = map.keySet();
		for(Iterator it = set.iterator(); it.hasNext();) {
			String key = (String) it.next();
			Object value = map.get(key);
			if(null == value)
				value = "";
			if(value.getClass().getName().equals("java.util.ArrayList")) {
				ArrayList list = (ArrayList) map.get(key);
				sb.append("<" + key + ">");
				for(int i = 0; i < list.size(); i++) {
					HashMap hm = (HashMap) list.get(i);
					mapToXML(hm, sb);
				}
				sb.append("</" + key + ">");
			} else {
				if(value instanceof HashMap) {
					sb.append("<" + key + ">");
					mapToXML((HashMap) value, sb);
					sb.append("</" + key + ">");
				} else {
					sb.append("<" + key + ">" + value + "</" + key + ">");
				}
			}
		}
	}

}
