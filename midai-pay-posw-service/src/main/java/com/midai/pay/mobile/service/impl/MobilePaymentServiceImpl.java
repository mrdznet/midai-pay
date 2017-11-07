package com.midai.pay.mobile.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.dubbo.config.annotation.Service;
import com.midai.pay.mobile.AppBaseEntity;
import com.midai.pay.mobile.service.MobilePaymentService;


/**   
*    
* 项目名称：midai-pay-posw-service   
* 类名称：MobilePaymentServiceImpl   
* 类描述：   
* 创建人：wrt   
* 创建时间：2016年11月11日 下午4:53:55   
* 修改人：wrt   
* 修改时间：2016年11月11日 下午4:53:55   
* 修改备注：   
* @version    
*    
*/
@Service
public class MobilePaymentServiceImpl implements MobilePaymentService {

	private Logger logger = LoggerFactory.getLogger(MobilePaymentServiceImpl.class);

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
		if (!isErr) {
			// 判断传人参数
			if (!json.has("PHONENUMBER")) {
				return new AppBaseEntity("01", "参数定义错误");
			}

			Map<String, Object> map = new HashMap<String, Object>();
			Map<String, String> maps = new HashMap<String, String>();
			maps.put("PAYID", "6");
			maps.put("PAYNAME", "");
			
			map.put("LISTS", maps);
			map.put("RSPCOD", "00");
			map.put("RSPMSG", "读取支付方式成功！");
			return map;
		}
		return new AppBaseEntity(pcode, pmsg);
	}

}
