package com.midai.pay.route.service.impl;

import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.midai.pay.customer.mapper.BoCustomerMapper;
import com.midai.pay.route.mapper.RouteMercInGroupConfigMapper;
import com.midai.pay.route.mapper.RouteMercInGroupMapper;
import com.midai.pay.route.mapper.RouteMercInOutMapper;
import com.midai.pay.route.mapper.RouteMercOutGroupConfigMapper;
import com.midai.pay.route.mapper.RouteMercOutGroupMapper;
import com.midai.pay.route.service.ShenXinConfigService;


@Service
public class ShenXinConfigServiceImpl implements ShenXinConfigService{

	private RouteMercInGroupConfigMapper rmigcMapper;
	
	private RouteMercInGroupMapper rmigMapper;
	
	private RouteMercInOutMapper rmioMapper;
	
	private RouteMercOutGroupConfigMapper rmogcMapper;
	
	private RouteMercOutGroupMapper rmogMapper;
	
	private BoCustomerMapper bcMapper;
	
	
	public ShenXinConfigServiceImpl(
			final RouteMercInGroupConfigMapper rmigcMapper,
			final RouteMercInGroupMapper rmigMapper,
			final RouteMercInOutMapper rmioMapper,
			final RouteMercOutGroupConfigMapper rmogcMapper,
			final RouteMercOutGroupMapper rmogMapper,
			final BoCustomerMapper bcMapper){
		super();
		this.rmigcMapper = rmigcMapper;
		this.rmigMapper = rmigMapper;
		this.rmioMapper = rmioMapper;
		this.rmogcMapper = rmogcMapper;
		this.rmogMapper = rmogMapper;
		this.bcMapper = bcMapper;
	}
	
	@Transactional
	@Override
	public void configMerc(String instCode, String mobile, String mercInstId, String mercInstDeviceId) {
		//根据手机号查询商户ID
		String mercId = bcMapper.findMercIdByMobile(mobile);
		if(StringUtils.isEmpty(mercId)){
			throw new RuntimeException("错误");
		}
		
		bcMapper.insertInstMerc(instCode, mercInstId, mercInstDeviceId);// tbl_inst_merc
		
		
		long inId = rmigcMapper.findNextMaxId();	//表 tbl_route_merc_in_group_config
		rmigcMapper.insertRouteMercIn((int)inId, "shenxin", "申鑫米袋商户号["+mercId+"]"); // tbl_route_merc_in_group_config
		rmigMapper.insertRouteMercInGroup((int)inId,"merc",mercId);	// tbl_route_merc_in_group
		
		
		long outId = rmogcMapper.findNextMaxId(); // tbl_route_merc_out_group_config
		rmogcMapper.insertRouteMercOutGroupConfig((int)outId, "shenxin", "1==1","申鑫商户号["+mercInstId+"]");	// tbl_route_merc_out_group_config
		rmogMapper.insertRouteMercOut((int)outId,mercInstId); //	tbl_route_merc_out_group
		
		
		rmioMapper.insertRouteMercInOut((int)inId, (int)outId);	// tbl_route_merc_in_out
	}
}
