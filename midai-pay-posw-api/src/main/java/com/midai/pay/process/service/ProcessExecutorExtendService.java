package com.midai.pay.process.service;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;

public interface ProcessExecutorExtendService{
	
	String create(DelegateTask task,DelegateExecution execution,String extendParam);
}
