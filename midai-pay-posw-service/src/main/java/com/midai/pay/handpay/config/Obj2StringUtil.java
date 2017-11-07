package com.midai.pay.handpay.config;

import org.apache.commons.lang3.StringUtils;

import com.midai.pay.handpay.vo.InstiMerchantRequestBean;
import com.midai.pay.handpay.vo.TransQueryRequestBean;
import com.midai.pay.handpay.vo.WithdrawRequestBean;

public class Obj2StringUtil {
	
	public static String obj2StringOfRegister(InstiMerchantRequestBean bean, String signKey){
		if(null == bean) return null;
		
		StringBuffer sb = new StringBuffer();
		sb.append(bean.getMerSysId() + "|");
		sb.append(bean.getMerName() + "|");
		sb.append(bean.getRealName() + "|");
		sb.append(bean.getMerState() + "|");
		sb.append(bean.getMerCity() + "|");
		sb.append(bean.getMerAddress() + "|");
		sb.append(bean.getCertType() + "|");
		sb.append(bean.getCertId() + "|");
		sb.append(bean.getMobile() + "|");
		sb.append(bean.getAccountId() + "|");
		sb.append(bean.getAccountName() + "|");
		sb.append(bean.getBankName() + "|");
		sb.append(bean.getBankCode() + "|");
		
		if(StringUtils.isNotEmpty(bean.getOpenBankName())) sb.append(bean.getOpenBankName() + "|");
		else sb.append(" " + "|");
		
		if(StringUtils.isNotEmpty(bean.getOpenBankState())) sb.append(bean.getOpenBankState() + "|");
		else sb.append(" " + "|");
		
		if(StringUtils.isNotEmpty(bean.getOpenBankCity())) sb.append(bean.getOpenBankCity() + "|");
		else sb.append(" " + "|");
		
		sb.append(bean.getT0drawFee() + "|");
		sb.append(bean.getT0drawRate() + "|");
		sb.append(bean.getT1consFee() + "|");
		sb.append(bean.getT1consRate() + "|");
		
		sb.append(signKey);
		
		return sb.toString();
	}
	
	public static String obj2StringOfWithdraw(WithdrawRequestBean bean, String signKey){
		if(null == bean) return null;
		
		StringBuffer sb = new StringBuffer();
		sb.append(bean.getMerSysId() + "|");
		sb.append(bean.getSubMerId() + "|");
		sb.append(bean.getOrderNo() + "|");
		sb.append(bean.getTransDate() + "|");
		sb.append(bean.getTransAmount() + "|");
		
		sb.append(signKey);
		
		return sb.toString();
	}
	
	public static String obj2StringOfTransQuery(TransQueryRequestBean bean, String signKey){
		if(null == bean) return null;
		
		StringBuffer sb = new StringBuffer();
		sb.append(bean.getMerSysId() + "|");
		sb.append(bean.getSubMerId() + "|");
		sb.append(bean.getOrderNo() + "|");
		sb.append(bean.getTransDate() + "|");
		sb.append(bean.getTransSeq() + "|");
		
		sb.append(signKey);
		
		return sb.toString();
	}
	
}
