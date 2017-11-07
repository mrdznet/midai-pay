package com.midai.pay.quick.qb.common.util;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.alibaba.dubbo.common.json.JSONObject;
import com.midai.pay.quick.qb.common.encry.DESUtil;
import com.midai.pay.quick.qb.common.encry.Tools;


/**
 * @author Zerml
 * @date 2016年8月17日上午11:13:59
 * @description httpClient 工具类
 */
@SuppressWarnings("deprecation")
public class HttpClientUtil {
	/**
	 * 
	 * @author Zerml
	 * @date 2016年8月17日上午11:14:19
	 * @description HttpClient Post
	 */
	public static String post(String url, Map<String, String> params) {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		String body = null;
		HttpPost post = postForm(url, params);
		body = invoke(httpclient, post);
		httpclient.getConnectionManager().shutdown();
		return body;
	}
	
	/**
	 * 
	 * @author Zerml
	 * @date 2016年8月17日上午11:14:19
	 * @description HttpClient Post
	 */
	public static String post(DefaultHttpClient httpclient, String url, Map<String, String> params) {
		String body = null;
		HttpPost post = postForm(url, params);
		body = invoke(httpclient, post);
		return body;
	}
	
	/**
	 * 
	 * @author Zerml
	 * @date 2016年8月17日上午11:14:41
	 * @description
	 */
	public static String get(String url) {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		String body = null;
		HttpGet get = new HttpGet(url);
		body = invoke(httpclient, get);
		httpclient.getConnectionManager().shutdown();
		return body;
	}
		
	
	private static String invoke(DefaultHttpClient httpclient,
			HttpUriRequest httpost) {
		HttpResponse response = sendRequest(httpclient, httpost);
		String body = paseResponse(response);
		return body;
	}

	@SuppressWarnings({ "unused" })
	private static String paseResponse(HttpResponse response) {
		HttpEntity entity = response.getEntity();
		String charset = EntityUtils.getContentCharSet(entity);
		String body = null;
		try {
			body = EntityUtils.toString(entity);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return body;
	}

	private static HttpResponse sendRequest(DefaultHttpClient httpclient,
			HttpUriRequest httpost) {
		HttpResponse response = null;
		
		try {
			response = httpclient.execute(httpost);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response;
	}

	private static HttpPost postForm(String url, Map<String, String> params){
		HttpPost httpost = new HttpPost(url);
		List<NameValuePair> nvps = new ArrayList <NameValuePair>();
		Set<String> keySet = params.keySet();
		for(String key : keySet) {
			nvps.add(new BasicNameValuePair(key, params.get(key)));
		}
		try {
			httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return httpost;
	}
	
	public static String makeMac(String json,String macKey){
    	Map<String, String> contentData = (Map<String, String>) Tools.parserToMap(json);
		String macStr="";
		Object[] key_arr = contentData.keySet().toArray();
		Arrays.sort(key_arr);
		for (Object key : key_arr) {
			Object value = contentData.get(key);
			if (value != null ) {
				if (!key.equals("mac") ) {
					macStr+= value.toString();
				}
			}
		}
		String rMac = DESUtil.mac(macStr,macKey);
		return rMac;
    }
	
	public static void main(String[] args) throws Exception {
		String str=getQrCodeT1("Test040");
		System.out.println(str);
	}
	
	public static String getQrCodeT1(String orderId) throws Exception {
		Map<String, String> condition = new HashMap<String, String>();
		DefaultHttpClient httpclient = new DefaultHttpClient();
		String url = "http://60.205.113.133:8020/payform/organization";
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
		String sendTime = sf.format(new Date());
		
		JSONObject json = new JSONObject();
		json.put("sendTime", sendTime);
		json.put("sendSeqId", orderId);
		json.put("transType", "B002");
		json.put("organizationId", "15001120301");
		json.put("payPass", "1");
		json.put("transAmt", "10000");
		json.put("body", "支付测试");
		json.put("notifyUrl", "http://111.161.76.60:8080/payform/payform");
		
		String makeMac = makeMac(json.toString(), "签到后的密钥");
		json.put("mac", makeMac);//报文鉴别码
		
		condition.put("data", json.toString());
		
		String resulta = HttpClientUtil.post(httpclient, url, condition);
		return resulta;
	}
	
}
