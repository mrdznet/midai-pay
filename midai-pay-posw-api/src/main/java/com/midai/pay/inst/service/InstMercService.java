package com.midai.pay.inst.service;

import com.midai.pay.inst.entity.InstMercAll;

public interface InstMercService {
	
	int insertInstMercAll(InstMercAll instMerc);
	
	int deleteInstMercAll(String instCode,String mercId);
	
	int selectInstMercAllCount(String instCode,String mercId);

	InstMercAll selectInstMerc(String instJdCode, String mercId);

}
