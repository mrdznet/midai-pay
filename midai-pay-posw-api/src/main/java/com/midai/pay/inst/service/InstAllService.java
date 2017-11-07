package com.midai.pay.inst.service;

import java.util.List;

import com.midai.pay.inst.entity.InstAll;
import com.midai.pay.inst.query.InstAllQuery;
import com.midai.pay.inst.query.InstSimpleVo;

public interface InstAllService {
	
	int insertInstAll(InstAll instAll);
	
	int selectInstAllCount(String instCode);
	
	int batchUpdateInst(String [] instCode,int instState);
	
	List<InstAll> instAllList(InstAllQuery query);
	int instAllListCount(InstAllQuery query);
	
	int updateInstAll(InstAll instAll);
	
	List<InstAll> findAll();
	
	InstAll  selectInstAllById( String instCode);
	
	List<InstSimpleVo> findAllSimple();
	
}
