/**
 * Project Name:chenxun-framework-start
 * File Name:UserService.java
 * Package Name:com.chenxun.framework.service
 * Date:2016年8月31日下午5:37:10
 * Copyright (c) 2016, www midaigroup com Technology Co., Ltd. All Rights Reserved.
 *
*/

package com.midai.pay.user.service;

import java.util.List;

import com.midai.framework.common.BaseService;
import com.midai.pay.user.entity.SystemOrganizationModel;
import com.midai.pay.user.query.SystemCommNodeQuery;
import com.midai.pay.user.vo.SystemCommNodeVo;

/**
 * 
 * ClassName: SystemOrganizationService <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON(可选). <br/>
 * date: 2016年9月13日 下午4:45:14 <br/>
 *
 * @author 屈志刚
 * @version 
 * @since JDK 1.7
 */
public interface SystemOrganizationService extends BaseService<SystemOrganizationModel>{
	
    public List<SystemOrganizationModel> findByExample(SystemOrganizationModel systemOrg);
    
    public int findByExampleCount(SystemOrganizationModel systemOrg);

    /**
     * 获取当前机构及其下属机构Id字符串
     * @param orgid	当前组织机构Id
     * @return	逗号分隔的机构Id字符串
     */
	public String findThemselvesAndChildrenIds(Integer orgid);
    
	/**
	 * 获取当前机构及其下属机构列表
	 * @param orgid orgid	当前组织机构Id
	 * @return
	 */
	public List<SystemOrganizationModel> findThemselvesAndChildrens(Integer orgid);

	public int deleteById(int id);

	public List<SystemCommNodeVo> loadNodes(int orgid);

	public int insertOrg(SystemOrganizationModel systemOrg);

	public SystemOrganizationModel findById(int id);

	public SystemOrganizationModel getOrgByNameAndPid(String name, Integer pid,  Integer orgType);
	/**
	 * 查看组织机构是否存在
	 * @param id
	 * @return true 存在 ， false 不存在
	 */
	public boolean checkOrgExists(String name, Integer id, Integer pid);

	public List<SystemCommNodeVo> getAllOrgAndUserNodes();

	public List<SystemOrganizationModel> getAllOrgNodes();

	public List<SystemCommNodeVo> queryOrgAndUserList(SystemCommNodeQuery query);

	public int queryOrgAndUserCount(SystemCommNodeQuery query);
	
	public  List<SystemOrganizationModel>  findThemselvesAndElderGenerationNode(int orgid);
	
	public List<Integer> findThemselvesAndElderGenerationNodeId(int orgid);
	
	public String findOrgCatalogue(int orgid);

	public int batchDel(String string);
}

