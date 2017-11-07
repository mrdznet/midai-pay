package com.midai.pay.customer.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.midai.framework.common.PageEnum;
import com.midai.pay.activiti.service.MidaiActivity;
import com.midai.pay.activiti.service.MidaiActivityParam;
import com.midai.pay.common.constants.Constants;
import com.midai.pay.customer.entity.CustomerStateProcessEnum;
import com.midai.pay.customer.mapper.BoCustomerMapper;
import com.midai.pay.customer.service.CustomerProcessService;
import com.midai.pay.mobile.utils.MassageUtil;
import com.midai.pay.process.po.PendingTask;
import com.midai.pay.process.service.ProcessFacadeService;
import com.midai.pay.process.service.ProcessTypeEnum;
import com.midai.pay.user.entity.SystemUser;
import com.midai.pay.user.mapper.SystemUserRoleMapper;
import com.midai.pay.user.service.SystemUserService;

@Service
public class CustomerProcessServiceImpl implements CustomerProcessService {

	@Autowired
	private ProcessFacadeService processFacadeService;
	
	@Autowired
	private SystemUserRoleMapper systemUserRoleMapper;
	
	@Autowired
	private SystemUserService systemUserService;
	
	@Autowired
	BoCustomerMapper mapper;
	
	@Autowired
	private MassageUtil massageUtil;
	
	@Override
	public void startCustomerApplyProcess(String mercNo, String employeeId, int channel) {
		int inscode = 0;
		
		if(channel == 0){	//网页端来的申请, 传的是当前登录用户的id
			SystemUser user = systemUserService.loadByUserLoginname(employeeId);
			
			if(StringUtils.isBlank(user.getInscode())){
				inscode =Integer.valueOf(Constants.MIFU_INSCODE);
			}else {
				inscode = Integer.valueOf(user.getInscode());
			}
		}else if(channel == 1){	//APP端来的申请, 传的是申请商户的手机号
			inscode = Integer.valueOf(mapper.findInscodeByMobile(employeeId));
		}
		
		List<PendingTask> taskList = processFacadeService.startBusinessProcess(ProcessTypeEnum.merchant_process, mercNo, inscode);
		
		if(null!=taskList && taskList.size()>0){
			PendingTask task = taskList.get(0);
			List<PendingTask> taskList_2 = processFacadeService.runBaseProcess(task.getId(), mercNo, employeeId, true);
			
			/**
			 * 发系统消息
			 */
			String assignee = taskList_2.get(0).getAssignee();
			massageUtil.sendMsgByProcess(PageEnum.dbsx.toString(), assignee);
		}
	}
	
	@MidaiActivity
	@Override
	public void runCustomerApplyProcess(@MidaiActivityParam String taskId, String mercNo, String employeeId, boolean approval, String road) {
		int state = 0;
		List<PendingTask> resList = null;
		
		/**  获取taskId:  手机端特殊处理  */
		if(StringUtils.isEmpty(taskId)) taskId = getTaskId(mercNo, employeeId);
		
		Map<String,String> paraMap = null;
		
		if(StringUtils.isNotEmpty(road)){ // 是否跳过复审
			paraMap = new HashMap<String,String>();
			paraMap.put("road", road);
		}
		
		resList = processFacadeService.runAdvanceProcess(employeeId, mercNo, taskId, paraMap, approval);
		
		if(null!=resList && resList.size()>0){
			PendingTask task = resList.get(0);
			String node = task.getTaskKey();
			
			if(node.equals(CustomerStateProcessEnum.apply.toString())){
				state = CustomerStateProcessEnum.check_un.getId();
				
			}else if(node.equals(CustomerStateProcessEnum.firstTrial.toString())){
				state = CustomerStateProcessEnum.firstTrial.getId();
				
			}else if(node.equals(CustomerStateProcessEnum.recheck.toString())){
				state = CustomerStateProcessEnum.recheck.getId();
			}
			
			// 发送系统消息
			massageUtil.sendMsgByProcess(PageEnum.dbsx.toString(), task.getAssignee());
		}else{
			state = CustomerStateProcessEnum.complete.getId();
		}
		
		mapper.updateState(state, mercNo);
	}
	
	//得到taskId
	public String getTaskId(String orderId, String userId){
		List<Integer> roleIds = systemUserRoleMapper.findAllRoleidByLoginName(userId);
		
		List<Integer> orgIds = getInscode(userId);
		List<PendingTask> ptList = processFacadeService.getPendingTaskByUserOrRolesInOrg(userId, roleIds, orgIds);
		
		if(null!=ptList && ptList.size()>0){
			for(PendingTask pt:ptList){
				if(pt.getBusinessKey().equals(orderId)){
					return pt.getId();
				}
			}
		}
		return "";
	}

	@Override
	public List<PendingTask> customerPendingTaskPages(String employeeId, int start, int size) {
		List<Integer> roleIds = systemUserRoleMapper.findAllRoleidByLoginName(employeeId);
		
		List<Integer> orgIds = getInscode(employeeId);
		
		List<PendingTask> ptList = processFacadeService.getPendingTaskByUserOrRolesInOrg(new String[]{"merchant_process", "merchant_bank_process"}, employeeId, 
				roleIds, orgIds, start, size);
		
		return ptList;
	}

	@Override
	public int customerPendingTaskCount(String employeeId) {
		List<Integer> roleIds = systemUserRoleMapper.findAllRoleidByLoginName(employeeId);
		List<Integer> orgIds = getInscode(employeeId);
		
		int count = processFacadeService.countPendingTaskByUserOrRolesInOrg(new String[]{"merchant_process", "merchant_bank_process"},employeeId, 
				roleIds, orgIds);
		
		return count;
	}

	@Override
	public int countPendingTask(String employeeId) {
		List<Integer> roleIds = systemUserRoleMapper.findAllRoleidByLoginName(employeeId);
		List<Integer> orgIds = getInscode(employeeId);
		
		int count = processFacadeService.countPendingTaskByUserOrRolesInOrg(new String[]{"merchant_process"}, employeeId, 
				roleIds, orgIds);
		
		return count;
	}
	
	public List<Integer> getInscode(String employeeId){
		List<Integer> inscodeList = new ArrayList<Integer>();
		
		SystemUser user = systemUserService.loadByUserLoginname(employeeId);
		
		if(null != user && StringUtils.isNotEmpty(user.getInscode())){
			inscodeList.add(Integer.valueOf(user.getInscode()));
		}else{
			inscodeList.add(Integer.valueOf(Constants.MIFU_INSCODE));	
			inscodeList.add(Integer.valueOf(Constants.HAIBEI_INSCODE));
			inscodeList.add(Integer.valueOf(Constants.ZHANGFU_INSCODE));
		}
		
		return inscodeList;
	}
}
