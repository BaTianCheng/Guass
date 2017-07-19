package com.esb.guass.client.security;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
 
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
 
import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.HttpClientError;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.protocol.ControllerThreadSocketFactory;
import org.apache.commons.httpclient.protocol.SecureProtocolSocketFactory;
 
/**
 * SSL加密
 * @author wicks
 */
public class MySecureProtocolSocketFactory implements SecureProtocolSocketFactory {
 
    private SSLContext sslContext = null;
 
    /**
     * Constructor for MySecureProtocolSocketFactory.
     */
    public MySecureProtocolSocketFactory() {
    }
 
    /**
     * @return
     */
    private static SSLContext createEasySSLContext() {
        try {
            SSLContext context = SSLContext.getInstance("SSL");
            context.init(null, new TrustManager[] {new MyX509TrustManager()}, null);
            return context;
        } catch (Exception e) {
            throw new HttpClientError(e.toString());
        }
    }
 
    /**
     * @return
     */
    private SSLContext getSSLContext() {
        if (this.sslContext == null) {
            this.sslContext = createEasySSLContext();
        }
        return this.sslContext;
    }
 
    public Socket createSocket(String host, int port, InetAddress clientHost, int clientPort) throws IOException,
        UnknownHostException {
 
        return getSSLContext().getSocketFactory().createSocket(host, port, clientHost, clientPort);
    }
 
    public Socket createSocket(final String host, final int port, final InetAddress localAddress, final int localPort,
            final HttpConnectionParams params) throws IOException, UnknownHostException, ConnectTimeoutException {
        if (params == null) {
            throw new IllegalArgumentException("Parameters may not be null");
        }
        int timeout = params.getConnectionTimeout();
        if (timeout == 0) {
            return createSocket(host, port, localAddress, localPort);
        } else {
            return ControllerThreadSocketFactory.createSocket(this, host, port, localAddress, localPort, timeout);
        }
    }
 
    public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
        return getSSLContext().getSocketFactory().createSocket(host, port);
    }
 
    public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException,
        UnknownHostException {
        return getSSLContext().getSocketFactory().createSocket(socket, host, port, autoClose);
    }
}
