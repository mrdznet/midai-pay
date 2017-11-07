package com.midai.pay.process.service.impl;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.springframework.util.StringUtils;

import com.midai.pay.process.service.ProcessExecutorExtendService;

public class RoleNoOrgExtendServiceImpl implements ProcessExecutorExtendService {

	@Override
	public String create(DelegateTask task, DelegateExecution execution, String extendParam) {
		StringBuilder sb = new StringBuilder();
		
		if(!StringUtils.isEmpty(extendParam)){
			String[] expArray = extendParam.split(",");	// r3
			
			for(String exp : expArray){
				sb.append(exp).append(",.*").append("|");
			}
			
			sb.deleteCharAt(sb.length()-1);
		}
		
		return sb.toString();
	}

}
