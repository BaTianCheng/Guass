package com.esb.guass.common.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Strings;

/**
 * 数据类型转换工具类
 * @author wicks
 */
public final class ConvertUtils {

	/**
	 * 将字符串转换为Double
	 * @param str
	 * @return
	 */
	public static Double toDouble(String str) {
		if (Strings.isNullOrEmpty(str)) {
			return null;
		} else {
			return Double.valueOf(str);
		}
	}
	
	/**
	 * 将字符串转换为Integer
	 * @param str
	 * @return
	 */
	public static Integer toInteger(String str) {
		if (Strings.isNullOrEmpty(str)) {
			return 0;
		} else {
			return Integer.valueOf(str);
		}
	}

	/**
	 * 将小数保留两位
	 * @param num
	 * @return
	 */
	public static Double toDouble2(double num) {
		BigDecimal b = new BigDecimal(num);
		double newNum = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		return newNum;
	}
	
	/**
	 * 转换字符串数组
	 * @param list
	 * @return
	 */
	public static List<String> toStrings(List<Integer> list){
		List<String> strs = new ArrayList<String>();
		if(list == null){
			return strs;
		} else {
			for(Object obj : list){
				strs.add(obj.toString());
			}
			return strs;
		}
	}

	/**
	 * Map转换为Bean
	 * @param <T>
	 * @param paramMap
	 * @param cls
	 * @param ignoreCase
	 * @return
	 */
	public <T> T convert(final Map<String, Object> paramMap, Class<T> cls, boolean ignoreCase) {
		T obj = null;
		try {
			obj = cls.newInstance();

			// 20161122忽略下划线,全部大写
			HashMap<String, Object> map = new HashMap<String, Object>();
			for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
				map.put(entry.getKey().toUpperCase().replaceAll("_", ""), entry.getValue());
			}

			BeanInfo beanInfo = Introspector.getBeanInfo(cls);

			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

			for (PropertyDescriptor property : propertyDescriptors) {
				// 忽略大小写要全部大写，忽略下划线
				String beanKey = ignoreCase ? property.getName().toUpperCase().replaceAll("_", "")
						: property.getName().replaceAll("_", "");

				// 对于BigDecimal的单独处理
				if (property.getPropertyType().toString().equals("class java.math.BigDecimal")) {
					Object value = map.get(beanKey);
					if (value == null)
						value = 0;
					BigDecimal big_value = new BigDecimal(value.toString());
					Method setter = property.getWriteMethod();
					setter.invoke(obj, big_value);

					// 对于Integer的单独处理
				} else if (property.getPropertyType().toString().equals("class java.lang.Integer")) {
					Object value = map.get(beanKey);
					if (value == null)
						value = 0;
					Integer i_value = new Integer(value.toString());
					Method setter = property.getWriteMethod();
					setter.invoke(obj, i_value);

					// 对于Short的单独处理
				} else if (property.getPropertyType().toString().equals("class java.lang.Short")) {
					Object value = map.get(beanKey);
					if (value == null)
						value = 0;
					Short s_value = new Short(value.toString());
					Method setter = property.getWriteMethod();
					setter.invoke(obj, s_value);
				} else {
					Object value = null;
					if (map.containsKey(beanKey)) {
						value = map.get(beanKey);
						// 得到property对应的setter方法
						Method setter = property.getWriteMethod();
						setter.invoke(obj, value);

					}
				}

			}

		} catch (Exception e) {

		}

		return obj;
	}

	/**
	 * 将二进制转换成16进制 
	 * @param buf 
	 * @return 
	 */
	public static String parseByte2HexStr(byte buf[]) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buf.length; i++) {
			String hex = Integer.toHexString(buf[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex.toUpperCase());
		}
		return sb.toString();
	}

	/**
	 * 将16进制转换为二进制 
	 * @param hexStr 
	 * @return 
	 */
	public static byte[] parseHexStr2Byte(String hexStr) {
		if (hexStr.length() < 1)
			return null;
		byte[] result = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
			result[i] = (byte) (high * 16 + low);
		}
		return result;
	}

}
