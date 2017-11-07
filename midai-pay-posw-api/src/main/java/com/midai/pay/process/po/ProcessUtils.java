package com.midai.pay.process.po;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.task.Task;


public class ProcessUtils{
	public static int PARAMETERS_ERROR=401;
	
	public static PendingTask BuildPendingTaskByTask(Task t){
		if(t==null){
			return null;
		}
		PendingTask pt = new PendingTask();
		BuildProcessTaskByTask(pt,t);
		return pt;
	}
	
	public static HistoryTask BuildHistoryTaskByTask(Task t){
		if(t==null){
			return null;
		}
		HistoryTask ht = new HistoryTask();
		BuildProcessTaskByTask(ht, t);
		return ht;
	}
	
	public static List<PendingTask> BuildListPendingTaskByTaskList(List<Task> tList){
		if(tList==null){
			return null;
		}
		List<PendingTask> pendingTaskList = new ArrayList<PendingTask>();
		for(Task t:tList){
			pendingTaskList.add(BuildPendingTaskByTask(t));
		}
		return pendingTaskList;
	}
	
	private static void BuildProcessTaskByTask(ProcessTask pt,Task t){
		pt.setId(t.getId());
		pt.setName(t.getName());
		pt.setDescription(t.getDescription());
		pt.setCreateTime(t.getCreateTime());
		pt.setAssignee(t.getAssignee());
		pt.setProcessKey(t.getProcessDefinitionId().split(":")[0]);
		pt.setTaskKey(t.getTaskDefinitionKey());
		pt.setFormKey(t.getFormKey());
		pt.setBusinessKey(t.getCategory());
	}
	
	public static HistoryTask BuildHistoryTaskByHistoryInstance(HistoricTaskInstance hti){
		if(hti==null){
			return null;
		}
		HistoryTask ht = new HistoryTask();
		ht.setId(hti.getExecutionId());
		ht.setName(hti.getName());
		ht.setDescription(hti.getDescription());
		ht.setCreateTime(hti.getCreateTime());
		ht.setAssignee(hti.getAssignee());
		ht.setProcessKey(hti.getProcessDefinitionId().split(":")[0]);
		ht.setTaskKey(hti.getTaskDefinitionKey());
		ht.setBusinessKey(hti.getCategory());
		return ht;
	}
	
	public static List<HistoryTask> BuildHistoryTaskByHistoryInstanceList(List<HistoricTaskInstance> htiList){
		if(htiList==null){
			return null;
		}
		List<HistoryTask> historyTaskList = new ArrayList<HistoryTask>();
		for(HistoricTaskInstance hti:htiList){
			historyTaskList.add(BuildHistoryTaskByHistoryInstance(hti));
		}
		return historyTaskList;
	}
}
