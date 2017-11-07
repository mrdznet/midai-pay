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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.midai.framework.common.BaseServiceImpl;
import com.midai.pay.user.entity.SystemOrganizationModel;
import com.midai.pay.user.entity.SystemUser;
import com.midai.pay.user.mapper.SystemOrganizationMapper;
import com.midai.pay.user.query.SystemCommNodeQuery;
import com.midai.pay.user.service.SystemOrganizationService;
import com.midai.pay.user.service.SystemUserService;
import com.midai.pay.user.vo.SystemCommNodeVo;

import tk.mybatis.mapper.entity.Example;

/**
 * 
 * ClassName: SystemOrganizationServiceImpl <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON(可选). <br/>
 * date: 2016年9月13日 下午4:48:10 <br/>
 *
 * @author 屈志刚
 * @version 
 * @since JDK 1.7
 */
@Service
public class SystemOrganizationServiceImpl extends BaseServiceImpl<SystemOrganizationModel> implements SystemOrganizationService{

	@Reference
    private SystemUserService sus;
	
	private final SystemOrganizationMapper mapper;
	
    public SystemOrganizationServiceImpl(SystemOrganizationMapper mapper) {           
        super(mapper);  
        this.mapper=mapper;
    }
    
	@Override
	public List<SystemOrganizationModel> findByExample(
			SystemOrganizationModel systemOrg) {
		Example example = new Example(SystemOrganizationModel.class);
		example.createCriteria().andEqualTo("parentId", systemOrg.getParentId()).andEqualTo("status", 1);
		List<SystemOrganizationModel> result = mapper.selectByExample(example);
		if(result == null) {
			result = new ArrayList<SystemOrganizationModel>();
		}
		return result;
	}



	@Override
	public int findByExampleCount(SystemOrganizationModel systemOrg) {
		Example example = new Example(SystemOrganizationModel.class);
		example.createCriteria().andEqualTo("parentId", systemOrg.getParentId());
		return mapper.selectCountByExample(example);
	}



	@Override
	public String findThemselvesAndChildrenIds(Integer orgid) {
		List<SystemOrganizationModel> list = findThemselvesAndChildrens(orgid);
		StringBuffer ids = new StringBuffer();
		for(SystemOrganizationModel s : list) {
			ids.append(s.getOrganizationId()).append(",");
		}
		if(ids.length() > 0) {
			ids.delete(ids.length()-1, ids.length());
		}
		return ids.toString();
	}
	
	@Override
	public List<SystemOrganizationModel> findThemselvesAndChildrens(Integer orgid) {
		List<SystemOrganizationModel> orglist = new ArrayList<SystemOrganizationModel>();
		if(orgid != null && !orgid.equals(Integer.parseInt("0"))) {
			SystemOrganizationModel som = mapper.selectByPrimaryKey(orgid);
			if(som != null) {
				orglist.add(som);
				findChildrens(orglist, orgid);
			}
		}
		return orglist;
	}
	
    public void findChildrens(List<SystemOrganizationModel> orglist, Integer orgid) {
    	SystemOrganizationModel som = new SystemOrganizationModel();
    	som.setParentId(orgid);
    	List<SystemOrganizationModel> corglist = findByExample(som);
    	if(corglist != null && corglist.size() > 0) {
    		orglist.addAll(corglist);
    		for(SystemOrganizationModel corg : corglist) {
    			findChildrens(orglist, corg.getOrganizationId());
    		}
    	} else {
    		return ;
    	}
    }


    @Transactional
	@Override
	public int deleteById(int id) {
    	int num = 0;
		String ids = findThemselvesAndChildrenIds(id);
		if(StringUtils.isNotEmpty(ids)) {
			num = mapper.updateThemselvesAndChildren(ids);
		}
		return num;
	}



	@Override
	public List<SystemCommNodeVo> loadNodes(int orgid) {
		//系统通用节点类
		List<SystemCommNodeVo> scnlist = new ArrayList<SystemCommNodeVo>();
		
		SystemOrganizationModel clickNode = mapper.selectByPrimaryKey(orgid);
		//根据组织机构查询子节点
		SystemOrganizationModel systemOrg = new SystemOrganizationModel();
		systemOrg.setParentId(orgid);
		List<SystemOrganizationModel> solist = findByExample(systemOrg);
		
		if(solist != null && solist.size() > 0) {
			for(SystemOrganizationModel o : solist) {
				SystemCommNodeVo scn = new SystemCommNodeVo();
				scn.setId(o.getOrganizationId() + "");
				scn.setPId(o.getParentId() + "");
				scn.setName(o.getOrganizationName());
				scn.setOrgType(o.getOrgType());
				scn.setLevel(o.getLevel());
				if(scn.getLevel()==0) {
					scn.setOpen(true);
				}
				scnlist.add(scn);
			}
		}
		//根据组织机构Id机构及同机构对应的用户
		List<SystemUser> sulist = sus.findByOrganizationId(orgid+"");
		if(sulist != null && sulist.size() > 0) {
			for(SystemUser u : sulist) {
				SystemCommNodeVo srcn = new SystemCommNodeVo();
				srcn.setId("user_" + u.getId());
				srcn.setPId(orgid+"");
				srcn.setName(u.getUsername());
				srcn.setLevel(clickNode.getLevel()+1);
				scnlist.add(srcn);
			}
		}
		return scnlist;
	}



	@Override
	public int insertOrg(SystemOrganizationModel systemOrg) {
		
		if(!this.checkOrgExists(systemOrg.getOrganizationName(), null, systemOrg.getParentId())) {
			mapper.insertOrg(systemOrg);
		} else {
			throw new RuntimeException("该组织机构名称已存在！");
		}
		return systemOrg.getOrganizationId();
	}

	@Override
	public SystemOrganizationModel findById(int id) {
		SystemOrganizationModel su = new SystemOrganizationModel();
		Example example = new Example(SystemOrganizationModel.class);
		example.createCriteria().andEqualTo("organizationId", id).andEqualTo("status", 1);
		List<SystemOrganizationModel> list = mapper.selectByExample(example);
		if(list != null && list.size() > 0 ) {
			su = list.get(0);
		} 
		return su;
	}

	@Override
	public boolean checkOrgExists(String name, Integer id, Integer pid) {
		boolean exists = false;
		SystemOrganizationModel org = getOrgByNameAndPid(name, pid, null);
		if(org != null) {
			if((id == null) || (org.getOrganizationId() != id.intValue())) {
				exists = true;
			} 
		}
		return exists;
	}

	@Override
	public SystemOrganizationModel getOrgByNameAndPid(String name, Integer pid, Integer orgType) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("pid", pid.toString());
		map.put("name", name);
		map.put("status", "1");
		map.put("orgType", orgType==null?"":orgType.toString());
		List<SystemOrganizationModel> list = mapper.getByParentIdAndNameAndStatus(map);
		if(list!=null && list.size()>0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public List<SystemCommNodeVo> getAllOrgAndUserNodes() {
		List<SystemCommNodeVo> listVo = new ArrayList<SystemCommNodeVo>();
		List<Integer> orgtype=new ArrayList<Integer>();
		orgtype.add(1);
		orgtype.add(2);
		Example example = new Example(SystemOrganizationModel.class);
		example.createCriteria().andEqualTo("status", 1).andIn("orgType", orgtype);
		List<SystemOrganizationModel> list = mapper.selectByExample(example);
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
			List<SystemUser> sulist = sus.findByOrganizationId(o.getOrganizationId()+"");
			for(SystemUser u : sulist) {
				SystemCommNodeVo svu = new SystemCommNodeVo();
				svu.setId("user_" + u.getId());
				svu.setName(u.getUsername());
				svu.setLevel(o.getLevel() + 1);
				svu.setPId(u.getOrgid() + "");
				svu.setOpen(false);
				listVo.add(svu);
			}
		}
		return listVo;
	}


	@Override
	public List<SystemOrganizationModel> getAllOrgNodes() {
		List<Integer> orgtype=new ArrayList<Integer>();
		orgtype.add(1);
		orgtype.add(2);
		Example example = new Example(SystemOrganizationModel.class);
		example.createCriteria().andEqualTo("status", 1).andIn("orgType", orgtype);
		return mapper.selectByExample(example);
	}

	@Override
	public List<SystemCommNodeVo> queryOrgAndUserList(SystemCommNodeQuery query) {
		
		List<SystemCommNodeVo> voList = mapper.queryOrgAndUserList(query);
		return voList;
	}

	@Override
	public int queryOrgAndUserCount(SystemCommNodeQuery query) {
		
		return mapper.queryOrgAndUserCount(query);
	}

	@Override
	public List<SystemOrganizationModel> findThemselvesAndElderGenerationNode(int orgid) {
		
		List<SystemOrganizationModel> list = new ArrayList<SystemOrganizationModel>();
		Example example = new Example(SystemOrganizationModel.class);
		example.createCriteria().andEqualTo("organizationId", orgid).andEqualTo("status", 1);
		List<SystemOrganizationModel> themself = mapper.selectByExample(example);
		if(themself != null && themself.size() > 0) {
			SystemOrganizationModel o = themself.get(0);
			list.add(o);
			findFatherNode(list, o.getParentId());
		}
		return list;
	}

	private void findFatherNode(List<SystemOrganizationModel> list, int pid) {
		Example example = new Example(SystemOrganizationModel.class);
		example.createCriteria().andEqualTo("organizationId", pid).andEqualTo("status", 1);
		List<SystemOrganizationModel> themself = mapper.selectByExample(example);
		
		if(themself==null || themself.size() <= 0) {
			return;
		} else {
			SystemOrganizationModel t = themself.get(0);
			list.add(t);
			findFatherNode(list, t.getParentId());
		}
	}

	@Override
	public List<Integer> findThemselvesAndElderGenerationNodeId(int orgid) {
		List<SystemOrganizationModel> list = findThemselvesAndElderGenerationNode(orgid);
		List<Integer> idsList = new ArrayList<Integer>();
		if(list!=null && list.size()>0) {
			for(SystemOrganizationModel s : list) {
				idsList.add(s.getOrganizationId());
			}
		}
		return idsList;
	}

	@Override
	public String findOrgCatalogue(int orgid) {
		StringBuffer sb = new StringBuffer();
		if(orgid == 0) {
			sb.append("组织机构/");
		} else {
			List<SystemOrganizationModel> list = findThemselvesAndElderGenerationNode(orgid);
			if(list!=null && list.size()>0) {
				Collections.sort(list);
				for(SystemOrganizationModel s : list) {
					sb.append(s.getOrganizationName() + "/");
				}
			}
		}
		String str = sb.toString();
		if(StringUtils.isNotEmpty(str)) {
			str = str.substring(0, str.length()-1);
		} else {
			str = "";
		}
		return str;
	}

	@Transactional
	@Override
	public int batchDel(String ids) {
		return mapper.updateThemselvesAndChildren(ids);
	}
   
}

