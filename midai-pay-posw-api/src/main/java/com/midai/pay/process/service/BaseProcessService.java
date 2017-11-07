package com.midai.pay.process.service;

import java.util.List;
import java.util.Map;

import com.midai.pay.common.po.ReturnVal;
import com.midai.pay.process.po.PendingTask;


public interface BaseProcessService{
	ReturnVal<String> startProcess(String businessID);
	
	ReturnVal<String> processTask(String taskID,Map<String,Object> valueMap);
	
//	ReturnVal<String> rejectTask(String businessID);
	
	List<PendingTask> findPendingTaskByUserID(String userID);
}
