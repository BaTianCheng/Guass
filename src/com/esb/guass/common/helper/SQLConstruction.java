package com.esb.guass.common.helper;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * SQL字符串构造类
 * @author wicks
 */
public class SQLConstruction {
	
	//SQL语句
	private String sql;
	
	//替换字符内容
	private Map<Integer, String> replaceStrs;
	
	/**
	 * 构造函数
	 * @param sql
	 */
	public SQLConstruction(String sql){
		this.sql = sql;
		replaceStrs = new HashMap<>();
	}
	
	/**
	 * 设置字符串参数
	 * @param index
	 * @param str
	 */
	public void setString(int index, String str){
		if(str == null || str.equals("\\N")){
			replaceStrs.put(index, "null");
		} else {
			str = str.replace("'", "''");
			replaceStrs.put(index, "'"+str +"'");
		}
	}
	
	/**
	 * 设置数字型参数
	 * @param index
	 * @param bigDecimal
	 */
	public void setBigDecimal(int index, BigDecimal bigDecimal) {
		if(bigDecimal == null){
			replaceStrs.put(index, "null");
		} else {
			replaceStrs.put(index, "'"+bigDecimal.stripTrailingZeros().toPlainString()+"'");
		}
		
	}
	
	/**
	 * 设置字符串参数
	 * @param index
	 * @param str
	 */
	public void setObject(int index, Object obj){
		if(obj == null){
			setString(index, null);
		} else {
			setString(index, String.valueOf(obj));
		}
	}

	/**
	 * 返回已构造的SQL语句
	 * @return
	 */
	public String getSQL(){
		int beginIndex = 0;
		int endIndex = 0;
		int count = 1;
		StringBuilder constructionSQL = new StringBuilder();
		while((endIndex=sql.indexOf('?', beginIndex)) > -1){
			constructionSQL.append(sql.substring(beginIndex, endIndex));
			if(replaceStrs.containsKey(count)){
				constructionSQL.append(replaceStrs.get(count));
			}
			beginIndex = endIndex + 1;
			count ++;
		}
		constructionSQL.append(sql.substring(beginIndex, sql.length()));
		return constructionSQL.toString();
	}
	
	public static void main(String[] args) {
		SQLConstruction sql = new SQLConstruction("insert into xxx(y1,y2) values(?,?)");
		sql.setString(1, "z1'2'3");
		sql.setBigDecimal(2, new BigDecimal("100.56"));
		System.out.println(sql.getSQL());
	}
	
}
