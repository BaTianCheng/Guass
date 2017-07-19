package com.esb.guass.common.util;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.httpclient.NameValuePair;

import com.google.common.base.Strings;

public class StringUtils {
	  /**
     * 将NameValuePairs数组转变为字符串
     * 
     * @param nameValues
     * @return
     */
    public static String toString(NameValuePair[] nameValues) {
        if (nameValues == null || nameValues.length == 0) {
            return "null";
        }

        StringBuffer buffer = new StringBuffer();

        for (int i = 0; i < nameValues.length; i++) {
            NameValuePair nameValue = nameValues[i];

            if (i == 0) {
                buffer.append(nameValue.getName() + "=" + nameValue.getValue());
            } else {
                buffer.append("&" + nameValue.getName() + "=" + nameValue.getValue());
            }
        }

        return buffer.toString();
    }
    
    /**
     * MAP类型数组转换成NameValuePair类型
     * @param properties  MAP类型数组
     * @return NameValuePair类型数组
     */
    public static NameValuePair[] generatNameValuePair(Map<String, String> properties) {
        NameValuePair[] nameValuePair = new NameValuePair[properties.size()];
        int i = 0;
        for (Map.Entry<String, String> entry : properties.entrySet()) {
            nameValuePair[i++] = new NameValuePair(entry.getKey(), entry.getValue());
        }

        return nameValuePair;
    }
    
    /**
	 * 获取本机地址
	 */
	public static String getHostIPAddr(){
        InetAddress inetAddress = null;
        try {
            inetAddress  = InetAddress.getLocalHost();
        }catch(Exception e){

        }
        return inetAddress.getHostAddress();
    }
	
    /**
     * Name:    split
     * Desc:    根据分割符号把字符串分割成一个数组
     * Author:  Wang Zhiyong
     * @param   strValue 要分割的字符串
     * @param   strSeparator 分割符号
     * @return  String[]
     */
    public static String[] split(String strValue,String strSeparator)
    {
        List<String> vTemp=new ArrayList<String>();
        StringTokenizer st=new StringTokenizer(strValue,strSeparator);
        while(st.hasMoreElements())
        {
            vTemp.add(st.nextToken());
        }
        String rtn[]=(String[]) vTemp.toArray(new String[0]);
        return rtn;
    }
    
    /**
     * SQL参数替换
     * @param sql
     * @param value
     */
    public static String setSqlParam(String sql, Object value){
    	return sql.replaceFirst("[?]", "'"+value+"'");
    }
    
    public static String getJsonMap(String value, String defaultStr){
    	if(Strings.isNullOrEmpty(value)){
    		return defaultStr;
    	} else if(value.equals("null") || value.equals("\"null\"")){
    		return defaultStr;
    	} else {
    		return value;
    	}
    }

}
