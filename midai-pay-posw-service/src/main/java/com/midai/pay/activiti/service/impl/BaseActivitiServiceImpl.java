package com.midai.pay.activiti.service.impl;

import java.util.List;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.midai.pay.activiti.service.BaseActivitiService;


public class BaseActivitiServiceImpl implements BaseActivitiService{
	

	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private HistoryService historyService;
	@Autowired
	private RepositoryService repositoryService;	

	private Logger log=LoggerFactory.getLogger(BaseActivitiServiceImpl.class);
	
	@Override
	public String startProcessInstance(String processDefinitionId) {
		//需要注入一个业务ID    
		//runtimeService.startProcessInstanceById(processDefinitionId,businessKey);
		 return runtimeService.startProcessInstanceById(processDefinitionId).getId();
	}


	
	
	
	@Override
	public List<Task> findTaskList(String assignee,int firstResult,int maxResults) {	
	  return taskService.createTaskQuery().taskAssignee( assignee).listPage(firstResult, maxResults);
	}

	@Override
	public long findTaskCount(String assignee) {
		return taskService.createTaskQuery().taskAssignee(assignee).count();
	}
	
	

	

}
