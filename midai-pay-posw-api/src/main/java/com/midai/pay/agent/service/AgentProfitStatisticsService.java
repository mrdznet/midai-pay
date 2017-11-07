package com.midai.pay.agent.service;

import java.util.List;

import com.midai.framework.query.PageVo;
import com.midai.pay.agent.query.AgentProfitQuery;
import com.midai.pay.agent.vo.AgentProfitStatisticsVo;
import com.midai.pay.agent.vo.AgentProfitVo;

/**
 * 
*    
* 项目名称：midai-pay-posw-api   
* 类名称：AgentProfitStatisticsService   
* 类描述：   代理商分润统计
* 创建人：wrt   
* 创建时间：2017年7月6日 上午10:41:18   
* 修改人：wrt   
* 修改时间：2017年7月6日 上午10:41:18   
* 修改备注：   
* @version    
*
 */

public interface AgentProfitStatisticsService {

	  /** 分页 */
    public PageVo<AgentProfitVo> paginateAgentProfit(AgentProfitQuery query, boolean flag);

    /** 根据父代理商查找代理商分润 */
	public List<AgentProfitVo> findListByParentAgentId(AgentProfitVo pv);

	/** 根据清算日期和代理商编号更新代理商分润 */
	public Integer updateAgentProfit(List<AgentProfitVo> voList);

	/** 根据查询条件导出数据 */
	public List<AgentProfitVo> queryListByQuery(AgentProfitQuery query);

	public AgentProfitStatisticsVo queryAgentProfitStatisticsInfo(AgentProfitQuery query);

}
