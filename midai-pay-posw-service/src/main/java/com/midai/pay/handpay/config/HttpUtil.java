package com.midai.pay.handpay.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

public class HttpUtil {
	
	private static Logger logger = LoggerFactory.getLogger(HttpUtil.class);
	
	public static String postForm(String url, Object obj) throws ClientProtocolException, IOException{
		String resp = null;
		CloseableHttpClient  httpClient = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(url);
		
		StringEntity postingString = new StringEntity(new Gson().toJson(obj),"UTF-8");
		logger.info("请求报文----" + new Gson().toJson(obj));
		
		post.setEntity(postingString);
		post.setHeader("Content-type", "application/json");
		
		HttpResponse  response = httpClient.execute(post);
		logger.info("请求报文状态码----" + response.getStatusLine().getStatusCode());
		
		if(response.getStatusLine().getStatusCode()==200){
			BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
			resp = reader.readLine();
		}
		
		logger.info("响应报文----" + resp);
		return resp;
	} 
}