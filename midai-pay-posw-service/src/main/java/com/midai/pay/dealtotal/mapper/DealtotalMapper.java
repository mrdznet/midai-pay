package com.midai.pay.dealtotal.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import com.midai.framework.config.mybatis.MyMapper;
import com.midai.pay.dealtotal.entity.Dealtotal;
import com.midai.pay.dealtotal.query.DealtotalQuery;
import com.midai.pay.dealtotal.vo.DealtotalVo;


/**
 * ClassName:User <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年9月21日  <br/>
 * @author zt
 * @version  
 * @since    JDK 1.7
 * @see
 */


public interface DealtotalMapper  extends MyMapper<Dealtotal>{

	
	@SelectProvider(type=com.midai.pay.dealtotal.provider.DealtotalProvider.class,method="queryList")
	public List<DealtotalVo> queryList(DealtotalQuery query);
	@SelectProvider(type=com.midai.pay.dealtotal.provider.DealtotalProvider.class,method="queryCount")
	public int queryCount(DealtotalQuery query);
	
	
	/*代理商交易查询*/
	@SelectProvider(type=com.midai.pay.dealtotal.provider.DealtotalProvider.class,method="angetQueryList")
	public List<DealtotalVo> angetQueryList(DealtotalQuery query);
	@SelectProvider(type=com.midai.pay.dealtotal.provider.DealtotalProvider.class,method="angetQueryCount")
	public int angetQueryCount(DealtotalQuery query);
 
	/*代理商交易查询Excel导出*/
	@SelectProvider(type=com.midai.pay.dealtotal.provider.DealtotalProvider.class,method="AgentExcelDownBodealtotal")
	public List<DealtotalVo> AgentExcelDownBodealtotal(DealtotalQuery query);
	@SelectProvider(type=com.midai.pay.dealtotal.provider.DealtotalProvider.class,method="AgentExcelDownBodealtotalCount")
    public int AgentExcelDownBodealtotalCount(DealtotalQuery query);
	
	@SelectProvider(type=com.midai.pay.dealtotal.provider.DealtotalProvider.class,method="ExcelDownBodealtotal")
	public List<DealtotalVo> ExcelDownBodealtotal(DealtotalQuery query);
	@SelectProvider(type=com.midai.pay.dealtotal.provider.DealtotalProvider.class,method="ExcelDownBodealtotalCount")
    public int ExcelDownBodealtotalCount(DealtotalQuery query);

	
	@Select(" select count(1) from tbl_deal_total t where t.host_trans_ssn=#{transno} and DATE_FORMAT(t.trans_time,'%m%d')=#{transdate} ")
	public int queryCountByTransnoAndTransdate(@Param("transno")String transno, @Param("transdate")String transdate);
	@Select(" select count(1) from tbl_deal_total t where t.host_trans_ssn=#{transno}  ")
	public int queryCountByTransno(String transno);

}
