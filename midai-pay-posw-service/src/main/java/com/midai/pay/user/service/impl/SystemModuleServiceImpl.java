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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.alibaba.dubbo.config.annotation.Service;
import com.midai.framework.common.BaseServiceImpl;
import com.midai.pay.user.entity.SystemModule;
import com.midai.pay.user.mapper.SystemModuleMapper;
import com.midai.pay.user.service.SystemModuleService;
import com.midai.pay.user.vo.SystemModuleSVo;

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
public class SystemModuleServiceImpl extends BaseServiceImpl<SystemModule> implements SystemModuleService{
	
	private final SystemModuleMapper mapper;

    public SystemModuleServiceImpl(SystemModuleMapper mapper) {           
        super(mapper);  
        this.mapper=mapper;
    }

    @Override
    public Map<SystemModuleSVo, List<SystemModuleSVo>> getAllResultMap() {
    	Map<SystemModuleSVo, List<SystemModuleSVo>> map = new HashMap<SystemModuleSVo, List<SystemModuleSVo>>();
    	Example example = new Example(SystemModule.class);
		example.createCriteria().andEqualTo("parentId", 0);
    	List<SystemModule> smrList = mapper.selectByExample(example);
    	example.clear();
    	example.createCriteria().andNotEqualTo("parentId", 0);
    	List<SystemModule> smnList = mapper.selectByExample(example);
    	
    	if(smrList!=null && smrList.size()>0) {
    		for(SystemModule sm : smrList) {
    			List<SystemModuleSVo> list = new ArrayList<SystemModuleSVo>();
    			if(smnList!=null && smnList.size()>0) {
    				Iterator<SystemModule> iterator = smnList.iterator();
    				while(iterator.hasNext()) {
    					SystemModule csm = iterator.next();
    					if(sm.getModuleId()==csm.getParentId().intValue()) {
    						SystemModuleSVo smv = new SystemModuleSVo(); 
    						smv.setModuleId(csm.getModuleId());
    						smv.setModuleName(csm.getModuleName());
    						list.add(smv);
    						iterator.remove();
    					}
    				}
    			}
    			
    			SystemModuleSVo key = new SystemModuleSVo();
    			key.setModuleId(sm.getModuleId());
    			key.setModuleName(sm.getModuleName());
    			map.put(key, list);
    		}
    	}
    	
    	return map;
    }
    
    
}

