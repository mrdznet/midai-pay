package com.midai.utils;

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


import com.midai.encry.DESUtil;
import com.midai.encry.Tools;
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
import org.json.JSONObject;


@SuppressWarnings("deprecation")
public class ZFBHttpClientUtil {

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
		Map<String, String> condition = new HashMap<String, String>();
		DefaultHttpClient httpclient = new DefaultHttpClient();
/*		String url = "http://60.205.113.137:8020/payform/orgMerchant_zfb";
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
		String sendTime = sf.format(new Date());

		JSONObject json = new JSONObject();

		json.put("sendTime", sendTime);
		json.put("sendSeqId", "ZFBTest_0002");
		json.put("transType", "Z001");
		json.put("organizationId", "15901101057");
		json.put("payPass", "1");
		json.put("fee", "1");

		json.put("transAmt", "2");
		json.put("cardNo", "6217000010080916292");
		json.put("name", "黄昊");
		json.put("idNum", "110228199005090919");
		json.put("subject", "支付宝支付");
		json.put("body", "罗技鼠标");
		json.put("notifyUrl", "http://60.205.113.137:8080/payform/payform");
		json.put("mobile", "15001120301");

		String makeMac = makeMac(json.toString(), "32029ED76A36F701B887A1BDF730612E");
		json.put("mac", makeMac);//报文鉴别码

		condition.put("data", json.toString());

		String resulta = HttpClientUtil.post(httpclient, url, condition);
		System.out.println(resulta);*/
		String url = "http://123.57.204.135:8020/payform/payform_zfb";

		JSONObject json = new JSONObject();
		json.put("transType", "QRCODE_ALI");
		json.put("userId", "15001120301");
		json.put("payPass", "2");
		json.put("transAmt", "100");
		json.put("subject", "橙付通支付宝支付");
		String makeMac = makeMac(json.toString(), "6EB5D1BD2AC61EC2D611FD4332A98C48");
		json.put("mac", makeMac);//报文鉴别码

		condition.put("data", json.toString());

		String resulta = HttpClientUtil.post(httpclient, url, condition);
		System.out.println(resulta);
	}
}
