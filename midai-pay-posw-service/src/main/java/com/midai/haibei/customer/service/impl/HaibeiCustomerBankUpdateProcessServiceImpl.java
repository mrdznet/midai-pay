package com.midai.haibei.customer.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.config.annotation.Service;
import com.midai.framework.common.BaseServiceImpl;
import com.midai.framework.common.PageEnum;
import com.midai.haibei.customer.service.HaibeiCustomerBankUpdateProcessService;
import com.midai.pay.activiti.service.MidaiActivity;
import com.midai.pay.activiti.service.MidaiActivityParam;
import com.midai.pay.common.utils.SmsModeIdHelper;
import com.midai.pay.common.utils.SmsSender;
import com.midai.pay.customer.entity.BoCustomer;
import com.midai.pay.customer.entity.BoCustomerTemp;
import com.midai.pay.customer.mapper.BoCustomerImgMapper;
import com.midai.pay.customer.mapper.BoCustomerMapper;
import com.midai.pay.customer.mapper.BoCustomerTempMapper;
import com.midai.pay.mobile.utils.MassageUtil;
import com.midai.pay.process.po.PendingTask;
import com.midai.pay.process.service.ProcessFacadeService;
import com.midai.pay.process.service.ProcessTypeEnum;
import com.midai.pay.user.mapper.SystemUserRoleMapper;
import com.midai.pay.user.service.SystemOrganizationService;
import com.midai.pay.user.service.SystemUserService;

/**
 * 海贝商户更改入账行流程
 * 
 * @author Feng
 *
 */
@Service
@Transactional
public class HaibeiCustomerBankUpdateProcessServiceImpl extends BaseServiceImpl<BoCustomerTemp>
		implements HaibeiCustomerBankUpdateProcessService {

	private Logger logger = LoggerFactory.getLogger(HaibeiCustomerBankUpdateProcessServiceImpl.class);

	public HaibeiCustomerBankUpdateProcessServiceImpl(BoCustomerTempMapper mapper) {
		super(mapper);
		this.mapper = mapper;
	}

	private final BoCustomerTempMapper mapper;

	@Autowired
	private BoCustomerMapper boCustomerMapper;

	@Autowired
	private BoCustomerImgMapper boCustomerImgMapper;

	@Autowired
	private ProcessFacadeService processFacadeService;

	@Autowired
	private SystemUserRoleMapper systemUserRoleMapper;

	@Autowired
	private SystemUserService systemUserService;

	@Autowired
	private SystemOrganizationService systemOrganizationService;

	private static int UNTHROUGH_STATE = 2;

	private static int THROUGH_STATE = 3;

	@Autowired
	private MassageUtil massageUtil;
	
	@Autowired
	private SmsSender smsSender;
	
	@Override
	public void startHaibeiCustomerBankUpdateProcess(String mercNo, String employeeId) {
		List<PendingTask> taskList = processFacadeService.startBusinessProcess(ProcessTypeEnum.merchant_bank_process,
				mercNo, 0);

		if (null != taskList && taskList.size() > 0) {
			PendingTask task = taskList.get(0);

			List<PendingTask> taskList_2 = processFacadeService.runBaseProcess(task.getId(), mercNo, employeeId, true);
			
			/**
			 * 发系统消息
			 */
			String assignee = taskList_2.get(0).getAssignee();
			massageUtil.sendMsgByProcess(PageEnum.dbsx.toString(), assignee);
			
			//更新商户状态 
			boCustomerMapper.updateState(6, mercNo);
		}
	}
	
	@MidaiActivity
	@Override
	public void runHaibeiCustomerBankUpdateProcess(@MidaiActivityParam String taskId, String mercNo, String employeeId, boolean approval) {
		if(StringUtils.isBlank(taskId)) taskId = getTaskId(mercNo, employeeId); // 获取taskId

		processFacadeService.runBaseProcess(taskId, mercNo, employeeId, approval);

		// 更新临时表状态
		int state = (approval == true) ? THROUGH_STATE : UNTHROUGH_STATE;
		mapper.updateState(mercNo, state);
		
		//更新商户状态 4
		int customer_state = (approval == true) ? 4 : 7;
		boCustomerMapper.updateState(customer_state, mercNo);
	}

	// 得到taskId
	public String getTaskId(String orderId, String userId) {
		List<Integer> roleIds = systemUserRoleMapper.findAllRoleidByLoginName(userId);

		int orgId = systemUserService.loadByUserLoginname(userId).getOrgid();
		List<Integer> orgIds = systemOrganizationService.findThemselvesAndElderGenerationNodeId(orgId);

		List<PendingTask> ptList = processFacadeService.getPendingTaskByUserOrRolesInOrg(userId, roleIds, orgIds);

		if (null != ptList && ptList.size() > 0) {
			for (PendingTask pt : ptList) {
				if (pt.getBusinessKey().equals(orderId)) {
					return pt.getId();
				}
			}
		}
		return "";
	}

	/**
	 * 海贝商户申请
	 */
	@Override
	@Transactional
	public int haibeiApply(BoCustomerTemp boCustomerTemp) {
			
		BoCustomerTemp vo = mapper.selectBoCustomerTemp(boCustomerTemp.getMercNo());
		if (vo!=null) {
			return 0;
		}
		/* 海贝商户申请insert信息到临时表 */
		boCustomerTemp.setCreateTime(new Date());
		int num = mapper.insertBoCustomerTemp(boCustomerTemp);
		
		// 工作流的
		this.startHaibeiCustomerBankUpdateProcess(boCustomerTemp.getMercNo(), boCustomerTemp.getMobile());
		
		return num;
	}

	/**
	 * 海贝商户申请审核
	 */
	@Override
	@Transactional
	public String haibeiCheck(BoCustomerTemp boCustomerTemp,String name) {
		String result = "";
		BoCustomer boCustomer = new BoCustomer();
		boCustomer.setAccountName(boCustomerTemp.getAccountName());
		boCustomer.setAccountNo(boCustomerTemp.getAccountNo());
		boCustomer.setBankId(boCustomerTemp.getBankId());
		boCustomer.setBranchbankId(boCustomerTemp.getBranchBankId());
		boCustomer.setBranchbankName(boCustomerTemp.getBranchBankName());
		
		boCustomer.setPeovinceId(boCustomerTemp.getProvinceId());
		boCustomer.setCityId(boCustomerTemp.getCityId());
		
		boCustomer.setMercId(boCustomerTemp.getMercId());
		boCustomer.setMercNo(boCustomerTemp.getMercNo());
		
		if (boCustomerTemp.getReviewResult() == 0) {
			/* 商户申请修改更新Customer信息 */
			boCustomerMapper.updateHaibeCustomer(boCustomer);

			/* 审核通过更新img */
			boCustomerImgMapper.updateBank(boCustomerTemp.getPicPath(), boCustomerTemp.getMercNo());

			// 调用工作流
			this.runHaibeiCustomerBankUpdateProcess(boCustomerTemp.getTaskId(), boCustomerTemp.getMercNo(), name, true);
			
			// 发短信
			BoCustomer temp = boCustomerMapper.selectByMercNo(boCustomerTemp.getMercNo());
			StringBuilder bs=new StringBuilder();
			/*bs.append("尊敬的").append(temp.getMercName()).append("用户: 您申请修改银行卡变更审核已通过。");*/
			
			ArrayList<String> list=new ArrayList<String>();
			list.add(temp.getMercName());list.add(bs.toString());
			try {
				smsSender.sendNotice(temp.getMobile(), list, SmsModeIdHelper.getSmsModeId(11));
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("************************更改开户行申请审核通过, 发送短信失败*******************************");
			}
			
			result = "通过";
		} else if (boCustomerTemp.getReviewResult() == 1) {
			// 调用工作流
			this.runHaibeiCustomerBankUpdateProcess(boCustomerTemp.getTaskId(), boCustomerTemp.getMercNo(),name, false);
			
			// 发短信
			BoCustomer temp = boCustomerMapper.selectByMercNo(boCustomerTemp.getMercNo());
			StringBuilder bs=new StringBuilder();
	/*		bs.append("尊敬的").append(temp.getMercName()).append("用户: 您申请修改银行卡变更审核未通过。原因："+boCustomerTemp.getAdvice());*/
			bs.append(boCustomerTemp.getAdvice());
			ArrayList<String> list=new ArrayList<String>();
			list.add(temp.getMercName());list.add(bs.toString());
			try {
				smsSender.sendNotice(temp.getMobile(), list, SmsModeIdHelper.getSmsModeId(12));
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("************************更改开户行申请审核拒绝, 发送短信失败*******************************");
			}
			
			result = "拒绝";
		}

		/* 审核不通过更新状态为处理 */
		mapper.updateDelState(boCustomerTemp);

		return result;
	}

	@Override
	public BoCustomerTemp loadBoCustomerBymercNo(String mercNo) {
		BoCustomerTemp vo=new BoCustomerTemp();
		BoCustomer boCustomer=boCustomerMapper.selectByMercNo(mercNo);
		if (boCustomer!=null) {
			vo.setAccountName(boCustomer.getAccountName());
			vo.setAccountNo(boCustomer.getAccountNo());
			vo.setBankId(boCustomer.getBankId());
			vo.setBranchBankId(boCustomer.getBranchbankId());
			vo.setBranchBankName(boCustomer.getBranchbankName());
			vo.setMercId(boCustomer.getMercId());
			vo.setMercName(boCustomer.getMercName());
			vo.setMercNo(boCustomer.getMercNo());
			vo.setMobile(boCustomer.getMobile());
			vo.setPicPath(boCustomerImgMapper.selectUrlBymercNo(mercNo));
			
			vo.setProvinceId(boCustomer.getPeovinceId());
			vo.setCityId(boCustomer.getCityId());
		}
		return vo;
	}

	@Override
	public BoCustomerTemp loadBoCustomerTempBymercNo(String mercNo) {
		return mapper.selectBoCustomerTemp(mercNo);
	}

}
