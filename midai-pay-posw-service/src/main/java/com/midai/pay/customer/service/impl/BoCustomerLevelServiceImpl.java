package com.midai.pay.customer.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import com.alibaba.dubbo.config.annotation.Service;
import com.midai.framework.common.BaseServiceImpl;
import com.midai.pay.common.po.ReturnVal;
import com.midai.pay.customer.entity.BoCustomerLevel;
import com.midai.pay.customer.mapper.BoCustomerLevelMapper;
import com.midai.pay.customer.query.BoCustomerLevelModeQuery;
import com.midai.pay.customer.service.BoCustomerLevelService;
import com.midai.pay.customer.vo.BoCustomerLevelVo;
import com.midai.pay.customer.vo.CustomerLevelSimpleVo;

@Service
public class BoCustomerLevelServiceImpl extends BaseServiceImpl<BoCustomerLevel> implements BoCustomerLevelService {

	private final BoCustomerLevelMapper boCustomerLevelMapper;
	
	public BoCustomerLevelServiceImpl(BoCustomerLevelMapper mapper){
		super(mapper);
		this.boCustomerLevelMapper = mapper;
	}

	@Override
	public int queryCount() {
		return boCustomerLevelMapper.queryCount();
	}

	@Override
	public List<BoCustomerLevelVo> queryList(BoCustomerLevelModeQuery query) {
		return boCustomerLevelMapper.queryList(query);
	}

	@Override
	public ReturnVal<String> insert(BoCustomerLevelVo vo) {
		if(!StringUtils.isEmpty(vo.getLevel())){
			BoCustomerLevel CustomerLevel = boCustomerLevelMapper.selectByPrimaryKey(vo.getLevel());
			if(null != CustomerLevel){
//				 throw new RuntimeException(" 此等级名已存在！");
				 return ReturnVal.FAIL(1,"此等级名已存在！");
			}
			
			BoCustomerLevel customerLevel = new BoCustomerLevel();
			BeanUtils.copyProperties(vo, customerLevel);
			customerLevel.setCreateTime(new Date());
			
			boCustomerLevelMapper.insert(customerLevel);
		}
		
		return ReturnVal.SUCCESS();
	}

	@Override
	public BoCustomerLevelVo load(String level) {
		BoCustomerLevelVo vo = new BoCustomerLevelVo();
		
		BoCustomerLevel CustomerLevel = boCustomerLevelMapper.selectByPrimaryKey(level);
		BeanUtils.copyProperties(CustomerLevel, vo);
		
		return vo;
	}

	@Override
	public int update(BoCustomerLevelVo vo) {
		BoCustomerLevel customerLevel = new BoCustomerLevel();
		BeanUtils.copyProperties(vo, customerLevel);
		customerLevel.setUpdateTime(new Date());
		
		int i = boCustomerLevelMapper.updateByPrimaryKey(customerLevel);
		return i;
	}

	@Override
	public int delete(String level) {
		String[] levelArr = level.split(",");
		for(String ll : levelArr){
			boCustomerLevelMapper.deleteByPrimaryKey(ll.trim());
		}
		return 1;
	}

	@Override
	public List<CustomerLevelSimpleVo> getAll() {
		List<CustomerLevelSimpleVo> voList = new ArrayList<CustomerLevelSimpleVo>();
		
		List<BoCustomerLevel> all = boCustomerLevelMapper.selectAll();
		if(null!=all && all.size()>0){
			CustomerLevelSimpleVo vo;
			for(BoCustomerLevel level : all){
				vo = new CustomerLevelSimpleVo();
				vo.setLevel(level.getLevel());
				
				voList.add(vo);
			}
		}
		
		return voList;
	}
	
}
