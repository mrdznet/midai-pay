package com.midai.pay.psop.test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Test;

import com.midai.pay.quick.qb.common.util.HttpClientUtil;
import com.midai.pay.quick.qb.common.util.ZFBHttpClientUtil;

import net.sf.json.JSONObject;

/**
 * 
*    
* 项目名称：midai-pay-posp-service   
* 类名称：QBServiceTest   
* 类描述：   钱宝交易接口
* 创建人：wrt   
* 创建时间：2017年6月14日 上午10:15:15   
* 修改人：wrt   
* 修改时间：2017年6月14日 上午10:15:15   
* 修改备注：   
* @version    
*
 */
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes=PospServicesRunner.class)
public class QBPayTest {
	
	//钱宝交易接口
	@Test
	public void testPay() {
		Map<String, String> condition = new HashMap<String, String>();
		DefaultHttpClient httpclient = new DefaultHttpClient();
		String url = "http://cfit0.daxtech.com.cn:8020/payform/organization"; // 微信
//		String url = "http://cfit0.daxtech.com.cn:8020/payform/organization_zfb";
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
		String sendTime = sf.format(new Date());
		JSONObject json = new JSONObject();
		json.put("sendTime", sendTime);
		json.put("sendSeqId", "ZFBTest_0052");
		json.put("transType", "B001");
//		json.put("transType", "Z001");
		json.put("organizationId", "15901101057");
		json.put("payPass", "1");
		json.put("transAmt", "2");
		json.put("fee", "1");

		json.put("cardNo", "6217000010080916292");
		json.put("name", "黄昊");
		json.put("idNum", "110228199005090919");
//		json.put("subject", "微信支付");
		json.put("body", "罗技鼠标");
		json.put("notifyUrl", "http://60.205.113.137:8080/payform/payform");
		json.put("mobile", "15001120301");
		String makeMac = ZFBHttpClientUtil.makeMac(json.toString(), "B339A1E9676EC03FD1BF797E831912D1");
		json.put("mac", makeMac);//报文鉴别码
		System.out.println("mac:" + makeMac);
		condition.put("data", json.toString());
		
		//{"mac":"45353443","sendTime":"20170628153319","transAmt":"2","respDesc":"获取成功","organizationId":"15901101057","transType":"Z001","respCode":"00","sendSeqId":"ZFBTest_0018","imgUrl":"https://qr.alipay.com/bax06140yaqvmvzakudg00da"}


		String resulta = HttpClientUtil.post(httpclient, url, condition);
		System.out.println("resulta :" + resulta);
	}


	@Test
	public void queryStatus() {

		Map<String, String> condition = new HashMap<String, String>();
		DefaultHttpClient httpclient = new DefaultHttpClient();

		String url = "http://cfit0.daxtech.com.cn:8020/payform/queryStatus";


		JSONObject json = new JSONObject();
		json.put("sendSeqId", "ZFBTest_0024");
		json.put("transType", "B001");
		json.put("payType", "WeChat");
//		json.put("transType", "Z001");
//		json.put("payType", "AliPay");



		condition.put("data", json.toString());
		String resulta = HttpClientUtil.post(httpclient, url, condition);
		System.out.println("resulta :" + resulta);
	}
  
	/**
	 * 签到
	 */
	@Test
	public void sign() {
		
		Map<String, String> condition = new HashMap<String, String>();
		DefaultHttpClient httpclient = new DefaultHttpClient();
		
		String url = "http://cfit0.daxtech.com.cn:8020/payform/organization_zfb";
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
		String sendTime = sf.format(new Date());
		JSONObject json = new JSONObject();
		json.put("sendTime", sendTime);
		json.put("sendSeqId", "ZFBTest_0006");
//		json.put("transType", "B001");
		json.put("transType", "A001");
		json.put("organizationId", "15901101057");
		
		condition.put("data", json.toString());
		String resulta = HttpClientUtil.post(httpclient, url, condition);
		System.out.println("resulta :" + resulta);
	}


	@Test
	public void check() {
		JSONObject json = new JSONObject();
		json.put("transType", "Z001");
		json.put("imgUrl", "https://qr.alipay.com/bax03368cmx0wtj1yt0o00b6");
		json.put("sendSeqId", "ZFBTest_0006");
		json.put("sendTime", "20170607143111");
		json.put("respDesc", "获取成功");
		json.put("respCode", "00");
		json.put("organizationId", "15901101057");
		json.put("transAmt", "2");
		json.put("mac", "");
		String makeMac = ZFBHttpClientUtil.makeMac(json.toString(), "B339A1E9676EC03FD1BF797E831912D1");
		System.out.println("makeMac:" + makeMac);
		String mac = "41454639";
		
	}
}
