package com.midai.pay.app.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import com.midai.pay.customer.entity.BoChannel;
import com.midai.pay.customer.service.BoChannelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.midai.encry.MrAzuEncryptionUtil;
import com.midai.pay.agent.common.enums.AgentProfitTransTypeEnum;
import com.midai.pay.agent.service.AgentProfitService;
import com.midai.pay.customer.entity.BoCustomer;
import com.midai.pay.customer.service.BoCustomerService;
import com.midai.pay.dealtotal.entity.DealtotalQuick;
import com.midai.pay.dealtotal.service.DealtotalQuickService;
import com.midai.pay.dealtotal.vo.DealtotalQuickStateUpdateVo;
import com.midai.pay.mobile.service.MobileQRCodePayService;
import com.midai.reqbean.AgentAuthReqBean;
import com.midai.reqbean.QRCodePayReqBean;
import com.midai.reqbean.QRCodeSignReqBean;
import com.midai.reqbean.QueryStatusReqBean;
import com.midai.reqbean.QueryStatusResBean;
import com.midai.reqbean.ReqBean;
import com.midai.resbean.AgentAuthResBean;
import com.midai.resbean.QRCodePayResBean;
import com.midai.resbean.QRCodeSignResBean;
import com.midai.resbean.QRResultResBean;
import com.midai.resbean.ResBean;
import com.midai.service.AgentAuthService;
import com.midai.service.QRCodePayService;
import com.midai.service.impl.QBAgentAuthServiceImpl;
import com.midai.service.impl.QRCodePayServiceImpl;
import com.midai.utils.GsonUtils;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by justin on 2017/6/29.
 */
@RestController
public class QRCodePayController {

	private Logger logger = LoggerFactory.getLogger(QRCodePayController.class);

	@Value("${channel.notify.url}")
	private String notifyUrl;
	@Value("${qb.sign.isswitch}")
	private Boolean isswitch;

	private AgentAuthService agentAuthService = new QBAgentAuthServiceImpl();

	// 第一次用到的时候初始化
	private QRCodePayService qrCodeService;

	@Reference
	private BoCustomerService boCustomerService;

	@Reference
	private DealtotalQuickService dealtotalQuickService;
	
	@Reference
	private MobileQRCodePayService mobileQRCodePayService;
	
	@Reference
	private AgentProfitService agentProfitService;

	@Reference
	BoChannelService boChannelService;

	@PostMapping("/channel/auth")
	@ResponseBody
	// 商户报备申请
	public ResBean applyForAgent(@RequestBody ReqBean reqBean) {
		try {

			// 对返回的信息解密
			String deThreeEncry = URLDecoder.decode(MrAzuEncryptionUtil.getDeThreeEncry(reqBean.getData()), "UTF-8");

			// 对数据处理返回结果
			AgentAuthReqBean agentAuthReqBean = GsonUtils.fromJson(deThreeEncry, AgentAuthReqBean.class);

			// 校验组织机构号
			if (organizetionIdInvalid(agentAuthReqBean.getInstCode())) {
				return new ResBean(
						URLEncoder.encode(GsonUtils.toJson(new QRCodePayResBean("组织机构验证失败", "99")), "UTF-8"));
			}
			AgentAuthResBean agentAuthResBean = agentAuthService.applyForAgent(agentAuthReqBean);

			// 若上游返回商户报备成功，则把商户信息存入数据库
			if ("success".equals(agentAuthResBean.getStatus())) {
				boCustomerService.customerEntry(
						GsonUtils.fromJson(deThreeEncry, com.midai.pay.customer.vo.AgentAuthReqBeanVo.class));
			}
			return new ResBean(URLEncoder.encode(GsonUtils.toJson(agentAuthResBean), "UTF-8"));

		} catch (Exception e) {
			e.printStackTrace();
			try {
				return new ResBean(URLEncoder
						.encode(GsonUtils.toJson(new AgentAuthResBean("false", "insData", "数据解析失败")), "UTF-8"));
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
			return null;
		}

	}

	@PostMapping("/channel/getQRCode")
	@ResponseBody
	public ResBean getQRCode(@RequestBody ReqBean reqBean) {
		try {

			// 对返回的信息解密
			String deThreeEncry = URLDecoder.decode(MrAzuEncryptionUtil.getDeThreeEncry(reqBean.getData()), "UTF-8");

			QRCodePayReqBean qrCodePayReqBean = GsonUtils.fromJson(deThreeEncry, QRCodePayReqBean.class);
			// 校验组织机构号
			if (organizetionIdInvalid(qrCodePayReqBean.getOrganizationId())) {
				return new ResBean(
						URLEncoder.encode(GsonUtils.toJson(new QRCodePayResBean("组织机构验证失败", "99")), "UTF-8"));
			}

			if (qrCodeService == null) {
				qrCodeService = new QRCodePayServiceImpl(notifyUrl);
			}
			QRCodePayResBean agentAuthResBean = qrCodeService.getQRCode(qrCodePayReqBean, true, "gdm", isswitch);

			BoCustomer bc = boCustomerService.getCustomerByMobile(qrCodePayReqBean.getMobile());
			Double rate = 0D;
			if (bc != null) {
				if(qrCodePayReqBean.getPayPass().equals("1")) {
					rate = bc.getScanCodeWxRate();
				} else if(qrCodePayReqBean.getPayPass().equals("2")) {
					rate = bc.getScanCodeZfbRate();
				}
			} else {
				bc = new BoCustomer();
			}
			if ("00".equals(agentAuthResBean.getRespCode())) {
				// 返回成功，存取订单信息到数据库
				dealtotalQuickService.saveOrderData(qrCodePayReqBean, agentAuthResBean, bc.getMercNo(), bc.getMercName(), bc.getAgentId(), 0, rate, null);
			}

			return new ResBean(URLEncoder.encode(GsonUtils.toJson(agentAuthResBean), "UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
			try {
				return new ResBean(URLEncoder.encode(GsonUtils.toJson(new QRCodePayResBean("数据解析失败", "99")), "UTF-8"));
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
			return null;
		}

	}

	// 交易通知回调接口
	@PostMapping("/channel/onResult")
	public void data(@RequestParam("data") String data) {
		System.out.println("交易通知回调：" + data);
		logger.info("扫码交易回调：" + data);
		QRResultResBean qrResultResBean = GsonUtils.fromJson(data, QRResultResBean.class);

		// TODO mac校验
		DealtotalQuick dqQuick = new DealtotalQuick();
		dqQuick.setSeqId(qrResultResBean.getOrgSendSeqId());
		dqQuick = dealtotalQuickService.findOne(dqQuick);
		DealtotalQuickStateUpdateVo updateVo = new DealtotalQuickStateUpdateVo();
		String notifyUrl = "";
		
		updateVo.setSeqId(qrResultResBean.getOrgSendSeqId());
		updateVo.setPayResult(qrResultResBean.getPayResult());
		updateVo.setPayDesc(qrResultResBean.getPayDesc());
		updateVo.setT0RespCode(qrResultResBean.getT0RespCode());
		updateVo.setT0RespDesc(qrResultResBean.getT0RespDesc());
		notifyUrl = dealtotalQuickService.updateState(updateVo);
		
		if(qrResultResBean.getT0RespCode().equals("00")) {
			//TODO 计算代理商手刷分润信息
			agentProfitService.saveAgentSwingCardProfit(qrResultResBean.getOrgSendSeqId(), AgentProfitTransTypeEnum.TRANSTYPE_SCANWX.getCode());
		}
		
		if (notifyUrl != null) {
			// 返回数据给后台
			OkHttpClient okHttpClient = new OkHttpClient();
			FormBody formBody = new FormBody.Builder().add("data", data).build();

			Request request = new Request.Builder().url(notifyUrl).post(formBody).build();
			try {
				okHttpClient.newCall(request).execute();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	@PostMapping("/channel/signForMacKey")
	@ResponseBody
	public ResBean signForMacKey(@RequestBody ReqBean reqBean) {
		try {
			// 对返回的信息解密
			String deThreeEncry = URLDecoder.decode(MrAzuEncryptionUtil.getDeThreeEncry(reqBean.getData()), "UTF-8");

			System.out.println(deThreeEncry);
			QRCodeSignReqBean qrCodeSignReqBean = GsonUtils.fromJson(deThreeEncry, QRCodeSignReqBean.class);

			if (organizetionIdInvalid(qrCodeSignReqBean.getOrganizationId()))
				return new ResBean(
						URLEncoder.encode(GsonUtils.toJson(new QRCodeSignResBean("组织机构验证失败", "99")), "UTF-8"));

			if (qrCodeService == null) {
				qrCodeService = new QRCodePayServiceImpl(notifyUrl);
			}
			QRCodeSignResBean qrCodeSignResBean = qrCodeService.signForMacKey(qrCodeSignReqBean, isswitch);

			return new ResBean(URLEncoder.encode(GsonUtils.toJson(qrCodeSignResBean), "UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
			try {
				return new ResBean(URLEncoder.encode(GsonUtils.toJson(new QRCodeSignResBean("数据解析失败", "99")), "UTF-8"));
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
			return null;
		}

	}

	@PostMapping("/channel/queryStatus")
	@ResponseBody
	public ResBean queryStatus(@RequestBody ReqBean reqBean) {
		try {
			// 对返回的信息解密
			String deThreeEncry = URLDecoder.decode(MrAzuEncryptionUtil.getDeThreeEncry(reqBean.getData()), "UTF-8");

			System.out.println(deThreeEncry);
			QueryStatusReqBean queryStatusReqBean = GsonUtils.fromJson(deThreeEncry, QueryStatusReqBean.class);

			if (qrCodeService == null) {
				qrCodeService = new QRCodePayServiceImpl(notifyUrl);
			}
			QueryStatusResBean queryStatusResBean = qrCodeService.queryOrderStatus(queryStatusReqBean);

			return new ResBean(URLEncoder.encode(GsonUtils.toJson(queryStatusResBean), "UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
			try {
				return new ResBean(URLEncoder.encode(GsonUtils.toJson(new QRCodeSignResBean("数据解析失败", "99")), "UTF-8"));
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
			return null;
		}

	}

	private boolean organizetionIdInvalid(String organizationId) {

		if (organizationId == null) {
			return true;
		}
		if (boChannelService.getAgentIDByOrgID(organizationId) != null) {
			return false;
		}
		return true;
	}

	@GetMapping("/checkQRCode")
	@ResponseBody
	public String check() {
		return "OK";
	}

}
