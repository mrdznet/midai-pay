package com.midai.pay.mobile.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.dubbo.config.annotation.Service;
import com.midai.pay.mobile.service.FreezeMoneyService;



/**   
*    
* 项目名称：midai-pay-posw-service   
* 类名称：FreezeMoneyServiceImpl   
* 类描述：   
* 创建人：wrt   
* 创建时间：2016年11月3日 下午4:26:51   
* 修改人：wrt   
* 修改时间：2016年11月3日 下午4:26:51   
* 修改备注：   
* @version    
*    
*/
@Service
public class FreezeMoneyServiceImpl implements FreezeMoneyService {

	private Logger logger = LoggerFactory.getLogger(FreezeMoneyServiceImpl.class);

	@Override
	public Object freeze(String value) {
		 Map<String, String> map = new HashMap<String, String>();
	     map.put("PAYSTATE", "2");
	     map.put("SUPERSTATE", "3");
		 map.put("RSPCOD", "00");
		 map.put("RSPMSG", "冻结合约机金额成功！");
		return map;
	}

}
