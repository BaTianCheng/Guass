package com.esb.guass.job.service;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
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
			fileHandler = new FileHandler("D:/testlog%g.log", true);
			fileHandler.setLevel(Level.INFO);
			log.addHandler(fileHandler);  
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
        
/*
        fileHandler.setFormatter(new Formatter()  
                                 {  
                                    public String format(LogRecord record)  
                                    {  
                                        SimpleDateFormat sd = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss]");    
                                        String d = sd.format(new Date());    
                                        return d + record.getLevel() + ":" + record.getMessage() + "/n";  
                                    }  
                                 });  
                                 */
        
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
