package com.midai.pay.mobile.service.impl;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.midai.pay.mobile.AppBaseEntity;
import com.midai.pay.mobile.service.MobileOSSService;
import com.midai.pay.oss.PayOssService;



@Service
public class MobileOSSServiceImpl implements MobileOSSService {

	private Logger logger = LoggerFactory.getLogger(MobileOSSServiceImpl.class);
	
	@Reference
	private PayOssService ossService;

	@Override
	public Object getOSSDocument(String content) {
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
				String mobile =json.getString("PHONENUMBER");
				if(StringUtils.isEmpty(mobile)) {
					pcode = "02";
					pmsg = "参数不允许为空";
				} else {
					
					Map<String, String> map = ossService.appUploadFile(mobile);
					map.put("RSPCOD", "00");
					map.put("RSPMSG", "成功");
					return map;
				}
			}
		}
		return new AppBaseEntity(pcode, pmsg);
	}


}
