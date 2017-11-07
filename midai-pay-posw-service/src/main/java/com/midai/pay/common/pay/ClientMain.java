package com.midai.pay.common.pay;

import java.text.SimpleDateFormat;
import java.util.Date;

import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class ClientMain {
	public static final String[] wi = { "7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7", "9", "10", "5", "8",
			"4", "2", "1" };
	public static final String[] valideCode = { "1", "0", "10", "9", "8", "7", "6", "5", "4", "3", "2" };

	/** 私钥路径 **/
	public static String cerPath = "../bin/qilin0817_pub.cer";

	public static void main(String[] args) throws Exception {
		String id = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

		// String url = "http://10.10.10.46:8089/crps-pay/G10002/sendMessage?a="
		// + Math.random();
		String url = "http://localhost:8089/crps-demo/G10002/sendMessage";

		String result = postMethodInvoke(url,
				"{\"reqSn\":\"CZ0000000008\",\"bankCode\":\"13818938957\",\"accountNo\":\"192325\"}");

		System.out.println(result);
	}

	public static String postMethodInvoke(String url, String json) throws Exception {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost httppost = new HttpPost(url);
		String responseMsg = null;
		try {
			HttpEntity re = new StringEntity(json, "utf-8");
			httppost.setHeader("Content-Type", "application/json;charset=utf-8");
			httppost.setEntity(re);
			HttpResponse response = httpClient.execute(httppost);

			HttpEntity entity = response.getEntity();
			responseMsg = EntityUtils.toString(entity, "utf-8");
		} catch (Exception e) {
			throw new Exception("webservice请求异常", e);
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
		return responseMsg;
	}

	public static String getMethodInvoke(String url) throws Exception {
		String responseMsg = "";
		CloseableHttpClient httpClient = HttpClients.createDefault();

		HttpGet httpget = new HttpGet(url);
		try {
			httpget.setHeader("Content-Type", "application/json; charset=utf-8");
			httpget.setHeader("os", "Android");
			httpget.setHeader("ip", "192.168.21.131");
			HttpResponse response = httpClient.execute(httpget);
			HttpEntity entity = response.getEntity();
			responseMsg = EntityUtils.toString(entity, "utf-8");
		} catch (Exception e) {
			throw new Exception("webservice请求异常", e);
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
		return responseMsg;
	}

	/**
	 * 发送数据用私钥签名
	 * 
	 * @param param
	 * @param url
	 * @return
	 */
	public static String postSecurity(String param, String url) {
		String requestStr;
		try {
			// 私钥签名
			requestStr = RsaCodingUtil.encryptByPubCerFile(SecurityUtil.Base64Encode(param), cerPath);
			// ================ POST 提交 =====================

			// 这里调用http接口
			// String responseStr = postHttp(requestStr,url);
			String responseStr = ClientMain.postMethodInvoke(url, requestStr);

			// 解析返回数据
			// 畅捷回复密文，先公钥验签，后base64解密
			responseStr = RsaCodingUtil.decryptByPubCerFile(responseStr, cerPath);
			// BASE64解密
			responseStr = SecurityUtil.Base64Decode(responseStr);

			System.out.println("[解密响应报文]:" + responseStr);
			return responseStr;
		} catch (Exception e) {
			e.printStackTrace();
			return JSONObject.fromObject("{'RSPCOD':'99','RSPMSG':'传输加密异常'}").toString();
		}
	}

}
