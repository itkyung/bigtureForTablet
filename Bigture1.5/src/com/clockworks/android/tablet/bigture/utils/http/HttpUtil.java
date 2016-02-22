package com.clockworks.android.tablet.bigture.utils.http;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class HttpUtil
{
	public static String TAG = "HttpUtil";

	public static boolean isNetworkAvailable(Context context)
	{
		boolean result = false;
		
		ConnectivityManager cm = (ConnectivityManager)context.getApplicationContext().
				getSystemService(Context.CONNECTIVITY_SERVICE);
		
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		
		if (networkInfo != null && networkInfo.isConnected())
			result = true;
		
		return result;
	}
	
	public static Bitmap loadImage(String address, BitmapFactory.Options options)
	{
		if(address == null || address.length() == 0)
			return null;

		InputStream is = null;
		Bitmap bitmap = null;
		
		try
		{
			URL url = new URL(address);
			is = (InputStream) url.openConnection().getInputStream();
			
			if (options != null)
				bitmap = BitmapFactory.decodeStream(is, null, options);
			else
				bitmap = BitmapFactory.decodeStream(is);
		}
		catch (MalformedURLException e)
		{
			e.printStackTrace();
			return null;
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
		finally
		{
			try { is.close(); } catch (Exception e) {}
		}

		return bitmap;
	}

	public static Bitmap downloadImage(String imageURL, String filePath)
	{
		if(imageURL == null || imageURL.length() == 0)
			return null;

		InputStream is = null;
		Bitmap bitmap = null;
		
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		
		try
		{
			URL url = new URL(imageURL);
			is = (InputStream) url.openConnection().getInputStream();

			fos = new FileOutputStream(new File(filePath));
			bos = new BufferedOutputStream(fos);
			
			byte[] buffer = new byte[2048];
			int count = 0;
			
			while ((count = is.read(buffer)) > 0)
				bos.write(buffer, 0, count);
		}
		catch (MalformedURLException e)
		{
			e.printStackTrace();
			return null;
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
		finally
		{
			try { is.close(); } catch (Exception e) {}
			try { bos.close(); fos.close(); } catch(Exception e) {}
		}

		return bitmap;
	}
	
	public static boolean downloadFile(String imageURL, String filePath)
	{
		if(imageURL == null || imageURL.length() == 0)
			return false;

		InputStream is = null;
		
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		
		try
		{
			URL url = new URL(imageURL);
			is = (InputStream) url.openConnection().getInputStream();

			fos = new FileOutputStream(new File(filePath));
			bos = new BufferedOutputStream(fos);
			
			byte[] buffer = new byte[2048];
			int count = 0;
			
			while ((count = is.read(buffer)) > 0)
				bos.write(buffer, 0, count);
		}
		catch (MalformedURLException e)
		{
			e.printStackTrace();
			return false;
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return false;
		}
		finally
		{
			try { is.close(); } catch (Exception e) {}
			try { bos.close(); fos.close(); } catch(Exception e) {}
		}

		return true;		
	}

	public String execute(HttpClient httpClient,String url,HttpRequestParameters params){
		String result = null;
		BufferedReader reader = null;
		
		try {
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
			HttpPost httpPost = new HttpPost(url);
			httpPost.setEntity(entity);
			httpPost.setHeader("mobile-call", "true");
			
			//HttpClient httpClient = HttpClientWrapper.getClient();
			
			
			HttpResponse response = httpClient.execute(httpPost);
			if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
				reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(),"UTF8"));
				StringBuilder sb = new StringBuilder();
				String line = null;
				while( (line = reader.readLine()) != null){
					sb.append(line).append("\n");
				}
				reader.close();
				result = sb.toString();
			}
			//result = httpClient.execute(httpPost, new BasicResponseHandler());
		}catch (IOException e) { 
			if (e.getMessage() !=null && e.getMessage().indexOf("Connection reset by peer") > 0){
				//이런경우에는 다시 시도한다. jelly bean에서만 발행하는 버그임.
				return this.execute(httpClient, url, params);
			}
		} catch (Throwable t) {
			Log.e(TAG, "Error Server Call ", t);
		} finally{
			//httpClient.getConnectionManager().shutdown();
		}
		return result;
	}
	
	public String execute(String url,HttpRequestParameters params){
		HttpClient httpClient = HttpClientWrapper.getClient();
		return execute(httpClient, url, params);
	}
	
	
	public String executeByMultipart(String address, Map<String,String>headers, Map<String,Object>params){
		HttpPost request = new HttpPost(address);
		MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
		
		try{
			if(headers == null){
				headers = new HashMap<String, String>();
				headers.put("mobile-call", "true");
				//무조건 header에 mobile-call여부를 알려준다.
			}
			
			
			if (headers != null){
				Iterator<String> it = headers.keySet().iterator();
				
				while (it.hasNext())
				{
					String name = it.next();
					String value = headers.get(name);
					request.setHeader(name, value);
				}
			}
		}catch(Throwable t){
			Log.e(TAG, t.getMessage());
		}
		
		try{
			if (params != null){
				Iterator<String> it = params.keySet().iterator();
				
				while (it.hasNext()){
					String key = it.next();
					
					Object object = params.get(key);
					if(object != null){
						if (object instanceof File)
							entity.addPart(key, new FileBody((File)object));
						else
							entity.addPart(key, new StringBody(object.toString(), Charset.forName("UTF-8")));
					}
				}
			}
			
			request.setEntity(entity);
		}catch(Throwable t){
			Log.e(TAG, "Error on uploading", t);
			t.printStackTrace();
		}
		
		String result = null;
		InputStream is = null;
		HttpClient httpClient = HttpClientWrapper.getClient();
		try{
			
			HttpResponse response = httpClient.execute(request);
			HttpEntity resEntity = response.getEntity();
			
			if(resEntity != null)
			{
				is = resEntity.getContent();
				result = convertStreamToString(is);
			}
		}catch(Throwable t){
			Log.e(TAG, "Error on uploading", t);
			result = null;
		}finally{
			try{
				if (is != null)
					is.close();
			}catch(Throwable t)
			{
			}
			//httpClient.getConnectionManager().shutdown();
		}
		
		return result;
	}
	
	private static String convertStreamToString(InputStream is) throws Exception
	{
		StringBuffer sb = new StringBuffer();
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));

		String line = null;
		while((line = reader.readLine()) != null)
		{
			sb.append(line);
			sb.append("\n");
		}

		return sb.toString();
	}

}
