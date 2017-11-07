package com.midai.pay.frozen.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.midai.framework.common.BaseServiceImpl;
import com.midai.pay.agent.service.impl.AgentServiceImpl;
import com.midai.pay.customer.mapper.BoCustomerMapper;
import com.midai.pay.frozen.entity.CustomerFrozen;
import com.midai.pay.frozen.mapper.CustomerFrozenMapper;
import com.midai.pay.frozen.query.CustomerFrozenQuery;
import com.midai.pay.frozen.service.CustomerFrozenService;
import com.midai.pay.frozen.vo.CustomerFrozenVo;
import com.midai.pay.settlemoney.mapper.BoGetMoneyMapper;


@Service
public class CustomerFrozenServiceImpl  extends BaseServiceImpl<CustomerFrozen> implements CustomerFrozenService{
	
	public CustomerFrozenServiceImpl(CustomerFrozenMapper mapper){
		super(mapper);
		this.mapper=mapper;
	}
    
	private final CustomerFrozenMapper mapper;
	
	@Autowired
	private BoGetMoneyMapper boGetMoneyMapper;
	
	@Autowired
	private BoCustomerMapper boCustomerMapper;
	
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AgentServiceImpl.class);
	
	/**
	 * 商户冻结管理查询
	 * @param query
	 * @return
	 */
	@Override
	public List<CustomerFrozenVo> findQueryMiIplafrozenreason(CustomerFrozenQuery query) {
		return mapper.findQueryMiIplafrozenreason(query);
	}

	@Override
	public int findQueryMiIplafrozenreasonCount(CustomerFrozenQuery query) {
		return mapper.findQueryMiIplafrozenreasonCount(query);
	}

	/**
	 * 商户冻结管理查询Excel表格下载
	 * @param query
	 * @return
	 */
	@Override
	public List<CustomerFrozenVo> ExcelDownMiIplafrozenreason(CustomerFrozenQuery query) {
		return mapper.ExcelDownMiIplafrozenreason(query);
	}

	@Override
	public int ExcelDownMiIplafrozenreasonCount(CustomerFrozenQuery query) {
		return mapper.ExcelDownMiIplafrozenreasonCount(query);
	}

	/**
	 * 商户冻结管理查询Excel表格下载有复选框
	 * @param hosttransssns
	 * @return
	 */
	@Override
	public List<CustomerFrozenVo> ExcelDownSelectMiIplafrozenreason(List<String> ids) {
		StringBuffer idsStr=new StringBuffer("");
		for(String id:ids){
			idsStr.append(""+id+",");
		}
		idsStr.setLength(idsStr.length()-1);
		return mapper.ExcelDownSelectMiIplafrozenreason(idsStr.toString());
	
	}

	/**
	 * 商户批量冻结和新增冻结
	 */
	@Transactional
	@Override
	public int customerfrozen( String frozenReason,String[] mercIds, String userName) {
		
		
		//批量修改get-Money状态和custemer表状态
		StringBuffer sb= new StringBuffer();
		
		for(String mercId:mercIds){
			sb.append("'").append(mercId).append("',");
		}
		sb.setLength(sb.length() - 1);
		
		//判断是否存在该小票号或者已经审核通过
		int oleMercIds = boGetMoneyMapper.selectCountMercId(sb.toString());
		if(oleMercIds <= 0){
			LOGGER.info("小票号不存在或已审核通过，冻结失败！");
			throw new RuntimeException("小票号不存在或已审核通过，冻结失败！");
		}
			
		
		int num = boGetMoneyMapper.customerUpdateCheckStatefrozen(sb.toString());
		int num1= boCustomerMapper.bacthUpdateOpstatefrozen(sb.toString()) ;
		
		//更新mi_ipla_frozenreason表，插入数据
		List<CustomerFrozenVo> reasonList = boGetMoneyMapper.queryCustomerData(sb.toString());//
		 /*SystemUser user = SystemUser.get*/
		 for (CustomerFrozenVo vo : reasonList) {
			 vo.setFrozenTime(new Date());
			 vo.setFrozenPerson(userName);
			 vo.setFrozenReason(frozenReason);
		}
		 if(!reasonList.isEmpty()) {
			 Map<String,List<CustomerFrozenVo>> map = new HashMap<String,List<CustomerFrozenVo>>();
			 map.put("list", reasonList);
			 mapper.frozenInsert(map);
		 }
		
		return 1;
	}

	/**
	 * 交易冻结管理批量解冻
	 */
	@Transactional
	@Override
	public int customerunfrozen(String[] mercIds,String userName) {
		//批量修改get-Money状态和custemer表状态
		StringBuffer idSb=new StringBuffer();
		for(String mercId:mercIds){
			idSb.append("'").append(mercId).append("',");
		}
		idSb.setLength(idSb.length() - 1);
		
		int num = boGetMoneyMapper.customerUpdateCheckStateunfrozen(idSb.toString());
		int num1= boCustomerMapper.bacthUpdateOpstateunfrozen(idSb.toString()) ;
		
		//更新mi_ipla_frozenreason信息
		int num2=mapper.unfrozenUpdate(idSb.toString(), new Date(), userName);
		return 1;
	}

}
