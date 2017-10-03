package com.esb.guass.test;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface ServiceHelloInface {
	
    @WebMethod
    public String getValue(String name);

}
