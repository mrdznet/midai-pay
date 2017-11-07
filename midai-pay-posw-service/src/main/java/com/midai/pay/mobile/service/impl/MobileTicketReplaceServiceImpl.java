package com.midai.pay.mobile.service.impl;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.midai.pay.mobile.AppBaseEntity;
import com.midai.pay.mobile.entity.MobileTicketEntity;
import com.midai.pay.mobile.service.MobileTicketReplaceService;
import com.midai.pay.trade.service.TradeReviewService;



@Service
public class MobileTicketReplaceServiceImpl implements MobileTicketReplaceService {

	private Logger logger = LoggerFactory.getLogger(MobileTicketReplaceServiceImpl.class);
	
	@Reference
	private TradeReviewService trrs;

	@Override
	public Object excuteQuery(String content) {
		String pcode = "";
		String pmsg = "";
		JSONObject json = null;
		boolean isErr = true;
		try {
			json = new JSONObject(content);
			isErr = false;
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.getMessage());
		} 
		if(!isErr) {
			//判断传人参数
			if(!json.has("PHONENUMBER")){
				pcode = "01";
				pmsg = "参数定义错误";
			} else {
				String mobile = json.getString("PHONENUMBER");
				MobileTicketEntity entity = new MobileTicketEntity();
				entity.setLISTS(trrs.findAllReSign(mobile));
				entity.setRSPCOD("00");
				entity.setRSPMSG("查询数据成功！");
				return entity;
			}
		}
		return new AppBaseEntity(pcode, pmsg);
	}

	@Override
	public Object excute(String content) {
		String pcode = "";
		String pmsg = "";
		JSONObject json = null;
		boolean isErr = true;
		try {
			json = new JSONObject(content);
			isErr = false;
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.getMessage());
		} 
		if(!isErr) {
			//判断传人参数
			if(!json.has("PHONENUMBER")){
				pcode = "01";
				pmsg = "参数定义错误";
			} else {
				String logno=json.getString("LOGNO");
				String mobile=json.getString("PHONENUMBER");
				if(trrs.tickReSign(logno, mobile)) {
					pcode = "00";
					pmsg = "更新签名状态成功！";
				} else {
					pcode = "99";
					pmsg = "数据异常，请检查！";
				}
				
			}
		}
		return new AppBaseEntity(pcode, pmsg);
	}


}
