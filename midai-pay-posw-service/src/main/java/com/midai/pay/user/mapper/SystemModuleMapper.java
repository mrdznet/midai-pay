/**
 * Project Name:demo-dubbo-provider
 * File Name:UserMapper.java
 * Package Name:com.midai.demo.mapper
 * Date:2016年7月27日下午6:31:38
 * Copyright (c) 2016, www midaigroup com Technology Co., Ltd. All Rights Reserved.
 *
*/

package com.midai.pay.user.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.SelectProvider;

import com.midai.framework.config.mybatis.MyMapper;
import com.midai.pay.common.po.SqlParam;
import com.midai.pay.user.entity.MiFuResource;
import com.midai.pay.user.entity.SystemModule;


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
public interface SystemModuleMapper extends MyMapper<SystemModule> {
	
	@SelectProvider(type=com.midai.pay.user.provider.SystemModuleProvider.class, method="findAllSource")
	List<MiFuResource> findAllSource(Map<String, String> param);
	
	@SelectProvider(type=com.midai.pay.user.provider.SystemModuleProvider.class, method="findModuleInfo")
	List<MiFuResource> findModuleInfo(String moduleIDs);
	
	@SelectProvider(type=com.midai.pay.user.provider.SystemModuleProvider.class, method="findAllParentInfo")
	List<MiFuResource> findAllParentInfo(String childIds);
	
	@SelectProvider(type=com.midai.pay.user.provider.SystemModuleProvider.class, method="findUserByModuleIdAndInscode")
	List<String> findUserByModuleIdAndInscode(SqlParam param);
}
