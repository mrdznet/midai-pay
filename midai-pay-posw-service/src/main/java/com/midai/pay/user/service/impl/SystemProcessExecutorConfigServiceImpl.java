/**
 * Project Name:chenxun-framework-start
 * File Name:UserServiceImpl.java
 * Package Name:com.chenxun.framework.service.impl
 * Date:2016年8月31日下午5:36:59
 * Copyright (c) 2016, www midaigroup com Technology Co., Ltd. All Rights Reserved.
 *
*/

package com.midai.pay.user.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.alibaba.dubbo.config.annotation.Service;
import com.midai.framework.common.BaseServiceImpl;
import com.midai.pay.process.service.ProcessExecutorExtendService;
import com.midai.pay.process.service.ProcessStatusEnum;
import com.midai.pay.process.service.ProcessVariablesEnum;
import com.midai.pay.user.entity.SystemProcessExecutorConfig;
import com.midai.pay.user.entity.SystemProcessExecutorConfigRole;
import com.midai.pay.user.mapper.SystemProcessExecutorConfigMapper;
import com.midai.pay.user.mapper.SystemProcessExecutorConfigRoleMapper;
import com.midai.pay.user.service.SystemProcessExecutorConfigService;
import com.midai.pay.user.vo.SystemProcessExecutorConfigSVo;

import tk.mybatis.mapper.entity.Example;

/**
 * ClassName:User <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年9月13日  <br/>
 * @author   wrt
 * @version  
 * @since    JDK 1.7
 * @see 	 
 */
@Service
public class SystemProcessExecutorConfigServiceImpl extends BaseServiceImpl<SystemProcessExecutorConfig> implements SystemProcessExecutorConfigService{
	
	private final SystemProcessExecutorConfigMapper mapper;
	

    public SystemProcessExecutorConfigServiceImpl(SystemProcessExecutorConfigMapper mapper) {           
        super(mapper);  
        this.mapper=mapper;
    }
    
	@Autowired
	private ApplicationContext appContext;
	
	@Autowired
	private HistoryService historyService;
	
	@Autowired
	private SystemProcessExecutorConfigRoleMapper  systemProcessExecutorConfigRoleMapper;

    @Override
    public Map<String, List<SystemProcessExecutorConfigSVo>> getAllMap() {
    	
    	Map<String, List<SystemProcessExecutorConfigSVo>> map = new HashMap<String, List<SystemProcessExecutorConfigSVo>>();
    	List<SystemProcessExecutorConfig> list = mapper.selectAll();
    	if(list!=null && list.size()>0) {
    		Iterator<SystemProcessExecutorConfig> it = list.iterator();
    		
    		while(it.hasNext()) {
    			SystemProcessExecutorConfig sec = it.next();
    			SystemProcessExecutorConfigSVo scv = new SystemProcessExecutorConfigSVo();
				scv.setId(sec.getId());
				scv.setName(sec.getTaskName());
				
    			if(map.containsKey(sec.getProcessName())) {
    				List<SystemProcessExecutorConfigSVo> exList = map.get(sec.getProcessName());
    				exList.add(scv);
    			} else {
    				List<SystemProcessExecutorConfigSVo> voList = new ArrayList<SystemProcessExecutorConfigSVo>();
    				voList.add(scv);
    				map.put(sec.getProcessName(), voList);
    			}
    		}
    	}
    	return map;
    }


	@Override
	public int findAllCount() {
		Example example = new Example(SystemProcessExecutorConfig.class);
		return mapper.selectCountByExample(example);
	}

	@Override
	public int updateBycondition(SystemProcessExecutorConfig systemProcessExecutorConfig) {
		Example example = new Example(SystemProcessExecutorConfig.class);
		example.createCriteria().andEqualTo("processKey", systemProcessExecutorConfig.getProcessKey())
		.andEqualTo("taskKey", systemProcessExecutorConfig.getTaskKey()); 
		return mapper.updateByExample(systemProcessExecutorConfig, example);
	}

	@Override
	public void delBycondition(SystemProcessExecutorConfig systemProcessExecutorConfig) {
		Example example = new Example(SystemProcessExecutorConfig.class);
		example.createCriteria().andEqualTo("processKey", systemProcessExecutorConfig.getProcessKey())
		.andEqualTo("taskKey", systemProcessExecutorConfig.getTaskKey()); 
		mapper.deleteByExample(example);
	}
	
	@Override
	public void assignTo(DelegateTask task){
		String taskKey = task.getTaskDefinitionKey();
		String processKey = task.getProcessDefinitionId().split(":")[0];
		SystemProcessExecutorConfig p = this.findOneBycondition(new SystemProcessExecutorConfig(processKey,taskKey,""));
		task.setAssignee(p.getExecutorExp());
		
	}
	
	@Override
	public void initTask(DelegateTask task,DelegateExecution execution){
		String taskKey = task.getTaskDefinitionKey();
		String processKey = task.getProcessDefinitionId().split(":")[0];
		
		task.setCategory(execution.getProcessBusinessKey());
		
		//如果taskkey以_sp__edit为结尾，则赋予特殊formkey
		if(task.getTaskDefinitionKey().endsWith("_sp__edit")){
			Object o = execution.getVariable(ProcessVariablesEnum.form_sp__edit.toString());
			if(o!=null){
				task.setFormKey((String)o);
				taskKey = task.getFormKey();
			}
		}

		Object o = execution.getVariable(ProcessVariablesEnum.approval.toString());
		if(o!=null){
			task.setDescription((String)o);
		}else{
			task.setDescription(ProcessStatusEnum.unknown.toString());
		}
		
		SystemProcessExecutorConfig pConfig = this.findOneBycondition(new SystemProcessExecutorConfig(processKey,taskKey,""));
		
		Object oLastUser = execution.getVariable(taskKey);
		
		if(1==pConfig.getLastoneRedo() && oLastUser!=null){
			String user = (String)oLastUser;

			task.setAssignee(user.endsWith(",") ? user : user+",");
		}else{
			
			Example example = new Example(SystemProcessExecutorConfigRole.class);
			example.createCriteria().andEqualTo("processid", pConfig.getId());
			List<SystemProcessExecutorConfigRole> SystemProcessExecutorConfigRoleList =  systemProcessExecutorConfigRoleMapper.selectByExample(example);
			StringBuffer bf = new StringBuffer();
			if(SystemProcessExecutorConfigRoleList != null && SystemProcessExecutorConfigRoleList.size() >0 ){
				for(int i=0; i<SystemProcessExecutorConfigRoleList.size(); i++){
					bf.append(SystemProcessExecutorConfigRoleList.get(i).getRoleid()).append(",");
				}
			}
			
			String assignee = pConfig.getExecutorExp();
			
			if(!StringUtils.isEmpty(pConfig.getExtendClassname())){
				ProcessExecutorExtendService extendService = (ProcessExecutorExtendService) appContext.getBean(pConfig.getExtendClassname());
				assignee += "|" + extendService.create(task, execution, bf.toString());
			
			}
			task.setAssignee(assignee);
		}
	}
	
	@Override
	public SystemProcessExecutorConfig findOneBycondition(SystemProcessExecutorConfig systemProcessExecutorConfig) {
		
		SystemProcessExecutorConfig temp = null;
		Example example = new Example(SystemProcessExecutorConfig.class);
		example.createCriteria().andEqualTo("processKey", systemProcessExecutorConfig.getProcessKey())
		.andEqualTo("taskKey", systemProcessExecutorConfig.getTaskKey()); 
		
		List<SystemProcessExecutorConfig> result = mapper.selectByExample(example);
		if(result == null) {
			return temp;
		}else{
			return result.get(0);
		}
	}

}

