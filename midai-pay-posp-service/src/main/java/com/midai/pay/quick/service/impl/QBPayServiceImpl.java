package com.midai.pay.quick.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.midai.pay.customer.entity.BoCustomer;
import com.midai.pay.customer.service.BoCustomerService;
import com.midai.pay.posp.entity.SeqEntity;
import com.midai.pay.posp.mapper.InstMapper;
import com.midai.pay.quick.qb.common.util.HttpClientUtil;
import com.midai.pay.quick.qb.common.util.ZFBHttpClientUtil;
import com.midai.pay.quick.qb.entity.InstMercMacEntity;
import com.midai.pay.quick.qb.entity.InstQuickConfigEntity;
import com.midai.pay.quick.qb.entity.InstQuickSendReceiveEntity;
import com.midai.pay.quick.qb.entity.QBQrCodePayReq;
import com.midai.pay.quick.qb.entity.QBQrCodePayResp;
import com.midai.pay.quick.qb.entity.QBQrCodeResultEntity;
import com.midai.pay.quick.qb.service.InstMercMacService;
import com.midai.pay.quick.qb.service.InstQuickConfigService;
import com.midai.pay.quick.qb.service.InstQuickSendReceiveService;
import com.midai.pay.quick.qb.service.QBPayService;

import net.sf.json.JSONObject;

@Service
public class QBPayServiceImpl implements QBPayService {

	private Logger logger = LoggerFactory.getLogger(QBPayServiceImpl.class);
	public static final String ERROR_CODE = "99";
	public static final String SUCCESS_CODE = "00";

	@Reference
	private BoCustomerService boCustomerService;
	@Reference
	private InstMercMacService instMercMacService;
	@Reference
	private InstQuickConfigService instQuickConfigService;
	@Reference
	private InstQuickSendReceiveService instQuickSendReceiveService;
	@Autowired
	private InstMapper instMapper;
	@Value("${quick.pay.notifyUrl}")
	private String notifyUrl;

	@Override
	public QBQrCodeResultEntity generateQBQRCode(QBQrCodePayReq param) {

		QBQrCodeResultEntity respEntity = new QBQrCodeResultEntity();
		// validate
		Assert.notNull(param);
		Assert.hasText(param.getMobile());
		Assert.hasText(param.getTransAmt());

		Double transAmt = 0D;
		try {
			transAmt = Double.parseDouble(param.getTransAmt());
		} catch (NumberFormatException e) {
			respEntity.setRSPCOD(ERROR_CODE);
			respEntity.setRSPMSG("金额不规范");
			logger.error("金额转换失败", e);
			return respEntity;
		}

		if (transAmt <= 0) {
			respEntity.setRSPCOD(ERROR_CODE);
			respEntity.setRSPMSG("金额不能小于等于0");
		}

		String phonenumber = param.getMobile();
		BoCustomer bocustomer = new BoCustomer();
		bocustomer.setMobile(phonenumber);
		bocustomer = boCustomerService.findOne(bocustomer);
		if (bocustomer == null) {
			// 商户不存在
			respEntity.setRSPCOD(ERROR_CODE);
			respEntity.setRSPMSG("商户不存在");
			return respEntity;
		}

		if (bocustomer.getState() != 4 && bocustomer.getState() != 5 && bocustomer.getState() != 6
				&& bocustomer.getState() != 7) {
			// 商户未实名认证
			respEntity.setRSPCOD(ERROR_CODE);
			respEntity.setRSPMSG("商户未实名认证");
			return respEntity;
		}

		//TODO 路由
		String instMercId = "501000000001";
		if(StringUtils.isEmpty(instMercId)){
			respEntity.setRSPCOD(ERROR_CODE);
			respEntity.setRSPMSG("通道选择异常");
			return respEntity;
		}
		
		InstMercMacEntity instMercMac = new InstMercMacEntity();
		instMercMac.setInstCode(instMercId);
		instMercMac.setMercId(bocustomer.getMercId());
		instMercMac = instMercMacService.findOne(instMercMac);
		
		if(StringUtils.isEmpty(instMercMac)){
			respEntity.setRSPCOD(ERROR_CODE);
			respEntity.setRSPMSG("获取商户密文异常");
			return respEntity;
		}
		
		//生成此通道的下个交易号(db函数生成)
		SeqEntity seqEntity = new SeqEntity();
		seqEntity.setInstCode(instMercId);
		String instSeq = instMapper.instSeqNextval(seqEntity);
		
		//过去快捷支付通道配置信息

		InstQuickConfigEntity iqcEntity = new InstQuickConfigEntity();
		iqcEntity.setInstCode(instMercId);
		iqcEntity.setPayPass(param.getPayPass());
		iqcEntity = instQuickConfigService.findOne(iqcEntity);
		
		if(iqcEntity == null) {
			respEntity.setRSPCOD(ERROR_CODE);
			respEntity.setRSPMSG("获取通道配置信息异常");
			return respEntity;
		}
		//获取二维码信息
		param.setSendSeqId(instSeq);
		param.setTransType(iqcEntity.getTransType());
		param.setOrganizationId(iqcEntity.getOrganizationId());
		param.setFee(bocustomer.getSingleFee() + "");
		param.setCardNo(bocustomer.getAccountNo());
		param.setName(bocustomer.getAccountName());
		param.setIdNum(bocustomer.getIdCard());
		param.setNotifyUrl(notifyUrl);
		JSONObject json = JSONObject.fromObject(param);
		
		String makeMac = ZFBHttpClientUtil.makeMac(json.toString(), instMercMac.getMackey());
		System.out.println("mac:" + makeMac);
		param.setMac(makeMac);
		
		Map<String, String> condition = new HashMap<String, String>();
		json = JSONObject.fromObject(param);
		condition.put("data", json.toString());
		
		//快捷支付接口传送数据
		InstQuickSendReceiveEntity iqsre = new InstQuickSendReceiveEntity();
		iqsre.setInstCode(instMercMac.getInstCode());
		iqsre.setReqMsg(json.toString());
		Integer iqsreId = instQuickSendReceiveService.insert(iqsre);
		iqsre.setId(iqsreId);
		String resulta = HttpClientUtil.post(iqcEntity.getUrl(), condition);
		if(StringUtils.isEmpty(resulta)) {
			respEntity.setRSPCOD(ERROR_CODE);
			respEntity.setRSPMSG("获取二维码信息异常");
			return respEntity;
		}
		iqsre.setRespMsg(resulta);
		instQuickSendReceiveService.update(iqsre);
		
		ObjectMapper mapper = new ObjectMapper();
		QBQrCodePayResp qbResp = new QBQrCodePayResp();
		try {
			qbResp = mapper.readValue(resulta, QBQrCodePayResp.class);
		} catch (IOException e) {
			logger.error("相应转换失败", e);
			respEntity.setRSPCOD(ERROR_CODE);
			respEntity.setRSPMSG("转换相应信息失败");
			return respEntity;
		}
		
		System.out.println("resulta :" + resulta);
		respEntity.setRSPCOD("00");
		respEntity.setRSPMSG("成功");
		respEntity.setIMGURL(qbResp.getImgUrl());
		return respEntity;
	}

}
