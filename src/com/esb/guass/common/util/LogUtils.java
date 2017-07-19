package com.esb.guass.common.util;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 日志工具类
 * @author wicks
 */
public class LogUtils {
	
    protected final static Logger logger = Logger.getLogger(LogUtils.class.getSimpleName());

    protected final boolean fine = logger.isLoggable(Level.FINE);
    
    
    /**
     * 输出信息
     * @param msg
     */
    public static void info(String msg){
    	logger.info(msg);
    }
    
    /**
     * 输出错误
     * @param msg
     * @param thrown
     */
    public static void error(String msg, Throwable thrown){
    	logger.log(Level.SEVERE, msg, thrown);
    }

}
