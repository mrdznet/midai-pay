package com.midai.pay.frozen.mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import com.midai.framework.config.mybatis.MyMapper;
import com.midai.pay.frozen.entity.TradeFrozen;
import com.midai.pay.frozen.query.TradeFrozenQuery;
import com.midai.pay.frozen.vo.TradeFrozenVo;


public interface TradeFrozenMapper extends MyMapper<TradeFrozen> {
	
	
	/*交易冻结管理查询*/
	@SelectProvider(type=com.midai.pay.frozen.provider.TradeFrozenProvider.class,method="findQueryMiIplaTradefrozenreason")
    public List<TradeFrozenVo> findQueryMiIplaTradefrozenreason(TradeFrozenQuery query);
	
	@SelectProvider(type=com.midai.pay.frozen.provider.TradeFrozenProvider.class,method="findQueryMiIplaTradefrozenreasonCount")
    public int  findQueryMiIplaTradefrozenreasonCount(TradeFrozenQuery query);

	/*交易冻结管理查询Excel表格下载*/
	@SelectProvider(type=com.midai.pay.frozen.provider.TradeFrozenProvider.class,method="ExcelDownMiIplaTradefrozenreason")
	public List<TradeFrozenVo> ExcelDownMiIplaTradefrozenreason(TradeFrozenQuery query);
	
	@SelectProvider(type=com.midai.pay.frozen.provider.TradeFrozenProvider.class,method="ExcelDownMiIplaTradefrozenreasonCount")
	public int ExcelDownMiIplaTradefrozenreasonCount(TradeFrozenQuery query);
	
	/*支持Excel下载有复选框*/
	@SelectProvider(type=com.midai.pay.frozen.provider.TradeFrozenProvider.class,method="ExcelDownSelectMiIplaTradefrozenreason")
	public List<TradeFrozenVo> ExcelDownSelectMiIplaTradefrozenreason(String ids);
	
	/*批量冻结插入数据*/
	@InsertProvider(type=com.midai.pay.frozen.provider.TradeFrozenProvider.class,method="frozenInsert")
    public long frozenInsert(Map<String,List<TradeFrozenVo>> frozenData);	
    
	/*批量冻结查询要id对应的流水号*/
	@Select("select host_trans_ssn from mi_ipla_tradefrozenreason where id in(#{ids})")
	List<String> findHostTransSsnList(String ids);
	
	/*批量解冻更新数据*/	
	@UpdateProvider(type=com.midai.pay.frozen.provider.TradeFrozenProvider.class,method="unfrozenUpdate")
    int unfrozenUpdate(@Param("ids") String ids, @Param("unfrozenTime") Date unfrozenTime, @Param("unfrozenPerson") String unfrozenPerson);	
    
	
	
	
	
}
	
	
	
	
	
	
	
	


