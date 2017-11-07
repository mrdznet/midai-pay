package com.midai.pay.frozen.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.midai.framework.common.BaseServiceImpl;
import com.midai.pay.frozen.entity.TradeFrozen;
import com.midai.pay.frozen.mapper.TradeFrozenMapper;
import com.midai.pay.frozen.query.TradeFrozenQuery;
import com.midai.pay.frozen.service.TradeFrozenService;
import com.midai.pay.frozen.vo.TradeFrozenVo;
import com.midai.pay.settlemoney.mapper.BoGetMoneyMapper;

@Service
public class TradeFrozenServiceImpl extends BaseServiceImpl<TradeFrozen> implements TradeFrozenService{

	
	public TradeFrozenServiceImpl(TradeFrozenMapper mapper){
		super(mapper);
		this.mapper=mapper;
	}
	private final TradeFrozenMapper mapper;
	
	@Autowired
	private BoGetMoneyMapper boGetMoneyMapper;
	
	
	/**
	 * 交易冻结管理查询
	 * @param query
	 * @return
	 */
	@Override
	public List<TradeFrozenVo> findQueryMiIplaTradefrozenreason(TradeFrozenQuery query) {
		return mapper.findQueryMiIplaTradefrozenreason(query);
	}
	@Override
	public int findQueryMiIplaTradefrozenreasonCount(TradeFrozenQuery query) {
		return mapper.findQueryMiIplaTradefrozenreasonCount(query);
	}

	/**
	 * 交易冻结管理查询Excel表格下载
	 * @param query
	 * @return
	 */
	@Override
	public List<TradeFrozenVo> ExcelDownMiIplaTradefrozenreason(TradeFrozenQuery query) {
		return  mapper.ExcelDownMiIplaTradefrozenreason(query);
	}
	@Override
	public int ExcelDownMiIplaTradefrozenreasonCount(TradeFrozenQuery query) {
		return mapper.findQueryMiIplaTradefrozenreasonCount(query);
	}

	/**
	 * 交易冻结管理查询Excel表格下载有复选框
	 * @param hosttransssns
	 * @return
	 */
	@Override
	public List<TradeFrozenVo> ExcelDownSelectMiIplaTradefrozenreason(List<String> ids) {
		StringBuffer idsStr=new StringBuffer("");
		for(String id:ids){
			idsStr.append(""+id+",");
		}
		idsStr.setLength(idsStr.length()-1);
		return mapper.ExcelDownSelectMiIplaTradefrozenreason(idsStr.toString());
	}
	
	/**
	 * 批量交易冻结
	 */
	@Transactional
	@Override
	public int updatetradeFrozen(String[] lognos, String userName) {
		// 1. 更新get-money 表
		StringBuilder sb = new StringBuilder();
		for(String logno : lognos) {
			sb.append("'").append(logno).append("',");
		}
		sb.setLength(sb.length() - 1);
		int num=boGetMoneyMapper.bacthUpdateCheckStatefrozen(sb.toString());
				
		// 2.插入流水数据
		List<TradeFrozenVo> reasonList = boGetMoneyMapper.queryData(sb.toString());//
		 /*SystemUser user = SystemUser.get*/
		 for (TradeFrozenVo vo : reasonList) {
			 vo.setFrozenTime(new Date());
			 vo.setFrozenPerson(userName);
		}
		 if(!reasonList.isEmpty()) {
			 Map<String,List<TradeFrozenVo>> map = new HashMap<String,List<TradeFrozenVo>>();
			 map.put("list", reasonList);
			 mapper.frozenInsert(map);
		 }
		return 1; 
		
	}
	
	/**
	 * 批量交易解冻
	 */
	@Transactional
	@Override
	public int updatetradeunFrozen(Integer[] ids, String userName) {
		//更新get-money表
		StringBuilder idSb = new StringBuilder();
		for(Integer id : ids) {
			idSb.append(id).append(",");
		}
		idSb.setLength(idSb.length() - 1);
		List<String> hostTransSsn = mapper.findHostTransSsnList(idSb.toString());
		
		StringBuilder ssnSb = new StringBuilder();
		for(String ssn : hostTransSsn) {
			ssnSb.append("'").append(ssn).append("',");
		}
		ssnSb.setLength(ssnSb.length() - 1);
		int num=boGetMoneyMapper.bacthUpdateCheckStateunfrozen(ssnSb.toString());
		
		//更新mi_ipla_tradefrozenreason表数据
		int num1=mapper.unfrozenUpdate(idSb.toString(), new Date(), userName);
		return 1;
	}
	
}
