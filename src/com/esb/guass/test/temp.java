package com.esb.guass.test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.esb.guass.common.util.MD5Utils;

public class temp {

	public static void main(String[] args) {
		try{
			System.out.println(MD5Utils.EncoderByMd5(new File("D:\\g.csv.gz")));
//			URL url = new URL("E:\\bm.csv");    
//	        HttpURLConnection conn = (HttpURLConnection)url.openConnection();    
//	        conn.setConnectTimeout(3*1000);
//	        InputStream inputStream = conn.getInputStream();    
//	        byte[] getData = readInputStream(inputStream);      
//	        String content = new String(getData);
//	        System.out.println(content);
		}
		catch(Exception ex){
			System.out.println(ex);
		}
	}
	
    /** 
     * 从输入流中获取字节数组 
     * @param inputStream 
     * @return 
     * @throws IOException 
     */  
    public static  byte[] readInputStream(InputStream inputStream) throws IOException {    
        byte[] buffer = new byte[1024];    
        int len = 0;    
        ByteArrayOutputStream bos = new ByteArrayOutputStream();    
        while((len = inputStream.read(buffer)) != -1) {    
            bos.write(buffer, 0, len);    
        }    
        bos.close();    
        return bos.toByteArray();    
    }  

}
