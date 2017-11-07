package com.midai.pay.route.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.midai.pay.common.util.PythonUtil;
import com.midai.pay.inst.entity.InstAll;
import com.midai.pay.route.entity.RoleFileConfigEntity;
import com.midai.pay.route.service.ChannelWeightFactoryService;
import com.midai.pay.route.service.RoleFileConfigService;
import com.midai.pay.routehistory.entity.HistoryData;
import com.midai.pay.routetransactionInformation.TransactionInformation;

@Service
public class ChannelWeightFactoryServiceImpl implements ChannelWeightFactoryService {

	@Autowired
	private RoleFileConfigService roleFileConfigService;
	
	@Override
	public Set<String> getResultInstAllList(TransactionInformation tsv, Set<InstAll> filterList,
			List<HistoryData> hdList) {
		
		List<RoleFileConfigEntity> roleConfig = roleFileConfigService.getRoleFileConfigList(0);
		
		List<String> pyList = new ArrayList<String>();
		
		if(roleConfig != null) {
			for(RoleFileConfigEntity rce : roleConfig) {
				pyList.add(PythonUtil.getInsCode(rce.getFileName(), tsv));
			}
		}
		
		Set<String> pySet = new HashSet<String>();
		
		if(pyList.size() > 0) {
			for(String s : pyList) {
				pySet.addAll(Arrays.asList(s.split(",")));
			}
		}
		Set<String> set = new HashSet<String>();
		for(InstAll inst : filterList) {
			set.add(inst.getInstCode());
		}
		
		pySet.retainAll(set);
		return pySet;
	}

}
