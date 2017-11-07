/**
 * Project Name:midai-activiti-demo
 * File Name:CustomGroupManagerFactory.java
 * Package Name:com.midai.activiti.service.impl
 * Date:2016年5月16日上午10:03:27
 * Copyright (c) 2016, www midaigroup com Technology Co., Ltd. All Rights Reserved.
 *
*/

package com.midai.pay.activiti.service.impl;

import org.activiti.engine.impl.interceptor.Session;
import org.activiti.engine.impl.interceptor.SessionFactory;

import com.midai.pay.activiti.service.CustomGroupManager;


/**
 * ClassName:CustomGroupManagerFactory <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年5月16日 上午10:03:27 <br/>
 * @author   陈勋
 * @version  
 * @since    JDK 1.7
 * @see 	 
 */
public class CustomGroupManagerFactory implements SessionFactory {

	private CustomGroupManager customGroupManager;
	
	@Override
	public Class<?> getSessionType() {
		return CustomGroupManager.class;
	}

	@Override
	public Session openSession() {
		return customGroupManager;
	}

	public void setCustomGroupManager(CustomGroupManager customGroupManager) {
		this.customGroupManager = customGroupManager;
	}

}

