package com.clockworks.android.tablet.bigture.utils.http;

import java.util.ArrayList;

import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EncodingUtils;

import android.content.Context;


public class HttpRequestParameters extends ArrayList<BasicNameValuePair>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8653847526243402870L;


	public HttpRequestParameters() {
		super();
	}
	

	public void add(String key, String value) {
		this.add(new BasicNameValuePair(key, value));
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for(BasicNameValuePair param : this) {
			sb.append("&");
			sb.append(param.getName());
			sb.append("=");
			sb.append(param.getValue());
		}
		String postData = sb.toString();
		return postData.length() > 1 ? postData.substring(1) : postData;
	}
	
	public byte[] toPostData() {
		return EncodingUtils.getBytes(toString(), "BASE64");
	}

}
