package com.midai.pay.process.service.impl;

import java.util.List;
import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.midai.pay.common.po.ReturnVal;
import com.midai.pay.process.po.PendingTask;
import com.midai.pay.process.po.ProcessUtils;
import com.midai.pay.process.service.BaseProcessService;

public class BaseProcessServiceImpl implements BaseProcessService{ 
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService; 
	
	@Override
	public ReturnVal<String> startProcess(String businessId) {
		if(StringUtils.isEmpty(businessId)){
			return ReturnVal.FAIL(ProcessUtils.PARAMETERS_ERROR,"businessId不合法");
		}
		ProcessInstance pi = this.runtimeService.startProcessInstanceByKey(businessId);
		return ReturnVal.SUCCESS(pi.getId());
	}
	
	@Override
	public ReturnVal<String> processTask(String taskID, Map<String, Object> valueMap) {
		if(StringUtils.isEmpty(taskID)){
			return ReturnVal.FAIL(ProcessUtils.PARAMETERS_ERROR,"taskID不合法");
		}
		taskService.complete(taskID, valueMap);
		return ReturnVal.SUCCESS();
	}

	@Override
	public List<PendingTask> findPendingTaskByUserID(String userID) {
		List<Task> list = taskService.createTaskQuery().taskAssignee(userID).list();
		return ProcessUtils.BuildListPendingTaskByTaskList(list);
	}
	
	
}
