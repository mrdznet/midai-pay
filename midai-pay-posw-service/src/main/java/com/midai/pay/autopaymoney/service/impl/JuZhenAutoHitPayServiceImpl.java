package com.midai.pay.autopaymoney.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.alibaba.dubbo.config.annotation.Service;
import com.ju.utils.EncryptUtils;
import com.midai.pay.autopaymoney.service.JuZhenAutoHitPayService;
import com.midai.pay.common.pay.HttpPost;

import net.sf.json.JSONObject;

@Service
public class JuZhenAutoHitPayServiceImpl implements JuZhenAutoHitPayService{
	
	private static Logger logger = LoggerFactory.getLogger(JuZhenAutoHitPayServiceImpl.class);
	
	@Value("${JZURL}")
	private String url;
	
	@Value("${merId}")
	private String mid;// 钜阵提供的商户号
	
	@Value("${pubKeyUrl}")
	public String pubKeyUrl;

	/**
	 * 
	 * @Title: jzpayamt @Description: TODO(这里用一句话描述这个方法的作用) @param @param
	 *         orderid @param @param accountno @param @param
	 *         accountname @param @param tixianamt @param @param
	 *         bankname @param @param mercid @param @param
	 *         hosttransssn @param @param tradeCode @param @param
	 *         searchCode @param @return 设定文件 @return String 返回类型 @throws
	 */
	@Override
	public String jzpayamt(String orderid, String accountno, String accountname, String tixianamt,
			String bankname, String mercid, String hosttransssn, String tradeCode, String searchCode) {
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmss");
		Map<String, String> map = new HashMap<String, String>();
		HttpPost post = new HttpPost();
		
		String signature;
		String msgInfo = null;
		String responts = "";
		
		try {
			// 交易时间|收款账户类型|收款卡号|收款姓名|收款证件类型|收款证件号|收款联行号|收款账户开户行名称|交易金额|资金用途(msg需加密)
			if (tradeCode.equals("500302")) {// 调用打款
				logger.info("------------------------调用矩真代付");
				// 开户行名称最大20位字符
				if (bankname.length() > 20) {
					bankname = bankname.substring(0, 20);
				}
				msgInfo = sdf1.format(new Date()) + "|02|" + accountno + "|" + accountname + "||||" + bankname.trim()
						+ "|" + tixianamt + "|单笔代付";
				
				logger.info("==500302==:" + msgInfo);
			} else if (tradeCode.equals("500303")) {
				msgInfo = sdf1.format(new Date()) + "|02|" + accountno + "|" + accountname + "||||" + bankname.trim()
						+ "|" + tixianamt + "|" + mercid + "|" + hosttransssn + "|单笔代付";
				
				System.out.println("==500303==:" + msgInfo);
			} else if (tradeCode.equals("500501")) {// 打款结果查询
				msgInfo = sdf1.format(new Date()) + "|" + orderid + "|" + searchCode;
				
			} else if (tradeCode.equals("500401")) {// 查询余额
				msgInfo = sdf1.format(new Date()) + "|" + searchCode;
				orderid = sdf1.format(new Date()) + "000000";
				
			}
			
			msgInfo = EncryptUtils.encrypt(msgInfo, pubKeyUrl );// 合成加密的msg
			signature = EncryptUtils.juSignature(mid + orderid + tradeCode + msgInfo, pubKeyUrl);// 合成签名
			
			map.put("merId", mid);
			map.put("tradeCode", tradeCode);
			map.put("orderId", orderid);
			map.put("msg", msgInfo);
			map.put("signature", signature);
			
			long a = System.currentTimeMillis();
			
			responts = post.http(url, map);
			logger.info("responts:===" + responts);
			
			// 获取signature 并通过调用EncryptUtils.juValidateSignature,判断signature是否正确
			String msgs[] = responts.split("\"signature\":\"", -1);
			String signature1 = msgs[1].split("\"}", -1)[0];
			String responts1 = responts.replace(signature1, "@eidpay");
			String signatureStr = EncryptUtils.juSignature(responts1, pubKeyUrl);
			logger.info("signatureStr:" + signatureStr);
			
			if (EncryptUtils.juValidateSignature(responts, pubKeyUrl)) {
				logger.info("校验失败" + responts);
				logger.info("耗时" + (System.currentTimeMillis() - a));
			} else {
				logger.info("校验失败" + responts);
			}
		} catch (Exception e) {
			e.printStackTrace(System.err);
			logger.error(e.toString());
			return JSONObject.fromObject("{'respCode':'99','respInfo':'发送矩阵打款失败'}").toString();
		}
		
		return responts;
	}

	/*
	 * public static String getPayResult (String orderid,String accountno,String
	 * accountname, String tixianamt,String bankname,String dfway,String
	 * mercid,String hosttransssn){ String resonts=null; //
	 * resonts=jzpayamt(orderid, accountno, accountname, tixianamt,
	 * bankname,"","",dfway,""); resonts =jzpayamt(orderid, accountno,
	 * accountname, tixianamt, bankname,mercid,hosttransssn,dfway,""); //resonts
	 * =jzpayamt(orderid, accountno, accountname, tixianamt,
	 * bankname,"812330351370001","000000172206","500303",""); JSONObject json=
	 * JSONObject.fromObject(resonts); String orderId=null; String
	 * respCode=null; String respInfo=null; String signature=null;
	 * if(!json.has("orderId")&&!json.has("transamt")&&!json.has("feeamt")) {
	 * respCode=json.getString("respCode"); respInfo=json.getString("respInfo");
	 * signature=json.getString("signature"); orderId=null;
	 * System.out.println(respInfo); }else{ respCode=json.getString("respCode");
	 * respInfo=json.getString("respInfo");
	 * signature=json.getString("signature"); orderId=json.getString("orderId");
	 * } return orderId; }
	 */
}
