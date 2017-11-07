package com.midai.service.impl;


import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.midai.constants.QBConstants;
import com.midai.reqbean.QRCodePayReqBean;
import com.midai.reqbean.QRCodeSignReqBean;
import com.midai.reqbean.QueryStatusReqBean;
import com.midai.reqbean.QueryStatusResBean;
import com.midai.resbean.QRCodePayResBean;
import com.midai.resbean.QRCodeSignResBean;
import com.midai.service.QRCodePayService;
import com.midai.utils.GsonUtils;
import com.midai.utils.HttpClientUtil;
import com.midai.utils.PropUtil;
import com.midai.utils.ZFBHttpClientUtil;

/**
 * Created by justin on 2017/6/28.
 */
@SuppressWarnings("Duplicates")
public class QRCodePayServiceImpl implements QRCodePayService {

    private String notifyUrl;

    private static final Logger LOGGER= LoggerFactory.getLogger(QRCodePayServiceImpl.class);
    
    private Properties properties;
    /**
     * 通知回调地址
     * @param notifyUrl
     */
    public QRCodePayServiceImpl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
        if(properties == null) {
    		properties = PropUtil.getProperties("channel-config.properties");
    	}
    }

    public QRCodePayServiceImpl() {
    	if(properties == null) {
    		properties = PropUtil.getProperties("channel-config.properties");
    	}
    }
    
    // 请求二维码交易
    @Override
    public QRCodePayResBean getQRCode(QRCodePayReqBean reqBean, Boolean checkMac, String type, Boolean isSwit) {

        // 获得macKey
        // --------------------------------------------------------------------------
    	QRCodeSignReqBean signBean = new QRCodeSignReqBean();
    	signBean.setSendTime(reqBean.getSendTime());
    	signBean.setSendSeqId(reqBean.getSendSeqId());
    	signBean.setTransType("A001");
    	LOGGER.info("扫码支付------签到开始时间3：" + System.currentTimeMillis());
        QRCodeSignResBean qrCodeSignResBean = signForMacKey(signBean, isSwit);
        LOGGER.info("扫码支付------签到结束时间3：" + System.currentTimeMillis());
        if(qrCodeSignResBean.getRespCode().equals("99")) {
        	return new QRCodePayResBean("type类型错误", "99");
        }
        // --------------------------------------------------------------------------


        try {
            Map<String, String> condition = new HashMap<String, String>();
            DefaultHttpClient httpclient = new DefaultHttpClient();
            String url = properties.getProperty("qb.gdm.href");
           if(type.equals("dtm")) {
            	
            	if ("1".equals(reqBean.getPayPass())) {
            		
            		url = properties.getProperty("qb.customer.url-t0wxpay");
            	} else if ("2".equals(reqBean.getPayPass())) {
            		url = properties.getProperty("qb.customer.url-t0zfbpay");
            	} else {
            		return new QRCodePayResBean("type类型错误", "99");
            	}
            }

            // 校验下游mac
            if(checkMac) {
            	
            	if (checkMacNotPass(reqBean, qrCodeSignResBean.getTerminalInfo())) {
            		return new QRCodePayResBean("Mac校验失败", "99");
            	}
            }


//            SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
//            String sendTime = sf.format(new Date());
            JSONObject json = new JSONObject();
            json.put("sendTime", reqBean.getSendTime());
            json.put("sendSeqId", reqBean.getSendSeqId());
            json.put("transType", reqBean.getTransType()); //固定值 微信：B001   支付宝：Z001
            json.put("organizationId", properties.getProperty("qb.organization-id"));
            json.put("payPass", reqBean.getPayPass());
            json.put("transAmt", reqBean.getTransAmt());
            json.put("fee", reqBean.getFee());
            json.put("cardNo", reqBean.getCardNo());
            json.put("name", reqBean.getName());
            json.put("idNum", reqBean.getIdNum());
            json.put("body", reqBean.getBody());
            json.put("notifyUrl", notifyUrl);
            json.put("mobile", reqBean.getMobile());

            // 和下游校验成功后生成新的mac值给上游校验
            String makeMac = ZFBHttpClientUtil.makeMac(json.toString(), qrCodeSignResBean.getTerminalInfo());
//            json.put("mac", reqBean.getMac());//报文鉴别码
            json.put("mac", makeMac);//报文鉴别码
            System.out.println("mac:" + reqBean.getMac());
            condition.put("data", json.toString());

            //{"mac":"45353443","sendTime":"20170628153319","transAmt":"2","respDesc":"获取成功","organizationId":"15901101057","transType":"Z001","respCode":"00","sendSeqId":"ZFBTest_0018","imgUrl":"https://qr.alipay.com/bax06140yaqvmvzakudg00da"}

            LOGGER.info("钱宝签到交易请求数据：" + condition);
            LOGGER.info("扫码支付------交易开始时间4：" + System.currentTimeMillis());
            String resulta = HttpClientUtil.post(httpclient, url, condition);
            LOGGER.info("扫码支付------交易结束时间4：" + System.currentTimeMillis());
            LOGGER.info("钱宝签到交易响应数据：" + resulta);
            System.out.println("resulta:" + resulta);
            QRCodePayResBean qrCodePayResBean = GsonUtils.fromJson(resulta, QRCodePayResBean.class);
            if (qrCodePayResBean == null) {
                return new QRCodePayResBean("请检查收款方手机号或者组织机构号是否有效", "99");
            }
            qrCodePayResBean.setOrganizationId(reqBean.getOrganizationId());
            qrCodePayResBean.setTransAmt(reqBean.getTransAmt());
            qrCodePayResBean.setSendSeqId(reqBean.getSendSeqId());
            qrCodePayResBean.setSendTime(reqBean.getSendTime());
            qrCodePayResBean.setTransType(reqBean.getTransType());
            return qrCodePayResBean;
        } catch (Exception e) {
            e.printStackTrace();
            return new QRCodePayResBean("数据解析失败", "99");
        }
    }



    @Override
    public QRCodeSignResBean signForMacKey(QRCodeSignReqBean reqBean, Boolean isSwit) {
    	
    	QRCodeSignResBean signBean = new QRCodeSignResBean();
    	if(!isSwit) {
    		signBean.setSendSeqId(reqBean.getSendSeqId());
    		signBean.setSendTime(reqBean.getSendTime());
    		signBean.setTransType(reqBean.getTransType());
    		signBean.setRespCode("00");
    		signBean.setRespDesc("签到成功");
    		signBean.setTerminalInfo(properties.getProperty("qb.sign.mackey"));
    		signBean.setSendTime(reqBean.getSendTime());
    		return signBean;
    		
    	} else {
    		
    		try {
    			Map<String, String> condition = new HashMap<String, String>();
    			DefaultHttpClient httpclient = new DefaultHttpClient();
    			String url;
    			if ("1".equals(reqBean.getPayPass())) {
    				url = properties.getProperty("qb.customer.url-t0wxpay");
    			} else if ("2".equals(reqBean.getPayPass())) {
    				url = properties.getProperty("qb.customer.url-t0zfbpay");
    			} else {
    				return new QRCodeSignResBean("type类型错误", "99");
    			}
//            SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
//            String sendTime = sf.format(new Date());
    			JSONObject json = new JSONObject();
    			json.put("sendTime", reqBean.getSendTime());
    			json.put("sendSeqId", reqBean.getSendSeqId());
    			json.put("transType", reqBean.getTransType());
    			json.put("organizationId", properties.getProperty("qb.organization-id"));
    			
    			condition.put("data", json.toString());
    			String resulta = HttpClientUtil.post(httpclient, url, condition);
    			System.out.println("resulta :" + resulta);
    			signBean = GsonUtils.fromJson(resulta, QRCodeSignResBean.class);
    		} catch (Exception e) {
    			e.printStackTrace();
    			return new QRCodeSignResBean("数据解析失败", "99");
    		}
    	}

    	return signBean;
    }

    @Override
    public QueryStatusResBean queryOrderStatus(QueryStatusReqBean reqBean) {
        Map<String, String> condition = new HashMap<String, String>();
        DefaultHttpClient httpclient = new DefaultHttpClient();

        String url = properties.getProperty("qb.pay.querystatus");


        net.sf.json.JSONObject json = new net.sf.json.JSONObject();
        json.put("sendSeqId", reqBean.getSendSeqId());
        json.put("transType", reqBean.getTransType());
        json.put("payType", reqBean.getPayType());
//		json.put("transType", "Z001");
//		json.put("payType", "AliPay");



        condition.put("data", json.toString());
        String resulta = HttpClientUtil.post(httpclient, url, condition);
        System.out.println("resulta :" + resulta);
        return GsonUtils.fromJson(resulta, QueryStatusResBean.class);
    }


    /**
     * 与我们和下游定义的字段进行校验，若校验成功后，在重新生成新的mac传递给上游
     * @param reqBean 交易需要的参数
     * @param macKey 加密用的macKey，向上游获取
     * @return 校验未通过吗
     */
    private boolean checkMacNotPass(QRCodePayReqBean reqBean, String macKey) {
        JSONObject json = new JSONObject();
        json.put("sendTime", reqBean.getSendTime());
        json.put("sendSeqId", reqBean.getSendSeqId());
        json.put("transType", reqBean.getTransType()); //固定值 微信：B001   支付宝：Z001
        json.put("organizationId", reqBean.getOrganizationId());
        json.put("payPass", reqBean.getPayPass());
        json.put("transAmt", reqBean.getTransAmt());
        json.put("fee", reqBean.getFee());
        json.put("cardNo", reqBean.getCardNo());
        json.put("name", reqBean.getName());
        json.put("idNum", reqBean.getIdNum());
        json.put("body", reqBean.getBody());
        json.put("notifyUrl", reqBean.getNotifyUrl());
        json.put("mobile", reqBean.getMobile());
        String makeMac = ZFBHttpClientUtil.makeMac(json.toString(), macKey);

        return !makeMac.equals(reqBean.getMac());
    }

}
