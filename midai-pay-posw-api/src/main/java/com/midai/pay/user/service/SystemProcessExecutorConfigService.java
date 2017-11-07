/**
 * Project Name:chenxun-framework-start
 * File Name:UserService.java
 * Package Name:com.chenxun.framework.service
 * Date:2016年8月31日下午5:37:10
 * Copyright (c) 2016, www midaigroup com Technology Co., Ltd. All Rights Reserved.
 *
*/

package com.midai.pay.user.service;

import java.util.List;
import java.util.Map;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;

import com.midai.framework.common.BaseService;
import com.midai.pay.user.entity.SystemProcessExecutorConfig;
import com.midai.pay.user.vo.SystemProcessExecutorConfigSVo;

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
public interface SystemProcessExecutorConfigService extends BaseService<SystemProcessExecutorConfig>{

	public Map<String, List<SystemProcessExecutorConfigSVo>> getAllMap();
	
	public SystemProcessExecutorConfig findOneBycondition(SystemProcessExecutorConfig systemProcessExecutorConfig);
	
	public int findAllCount();
	
	public int updateBycondition(SystemProcessExecutorConfig systemProcessExecutorConfig); 
	
	public void delBycondition(SystemProcessExecutorConfig processExecutorConfigModel);
	
	public SystemProcessExecutorConfig findOne(SystemProcessExecutorConfig processExecutorConfig);
	
	public void assignTo(DelegateTask task);
	
	public void initTask(DelegateTask task,DelegateExecution execution);
	
}

