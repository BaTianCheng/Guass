package com.esb.guass.common.base;

/**
 * 运行时异常基类
 * 
 * @author wicks
 */
public class BaseException extends RuntimeException {

	/**
	 * 序列
	 */
	private static final long serialVersionUID = 1217793455744380524L;

	private String message;

	public BaseException(String message) {
		super(message);
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

}
