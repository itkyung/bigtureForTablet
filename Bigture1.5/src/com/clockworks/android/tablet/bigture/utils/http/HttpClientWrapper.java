package com.clockworks.android.tablet.bigture.utils.http;

import java.lang.reflect.Method;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import android.content.Context;

public class HttpClientWrapper {

	private static DefaultHttpClient client;
	
	public static DefaultHttpClient getClient(){
		if(client == null){
			final HttpParams params = new BasicHttpParams();
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
			HttpConnectionParams.setConnectionTimeout(params, 20000);
			HttpConnectionParams.setSoTimeout(params, 20000);
			
		
			ConnManagerParams.setMaxTotalConnections(params, 10);
			
			SchemeRegistry schemeRegistry = new SchemeRegistry();
			schemeRegistry.register(
			        new Scheme("http",  PlainSocketFactory.getSocketFactory(), 80));
//			schemeRegistry.register(
//					new Scheme("https", getHttpsSocketFactory(), 443));

			ClientConnectionManager cm = new ThreadSafeClientConnManager(params,schemeRegistry);
			
			client = new DefaultHttpClient(cm,params);
		}
		return client;
	//	return new DefaultHttpClient();
	}
//
//	protected static SocketFactory getHttpsSocketFactory(){
//		try {
//			Class sslSessionCacheClass = Class.forName("android.net.SSLSessionCache");
//			Object sslSessionCache = sslSessionCacheClass.getConstructor(Context.class).newInstance(application);
//			Method getHttpSocketFactory = Class.forName("android.net.SSLCertificateSocketFactory").getMethod("getHttpSocketFactory", new Class[]{int.class, sslSessionCacheClass});
//			return (SocketFactory) getHttpSocketFactory.invoke(null, CONNECTION_TIMEOUT, sslSessionCache);
//		}catch(Exception e){
//			Log.e("HttpClientProvider", "Unable to use android.net.SSLCertificateSocketFactory to get a SSL session caching socket factory, falling back to a non-caching socket factory",e);
//			return SSLSocketFactory.getSocketFactory();
//		}
//		 
//	}
}
