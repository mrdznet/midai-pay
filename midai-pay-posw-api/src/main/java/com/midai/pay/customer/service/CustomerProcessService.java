package com.midai.pay.customer.service;

import java.util.List;

import com.midai.pay.process.po.PendingTask;

public interface CustomerProcessService {
	
	/**
	 * 启动商户申请流程
	 * 
	 * @param mercNo 商户编号
	 * @param channel 网页端:0, APP端:1 
	 * @return
	 */
	void startCustomerApplyProcess(String mercNo, String employeeId, int channel);
	
	/**
	 * 商户申请审核流程
	 * 
	 * @param processRole 流程角色
	 * @param mercNo 商户编号
	 * @param employeeId 操作人
	 * @param approval	同意:1, 拒绝:0
	 * @return
	 */
	void runCustomerApplyProcess(String taskId, String mercNo, String employeeId, boolean approval, String road);
	
	List<PendingTask> customerPendingTaskPages(String employeeId, int start, int size);
	int customerPendingTaskCount(String employeeId);
	
	
	int countPendingTask(String employeeId);
}
