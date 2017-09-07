package com.esb.guass.job.service;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * 任务日志
 * @author wicks
 */
public class JobLogService{

	private static Logger log = Logger.getLogger("com.esb.job");  

	static{
		log.setLevel(Level.INFO);  
        
        FileHandler fileHandler;
		try {
			String userDir = System.getProperty("user.dir");
			File file = new File(userDir+"/logs/jobs/");
			if(!file.exists() && !file.isDirectory()){
				file.mkdir();
			}
			fileHandler = new FileHandler(userDir+"/logs/jobs/job-log%g.log", true);
			fileHandler.setLevel(Level.INFO);
			fileHandler.setFormatter(new Formatter() {
				public String format(LogRecord record) {
					SimpleDateFormat sd = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss]");
					String d = sd.format(new Date());
					return d + record.getLevel() + ":" + record.getMessage() + "\n";
				}
			});
			log.addHandler(fileHandler);
			log.setUseParentHandlers(false);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}  
        
	}
	
	public static void info(String info) {
		log.info(info);
	}
	
    /**
     * 输出错误
     * @param msg
     * @param thrown
     */
    public static void error(String msg, Throwable thrown){
    	log.log(Level.SEVERE, msg, thrown);
    }
	
}
