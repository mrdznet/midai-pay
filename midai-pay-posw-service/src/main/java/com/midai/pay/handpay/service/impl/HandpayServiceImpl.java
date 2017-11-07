package com.midai.pay.handpay.service.impl;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.midai.framework.common.po.ResultVal;
import com.midai.pay.handpay.config.HttpUtil;
import com.midai.pay.handpay.config.Md5Utils;
import com.midai.pay.handpay.config.Obj2StringUtil;
import com.midai.pay.handpay.entity.HyTztxEntity;
import com.midai.pay.handpay.service.HandpayService;
import com.midai.pay.handpay.service.HyTztxService;
import com.midai.pay.handpay.vo.InstiMerchantRequestBean;
import com.midai.pay.handpay.vo.InstiMerchantResponseBean;
import com.midai.pay.handpay.vo.TransQueryRequestBean;
import com.midai.pay.handpay.vo.TransQueryResponseBean;
import com.midai.pay.handpay.vo.WithdrawRequestBean;
import com.midai.pay.handpay.vo.WithdrawResponseBean;
import com.midai.pay.posp.service.PospService;

@Service
public class HandpayServiceImpl implements HandpayService {

	private Logger logger = LoggerFactory.getLogger(HandpayServiceImpl.class);
	
	private static final String SUCCESS_CODE = "00";
	private static final String OTHER_SUCCESS_CODE = "Z5";

	@Value("${handpay.signKey}")
	private String signKey;
	@Value("${handpay.merSysId}")
	private String merSysId;
	
	@Value("${handpay.url.regist}")
	private String registUrl;
	@Value("${handpay.url.update}")
	private String updateUrl;
	@Value("${handpay.url.apply}")
	private String applyUrl;
	@Value("${handpay.url.query}")
	private String queryUrl;
	
	@Reference
	private PospService  pospService;
	
	@Autowired
	private HyTztxService hyTztxService;
	
	@Override
	public ResultVal<InstiMerchantResponseBean> instiMerchant(String flag, InstiMerchantRequestBean req) {
		req.setMerSysId(merSysId);
		String signData = Obj2StringUtil.obj2StringOfRegister(req, signKey);
		
		try {
			String signature = Md5Utils.md5Common(signData);
			req.setSignature(signature);
			String url = registUrl;
			if(StringUtils.isNotEmpty(flag) && flag.equals("up")) {
				url = updateUrl;
			}
			logger.info("吉点请求消息：" + new Gson().toJson(req));
			String response = HttpUtil.postForm(url, req);
			logger.info("吉点相应消息：" + response);
			InstiMerchantResponseBean resp = new Gson().fromJson(response, InstiMerchantResponseBean.class);
			
			logger.info("吉点商户入网, mobile:"+req.getMobile()+",code:" + resp.getCode() +", msg:" + resp.getMsg());
			if(SUCCESS_CODE.equals(resp.getCode())){
				 // 终端号 商户号的后8位
				String subMerId = resp.getSubMerId();
				String mercCode = subMerId.substring(subMerId.length()-8, subMerId.length());
				resp.setTsn(mercCode);
				
				if(StringUtils.isEmpty(flag) ||  !flag.equals("up")) {
					
					//签到
					boolean sign = pospService.signToJidian(subMerId, mercCode);
					if(!sign) resp=null;
				}
				
				return ResultVal.SUCCESS(resp);	
			}else{
				logger.error("吉点商户入网错误");
				logger.error("code:" + resp.getCode() +", msg:" + resp.getMsg());
				return ResultVal.FAIL(-1, resp.getMsg());
			}
		} catch (IOException | NoSuchAlgorithmException e) {
			logger.error("商户进件请求错误");
			logger.error(e.toString());
			return ResultVal.FAIL(-1, "商户进件请求错误");
		}
	}
	
	@Override
	public String t0WithdrawStr(WithdrawRequestBean req) {
		req.setMerSysId(merSysId);
		String signData = Obj2StringUtil.obj2StringOfWithdraw(req, signKey);
		String response = "";
		try {
			String signature = Md5Utils.md5Common(signData);
			req.setSignature(signature);
			response = HttpUtil.postForm(applyUrl, req);
		} catch (Exception e) {
			logger.error("吉点T0 提现申请错误");
			logger.error(e.toString());
			
			return "fail";
		}
		return response;
	}

	@Override
	public ResultVal<WithdrawResponseBean> t0Withdraw(WithdrawRequestBean req) {
		
		String result = t0WithdrawStr(req);
		if(result.equals("fail")) {
			return ResultVal.FAIL(-1, "吉点T0 提现申请错误");
		}
		
		WithdrawResponseBean resp = new Gson().fromJson(result, WithdrawResponseBean.class);
		
		//调用吉点T0提现请求与响应日志
		HyTztxEntity entity = new HyTztxEntity();
		entity.setCreateTime(new Date());
		entity.setHostTramsSsn(req.getOrderNo());
		ObjectMapper objectMapper = new ObjectMapper(); 
		String reqData = "";
		try {
			reqData = objectMapper.writeValueAsString(req);
		} catch (JsonProcessingException e) {
			logger.info("返回结果JSON转化失败");
		}
		entity.setReqData(reqData);
		entity.setRespData(result);
		hyTztxService.insert(entity);
		
		logger.error("吉点T0 提现, 商户号:" + req.getSubMerId()+", code: "+resp.getCode()+",msg:" + resp.getMsg());
		if(OTHER_SUCCESS_CODE.equals(resp.getCode()) || SUCCESS_CODE.equals(resp.getCode())){
			logger.info("吉点T0 提现申请成功");
			return ResultVal.SUCCESS(resp);
		}else{
			logger.info("吉点T0 提现申请失败, 错误信息：" + resp.getMsg());
			return ResultVal.FAIL(-1, resp.getMsg());
		}
		
	}

	@Override
	public ResultVal<TransQueryResponseBean> transQuery(TransQueryRequestBean req) {
		
		String result = tranQueryTransQueryResponseBean(req);
		if(result.equals("fail")) {
			return ResultVal.FAIL(-1, "T+0 提现交易查询请求错误"); 
		}
		
		TransQueryResponseBean resp = new Gson().fromJson(result, TransQueryResponseBean.class);
		
		if(SUCCESS_CODE.equals(resp.getCode())){
			return ResultVal.SUCCESS(resp);
		}else{
			return ResultVal.FAIL(-1, resp.getMsg());
		}
	
	}
	
	@Override
	public String tranQueryTransQueryResponseBean(TransQueryRequestBean req) {
		req.setMerSysId(merSysId);
		String signData = Obj2StringUtil.obj2StringOfTransQuery(req, signKey);
		
		String response  = null;
		try {
			String signature = Md5Utils.md5Common(signData);
			req.setSignature(signature);
			response = HttpUtil.postForm(queryUrl, req);
		} catch (NoSuchAlgorithmException | IOException e) {
			logger.error("T+0 提现交易查询请求错误");
			logger.error(e.toString());
			return "fail";
		}
		
		return response;
		
	}
	
}
