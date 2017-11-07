package com.midai.haibei.customer.service;

import com.midai.pay.customer.entity.BoCustomerTemp;

/**
 * 海贝商户更改入账行流程
 * 
 * @author Feng
 *
 */
public interface HaibeiCustomerBankUpdateProcessService {
	
	/**
	 * 启动海贝商户更改入账行流程
	 * 
	 * @param mercNo 商户编号
	 * @param employeeId 操作人
	 *  
	 * @return
	 */
	void startHaibeiCustomerBankUpdateProcess(String mercNo, String employeeId);
	
	/**
	 * 海贝商户更改入账行流程
	 * 
	 * @param mercNo 商户编号
	 * @param employeeId 操作人
	 * @param approval
	 * 
	 * @return
	 */
	void runHaibeiCustomerBankUpdateProcess(String taskId, String mercNo, String employeeId, boolean approval);
	
	/**
	 * 申请前加载信息
	 */
	BoCustomerTemp loadBoCustomerBymercNo(String mercNo);
	
	BoCustomerTemp loadBoCustomerTempBymercNo(String mercNo);
	/**
	 * 海贝商户申请
	 */
     int haibeiApply(BoCustomerTemp  boCustomerTemp);
     
     /**
      * 海贝商户申请审核
      */
     String  haibeiCheck(BoCustomerTemp boCustomerTemp,String name);
	
}
