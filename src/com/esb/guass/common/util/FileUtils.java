package com.esb.guass.common.util;

import java.io.File;
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

}
