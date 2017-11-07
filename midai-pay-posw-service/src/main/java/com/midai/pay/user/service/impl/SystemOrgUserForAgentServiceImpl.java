/**
 * Project Name:chenxun-framework-start
 * File Name:UserServiceImpl.java
 * Package Name:com.chenxun.framework.service.impl
 * Date:2016年8月31日下午5:36:59
 * Copyright (c) 2016, www midaigroup com Technology Co., Ltd. All Rights Reserved.
 *
*/

package com.midai.pay.user.service.impl;

import java.util.Date;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.midai.pay.user.common.enums.RoleInsCodeEnum;
import com.midai.pay.user.entity.SystemOrganizationModel;
import com.midai.pay.user.entity.SystemRole;
import com.midai.pay.user.entity.SystemUser;
import com.midai.pay.user.mapper.SystemOrganizationMapper;
import com.midai.pay.user.service.SystemOrgUserForAgentService;
import com.midai.pay.user.service.SystemOrganizationService;
import com.midai.pay.user.service.SystemRoleService;
import com.midai.pay.user.service.SystemUserService;
import com.midai.pay.user.vo.SystemOrgUserForAgentVo;
import com.midai.pay.user.vo.SystemUserSVo;

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
public class SystemOrgUserForAgentServiceImpl implements SystemOrgUserForAgentService{
	 
	@Reference
	private SystemOrganizationService systemOrganizationService;
	@Reference
	private SystemUserService systemUserService;
	@Reference
	private SystemRoleService systemRoleService;
	@Autowired
	private SystemOrganizationMapper somapper;
	
	@Transactional(propagation=Propagation.NESTED)
	@Override
	public int insertSystemOrgUserByAgent(SystemOrgUserForAgentVo av) {
		int num = 0;
		SystemOrganizationModel org = new SystemOrganizationModel();
		BeanUtils.copyProperties(av, org);
		org.setOrgType(3);
		org.setCreateTime(new Date());
		int orgId = 0;
		if(!systemOrganizationService.checkOrgExists(org.getOrganizationName(), null, org.getParentId())) {
			somapper.insertOrg(org);
			orgId = org.getOrganizationId();
		} else {
			throw new RuntimeException("该组织机构名称已存在！");
		}
		
		SystemUserSVo sv = new SystemUserSVo();
		SystemUser su = new SystemUser();
		BeanUtils.copyProperties(av, su);
		su.setOrgid(orgId);
		su.setIsmanger(2);
		su.setStatus(1);
		sv.setSu(su);
		SystemRole sr = systemRoleService.getSystemRoleByName(RoleInsCodeEnum.getStatusName(av.getInscode()));
	
		if(sr!=null) {
			sv.setRoleIds(sr.getId()+"");
			systemUserService.insertSystemUserVo(sv);
		} else {
			throw new RuntimeException(" 未找到代理商角色！");
		}
		return num;
	}

	@Override
	public String getAgentCodeByLoginname(String loginname) {
		String agentCode = null;
		
		SystemUser user = systemUserService.loadByUserLoginname(loginname);
		Integer orgId = user.getOrgid();
		if(orgId != null) {
			SystemOrganizationModel som = systemOrganizationService.findByPrimaryKey(orgId);
			if(som!=null) {
				agentCode = som.getAgentNum();
				/*if(StringUtils.isBlank(agentCode)){
					throw new RuntimeException("用户不属于任何代理商,操作终止！");
				}*/
			} else {
				throw new RuntimeException("组织机构不存在！");
			}
		} else {
			throw new RuntimeException("组织机构ID为空！");
		}
		return agentCode;
	}
}

