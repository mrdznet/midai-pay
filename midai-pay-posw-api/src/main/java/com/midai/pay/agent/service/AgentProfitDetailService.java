package com.midai.pay.agent.service;

import com.midai.framework.query.PageVo;
import com.midai.pay.agent.query.AgentProfitQuery;
import com.midai.pay.agent.vo.AgentProfitStatisticsVo;
import com.midai.pay.agent.vo.AgentProfitVo;

/**
 * 
*    
* 项目名称：midai-pay-posw-api   
* 类名称：AgentProfitDetailService   
* 类描述：   代理商分润统计
* 创建人：wrt   
* 创建时间：2017年7月6日 上午10:41:18   
* 修改人：wrt   
* 修改时间：2017年7月6日 上午10:41:18   
* 修改备注：   
* @version    
*
 */

public interface AgentProfitDetailService {

	  /** 分页 */
    public PageVo<AgentProfitVo> paginateAgentProfitDetail(AgentProfitQuery query, String exp);

	public AgentProfitStatisticsVo queryAgentProfitDetailInfo(AgentProfitQuery query, String exp);

}
