package com.midai.pay.process.service.impl;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;

import com.alibaba.dubbo.config.annotation.Service;
import com.midai.pay.process.service.ProcessExecutorExtendService;

@Service(version="1.0.0",protocol="dubbo")
public class NodeUserExtendServiceImpl implements ProcessExecutorExtendService {

	@Override
	public String create(DelegateTask task, DelegateExecution execution, String extendParam) {
		String assignee = "";
		
		Object oLastUser = execution.getVariable(extendParam);
		
		if(oLastUser!=null){
			String user = (String)oLastUser;

			assignee = user.endsWith(",") ? user : user+",";
		}
		return assignee;
	}

}
