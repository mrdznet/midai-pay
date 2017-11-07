package com.midai.pay.mobile.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.alibaba.dubbo.config.annotation.Service;
import com.midai.enums.QBQrCodePayReqPayPassEnum;
import com.midai.framework.common.DoubleUtil;
import com.midai.pay.customer.entity.BoCustomer;
import com.midai.pay.customer.mapper.BoCustomerMapper;
import com.midai.pay.dealtotal.service.DealtotalQuickService;
import com.midai.pay.mobile.ScanAppBaseEntity;
import com.midai.pay.mobile.service.MobileQRCodePayService;
import com.midai.reqbean.QRCodePayReqBean;
import com.midai.resbean.QRCodePayResBean;
import com.midai.service.QRCodePayService;
import com.midai.service.impl.QRCodePayServiceImpl;

@Service
public class MobileQRCodePayServiceImpl implements MobileQRCodePayService {

	private Logger logger = LoggerFactory.getLogger(MobileQRCodePayServiceImpl.class);
	@Autowired
	private BoCustomerMapper mapper;
	@Autowired
	private DealtotalQuickService dealtotalQuickService;

	@Value("${qb.organization-id}")
	private String orgId;
	
	@Value("${qb.sign.isswitch}")
	private Boolean isswitch;
	
	@Override
	public ScanAppBaseEntity transScanHandler(String userAgent, String mercNo, Double transAmtReq, String remarks, String notifyUrl) {
		ScanAppBaseEntity entity = new ScanAppBaseEntity();
		QRCodePayReqBean qrCodePayReqBean = new QRCodePayReqBean();
		BoCustomer customer = mapper.selectByMercNo(mercNo);

		if (customer != null) {

			qrCodePayReqBean.setBody(customer.getMercName());
			qrCodePayReqBean.setCardNo(customer.getAccountNo());
			Double transAmt = DoubleUtil.mul(transAmtReq, 100);
			Double rate = 0D;
			Boolean flag = true;
			String type = "";
			String paypass = "";
			if (userAgent.contains("MicroMessenger")) { //微信
				
				rate = customer.getScanCodeWxRate();
				type = QBQrCodePayReqPayPassEnum.TYPE_WX.getCode();
				paypass = QBQrCodePayReqPayPassEnum.TYPE_WX.getType();
			} else if (userAgent.contains("AlipayClient")) {  //支付宝
				
				rate = customer.getScanCodeZfbRate();
				type = QBQrCodePayReqPayPassEnum.TYPE_ZFB.getCode();
				paypass = QBQrCodePayReqPayPassEnum.TYPE_ZFB.getType();
			} else {
				
				flag = false;
				entity.setRSPCOD("99");
				entity.setRSPMSG("未知消息来源！");
			}

			if(flag) {
				Double dfee = DoubleUtil.divHalfUp(DoubleUtil.mul(transAmt, rate), 100D, 2);
				if(dfee < 1) {
					dfee = 1D;
				}
				qrCodePayReqBean.setFee(Double.valueOf(Math.ceil(dfee)).intValue() + "");
				qrCodePayReqBean.setIdNum(customer.getIdCard());
				qrCodePayReqBean.setMobile(customer.getMobile());
//				qrCodePayReqBean.setMobile("15901101057");
				qrCodePayReqBean.setName(customer.getAccountName());
				qrCodePayReqBean.setOrganizationId(orgId);
				qrCodePayReqBean.setPayPass(type);
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				String curDateStr = sdf.format(new Date());
				qrCodePayReqBean.setSendTime(curDateStr);
				qrCodePayReqBean.setTransAmt(transAmt.intValue() + "");
				qrCodePayReqBean.setTransType(paypass);
				Random random = new Random(); 
				int rannum = (int) (random.nextDouble() * (99999 - 10000 + 1)) + 10000;// 获取5位随机数  
				qrCodePayReqBean.setSendSeqId(curDateStr + rannum);
				QRCodePayService qrCodeService = new QRCodePayServiceImpl(notifyUrl);
				
				logger.info("扫码支付------请求Bean时间2：" + System.currentTimeMillis());
				logger.info("扫码支付------请求Bean：" + qrCodePayReqBean);
				QRCodePayResBean agentAuthResBean = qrCodeService.getQRCode(qrCodePayReqBean, false, "gdm", isswitch);
				logger.info("扫码支付------返回Bean：" + agentAuthResBean);
				logger.info("扫码支付------响应Bean时间2：" + System.currentTimeMillis());

				if(agentAuthResBean.getRespCode().equals("00")) {
					
					entity.setRSPCOD("00");
					entity.setRSPMSG("交易成功");
					entity.setIMGURL(agentAuthResBean.getImgUrl());
					entity.setSENDSEQID(agentAuthResBean.getSendSeqId());
					entity.setSENDTIME(agentAuthResBean.getSendTime());
					entity.setTRANSTYPE(agentAuthResBean.getTransType());
					if ("00".equals(agentAuthResBean.getRespCode())) {
						// 返回成功，存取订单信息到数据库
						dealtotalQuickService.saveOrderData(qrCodePayReqBean, agentAuthResBean, mercNo, customer.getMercName(), customer.getAgentId(), 1, rate, remarks);
					}
				} else {
					entity.setRSPCOD(agentAuthResBean.getRespCode());
					entity.setRSPMSG(agentAuthResBean.getRespDesc());
				}
			}
		
		} else {
			entity.setRSPCOD("99");
			entity.setRSPMSG("商户信息未查到！");
		}

	
		return entity;
	}

	@Override
	public BoCustomer queryCustomerInfo(String qrcode) {
		return mapper.queryCustomerInfo(qrcode);
	}
	
	 
}
