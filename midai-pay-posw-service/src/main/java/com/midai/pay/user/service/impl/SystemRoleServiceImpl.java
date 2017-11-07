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
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.midai.framework.common.BaseServiceImpl;
import com.midai.pay.user.entity.SystemOrgRole;
import com.midai.pay.user.entity.SystemOrganizationModel;
import com.midai.pay.user.entity.SystemProcessExecutorConfigRole;
import com.midai.pay.user.entity.SystemRole;
import com.midai.pay.user.entity.SystemRoleModule;
import com.midai.pay.user.mapper.SystemRoleMapper;
import com.midai.pay.user.query.SystemCommNodeQuery;
import com.midai.pay.user.service.SystemOrgRoleService;
import com.midai.pay.user.service.SystemOrganizationService;
import com.midai.pay.user.service.SystemProcessExecutorConfigRoleService;
import com.midai.pay.user.service.SystemRoleModuleService;
import com.midai.pay.user.service.SystemRoleService;
import com.midai.pay.user.vo.SystemCommNodeVo;
import com.midai.pay.user.vo.SystemRoleSVo;

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
public class SystemRoleServiceImpl extends BaseServiceImpl<SystemRole> implements SystemRoleService{
	
	private final SystemRoleMapper mapper;

	@Reference
	private SystemOrgRoleService sors;
	@Reference
    private SystemOrganizationService sos;
	@Reference
	private SystemRoleModuleService srms;
	@Reference
	private SystemProcessExecutorConfigRoleService specs;
	
    public SystemRoleServiceImpl(SystemRoleMapper mapper) {           
        super(mapper);  
        this.mapper=mapper;
    }

	@Transactional
	@Override
	public int save(SystemRoleSVo rv) {
		SystemRole sr = rv.getSr();
		int num = 0;
		if(checkExists(sr.getRolename(), rv.getOrgid(), null)) {
			throw new RuntimeException("角色名已存在！");
		}
		sr.setCreateTime(new Date());
		if((num=mapper.insertUseGeneratedKeys(sr)) > 0) {
			
			SystemOrgRole sor = new SystemOrgRole();
			sor.setRoleid(sr.getId());
			sor.setOrgid(rv.getOrgid());
			sor.setCreateTime(new Date());
			if(sors.insert(sor) > 0) {
				
				num += 1;
				String specIds = rv.getSpecIds();
				if(StringUtils.isNotEmpty(specIds)) {
					num += specs.insertList(getProcessConfigRoleList(sr.getId(), specIds));
				}
				
				String smvIds = rv.getSmvIds();
				if(StringUtils.isNotEmpty(smvIds)) {
					num += srms.insertList(getSystemRoleModuleList(sr.getId(), smvIds));
				}
			}
		}
		return sr.getId();
	}

	private List<SystemRoleModule> getSystemRoleModuleList(Integer roleid, String smvIds) {
		List<SystemRoleModule> srmList = new ArrayList<SystemRoleModule>();
		
		for(String id : smvIds.split(",")) {
			SystemRoleModule srm = new SystemRoleModule();
			srm.setModuleId(Integer.parseInt(id.trim()));
			srm.setRoleId(roleid);
			srm.setCreateTime(new Date());
			srmList.add(srm);
		}
		
		return srmList;
	}
	
	private List<SystemProcessExecutorConfigRole> getProcessConfigRoleList(Integer roleid, String processids) {
		List<SystemProcessExecutorConfigRole> specList = new ArrayList<SystemProcessExecutorConfigRole>();
		for(String id : processids.split(",")) {
			SystemProcessExecutorConfigRole sf = new SystemProcessExecutorConfigRole();
			sf.setRoleid("r" + roleid);
			sf.setProcessid(Integer.parseInt(id.trim()));
			sf.setCreateTime(new Date());
			specList.add(sf);
		}
		return specList;
	}
	
	@Override
	public List<SystemRole> list(int orgid) {
		String orgIds = sos.findThemselvesAndChildrenIds(orgid);
		List<SystemRole> roleModelList = mapper.findByorgIds(orgIds);
		return roleModelList;
	}

	@Transactional
	@Override
	public int delete(int id) {
		int num = 0;
		num += mapper.deleteByPrimaryKey(id);
		if(num > 0) {
			num += sors.deleteByRoleId(id);
			num += srms.deleteByRoleid(id);
			num += specs.deleteByRoleId("r" + id);
		}
		return num;
	}
	
	/*批量删除角色*/
	@Transactional
	@Override
	public int deletes(Integer[] ids)
	{
		StringBuffer sb =new StringBuffer();
		for(Integer id : ids){
			sb.append(id).append(",");	
		}
		sb.setLength(sb.length() - 1);
		
		/*SystemRole表*/
		int num = mapper.deleteSystemRole(sb.toString());
		
		/*SystemOrganization表*/
		int	num1 = mapper.deleteByRoleIds(sb.toString());
		
		/*SystemRoleModule表*/
		int	num2 = mapper.deleteByRoleids(sb.toString());
			
			StringBuffer sbid =new StringBuffer();
			for(Integer id :ids){
				sbid.append("'").append("r").append(id).append("'").append(",");
			}
			sbid.setLength(sbid.length() - 1);
			
			/*SystemProcessExecutorConfigRole*/
	        int num4 = mapper.deleteByRoleIdCs(sbid.toString());
		
		return 1;
}

	
	@Override
	public List<SystemCommNodeVo> loadNodes(int orgid) {
		//系统通用节点类
		List<SystemCommNodeVo> scnlist = new ArrayList<SystemCommNodeVo>();
		
		SystemOrganizationModel clickNode = sos.findByPrimaryKey(orgid);
		//根据组织机构查询子节点
		SystemOrganizationModel systemOrg = new SystemOrganizationModel();
		systemOrg.setParentId(orgid);
		systemOrg.setStatus(1);
		List<SystemOrganizationModel> solist = sos.findByExample(systemOrg);
		
		if(solist != null && solist.size() > 0) {
			for(SystemOrganizationModel o : solist) {
				SystemCommNodeVo scn = new SystemCommNodeVo();
				scn.setId(o.getOrganizationId() + "");
				scn.setPId(o.getParentId() + "");
				scn.setName(o.getOrganizationName());
				scn.setLevel(o.getLevel());
				scnlist.add(scn);
			}
		}
		
		//根据组织机构Id机构及同机构对应的角色
		List<SystemRole> srlist = mapper.findByorgIds(orgid + "");
		if(srlist != null && srlist.size() > 0) {
			for(SystemRole r : srlist) {
				SystemCommNodeVo srcn = new SystemCommNodeVo();
				srcn.setId("role_" + r.getId());
				srcn.setPId(orgid+"");
				srcn.setName(r.getRolename());
				srcn.setLevel(clickNode.getLevel() + 1);
				scnlist.add(srcn);
			}
		}
		return scnlist;
	}

	@Override
	public SystemRoleSVo getSystemRoleVo(int roleid) {
		SystemRoleSVo srv = new SystemRoleSVo();
		SystemRole sr = mapper.selectByPrimaryKey(roleid);
		srv.setSr(sr);
		
		List<SystemRoleModule> srmList = srms.getByRoleId(roleid);
		if(srmList!=null && srmList.size() > 0) {
			StringBuffer srmbf = new StringBuffer();
			for(SystemRoleModule s : srmList) {
				srmbf.append(s.getModuleId()).append(",");
			}
			srmbf.setLength(srmbf.length()-1);
			srv.setSmvIds(srmbf.toString());
		} else {
			srv.setSmvIds("");
		}
		
		List<SystemProcessExecutorConfigRole> specsList = specs.getByRoleId("r" + roleid);
		if(specsList!=null && specsList.size() > 0) {
			StringBuffer specsbf = new StringBuffer();
			for(SystemProcessExecutorConfigRole sc : specsList) {
				specsbf.append(sc.getProcessid()).append(",");
			}
			specsbf.setLength(specsbf.length()-1);
			srv.setSpecIds(specsbf.toString());
		} else {
			srv.setSpecIds("");
		}
		return srv;
	}

	@Transactional
	@Override
	public int updateSystemRoleVo(SystemRoleSVo srv) {
		int num = 0;
		SystemRole sr = srv.getSr();
		if((num=mapper.updateByPrimaryKey(sr)) > 0) {
			srms.deleteByRoleid(sr.getId());
			specs.deleteByRoleId("r" + sr.getId());
			num += srms.insertList(getSystemRoleModuleList(sr.getId(), srv.getSmvIds()));
			num += specs.insertList(getProcessConfigRoleList(sr.getId(), srv.getSpecIds()));
		}
		return num;
	}

	@Transactional
	@Override
	public int updateBySystemRoleVo(SystemRoleSVo srv) {
		SystemRole sr = srv.getSr();
		int num = 0;
		if(checkExists(sr.getRolename(), srv.getOrgid(), sr.getId())) {
			throw new RuntimeException("角色名已存在！");
		}
		if((num=mapper.updateByPrimaryKey(sr)) > 0) {
			srms.deleteByRoleid(sr.getId());
			specs.deleteByRoleId("r" + sr.getId());
			String modelids = srv.getSmvIds();
			if(StringUtils.isNotEmpty(modelids)) {
				num += srms.insertList(getSystemRoleModuleList(sr.getId(), srv.getSmvIds()));
			}
			String pecids = srv.getSpecIds();
			if(StringUtils.isNotEmpty(pecids)) {
				num += specs.insertList(getProcessConfigRoleList(sr.getId(), srv.getSpecIds()));
			}
		}
		
		return num;
	}

	@Override
	public SystemRole getSystemRoleByName(String agentRole) {
		Example example = new Example(SystemRole.class);
		example.createCriteria().andEqualTo("rolename", agentRole);
		List<SystemRole> list = mapper.selectByExample(example);
		if(list!=null && list.size()>0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public boolean checkExists(String name, Integer orgId, Integer Id) {
		boolean exists = false;
		SystemRole sr = getSystemRoleByNameAndOrgid(name, orgId);
		if(sr != null) {
			if((Id == null) || (sr.getId() != Id.intValue())) {
				exists = true;
			} 
		}
		return exists;
	}

	@Override
	public SystemRole getSystemRoleByNameAndOrgid(String name, Integer orgid) {
		
		List<SystemRole> list = mapper.getSystemRoleByNameAndOrgid(name, orgid);
		if(list!=null && list.size()>0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public List<SystemCommNodeVo> loadAll() {
		List<SystemCommNodeVo> listVo = new ArrayList<SystemCommNodeVo>();
		
		List<SystemOrganizationModel> list = sos.getAllOrgNodes();
		for(SystemOrganizationModel o : list) {
			SystemCommNodeVo sv = new SystemCommNodeVo();
			sv.setId(o.getOrganizationId() + "");
			sv.setName(o.getOrganizationName());
			sv.setLevel(o.getLevel());
			sv.setPId(o.getParentId() + "");
			sv.setOrgType(o.getOrgType());
			if(sv.getLevel().intValue() == 1) {
				sv.setOpen(true);
			} else {
				sv.setOpen(false);
			}
			listVo.add(sv);
			List<SystemRole> sulist = mapper.getByOrgId(o.getOrganizationId());
			for(SystemRole r : sulist) {
				SystemCommNodeVo svu = new SystemCommNodeVo();
				svu.setId("role_" + r.getId());
				svu.setName(r.getRolename());
				svu.setLevel(o.getLevel() + 1);
				svu.setPId(o.getOrganizationId() + "");
				svu.setOpen(false);
				listVo.add(svu);
			}
		}
		return listVo;
	}

	@Override
	public List<SystemCommNodeVo> queryRoleList(SystemCommNodeQuery query) {
		String orgIds = sos.findThemselvesAndChildrenIds(Integer.parseInt(query.getPid()));
		query.setPid(orgIds);
		return mapper.queryRoleList(query);
	}

	@Override
	public int queryRoleCount(SystemCommNodeQuery query) {
		String orgIds = sos.findThemselvesAndChildrenIds(Integer.parseInt(query.getPid()));
		query.setPid(orgIds);
		return mapper.queryRoleCount(query);
	}

}

