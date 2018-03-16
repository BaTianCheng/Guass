package com.esb.guass.common.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 文件操作工具类
 * 
 * @author wicks
 */
public class FileUtils {

	/**
	 * 写入文件
	 * 
	 * @param path
	 * @param fileName
	 * @param str
	 * @throws IOException
	 */
	public static void write(String path, String fileName, String str) throws IOException {
		File file = new File(path + fileName);
		FileWriter fileWriter = new FileWriter(file);
		fileWriter.write(str);
		fileWriter.flush();
		fileWriter.close();
	}
	
	/**
	 * 读取文件
	 * 
	 * @param path
	 * @param fileName
	 * @param str
	 * @throws IOException
	 */
	public static String read(String path, String fileName) throws IOException {
		File file = new File(path + fileName);
		FileInputStream fIP = new FileInputStream(file);
		BufferedInputStream input = new BufferedInputStream(fIP, 2*1024);
		byte[] byteArray=new byte[1024];
		int tmp=0;
		StringBuilder sb = new StringBuilder();
		 
		while((tmp=input.read(byteArray))!=-1){
			sb.append(new String(byteArray, 0, tmp));
		}
		input.close();
		fIP.close();
		
		return sb.toString();
	}

}
