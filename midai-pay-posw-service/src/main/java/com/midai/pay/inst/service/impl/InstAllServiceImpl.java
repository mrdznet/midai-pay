package com.midai.pay.inst.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.midai.pay.inst.entity.InstAll;
import com.midai.pay.inst.mapper.InstAllMapper;
import com.midai.pay.inst.query.InstAllQuery;
import com.midai.pay.inst.query.InstSimpleVo;
import com.midai.pay.inst.service.InstAllService;

@Service
public class InstAllServiceImpl implements InstAllService {
	
	@Autowired 
	private InstAllMapper instAllMapper;

	@Override
	public int insertInstAll(InstAll instAll) {
		return instAllMapper.insertInstAll(instAll);
	}

	@Override
	public int selectInstAllCount(String instCode) {
		return instAllMapper.selectInstAllCount(instCode);
	}

	@Override
	public int batchUpdateInst(String[] instCode, int instState) {
		
		StringBuilder idStr = new StringBuilder();
		for (String id : instCode) {
			idStr.append("'").append(id).append("',");
		
		}
		idStr.setLength(idStr.length() - 1);
		
		return instAllMapper.batchUpdateInst(idStr.toString(),instState);
	}

	@Override
	public List<InstAll> instAllList(InstAllQuery query) {
		return instAllMapper.instAllList(query);
	}

	@Override
	public int instAllListCount(InstAllQuery query) {
		return instAllMapper.instAllListCount(query);
	}

	@Override
	public int updateInstAll(InstAll instAll) {
		return instAllMapper.updateInstAll(instAll);
	}

	@Override
	public List<InstAll> findAll() {
		return instAllMapper.findAll();
	}

	@Override
	public InstAll selectInstAllById(String instCode) {
		return instAllMapper.selectInstAllById(instCode);
	}

	@Override
	public List<InstSimpleVo> findAllSimple() {
		List<InstSimpleVo> voList = new ArrayList<InstSimpleVo>();
		
		List<InstAll> all = instAllMapper.findAll();
		
		if(null!=all && all.size()>0){
			InstSimpleVo inst;
			for(InstAll ll : all){
				inst = new InstSimpleVo();
				inst.setInstCode(ll.getInstCode());
				inst.setInstName(ll.getInstName());
				
				voList.add(inst);
			}
		}
		
		return voList;
	}

}
