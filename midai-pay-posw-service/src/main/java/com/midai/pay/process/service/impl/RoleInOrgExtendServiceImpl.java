package com.midai.pay.process.service.impl;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.springframework.util.StringUtils;

import com.midai.pay.process.service.ProcessExecutorExtendService;
import com.midai.pay.process.service.ProcessVariablesEnum;

@org.springframework.stereotype.Service
public class RoleInOrgExtendServiceImpl implements ProcessExecutorExtendService {
	
	@Override
	public String create(DelegateTask task, DelegateExecution execution, String extendParam) {
		String org = (String)execution.getVariable(ProcessVariablesEnum.include_orgs.toString()) + ",";
		
		StringBuilder sb = new StringBuilder();
		
		if(!StringUtils.isEmpty(extendParam)){
			String[] expArray = extendParam.split(",");	// r3
			
			for(String exp : expArray){
				sb.append(exp).append(",.*").append(org).append("|");
			}
			
			sb.deleteCharAt(sb.length()-1);
		}
		
		return sb.toString();
	}

}