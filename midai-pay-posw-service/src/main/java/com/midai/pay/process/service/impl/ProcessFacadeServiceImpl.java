package com.midai.pay.process.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.task.NativeTaskQuery;
import org.activiti.engine.task.Task;
import org.activiti.image.ProcessDiagramGenerator;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.alibaba.dubbo.config.annotation.Service;
import com.midai.pay.process.po.HistoryTask;
import com.midai.pay.process.po.PendingTask;
import com.midai.pay.process.po.ProcessUtils;
import com.midai.pay.process.service.ProcessFacadeService;
import com.midai.pay.process.service.ProcessTypeEnum;
import com.midai.pay.process.service.ProcessVariablesEnum;
import com.midai.pay.process.service.ProcessVariablesValuesEnum;

@Service(version = "1.0.0", protocol = "dubbo")
public class ProcessFacadeServiceImpl implements ProcessFacadeService {

	@Autowired
	private TaskService taskService;

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private HistoryService historyService;

	@Autowired
	RepositoryService repositoryService;

	@Autowired
	ProcessEngineConfiguration processEngineConfiguration;

	@Autowired
	ProcessEngineFactoryBean processEngine;

	@Override
	public List<PendingTask> startBusinessProcess(ProcessTypeEnum processType, String bussinessId, Integer orgId) {
		Map<String, String> map = null;
		StringBuilder sb = null;

		if (orgId != null) {
			sb = new StringBuilder();
			sb.append("o").append(orgId);
		}

		if (sb != null) {
			map = new HashMap<String, String>();
			map.put(ProcessVariablesEnum.include_orgs.toString(), sb.toString());
		}

		return startProcess(processType, bussinessId, map);
	}
	
	@Override
	public List<PendingTask> runBaseProcess(String taskId, String bussinessId, String uId, boolean approval) {
		Map<String, String> processVariables = new HashMap<String, String>();

		if (approval)
			processVariables.put(ProcessVariablesEnum.approval.toString(),
					ProcessVariablesValuesEnum.approval_approve.toString());
		else
			processVariables.put(ProcessVariablesEnum.approval.toString(),
					ProcessVariablesValuesEnum.approval_reject.toString());

		return this.claimAndCompleteTask(uId, bussinessId, taskId,
				processVariables);
	}

	@Override
	public List<PendingTask> runAdvanceProcess(String uid, String businessID, String taskID,
			Map<String, String> processVariables, boolean approval) {
		
		if (processVariables == null) {
			processVariables = new HashMap<String, String>();
		}
		if (approval)
			processVariables.put(ProcessVariablesEnum.approval.toString(),
					ProcessVariablesValuesEnum.approval_approve.toString());
		else
			processVariables.put(ProcessVariablesEnum.approval.toString(),
					ProcessVariablesValuesEnum.approval_reject.toString());
		
		return completeProcessTask(uid, businessID, taskID, processVariables);
	}
	
	public List<PendingTask> startProcess(ProcessTypeEnum processType,
			String businessKey, Map<String, String> processVariables) {
		Map<String, Object> map = new HashMap<String, Object>();

		if (processVariables != null) {
			for (Entry<String, String> entry : processVariables.entrySet()) {
				map.put(entry.getKey(), entry.getValue());
			}
		}
		map.put("_ACTIVITI_SKIP_EXPRESSION_ENABLED", true); // 跳出莫个节点
		this.runtimeService.startProcessInstanceByKey(
				processType.getProcessDefID(), businessKey, map);
		return getCurrentProcessPendingTasksByBusiness(businessKey);
	}

	private List<PendingTask> getCurrentProcessPendingTasksByBusiness(
			String businessKey) {
		List<Task> taskList = this.taskService.createTaskQuery()
				.processInstanceBusinessKey(businessKey).list();
		return taskList == null ? null : ProcessUtils
				.BuildListPendingTaskByTaskList(taskList);
	}

	@Override
	public List<PendingTask> getPendingTaskByUserID(String userId) {
		if (userId == null) {
			return null;
		}
		List<String> userList = new ArrayList<String>();
		userList.add(userId);
		return getPendingTaskByUserOrRoleOrOrgIDs(userList, null, null);
	}

	@Override
	public List<PendingTask> getPendingTaskByRoleID(Integer roleId) {
		if (roleId == null) {
			return null;
		}
		List<Integer> roleList = new ArrayList<Integer>();
		roleList.add(roleId);
		return getPendingTaskByUserOrRoleOrOrgIDs(null, roleList, null);
	}

	@Override
	public List<PendingTask> getPendingTaskByOrgID(Integer orgId) {
		if (orgId == null) {
			return null;
		}
		List<Integer> orgList = new ArrayList<Integer>();
		orgList.add(orgId);
		return getPendingTaskByUserOrRoleOrOrgIDs(null, null, orgList);
	}

	@Override
	public List<PendingTask> getPendingTaskByUserOrRoleOrOrgIDs(List<String> userIds, List<Integer> roleIds, List<Integer> orgIds) {
		StringBuffer sb = new StringBuffer();
		if (userIds != null) {
			for (String uid : userIds) {
				sb.append("u").append(uid).append(",");
			}
		}

		if (roleIds != null) {
			for (Integer rid : roleIds) {
				sb.append("r").append(rid).append(",");
			}
		}

		if (orgIds != null) {
			for (Integer oid : orgIds) {
				sb.append("o").append(oid).append(",");
			}
		}

		return ProcessUtils.BuildListPendingTaskByTaskList(heloMakeNativeTaskQuery(sb.toString()));
	}

	@Deprecated
	@Override
	public List<PendingTask> getPendingTaskByUserOrRolesInOrg(String userId, List<Integer> roleIds, Integer orgId) {
		StringBuffer sb = new StringBuffer();
		
		if (userId != null) {
			sb.append("u").append(userId).append(",");
		}

		if (roleIds != null) {
			if (orgId != null) {
				for (Integer rid : roleIds) {
					sb.append("r").append(rid).append("o").append(orgId).append(",");
				}
			} else {
				for (Integer rid : roleIds) {
					sb.append("r").append(rid).append(",");
				}
			}
		}

		return ProcessUtils.BuildListPendingTaskByTaskList(heloMakeNativeTaskQuery(sb.toString()));
	}
	
	@Override
	public List<PendingTask> getPendingTaskByUserOrRolesInOrg(String userId, List<Integer> roleIds, List<Integer> orgIds) {
		StringBuffer sb = new StringBuffer();
		
		if(null != userId)	sb.append("u").append(userId).append(",");
		
		if(null != roleIds && roleIds.size()>0){
			for(Integer roleId : roleIds){
				sb.append("r").append(roleId).append(",");
			}
		}
		
		if(null != orgIds && orgIds.size()>0){
			for(Integer orgId : orgIds){
				sb.append("o").append(orgId).append(",");
			}
		}
		
		return ProcessUtils.BuildListPendingTaskByTaskList(heloMakeNativeTaskQuery(sb.toString()));
	}
	@Override
	public int countPendingTaskByUserOrRolesInOrg(String[] processDefArr, String userId, List<Integer> roleIds, List<Integer> orgIds) {
		StringBuffer sb_ = new StringBuffer();
		
		if(null != userId)	sb_.append("u").append(userId).append(",");
		
		if(null != roleIds && roleIds.size()>0){
			for(Integer roleId : roleIds){
				sb_.append("r").append(roleId).append(",");
			}
		}
		
		if(null != orgIds && orgIds.size()>0){
			for(Integer orgId : orgIds){
				sb_.append("o").append(orgId).append(",");
			}
		}
		
		StringBuilder sb = new StringBuilder("select count(1) from ACT_RU_TASK RES WHERE ");
		
		if(!StringUtils.isEmpty(processDefArr)){
			String orString = " or ";
			sb.append(" ( ");
			
			for(String processDef : processDefArr){
				sb.append(" PROC_DEF_ID_ like '%"+processDef.trim()+"%' ").append(orString);
			}
			sb.delete(sb.length() - orString.length(), sb.length());
			
			sb.append(" ) and ( ");
		}
		
		sb.append("  #{parameter} REGEXP RES.ASSIGNEE_ ");
		
		if(!StringUtils.isEmpty(processDefArr)) sb.append(" ) ");
		
		NativeTaskQuery ntq = taskService.createNativeTaskQuery().sql(sb.toString());

		ntq.parameter("parameter", sb_.toString());
		return (int) ntq.count();
	}

	@Override
	public List<PendingTask> getPendingTaskByUserOrRolesInOrg(String[] processDefArr, String userId, List<Integer> roleIds, List<Integer> orgIds, int start, int size) {
		StringBuffer sb = new StringBuffer();
		
		if(null != userId)	sb.append("u").append(userId).append(",");
		
		if(null != roleIds && roleIds.size()>0){
			for(Integer roleId : roleIds){
				sb.append("r").append(roleId).append(",");
			}
		}
		
		if(null != orgIds && orgIds.size()>0){
			for(Integer orgId : orgIds){
				sb.append("o").append(orgId).append(",");
			}
		}
		
		return ProcessUtils.BuildListPendingTaskByTaskList(heloMakeNativeTaskQuery(processDefArr, sb.toString(), start, size));
	}

	private List<Task> heloMakeNativeTaskQuery(String[] processDefArr, String parameterStr, int start, int size) {
		StringBuilder sb = new StringBuilder("select RES.* from ACT_RU_TASK RES WHERE ");
		
		if(!StringUtils.isEmpty(processDefArr)){
			String orString = " or ";
			sb.append(" ( ");
			
			for(String processDef : processDefArr){
				sb.append(" PROC_DEF_ID_ like '%"+processDef.trim()+"%' ").append(orString);;
			}
			sb.delete(sb.length() - orString.length(), sb.length());
			
			sb.append(" ) and ( ");
		}
		
		sb.append("  #{parameter} REGEXP RES.ASSIGNEE_ ");

		if(!StringUtils.isEmpty(processDefArr)) sb.append(" ) ");
		
		sb.append(" order by CREATE_TIME_ desc ");

		sb.append(" limit " + start + " ," + size);

		NativeTaskQuery ntq = taskService.createNativeTaskQuery().sql(sb.toString());

		ntq.parameter("parameter", parameterStr);
		
		return ntq.list();
	}

	private List<Task> heloMakeNativeTaskQuery(String parameterStr) {
		StringBuilder sb = new StringBuilder("select RES.* from ACT_RU_TASK RES WHERE ");

		sb.append("  #{parameter} REGEXP RES.ASSIGNEE_ ");
		
		sb.append(" order by CREATE_TIME_ asc ");

		NativeTaskQuery ntq = taskService.createNativeTaskQuery().sql(sb.toString());
		
		ntq.parameter("parameter", parameterStr);
		
		return ntq.list();
	}

	@Override
	public List<HistoryTask> getHistoryTaskByUserID(String userId) {
		List<HistoricTaskInstance> histList = historyService
				.createHistoricTaskInstanceQuery().taskOwner("u" + userId)
				.list();
		return ProcessUtils.BuildHistoryTaskByHistoryInstanceList(histList);
	}


	@Override
	public List<PendingTask> completeProcessTask(String uid,
			String businessKey, String taskID,
			Map<String, String> processVariables) {
		return claimAndCompleteTask(uid, businessKey, taskID, processVariables);
	}

	private List<PendingTask> claimAndCompleteTask(String uid,
			String businessKey, String taskID,
			Map<String, String> processVariables) {
		Map<String, Object> map = null;
		if (processVariables != null) {
			map = new HashMap<String, Object>();
			for (Entry<String, String> entry : processVariables.entrySet()) {
				map.put(entry.getKey(), entry.getValue());
			}
		}
		// 让流程记住每个任务的最后执行人
		Task task = this.taskService.createTaskQuery().taskId(taskID)
				.singleResult();

		if (null != task)
			map.put(task.getTaskDefinitionKey(), "u" + uid);

		this.taskService.setOwner(taskID, "u" + uid);
		this.taskService.complete(taskID, map);

		return getCurrentProcessPendingTasksByBusiness(businessKey);
	}

	@Override
	public PendingTask getPendingTaskByTaskID(String taskID) {
		return ProcessUtils.BuildPendingTaskByTask(taskService
				.createTaskQuery().taskId(taskID).singleResult());
	}

	@Override
	public void completeTask(String taskId) {
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		runtimeService.deleteProcessInstance(task.getProcessInstanceId(), "canceled");
	}

	@Override
	public String generateDiagram(String processInstanceId) {

		Assert.hasText(processInstanceId);
		// 获取历史流程实例
		HistoricProcessInstance processInstance = historyService
				.createHistoricProcessInstanceQuery()
				.processInstanceId(processInstanceId).singleResult();
		// 获取流程图
		BpmnModel bpmnModel = repositoryService.getBpmnModel(processInstance
				.getProcessDefinitionId());
		processEngineConfiguration = processEngine
				.getProcessEngineConfiguration();
		Context.setProcessEngineConfiguration((ProcessEngineConfigurationImpl) processEngineConfiguration);

		ProcessDiagramGenerator diagramGenerator = processEngineConfiguration
				.getProcessDiagramGenerator();
		ProcessDefinitionEntity definitionEntity = (ProcessDefinitionEntity) repositoryService
				.getProcessDefinition(processInstance.getProcessDefinitionId());

		List<HistoricActivityInstance> highLightedActivitList = historyService
				.createHistoricActivityInstanceQuery()
				.processInstanceId(processInstanceId).list();
		// 高亮环节id集合
		List<String> highLightedActivitis = new ArrayList<String>();
		// 高亮线路id集合
		List<String> highLightedFlows = getHighLightedFlows(definitionEntity,
				highLightedActivitList);

		for (HistoricActivityInstance tempActivity : highLightedActivitList) {
			String activityId = tempActivity.getActivityId();
			highLightedActivitis.add(activityId);
		}

		// 中文显示的是口口口，设置字体就好了
		InputStream imageStream = diagramGenerator.generateDiagram(bpmnModel,
				"png", highLightedActivitis, highLightedFlows, "宋体", "宋体",
				null, null, 1.0);
		
		// 单独返回流程图，不高亮显示
		// InputStream imageStream =
		// diagramGenerator.generatePngDiagram(bpmnModel);
		// 输出资源内容到相应对象
		
		
		byte[] data = null;
		// 读取图片字节数组
		try {
		data = new byte[imageStream.available()];
		imageStream.read(data);
		
		} catch (IOException e) {
		e.printStackTrace();
		}finally{
			try {
				if(imageStream!=null)
				imageStream.close();
			} catch (IOException e) {
				
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}
		}
		// 对字节数组Base64编码
		Base64 coder=new Base64();
		
		return coder.encodeToString(data);// 返回Base64编码过的字节数组字符串
	}

	/**
	 * 获取需要高亮的线
	 * 
	 * @param processDefinitionEntity
	 * @param historicActivityInstances
	 * @return
	 */
	private List<String> getHighLightedFlows(
			ProcessDefinitionEntity processDefinitionEntity,
			List<HistoricActivityInstance> historicActivityInstances) {
		List<String> highFlows = new ArrayList<String>();// 用以保存高亮的线flowId
		for (int i = 0; i < historicActivityInstances.size() - 1; i++) {// 对历史流程节点进行遍历
			ActivityImpl activityImpl = processDefinitionEntity
					.findActivity(historicActivityInstances.get(i)
							.getActivityId());// 得到节点定义的详细信息
			List<ActivityImpl> sameStartTimeNodes = new ArrayList<ActivityImpl>();// 用以保存后需开始时间相同的节点
			ActivityImpl sameActivityImpl1 = processDefinitionEntity
					.findActivity(historicActivityInstances.get(i + 1)
							.getActivityId());
			// 将后面第一个节点放在时间相同节点的集合里
			sameStartTimeNodes.add(sameActivityImpl1);
			for (int j = i + 1; j < historicActivityInstances.size() - 1; j++) {
				HistoricActivityInstance activityImpl1 = historicActivityInstances
						.get(j);// 后续第一个节点
				HistoricActivityInstance activityImpl2 = historicActivityInstances
						.get(j + 1);// 后续第二个节点
				if (activityImpl1.getStartTime().equals(
						activityImpl2.getStartTime())) {
					// 如果第一个节点和第二个节点开始时间相同保存
					ActivityImpl sameActivityImpl2 = processDefinitionEntity
							.findActivity(activityImpl2.getActivityId());
					sameStartTimeNodes.add(sameActivityImpl2);
				} else {
					// 有不相同跳出循环
					break;
				}
			}
			List<PvmTransition> pvmTransitions = activityImpl
					.getOutgoingTransitions();// 取出节点的所有出去的线
			for (PvmTransition pvmTransition : pvmTransitions) {
				// 对所有的线进行遍历
				ActivityImpl pvmActivityImpl = (ActivityImpl) pvmTransition
						.getDestination();
				// 如果取出的线的目标节点存在时间相同的节点里，保存该线的id，进行高亮显示
				if (sameStartTimeNodes.contains(pvmActivityImpl)) {
					highFlows.add(pvmTransition.getId());
				}
			}
		}
		return highFlows;
	}

}
