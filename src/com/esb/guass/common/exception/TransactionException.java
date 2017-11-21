package com.esb.guass.common.exception;

/**
 * 业务异常
 * @author wicks
 */
public class TransactionException extends RuntimeException{

	private static final long serialVersionUID = 7052397508526195415L;
	
    public TransactionException(String message) {
        super(message);
    }
    
    public TransactionException(Throwable cause) {
        super(cause);
    }

}
