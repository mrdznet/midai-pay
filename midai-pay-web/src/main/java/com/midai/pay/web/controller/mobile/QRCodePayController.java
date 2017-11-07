package com.midai.pay.web.controller.mobile;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.midai.pay.customer.entity.BoCustomer;
import com.midai.pay.customer.service.BoCustomerService;
import com.midai.pay.dealtotal.service.DealtotalQuickService;
import com.midai.pay.mobile.ScanAppBaseEntity;
import com.midai.pay.mobile.service.MobileQRCodePayService;
import com.midai.pay.web.controller.vo.MiFuScanPayVo;
import com.midai.pay.web.controller.vo.MiFuScanReqVo;
import com.midai.pay.web.vo.qrcode.CustomerQrCodePayVo;

import io.swagger.annotations.ApiOperation;

/**
 * Created by justin on 2017/6/29.
 */
@RestController
public class QRCodePayController {

	private Logger logger = LoggerFactory.getLogger(QRCodePayController.class);

	@Value("${channel.notify.url}")
	private String notifyUrl;

	@Reference
	private BoCustomerService boCustomerService;

	@Reference
	private DealtotalQuickService dealtotalQuickService;
	
	@Reference
	private MobileQRCodePayService mobileQRCodePayService;



	@PostMapping("/mifu/mobile/scanhandler")
	@ResponseBody
	public ResponseEntity<ScanAppBaseEntity> transScanHandler(@RequestHeader("User-Agent") String userAgent,
			@RequestBody MiFuScanReqVo reqBean) {

		logger.info("扫码手机接口请求时间：" + System.currentTimeMillis());
		ScanAppBaseEntity entity = new ScanAppBaseEntity();
		//GSON禁止转义JSON等号大括号字符串
		Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
		String returnValue = "";
		logger.info("扫码支付------商户编码：" + reqBean.getMerNo() + "------------交易金额：" 
					+ reqBean.getTransAmt() + "--------数据来源：" + userAgent);
		try {
			if (StringUtils.isNotEmpty(reqBean.getMerNo()) && reqBean.getTransAmt() != null) {

				if (reqBean.getTransAmt() < 1) {
					
					entity.setRSPCOD("99");
					entity.setRSPMSG("交易金额不能小于1元！");
				} else {
					
					entity = mobileQRCodePayService.transScanHandler(userAgent, reqBean.getMerNo(), reqBean.getTransAmt(), reqBean.getRemarks() , notifyUrl);
				}
				logger.info("扫码手机接口响应时间：" + System.currentTimeMillis());
			} else {
				entity.setRSPCOD("99");
				entity.setRSPMSG("数据异常，请检查！");
			}
		} catch (Exception e) {
			logger.error("扫码信息异常！", e);
			entity.setRSPCOD("99");
			entity.setRSPMSG("数据异常，请检查！");
		}
		returnValue = gson.toJson(entity);
		logger.info("响应值:" + returnValue);
		ResponseEntity<ScanAppBaseEntity> responseEntity = new ResponseEntity<ScanAppBaseEntity>(entity, HttpStatus.OK);
		return responseEntity;
	}
	
	@ApiOperation("商户二维码信息查询")
	@PostMapping("/mifu/mobile/custinfo")
	@ResponseBody
	public ResponseEntity<ScanAppBaseEntity> queryCustomerInfo(@RequestBody MiFuScanPayVo pv) {
		
		ScanAppBaseEntity entity = new ScanAppBaseEntity();
		String returnValue = "";
		Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
		if(pv != null && StringUtils.isNotEmpty(pv.getQrcode())) {
			
			try {
				BoCustomer customer = mobileQRCodePayService.queryCustomerInfo(pv.getQrcode());
				if(StringUtils.isNotEmpty(customer.getMercNo()) && StringUtils.isNotEmpty(customer.getMercName())) {
					
					entity.setMERCNO(customer.getMercNo());
					entity.setMERCNAME(customer.getMercName());
				} else {
					entity.setRSPCOD("99");
					entity.setRSPMSG("二维码未绑定商户！");
				}
			} catch (Exception e) {
				logger.error("商户信息查询异常！", e);
				entity.setRSPCOD("99");
				entity.setRSPMSG("数据异常，请检查！");
			}
			entity.setRSPCOD("00");
			entity.setRSPMSG("查询成功！");
		} else {
			entity.setRSPCOD("99");
			entity.setRSPMSG("参数不能为空！");
		}
		
		returnValue = gson.toJson(entity);
		logger.info("响应值:" + returnValue);
		ResponseEntity<ScanAppBaseEntity> responseEntity = new ResponseEntity<ScanAppBaseEntity>(entity, HttpStatus.OK);
		return responseEntity;
	}
}
