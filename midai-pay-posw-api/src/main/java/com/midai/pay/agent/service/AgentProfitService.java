package com.midai.pay.agent.service;

import com.midai.framework.common.BaseService;
import com.midai.pay.agent.entity.AgentProfit;

/**
 * 
*    
* 项目名称：midai-pay-posw-api   
* 类名称：AgentProfitService   
* 类描述：   代理商分润Service
* 创建人：wrt   
* 创建时间：2017年7月5日 上午11:10:15   
* 修改人：wrt   
* 修改时间：2017年7月5日 上午11:10:15   
* 修改备注：   
* @version    
*
 */

public interface AgentProfitService extends BaseService<AgentProfit> {

	/**
	 * 计算代理商手刷分润信息
	 * @param hostTramsSsn
	 * @return
	 */
	int saveAgentSwingCardProfit(String hostTramsSsn, Integer transType);


}