package com.esb.guass.job.service;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * 任务日志
 * @author wicks
 */
public class JobLogService{

	private static Logger log = Logger.getLogger("com.esb.job");  

	static{
		// 读取配置文件  
        ClassLoader cl = LogManager.class.getClassLoader();  
        InputStream inputStream = null;  
        if (cl != null) {  
            inputStream = cl.getResourceAsStream(System.getProperty("user.dir")+"/conf/logging.properties");  
        } else {  
            inputStream = ClassLoader  
                    .getSystemResourceAsStream(System.getProperty("user.dir")+"/conf/logging.properties");  
        }  
        java.util.logging.LogManager logManager = java.util.logging.LogManager  
                .getLogManager();  
        try {  
            // 重新初始化日志属性并重新读取日志配置。  
            logManager.readConfiguration(inputStream);  
        } catch (SecurityException e) {  
            System.err.println(e);  
        } catch (IOException e) {  
            System.err.println(e);  
        }  
		
		log.setLevel(Level.INFO);  
        
        FileHandler fileHandler;
		try {
			String userDir = System.getProperty("user.dir");
			fileHandler = new FileHandler(userDir+"/logs/jobs/joblog-%g.log", true);
			fileHandler.setLevel(Level.INFO);
			fileHandler.setFormatter(new Formatter() {
				public String format(LogRecord record) {
					SimpleDateFormat sd = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss]");
					String d = sd.format(new Date());
					return d + record.getLevel() + ":" + record.getMessage() + "\n";
				}
			});
		          
			log.addHandler(fileHandler);  
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
    
    public static void main(String[] args) {
    	
    	
    	
    	JobLogService.info("xxx");
    }
	
}
