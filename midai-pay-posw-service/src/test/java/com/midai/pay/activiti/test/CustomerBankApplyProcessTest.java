package com.midai.pay.activiti.test;

import java.util.ArrayList;
import java.util.List;
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
public class CustomerBankApplyProcessTest {
	
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
		String businessId = UUID.randomUUID().toString(); //业务Id
		System.out.println("--------------------------------------businessId:" + businessId);
		
		List<PendingTask> ptList = processFacadeService.startBusinessProcess(ProcessTypeEnum.merchant_bank_process, businessId, 0);
		System.out.println(ptList.size());
		
		//apply(r101), review(r102)
		Assert.assertEquals("review", completeTask(businessId,"101",true));
		
		//case 1.0: 通过
//		Assert.assertEquals("complete", completeTask(businessId,"102",true));
		//case 1.1: 拒绝
		Assert.assertEquals("complete", completeTask(businessId,"102",false));
		
	}
	
	private String completeTask(String businessId, String uId, boolean approval){
		List<Integer> roleIds = new ArrayList<Integer>();
		roleIds.add(Integer.parseInt(uId));
		
		List<Integer> orgIds = new ArrayList<Integer>(); //组织机构
		orgIds.add(105);
		
		List<PendingTask> ptList = processFacadeService.getPendingTaskByUserOrRolesInOrg(uId, roleIds, orgIds);
		
		String processStatus = null;
		for(PendingTask task : ptList){
			if(task.getBusinessKey().equals(businessId)){
				List<PendingTask> pendinglist = processFacadeService.runBaseProcess(task.getId(), businessId, uId, approval);
				
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
	
}
