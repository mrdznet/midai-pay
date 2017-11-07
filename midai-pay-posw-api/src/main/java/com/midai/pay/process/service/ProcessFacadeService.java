package com.midai.pay.process.service;

import java.util.List;
import java.util.Map;

import com.midai.pay.process.po.HistoryTask;
import com.midai.pay.process.po.PendingTask;


public interface ProcessFacadeService {
	
	/**
	 * 开启一个流程
	 * @param processType 流程类型
	 * @param bussinessId 业务ID
	 * @param orgId 组织机构ID
	 * @return
	 */
	List<PendingTask> startBusinessProcess(ProcessTypeEnum processType, String bussinessId, Integer orgId);
	
	/**
	 * 流程启动
	 * 
	 * @param taskId
	 * @param bussinessId
	 * @param uId
	 * @param approval
	 * @return
	 */
	
	List<PendingTask> runBaseProcess(String taskId, String bussinessId, String uId, boolean approval);
	
	List<PendingTask> runAdvanceProcess(String uid,String businessID,String taskID,Map<String,String> processVariables, boolean approval);
	
	/**
	 * 通过用户ID获取代办事项
	 * @param userId
	 * @return
	 */
	List<PendingTask> getPendingTaskByUserID(String userId);
	
	/**
	 * 通过单个角色ID获取代办事项
	 * @param roleId
	 * @return
	 */
	List<PendingTask> getPendingTaskByRoleID(Integer roleId);
	
	/**
	 * 通过单个组织机构ID获取代办事项
	 * @param orgId
	 * @return
	 */
	List<PendingTask> getPendingTaskByOrgID(Integer orgId);
	
	/**
	 * 通过单个用户ID，角色ID列表，组织机构ID获取代办事项
	 * 获取该用户ID的代办事项，或者角色ID将于组织机构ID一起进行判断，获取当前机构内该角色的代办事项
	 * @param userId
	 * @param roleIds
	 * @param orgId
	 * @return
	 */
	@Deprecated
	List<PendingTask> getPendingTaskByUserOrRolesInOrg(String userId,List<Integer> roleIds,Integer orgId);
	
	/**
	 * 通过单个用户ID，角色ID列表，组织机构ID获取代办事项
	 * 获取该用户ID的代办事项，或者角色ID将于组织机构ID一起进行判断，获取当前机构内该角色的代办事项
	 * @param userId
	 * @param roleIds
	 * @param orgIds
	 * @return
	 */
	List<PendingTask> getPendingTaskByUserOrRolesInOrg(String userId,List<Integer> roleIds,List<Integer> orgIds);
	/**
	 * 通过单个用户ID，角色ID列表，组织机构ID获取代办事项
	 * 获取该用户ID的代办事项，或者角色ID将于组织机构ID一起进行判断，获取当前机构内该角色的代办事项
	 * @param userId
	 * @param roleIds
	 * @param orgId
	 * @param start 
	 * @param size  
	 * @return
	 */
	List<PendingTask> getPendingTaskByUserOrRolesInOrg(String[] processDefArr, String userId, List<Integer> roleIds, List<Integer> orgIds,int start,int size);
	/**
	 * 
	 * countPendingTaskByUserOrRolesInOrg:(统计任务数量). <br/>
	 *
	 * @author 陈勋
	 * @param userId
	 * @param roleIds
	 * @param orgId
	 * @return
	 * @since JDK 1.7
	 */
	int countPendingTaskByUserOrRolesInOrg(String[] processDefArr, String userId,List<Integer> roleIds,List<Integer> orgIds);
	
	/**
	 * 通过用户ID列表，角色ID列表，组织机构ID列表获取代办事项
	 * 获取列表用户的，或者角色列表的，或者组织机构列表的代办事项
	 * @param userIds
	 * @param roleIds
	 * @param orgIds
	 * @return
	 */
	List<PendingTask> getPendingTaskByUserOrRoleOrOrgIDs(List<String> userIds,List<Integer> roleIds,List<Integer> orgIds);
	
	/**
	 * 通过用户ID获取已办事项
	 * @param userId
	 * @return
	 */
	List<HistoryTask> getHistoryTaskByUserID(String userId);
	
	/**
	 * 完成一个任务
	 * @param uid   执行人ID
	 * @param businessKey   业务键，唯一标识
	 * @param taskID    任务ID
	 * @param processVariables   流程变量
	 * @return
	 */
	List<PendingTask> completeProcessTask(String uid,String businessKey,String taskID,Map<String,String> processVariables);
	
	/**
	 * 根据任务ID获取一个代办事项
	 * @param taskID
	 * @return
	 */
	PendingTask getPendingTaskByTaskID(String taskID);
	
	void completeTask(String taskId);
	
	/**
	 * 
	 * generateDiagram:(历史流程图). <br/>
	 *
	 * @author 陈勋
	 * @param taskId
	 * @return
	 * @since JDK 1.7
	 */
	String  generateDiagram(String processInstanceId);
}
