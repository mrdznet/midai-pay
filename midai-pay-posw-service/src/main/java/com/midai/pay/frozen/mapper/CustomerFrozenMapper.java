package com.midai.pay.frozen.mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import com.midai.framework.config.mybatis.MyMapper;
import com.midai.pay.frozen.entity.CustomerFrozen;
import com.midai.pay.frozen.query.CustomerFrozenQuery;
import com.midai.pay.frozen.vo.CustomerFrozenVo;

public interface CustomerFrozenMapper extends MyMapper<CustomerFrozen> {
	
	/*商户冻结管理查询*/
	@SelectProvider(type=com.midai.pay.frozen.provider.CustomerFrozenProvider.class,method="findQueryMiIplafrozenreason")
    public List<CustomerFrozenVo> findQueryMiIplafrozenreason(CustomerFrozenQuery query);
	
	@SelectProvider(type=com.midai.pay.frozen.provider.CustomerFrozenProvider.class,method="findQueryMiIplafrozenreasonCount")
    public int  findQueryMiIplafrozenreasonCount(CustomerFrozenQuery query);

	/*商户冻结管理查询Excel表格下载*/
	@SelectProvider(type=com.midai.pay.frozen.provider.CustomerFrozenProvider.class,method="ExcelDownMiIplafrozenreason")
	public List<CustomerFrozenVo> ExcelDownMiIplafrozenreason(CustomerFrozenQuery query);
	
	@SelectProvider(type=com.midai.pay.frozen.provider.CustomerFrozenProvider.class,method="ExcelDownMiIplafrozenreasonCount")
	public int ExcelDownMiIplafrozenreasonCount(CustomerFrozenQuery query);
	
	/*支持Excel下载有复选框*/
	@SelectProvider(type=com.midai.pay.frozen.provider.CustomerFrozenProvider.class,method="ExcelDownSelectMiIplafrozenreason")
	public List<CustomerFrozenVo> ExcelDownSelectMiIplafrozenreason(String ids);
	
	/*商户冻结插入数据*/
	@InsertProvider(type=com.midai.pay.frozen.provider.CustomerFrozenProvider.class,method="frozenInsert")
    public long frozenInsert(Map<String,List<CustomerFrozenVo>> frozenData);
	
	/*商户解冻更新数据*/
	@UpdateProvider(type=com.midai.pay.frozen.provider.CustomerFrozenProvider.class,method="unfrozenUpdate")
    int unfrozenUpdate(@Param("mercIds") String mercIds, @Param("unfrozenTime") Date unfrozenTime, @Param("unfrozenPerson") String unfrozenPerson);	
	
	

}
