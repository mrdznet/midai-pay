package com.midai.pay.mobile.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.midai.pay.agent.common.enums.AgentInsCodeEnum;
import com.midai.pay.agent.entity.Agent;
import com.midai.pay.agent.service.AgentService;
import com.midai.pay.dict.entity.Dict;
import com.midai.pay.dict.service.BoDictService;
import com.midai.pay.mobile.service.MobileRateQuotaService;




/**   
*    
* 项目名称：midai-pay-posw-service   
* 类名称：MobileRateQuotaServiceImpl   
* 类描述：   
* 创建人：wrt   
* 创建时间：2016年11月11日 下午1:57:31   
* 修改人：wrt   
* 修改时间：2016年11月11日 下午1:57:31   
* 修改备注：   
* @version    
*    
*/
@Service
public class MobileRateQuotaServiceImpl implements MobileRateQuotaService {

	private Logger logger = LoggerFactory.getLogger(MobileRateQuotaServiceImpl.class);
	
	@Reference
	private AgentService as;
	@Reference
	private BoDictService bds;

	@Override
	public Object mchntRateQuota(String value) {
		Map<String, String> map = new HashMap<String, String>();
		Agent agent = as.getTopAgentByNameAndParentId(AgentInsCodeEnum.INSCODE_HB.getInsCode(), 0);
		
		Dict dict = bds.getDictByInsCode(AgentInsCodeEnum.INSCODE_HB.getInsCode());
		map.put("DAILYLIMIT", dict.getDailylimit()==null?"":dict.getDailylimit().toString());//日限额
		map.put("SINGLELIMIT",dict.getSinglelimit()==null?"":dict.getSinglelimit().toString());//单笔限额
		map.put("CARDLIMIT", dict.getCardlimit()==null?"":dict.getCardlimit().toString());//单卡最高限额
//		map.put("FEERATE", agent==null?"":agent.getMerchantRate()==null?"":agent.getMerchantRate().toString());//商户扣率
		map.put("FEERATE", "0.6%+3");//商户扣率
		map.put("REMARK0", dict==null?"":dict.getRemark()==null?"":dict.getRemark());//单卡最高限额
		map.put("REMARK1", dict==null?"":dict.getRemark1()==null?"":dict.getRemark1());//单卡最高限额
		map.put("RSPCOD", "00");
		map.put("RSPMSG", "获取费率限额信息成功");
		return map;
	}

	

}
