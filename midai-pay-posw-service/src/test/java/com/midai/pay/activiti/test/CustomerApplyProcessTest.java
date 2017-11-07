package com.midai.pay.activiti.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.test.Deployment;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.midai.pay.process.po.PendingTask;
import com.midai.pay.process.service.ProcessFacadeService;
import com.midai.pay.process.service.ProcessTypeEnum;
import com.midai.pay.user.service.SystemProcessExecutorConfigService;


@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomerApplyProcessTest {
	
	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private HistoryService historyService;
	
	@Autowired
	private SystemProcessExecutorConfigService processExecutorConfigService;
	
	@Autowired
	private ProcessFacadeService processFacadeService;
	
	
	@Test
	@Deployment
	public void productProcessTest(){
		/*String businessId = UUID.randomUUID().toString(); //业务Id
		System.out.println("--------------------------------------businessId:" + businessId);
		
		List<PendingTask> ptList = processFacadeService.startBusinessProcess(ProcessTypeEnum.merchant_process, businessId, 0);
		System.out.println(ptList.size());
		
		//apply(r90), firstTrial(r91), recheck(r92)
		Assert.assertEquals("firstTrial", completeTask(businessId, "90", null, true));
		Assert.assertEquals("apply", completeTask(businessId, "91", null, false));
		
		Assert.assertEquals("firstTrial", completeTask(businessId,"90",null, true));
		
		// case 1.0 : 走复审
		Assert.assertEquals("recheck", completeTask(businessId,"91", makeMapfromString(new String[]{"road","2"}), true));
		Assert.assertEquals("firstTrial", completeTask(businessId,"92", null, false));
		
		Assert.assertEquals("recheck", completeTask(businessId,"91", makeMapfromString(new String[]{"road","2"}), true));
		Assert.assertEquals("complete", completeTask(businessId,"92", null, true));*/
		
		// case 1.1 : 跳过复审
//		Assert.assertEquals("complete", completeTask(businessId,"91",makeMapfromString(new String[]{"road","3"}), true));
	}
	
	private String completeTask(String businessId, String uId, Map<String,String> map, boolean approval){
		List<Integer> roleIds = new ArrayList<Integer>();
		roleIds.add(Integer.parseInt(uId));
		
		List<Integer> orgIds = new ArrayList<Integer>(); //组织机构
		orgIds.add(105);
		
		List<PendingTask> ptList = processFacadeService.getPendingTaskByUserOrRolesInOrg(uId, roleIds, orgIds);
		
		String processStatus = null;
		for(PendingTask task : ptList){
			if(task.getBusinessKey().equals(businessId)){
				List<PendingTask> pendinglist = processFacadeService.runAdvanceProcess(uId, businessId, task.getId(), map, approval);
				
				if(null==pendinglist || pendinglist.size()==0){
					processStatus =  "complete";
				} else{
					for(PendingTask pending : pendinglist){
						processStatus = pending.getTaskKey();
					}
				}
			}
		}
		System.out.println("result: " + processStatus);
		return processStatus;
	}
	
	private Map<String,String> makeMapfromString(String[]... strs){
		if(strs==null){
			return null;
		}
		Map<String,String> map = new HashMap<String,String>();
		for(String[] ss:strs){
			map.put(ss[0], ss[1]);
		}
		return map;
	}
	
	
	@Test
	@Deployment
	public void existTask(){
		PendingTask task = processFacadeService.getPendingTaskByTaskID("eef9f7c6-abdd-11e6-9f60-448a5b9b215799999999");
		System.out.println(task);
	}
}
