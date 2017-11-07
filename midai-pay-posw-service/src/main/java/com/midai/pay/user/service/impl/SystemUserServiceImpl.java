

/**
 * Project Name:chenxun-framework-start
 * File Name:UserServiceImpl.java
 * Package Name:com.chenxun.framework.service.impl
 * Date:2016年8月31日下午5:36:59
 * Copyright (c) 2016, www midaigroup com Technology Co., Ltd. All Rights Reserved.
 *
*/

package com.midai.pay.user.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.midai.framework.common.BaseServiceImpl;
import com.midai.pay.agent.entity.Agent;
import com.midai.pay.agent.mapper.AgentMapper;
import com.midai.pay.agent.service.AgentService;
import com.midai.pay.user.entity.MiFuResource;
import com.midai.pay.user.entity.SystemRole;
import com.midai.pay.user.entity.SystemUser;
import com.midai.pay.user.entity.SystemUserRole;
import com.midai.pay.user.mapper.SystemModuleMapper;
import com.midai.pay.user.mapper.SystemUserMapper;
import com.midai.pay.user.mapper.SystemUserRoleMapper;
import com.midai.pay.user.service.SystemUserRoleService;
import com.midai.pay.user.service.SystemUserService;
import com.midai.pay.user.vo.SystemUserSVo;

import tk.mybatis.mapper.entity.Example;

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
public class SystemUserServiceImpl extends BaseServiceImpl<SystemUser> implements SystemUserService{
	
	private final SystemUserMapper mapper;
	
	@Reference
	private SystemUserRoleService systemUserRoleService;

    public SystemUserServiceImpl(SystemUserMapper mapper) {           
        super(mapper);  
        this.mapper=mapper;
    }
    
    @Autowired
    private SystemModuleMapper systemModuleMapper;
    
    @Autowired
    private SystemUserRoleMapper systemUserRoleMapper;
    
    @Autowired
    private AgentMapper agentMapper;
    
    @Reference
    private AgentService agentService;
    
    @Override
    public SystemUser loadByUserLoginname(String userId) {
    	Example example = new Example(SystemUser.class);
		example.createCriteria().andEqualTo("loginname", userId).andEqualTo("status", 1);
    	List<SystemUser> list = mapper.selectByExample(example);
    	if(list!=null && list.size()>0) {
    		return list.get(0);
    	}
        return null;
    }

	@Override
	public List<SystemUser> findByOrganizationId(String orgid) {
		Example example = new Example(SystemUser.class);
		example.createCriteria().andEqualTo("orgid", orgid).andEqualTo("status", 1);
		return mapper.selectByExample(example);
	}

	@Transactional
	@Override
	public int insertSystemUserVo(SystemUserSVo suv) {
		SystemUser su = suv.getSu();
		if(checkExistsUser(su.getLoginname(), null)) {
			throw new RuntimeException("登陆用户名称已存在！");
		}
		su.setCreateTime(new Date());
		Integer ismanger = su.getIsmanger();
		if(ismanger == null) {
			su.setIsmanger(2);
		}
		su.setStatus(1);
		
		String orgNames = suv.getOrgNames();
		if(StringUtils.isNotEmpty(orgNames)){
			String orgName = orgNames.split("/")[0];
			String inscode = mapper.findInscode(orgName);
			
			su.setInscode(inscode);
		}
		if(mapper.insertUseGeneratedKeys(su) > 0) {
			String roleids = suv.getRoleIds();
			if(StringUtils.isNotEmpty(roleids)) {
				List<SystemUserRole> surList = getSystemUserRoleList(roleids.split(","), su.getId());
				systemUserRoleService.insertList(surList);
			}
		}
		return su.getId();
	}

	private List<SystemUserRole> getSystemUserRoleList(String[] roleIds, Integer userId) {
		List<SystemUserRole> surList = new ArrayList<SystemUserRole>();
		for(String rid : roleIds) {
			SystemUserRole sur = new SystemUserRole();
			sur.setRoleid(Integer.parseInt(rid.trim()));
			sur.setUserid(userId);
			sur.setCreateTime(new Date());
			surList.add(sur);
		}
		return surList;
	}
	
	@Transactional
	@Override
	public int delete(Integer id) {
		int deln = 0;
		SystemUser su = new SystemUser();
		su.setId(id);
		su.setStatus(2);
		deln = mapper.updateByPrimaryKeySelective(su);
//		if((deln=mapper.deleteByPrimaryKey(id)) > 0) {
//			deln += systemUserRoleService.deleteByUserId(id);
//		}
		return deln;
	}

	@Override
	public SystemUserSVo findSystemUserVo(Integer id) {
		SystemUserSVo suv = new SystemUserSVo();
		SystemUser su = mapper.selectByPrimaryKey(id);
		List<SystemUserRole> surList = systemUserRoleService.findByUserId(id);
		StringBuffer ids = new StringBuffer("");
		if(surList!=null && surList.size()>0) {
			for(SystemUserRole sur : surList) {
				ids.append(sur.getRoleid()).append(",");
			}
			ids.delete(ids.length()-1, ids.length());
		}
		suv.setSu(su);
		suv.setRoleIds(ids.toString());
		return suv;
	}

	@Transactional
	@Override
	public int update(SystemUserSVo suv) {
		SystemUser su = suv.getSu();
		int inur = 0;
		if(checkExistsUser(su.getLoginname(), su.getId())) {
			throw new RuntimeException("登陆用户名称已存在！");
		}
		if(mapper.updateByPrimaryKeySelective(su) > 0) {
			systemUserRoleService.deleteByUserId(su.getId());
			String roleids = suv.getRoleIds();
			if(StringUtils.isNotEmpty(roleids)) {
				List<SystemUserRole> surList = getSystemUserRoleList(suv.getRoleIds().split(","), su.getId());
				inur = systemUserRoleService.insertList(surList);
			}
		}
		return inur + 1;
	}

	@Override
	public boolean checkExistsUser(String userId, Integer id) {
		boolean isExists = false;
		SystemUser su = getByLoginnameIgnoreStatus(userId);
		if(su != null) {
			if((id==null) || (id.intValue()!=su.getId())) {
				isExists = true;
			}
		}
		return isExists;
	}

	@Override
	public SystemUser getByLoginnameIgnoreStatus(String userId) {
		Example example = new Example(SystemUser.class);
		example.createCriteria().andEqualTo("loginname", userId);
		List<SystemUser> list = mapper.selectByExample(example);
		if(list!=null && list.size()>0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public int updatePassword(SystemUser su) {
		Example example = new Example(SystemUser.class);
		example.createCriteria().andEqualTo("loginname", su.getLoginname()).andEqualTo("status", 1);
		return mapper.updateByExampleSelective(su, example);
	}
	
	@Transactional
	@Override
	public int updateBatch(String ids) {
		return mapper.updateBatch(ids);
	}

	@Override
	public SystemUserSVo findVoByLoginName(String name) {
		
		SystemUserSVo suv = new SystemUserSVo();
		Example example = new Example(SystemUser.class);
		example.createCriteria().andEqualTo("loginname", name);
		suv.setSu(mapper.selectByExample(example).get(0));
		List<SystemRole> surList = mapper.findVoByLoginName(name);
		StringBuffer ids = new StringBuffer("");
		StringBuffer names=new StringBuffer("");
		if(surList!=null && surList.size()>0) {
			for(SystemRole sur : surList) {
				ids.append(sur.getId()).append(",");
				names.append(sur.getRolename()).append(",");
			}
			ids.delete(ids.length()-1, ids.length());
			names.setLength(names.length()-1);
		}
		suv.setRoleIds(ids.toString());
		suv.setRoleName(names.toString());

		return suv;
	}

	@Override
	public SystemUser getByLoginNameAndStatus(String userId, Integer status) {
		Example example = new Example(SystemUser.class);
		example.createCriteria().andEqualTo("loginname", userId).andEqualTo("status", status);
		List<SystemUser> us = mapper.selectByExample(example);
		if(us != null && us.size() == 1) {
			return us.get(0);
		}
		return null;
	}

	@Override
	public int updateOnlyUserInfo(SystemUser user) {
		return mapper.updateByPrimaryKeySelective(user);
	}

	@Override
	public String getAgentInscodeByUserId(String userId) {
		return mapper.getAgentInscodeByUserId(userId);
	}

	@Override
	public SystemUser getByUserIdOrMobile(String mobile) {
		return mapper.getByUserIdOrMobile(mobile);
	}
	
	@Override
	public String getInscode(String loginname) {
		return mapper.getInscode(loginname);
	}

	@Override
	public Map<String, Map<String, List<String>>> findAllResource(String loginName) {
		Map<String, Map<String, List<String>>> map = new HashMap<String, Map<String, List<String>>>();
		
		List<Integer> roleList = systemUserRoleMapper.findAllRoleidByLoginName(loginName);//加载所有角色
		if(null!=roleList && roleList.size()>0){
			String roles = convertToCommaDelimited(roleList);
			
			/**
			 * 资源共分三级, 菜单、子菜单，按钮. 
			 * 1. 从最低级遍历加载, 加载子菜单及其所有按钮
			 */
			Map<String, String> param_1 = new HashMap<String, String>();
			param_1.put("roles", roles);
			param_1.put("pageLevel", "2");
			
			Map<String, List<String>> secMap = new HashMap<String,List<String>>(); //需返回的第二级及其直接子级
			List<MiFuResource> allButtonResource = systemModuleMapper.findAllSource(param_1); //加载所有三级
			
			List<Integer> parendsIds = new ArrayList<Integer>(); 
			for(MiFuResource resource : allButtonResource){
				parendsIds.add(resource.getPageParentId());
			}
			if(parendsIds.size() > 0){
				List<MiFuResource> nodesInfo = systemModuleMapper.findModuleInfo(convertToCommaDelimited(parendsIds)); //加载二级节点信息
				
				List<String> buttons;
				for(MiFuResource nodes : nodesInfo){
					buttons = new ArrayList<String>();
					for(MiFuResource child : allButtonResource){
						if(nodes.getModuleId().equals(child.getPageParentId())){
							buttons.add(child.getPageId());
						}
					}
					
					secMap.put(nodes.getPageId(), buttons);
				}
			}
			map.put("sec", secMap);
			
			/**
			 * 2. 加载菜单及其子菜单
			 */
			Map<String, String> param_2 = new HashMap<String, String>();
			param_2.put("roles", roles);
			param_2.put("pageLevel", "1");
			
			Map<String, List<String>> firMap = new HashMap<String,List<String>>(); //需返回的第一级及其直接子级
			List<MiFuResource> allNodesResource = systemModuleMapper.findAllSource(param_2); //加载所有二级
			
			for(MiFuResource notes : allNodesResource){	// 添加到所有二级节点
				parendsIds.add(notes.getModuleId());
			}
			
			if(parendsIds.size()>0){
				List<MiFuResource> allNodesInfo = systemModuleMapper.findModuleInfo(convertToCommaDelimited(parendsIds)); //加载二级节点信息
				
				//获取所有父级节点信息
				List<MiFuResource> parList = systemModuleMapper.findAllParentInfo(convertToCommaDelimited(parendsIds));
				List<String> nodeList;
				for(MiFuResource parent : parList){
					nodeList = new ArrayList<String>();
					
					for(MiFuResource child : allNodesInfo){
						if(parent.getModuleId().equals(child.getPageParentId())){
							nodeList.add(child.getPageId());
						}
					}
					
					firMap.put(parent.getPageId(), nodeList);
				}
			}
			
			map.put("fir", firMap);
		}
		
		return map;
	}
	
	public static String convertToCommaDelimited(List<Integer> list) {
        StringBuffer ret = new StringBuffer("");
        for (int i = 0; list != null && i < list.size(); i++) {
            ret.append(list.get(i));
            if (i < list.size() - 1) {
                ret.append(',');
            }
        }
        return ret.toString();
    }

	@Override
	public String getAllChildAgents(String loginname) {
		String agents = " ";
		List<String> all = new ArrayList<String>();
		
		if(StringUtils.isNotEmpty(loginname)){
			
			Agent agent = agentService.fetchAgentByUserName(loginname);
			
			if(agent != null && StringUtils.isNotEmpty(agent.getAgentNo())){ //代理商用户
				all.add(agent.getAgentNo());
				getChildByrecursion(all, agent.getAgentNo());
			}else{ //两类用户: 1.系统管理员,  2.各公司管理员
				String inscode = mapper.getInscode(loginname);
				String topAgentNo = "";
				if(StringUtils.isNotEmpty(inscode)) {
					
					if(StringUtils.isNotEmpty(inscode)){ //各公司管理员
						topAgentNo = agentMapper.findTopAgentNo(inscode);
					}
				} else {
					topAgentNo = "AGEi000460";
				}
				all.add(topAgentNo);
				getChildByrecursion(all, topAgentNo);
			}
		}
		
		for(String agent : all){
			agents += ("'" + agent + "',");
		}
		
		return agents.substring(0, agents.length()-1);
	}
	
	public void getChildByrecursion(List<String> list, String agentNo){
		List<String> childs = agentMapper.findAllChildAgents(agentNo);
		
		if(null==childs || childs.size()<=0) return;
		else{
			for(String child : childs){
				list.add(child);
				getChildByrecursion(list, child);
			}
		}
	}

	@Override
	public int selectBymobile(String mobile) {
		return mapper.selectBymobile(mobile);
	}
}

