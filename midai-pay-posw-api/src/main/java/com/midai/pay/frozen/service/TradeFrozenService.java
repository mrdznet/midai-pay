package com.midai.pay.frozen.service;

import java.util.List;

import com.midai.framework.common.BaseService;
import com.midai.pay.frozen.entity.TradeFrozen;
import com.midai.pay.frozen.query.TradeFrozenQuery;
import com.midai.pay.frozen.vo.TradeFrozenVo;

public interface TradeFrozenService extends BaseService<TradeFrozen>{

	/**
	 * 交易冻结管理查询
	 * @param query
	 * @return
	 */
	public List<TradeFrozenVo> findQueryMiIplaTradefrozenreason(TradeFrozenQuery query);
	public int findQueryMiIplaTradefrozenreasonCount(TradeFrozenQuery query);
	
	/**
	 * 交易冻结管理查询Excel表格下载
	 * @param query
	 * @return
	 */
	public List<TradeFrozenVo>ExcelDownMiIplaTradefrozenreason(TradeFrozenQuery query);
	public int ExcelDownMiIplaTradefrozenreasonCount(TradeFrozenQuery query);
	
	/**
	 * 交易冻结管理查询Excel表格下载有复选框
	 * @param ids
	 * @return
	 */
	public List<TradeFrozenVo> ExcelDownSelectMiIplaTradefrozenreason(List<String> ids);
	
	
	/**
	 * 批量冻结流水
	 * 
	 * @return
	 */
      int  updatetradeFrozen(String[] lognos, String userName);
	
     
	
	/**
	 * 交易冻结管理批量解冻
	 * 
	 * @return
	 */
      
      int updatetradeunFrozen(Integer[] ids,String userName);
	
	
	
}
