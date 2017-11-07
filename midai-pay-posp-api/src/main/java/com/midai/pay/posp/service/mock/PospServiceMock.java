/**
 * Project Name:midai-pay-posp-service
 * File Name:PospServiceMock.java
 * Package Name:com.midai.pay.posp.service.impl
 * Date:2016年11月17日上午11:01:41
 * Copyright (c) 2016, Shanghai Law Cloud Technology Co., Ltd. All Rights Reserved.
 *
*/

package com.midai.pay.posp.service.mock;

import com.midai.framework.common.po.ResultVal;
import com.midai.pay.posp.entity.ConSumptionEntity;
import com.midai.pay.posp.entity.ConSumptionResult;
import com.midai.pay.posp.entity.MiShuaPayParam;
import com.midai.pay.posp.entity.MiShuaPayQueryParam;
import com.midai.pay.posp.entity.SignEntity;
import com.midai.pay.posp.entity.SignParam;
import com.midai.pay.posp.service.PospService;

/**
 * ClassName:PospServiceMock <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年11月17日 上午11:01:41 <br/>
 * @author   陈勋
 * @version  
 * @since    JDK 1.7
 * @see 	 
 */
public class PospServiceMock implements PospService {

	@Override
	public ConSumptionResult consumption(ConSumptionEntity entity) {

		ConSumptionResult result=new ConSumptionResult();
		result.setRSPCOD("99");
		result.setRSPMSG("请求超时");
		return result;
	}

	@Override
	public SignEntity sgin(SignParam param) {

		SignEntity sgin=new SignEntity();
		sgin.setRSPCOD("99");
		sgin.setRSPMSG("请求超时");
		return sgin;
	}

	@Override
	public ResultVal<String> miShuaPay(MiShuaPayParam param) {
		return null;
	}
	

	@Override
	public ResultVal<String> miShuaPayQuery(MiShuaPayQueryParam param) {
		return null;	
	}

	@Override
	public String[] generateTermianlKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void signToInst(String instCode,String mercId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean signToJidian(String mercId, String mercCode) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public ConSumptionResult consumptionToJidian(ConSumptionEntity param) {
		// TODO Auto-generated method stub
		return null;
	}
}

