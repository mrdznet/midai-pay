package com.midai.pay.dealtotal.service;

import java.util.List;

import com.midai.framework.common.BaseService;
import com.midai.pay.dealtotal.entity.Dealtotal;
import com.midai.pay.dealtotal.query.DealtotalQuery;
import com.midai.pay.dealtotal.vo.DealtotalVo;

/**
 * ClassName:User <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年9月21日  <br/>
 * @author   zt
 * @version  
 * @since    JDK 1.7
 * @see 	 
*/


public interface DealtotalService extends BaseService<Dealtotal>{
    
	/**
     * 列表查询历史交易明细记录
     * @param  query
     * @return
     * 
     */
	  public List<DealtotalVo> queryList(DealtotalQuery query);
	  public int queryCount(DealtotalQuery query);
	  
 
	  /**
	   * 代理商交易查询
	   */
	  public List<DealtotalVo> angetQueryList(DealtotalQuery query);
	  public int angetQueryCount(DealtotalQuery query);

	  /**
	   * 代理商交易查询Excel导出
	   */  
	  public List<DealtotalVo> AgentExcelDownBodealtotal(DealtotalQuery query);
	  public int AgentExcelDownBodealtotalCount(DealtotalQuery query);

	  
	  /**
	   * 历史交易明细导出Excel
	   */
	  public List<DealtotalVo> ExcelDownBodealtotal(DealtotalQuery query);
	  public int ExcelDownBodealtotalCount(DealtotalQuery query);

	  public int queryCountByTransnoAndTransdate(String transno, String transdate);

	  public int queryCountByTransno(String transno);
	  
	
}
