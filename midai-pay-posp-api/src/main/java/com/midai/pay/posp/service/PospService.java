/**
 * Project Name:midai-pay-posp-api
 * File Name:PospService.java
 * Package Name:com.midai.posp.service
 * Date:2016年9月12日下午1:42:13
 * Copyright (c) 2016, www midaigroup com Technology Co., Ltd. All Rights Reserved.
 *
*/

package com.midai.pay.posp.service;

import com.midai.framework.common.po.ResultVal;
import com.midai.pay.posp.entity.ConSumptionEntity;
import com.midai.pay.posp.entity.ConSumptionResult;
import com.midai.pay.posp.entity.MiShuaPayParam;
import com.midai.pay.posp.entity.MiShuaPayQueryParam;
import com.midai.pay.posp.entity.SignEntity;
import com.midai.pay.posp.entity.SignParam;

/**
 * ClassName:PospService <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年9月12日 下午1:42:13 <br/>
 * @author   陈勋
 * @version  
 * @since    JDK 1.7
 * @see 	 
 */
public interface PospService {
    
    /**
     * 
     * consumption:(交易). <br/>
     *
     * @author 陈勋
     * @param entity
     * @since JDK 1.7
     */
	ConSumptionResult consumption(ConSumptionEntity entity)  ;
    
    /**
     * 
     * sgin:(签到). <br/>
     *
     * @author 陈勋
     * @param param
     * @return
     * @since JDK 1.7
     */
    public SignEntity sgin(SignParam param);
    
    /**
     * 米刷代付接口
     */
    ResultVal<String> miShuaPay(MiShuaPayParam param);
    
    /**
     * 米刷代付查询接口
     */
    ResultVal<String> miShuaPayQuery(MiShuaPayQueryParam param);
    
    
   
    
    String[] generateTermianlKey();
    
    
    /**
     * 向通道签到
     * @param instCode 通道代码
     */
    void signToInst(String instCode,String mercId);

    /**
     * 吉点商户签到
     * @param mercId  商户号
     * @param mercCode	终端号
     */
    boolean signToJidian(String mercId, String mercCode);
    
    /**
     * 吉点交易
     * @param param
     */
    ConSumptionResult consumptionToJidian(ConSumptionEntity param);
}

