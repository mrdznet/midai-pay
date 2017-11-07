package com.midai.pay.activiti.service;



import java.util.List;

import org.activiti.engine.task.Task;

public interface BaseActivitiService {
	/**
	 * 
	 * startProcessInstance:(启动一个流程实例). <br/>
	 *
	 * @author 陈勋
	 * @param processDefinitionId 流程定义编号
	 * @return 流程实例编号
	 * @since JDK 1.7
	 */
	public String startProcessInstance(String processDefinitionId);
	/**
	 * 
	 * findTaskList:(获取当前任务). <br/>
	 * @author 陈勋
	 * @param assignee  角色
	 * @param firstResult 开始记录数
	 * @param maxResults  结束记录数
	 * @return 
	 * @since JDK 1.7
	 */
	public List<Task> findTaskList(String assignee,int firstResult,int maxResults);
	/**
	 * 
	 * findTaskCount:(获取当前任务统计). <br/>
	 * @author 陈勋
	 * @param assignee  角色
	 * @return
	 * @since JDK 1.7
	 */
	public long findTaskCount(String assignee);
	
	

}
