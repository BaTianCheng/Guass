package com.esb.guass.test;

import javax.jws.WebService;
import javax.xml.ws.Endpoint;

@WebService(endpointInterface = "com.esb.guass.test.ServiceHelloInface", serviceName = "JaxWS")
public class ServiceHello implements ServiceHelloInface {
	/**
	 * 供客户端调用的方法
	 * 
	 * @param name
	 *            传入参数
	 * @return String 返回结果
	 */
	public String getValue(String name) {
		return "我叫：" + name;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ServiceHelloInface in = new ServiceHello();
		Endpoint.publish("http://localhost:9001/Service/ServiceHello", in);
		System.out.println("service success!");
	}
}
