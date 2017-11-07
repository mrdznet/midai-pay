package com.midai.pay.mobile.service.impl;

import java.util.Date;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.midai.pay.customer.entity.BoCustomer;
import com.midai.pay.customer.service.BoCustomerService;
import com.midai.pay.mobile.AppBaseEntity;
import com.midai.pay.mobile.service.MobileSuggestionService;
import com.midai.pay.suggest.entity.BoSuggestEntity;
import com.midai.pay.suggest.mapper.BoSuggestMapper;



/**   
*    
* 项目名称：midai-pay-posw-service   
* 类名称：MobileCCBDServiceImpl   
* 类描述：   
* 创建人：wrt   
* 创建时间：2016年10月31日 下午4:35:07   
* 修改人：wrt   
* 修改时间：2016年10月31日 下午4:35:07   
* 修改备注：   
* @version    
*    
*/
@Service
public class MobileSuggestionServiceImpl implements MobileSuggestionService {

	private Logger logger = LoggerFactory.getLogger(MobileSuggestionServiceImpl.class);

	@Autowired
	private BoSuggestMapper mapper;
	
	@Reference
	private  BoCustomerService bcs;

	@Override
	public Object submitNotice(String content) {
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
		if (!isErr) {
			// 判断传人参数
			if (!json.has("PHONENUMBER")||!json.has("CONTENT")) {
				pcode = "01";
				pmsg = "参数定义错误";
			} else {
				String userId=json.getString("PHONENUMBER").trim();
				String advicecontent =json.getString("CONTENT").trim();
				//提交内容不允许为空
				if(StringUtils.isEmpty(userId)) {
					pcode = "02";
					pmsg = "手机号码不能为空";
				} else if(StringUtils.isEmpty(advicecontent)){
					pcode = "02";
					pmsg = "提交内容不允许为空";
				} else {
					BoCustomer bc = bcs.getCustomerByMobile(userId);
					String mercName = "";
					if(bc == null || StringUtils.isEmpty(bc.getMercName())){
						mercName="匿名提交";
					}
					BoSuggestEntity bse = new BoSuggestEntity();
					bse.setAdvicePhone(userId);
					bse.setAdviceName(mercName);
					bse.setAdviceContent(advicecontent);
					//创建意见反馈工作流
					 String inscode="101002";
					 if(json.has("INSCODE")){
						  inscode=json.getString("INSCODE");
					 }
					  String createuser="";
					 if(inscode.equals("101002")){
						 createuser="admin";
					 }
					 if(inscode.equals("101003")){
						 createuser="haibei";
					 }
					 if(inscode.equals("101004")){
						 createuser="zhangfu";
					 }
					 bse.setCreateUser(createuser);
					 bse.setCreateTime(new Date());
					 bse.setUpdateTime(bse.getCreateTime());
					 if(mapper.insert(bse) > 0) {
						 pcode = "00";
						 pmsg = "提交成功！";
					 } else {
						 pcode = "03";
						 pmsg = "提交失败！";
						 
					 }
				}
				
			}
		}
		return new AppBaseEntity(pcode, pmsg);
	}

}
