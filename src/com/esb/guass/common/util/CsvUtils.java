package com.esb.guass.common.util;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import com.esb.guass.common.helper.CsvReader;
import com.esb.guass.common.helper.CsvWriter;

/**
 * CSV文件操作类
 * @author wicks
 */
public class CsvUtils {
	
	private static String charset = "GBK";

	/**
	 * 读取CSV文件
	 */
	public static List<String[]> readeCsv(String path, String filename) {
		List<String[]> csvList = new ArrayList<String[]>(); 
		try {
			String csvFilePath = path + filename;
			CsvReader reader = new CsvReader(csvFilePath, ',', Charset.forName(charset)); 
			reader.readHeaders();
			csvList.add(reader.getValues());
			while (reader.readRecord()) { //逐行读入除表头的数据    
				if(reader.getValues()[0] == null || reader.getValues()[0].equals("")){
					break;
				}
				csvList.add(reader.getValues());
			}
			reader.close();

		} catch (Exception ex) {
			LogUtils.error("读取CSV文件失败，"+filename, ex);
		}
		
		return csvList;
	}

	/**
	 * 写入CSV文件
	 */
	public static String writeCsv(String path, String filename, List<String[]> csvList) {
		try {
			String csvFilePath = path + filename;
			CsvWriter wr = new CsvWriter(csvFilePath, ',', Charset.forName(charset));
			if(csvList != null && csvList.size() > 0){
				for(String[] strs : csvList){
					wr.writeRecord(strs);
				}
			}
			wr.close();
			return csvFilePath;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
