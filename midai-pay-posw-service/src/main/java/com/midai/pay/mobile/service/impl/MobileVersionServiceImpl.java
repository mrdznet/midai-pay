package com.midai.pay.mobile.service.impl;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.midai.pay.mobile.AppBaseEntity;
import com.midai.pay.mobile.entity.MobileVersionEntity;
import com.midai.pay.mobile.service.MobileVersionService;
import com.midai.pay.user.entity.Mobileversion;
import com.midai.pay.user.service.MobileversionService;




/**   
*    
* 项目名称：midai-pay-posw-service   
* 类名称：MobileVersionServiceImpl   
* 类描述：   
* 创建人：wrt   
* 创建时间：2016年11月1日 上午10:13:56   
* 修改人：wrt   
* 修改时间：2016年11月1日 上午10:13:56   
* 修改备注：   
* @version    
*    
*/
@Service
public class MobileVersionServiceImpl implements MobileVersionService {

	private Logger logger = LoggerFactory.getLogger(MobileVersionServiceImpl.class);

	@Reference
	private MobileversionService mss;
	@Override
	public Object checkMobileVersion(String content) {
		
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
			if (!json.has("IP") || !json.has("os") || !json.has("version") || !json.has("type") ) {
				return new AppBaseEntity("01", "参数定义错误");
			} 
			
			logger.info("###################### IP:" + json.getString("IP") + "###############################");
			String version = json.getString("version");
			String device = json.getString("os");
			String typeStr = json.getString("type");
			
			//判断版本号是否为空
			if(StringUtils.isEmpty(version)) {
				return new AppBaseEntity("02", "版本号不能为空");
			} 
			if(StringUtils.isEmpty(device)) {
				return new AppBaseEntity("02", "类型不能为空");
			} 
			
			if(StringUtils.isEmpty(typeStr)) {
				return new AppBaseEntity("02", "系统类型不能为空");
			}
			Integer type = Integer.parseInt(typeStr);
			
			
			//为了修复ios的更新遗留问题，进行修改
			if("IOS".equalsIgnoreCase(device)&&
				("2.2".equalsIgnoreCase(version)||"2.1".equalsIgnoreCase(version))){
				type=3;
			}
			
			Mobileversion mv = mss.getLastVersion(device, type);
			if(null != mv && StringUtils.isNotEmpty(mv.getVersionId())){
//				String appVersion = version.replace(".", "");//当前APP版本号
//				String servicerVersion = mv.getVersionId().replace(".", "");//服务器APP最新版本号
				MobileVersionEntity entity = new MobileVersionEntity();
				entity.setAndroidFlag(mv.getAndroidFlag());
				entity.setAndroidLoadURL(mv.getAndroidLoadUrl());
				entity.setContents(mv.getContents());
				entity.setIosFlag(mv.getIosFlag());
				entity.setIosLoadURL(mv.getIosLoadUrl());
				entity.setVersionID(mv.getVersionId());
				entity.setVersionSize(mv.getVersionSize());
				entity.setRSPCOD("00");
				entity.setRSPMSG("成功");
				return entity;
					
			} else {
				pcode = "99";
				pmsg = "未找到版本信息";
			}
			
		}
		
	
		return new AppBaseEntity(pcode, pmsg);
	}


}
