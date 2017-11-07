package com.midai.pay.handpay.service.impl;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.midai.pay.handpay.config.HttpClient;
import com.midai.pay.handpay.config.Md5Utils;
import com.midai.pay.handpay.service.JiDian4AuthService;
import com.midai.pay.handpay.vo.AuthBean;
import com.midai.pay.handpay.vo.CustomerAuthBean;
import com.midai.pay.mobile.AppBaseEntity;
import com.midai.pay.mobile.entity.CustomerAuthInfoEntity;
import com.midai.pay.mobile.mapper.CustomerAuthMapper;
import com.midai.pay.mobile.utils.ZUtil;
import com.midai.pay.posp.service.ParCarBinService;
import com.midai.pay.sms.entity.BoSMS;
import com.midai.pay.sms.mapper.BoSMSMapper;

@Service
public class JiDian4AuthServiceImpl implements JiDian4AuthService {
	
	private Logger logger = LoggerFactory.getLogger(JiDian4AuthServiceImpl.class);
	
	@Value("${jidian.url}")
	private String jidian_url;
	
	@Value("${jidian.signKey}")
	private String signKey;		// 每个商户分配md5盐值
	
	@Value("${jidian.merid}")
	private String merid;	// 分配商户号
	
	@Autowired
	private BoSMSMapper bmp;
	
	@Autowired
	private CustomerAuthMapper camp;
	
	@Reference
	private ParCarBinService pcbs;
	
	@Override
	public Map<String, String> auth4(AuthBean auth) {
		Map<String, String> map = new HashMap<>();
		
		Map<String, String> contentData = new TreeMap<String, String>();
		contentData.put("version", "5.0.0");
		contentData.put("encoding", "UTF-8");
		
		contentData.put("txnType", "00"); //  00 四要素认证, 05 实名, 06 实人验证
		
		contentData.put("merId", merid);
		contentData.put("orderId", String.valueOf(System.currentTimeMillis()));	// auth.getOrderId()
		contentData.put("txnTime", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		
		// 位图形式: 第一位数字是1代表卡号, 第二为数字1代表验证身份证, 第三位为数字1代表验证姓名, 第四位为数字1代表验证手机
		contentData.put("defaultPayType", "1111"); // 实名认证: 0110, 四要素认证:1111
		
		contentData.put("certifTp","01"); //证件类型: 01身份证
		contentData.put("certifId", auth.getCertifId()); //证件号码:  身份证号
		
		contentData.put("customerNm", auth.getCustomerNm()); // 姓名
		
		contentData.put("accType", "01"); // 01 卡
		contentData.put("accNo", auth.getAccNo()); // 卡号
		
		contentData.put("phoneNo", auth.getPhoneNo()); // 手机
		
		String sign = signData(contentData);
		contentData.put("signature", sign);
		
		try {
			logger.info("四要素验证请求数据：" + contentData);
			logger.info("四要素验证请求链接：" + jidian_url);
			
			HttpClient client = new HttpClient(jidian_url, 6000, 60000);
			client.send(contentData, "utf-8");
			String respons = client.getResult();
			
			String decode = URLDecoder.decode(respons, "utf-8");
			Map<String, String> res = coverString2Map(decode);
			logger.info("四要素验证响应数据：" + res);
			
			if(res.get("respCode").equals("00")){ //验证成功
				map.put("result", "true");
			}else{
				map.put("result", "false");
				map.put("msg", res.get("respMsg"));
				
				logger.error("四要素验证不通过："+res.get("respMsg"));
			}
		} catch (Exception e) {
			logger.error("四要素验证错误");
			logger.error(e.toString());
		}

		return map;
	}

	@Override
	public Map<String, String> authQuery(String orderId) {
		Map<String, String> map = new HashMap<>();
		
		Map<String, String> contentData = new TreeMap<String, String>();
		contentData.put("version", "5.0.0");
		contentData.put("encoding", "UTF-8");

		// 01 认证订单查询
		contentData.put("txnType", "01");
		contentData.put("merId", merid);
		
		// 商户原交易订单号
		contentData.put("orderId", orderId);
		// 商户原交易订单时间
		contentData.put("txnTime", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		
		String sign = signData(contentData);
		contentData.put("signature", sign);
		
		try {
			HttpClient client = new HttpClient(jidian_url, 6000, 60000);
			
			client.send(contentData, "utf-8");
			String respons = client.getResult();
			
			String decode =  URLDecoder.decode(respons, "utf-8");
			Map<String, String> res = coverString2Map(decode);
			
			if(res.get("origRespCode").equals("00")){ //验证成功
				map.put("result", "true");
			}else{
				map.put("result", "false");
				map.put("msg", res.get("origRespMsg"));
			}
		} catch (Exception e) {
			logger.error("认证查询错误");
			logger.error(e.toString());
		}
		
		return map;
	}
	
	public String signData(Map<String, String> contentData) {
		Map.Entry<String, String> obj = null;
		Map<String, String> submitFromData = new TreeMap<String, String>();
		for (Iterator<?> it = contentData.entrySet().iterator(); it.hasNext();) {
			obj = (Map.Entry<String, String>) it.next();
			String value = obj.getValue();
			if (!Strings.isNullOrEmpty(value)) {
				// 对value值进行去除前后空处理
				submitFromData.put(obj.getKey(), value.trim());
			}
		}
		String signData = coverMap2String(submitFromData) + "&signkey=" + signKey;
		String sign = Md5Utils.md5(signData.getBytes(Charsets.UTF_8));
		return sign;
	}

	public static String coverMap2String(Map<String, String> data) {
		TreeMap tree = new TreeMap();
		Iterator it = data.entrySet().iterator();
		StringBuffer sf1 = new StringBuffer();
		
		while (it.hasNext()) {
			Map.Entry en = (Map.Entry) it.next();
			if (!en.getKey().equals("photo")) {
				sf1.append(en.getKey() + "=" + en.getValue() + "&");
			}
		}
		return sf1.substring(0, sf1.length() - 1);
	}

	public static Map<String, String> coverString2Map(String param) {
		Iterable<String> response = Splitter.on("&").split(param);
		Map<String, String> maps = new TreeMap<String, String>();

		for (Iterator<String> it = response.iterator(); it.hasNext();) {
			String asd = it.next();
			String[] s = asd.split("=");
			maps.put(s[0], s[1]);
		}
		return maps;
	}

	@Override
	public Object mobileAuth4(AuthBean auth) {
		if (!auth.getMscode().isEmpty()) {
			// 验证短信
			String currenttime = ZUtil.getDTimes();
			BoSMS sms = bmp.getByPhoneNumCur(auth.getPhoneNo(), currenttime, 0);
			String code = null;
			if (sms != null) {
				code = sms.getSmscontent();
			}
			
			if (code == null || code.equals("")) {
				return new AppBaseEntity("02", "验证码不存在或已过期");
			} else if (!auth.getMscode().equals(code)) {
				return new AppBaseEntity("03", "验证码有误");
			}
			
			//四要素验证
			Map<String, String> map = this.auth4(auth);
			
			if(map.get("result").equals("true")){
				
				try {
					CustomerAuthInfoEntity entity = new CustomerAuthInfoEntity();
					entity.setCustomer(auth.getCustomerNm());
					entity.setBankCard(auth.getAccNo());
					entity.setIdCard(auth.getCertifId());
					entity.setAuthTel(auth.getPhoneNo());
					entity.setLoginTel(auth.getLoginPhone());
					entity.setCreateTime(new Date());
					entity.setUpdateTime(new Date());
					camp.insert(entity);
				} catch(Exception e) {
					logger.info("四要素保存失败", e);
				}
				return new AppBaseEntity("00", "四要素验证通过！");
			} else {
				
				return new AppBaseEntity("01", map.get("msg"));
			}
		}
    	
		return null;
	}
	
	/**
	 * 判断是否已经经过四要素验证
	 */
	@Override
	public Object ifAlreadyAuth(CustomerAuthBean authInfo) {
		Map<String, String> map = new HashMap<>();
		if (!StringUtils.isEmpty(authInfo.getLoginTel()) && !StringUtils.isEmpty(authInfo.getBankCard())) {
			try {
				Boolean isdebi = pcbs.isDebiCard(authInfo.getBankCard());
				if (isdebi) {
					map.put("isDebiCard", "true");
				}
			} catch (Exception e) {
				if (e.getMessage().equals("nobin")) {
					map.put("RSPCOD", "99");
					map.put("RSPMSG", "卡BIN不存在");
				}else{
					e.printStackTrace();
					logger.error("获取carbin异常：",e.getMessage());
					throw new RuntimeException(e);
				}
			}
			CustomerAuthInfoEntity entity = camp.getByMobileAndBankCard(authInfo.getLoginTel(), authInfo.getBankCard());
			if (entity != null && entity.getId()>0) {
				map.put("RSPCOD", "00");
				map.put("RSPMSG", "四要素已验证");
			}else{
				map.put("RSPCOD", "99");
				map.put("RSPMSG", "四要素未验证");
			}
			
		}else{
			map.put("RSPCOD", "98");
			map.put("RSPMSG", "参数缺失");
		}
		return map;
	}

}
