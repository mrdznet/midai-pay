package com.midai.pay.frozen.service;

import java.util.List;

import com.midai.framework.common.BaseService;
import com.midai.pay.frozen.entity.CustomerFrozen;
import com.midai.pay.frozen.query.CustomerFrozenQuery;
import com.midai.pay.frozen.vo.CustomerFrozenVo;

public interface CustomerFrozenService extends BaseService<CustomerFrozen> {
	
	/**
	 * 商户冻结管理查询
	 * @param query
	 * @return
	 */
	public List<CustomerFrozenVo> findQueryMiIplafrozenreason(CustomerFrozenQuery query);
	public int findQueryMiIplafrozenreasonCount(CustomerFrozenQuery query);

	
	/**
	 * 商户冻结管理查询Excel表格下载
	 * @param query
	 * @return
	 */
	public List<CustomerFrozenVo> ExcelDownMiIplafrozenreason(CustomerFrozenQuery query);
	public int  ExcelDownMiIplafrozenreasonCount(CustomerFrozenQuery query);
	
	
	/**
	 * 商户冻结管理查询Excel表格下载有复选框
	 * @param ids
	 * @returns
	 */
	public List<CustomerFrozenVo>ExcelDownSelectMiIplafrozenreason(List<String> ids);
    
	
	/**
	 * 商户批量冻结和新增冻结
	 */
    public int customerfrozen (String frozenReason,String[] mercIds, String userName); 
	
	/**
	 * 商户批量解冻
	 */

	public int customerunfrozen(String[] mercIds,String userName);
    
	
}
