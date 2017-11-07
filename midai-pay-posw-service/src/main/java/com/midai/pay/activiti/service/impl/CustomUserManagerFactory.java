/**
 * Project Name:midai-activiti-demo
 * File Name:CustomUserManagerFactory.java
 * Package Name:com.midai.activiti.service.impl
 * Date:2016年5月16日上午10:13:47
 * Copyright (c) 2016, www midaigroup com Technology Co., Ltd. All Rights Reserved.
 *
*/

package com.midai.pay.activiti.service.impl;

import org.activiti.engine.impl.interceptor.Session;
import org.activiti.engine.impl.interceptor.SessionFactory;

import com.midai.pay.activiti.service.CustomUserManager;


/**
 * ClassName:CustomUserManagerFactory <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年5月16日 上午10:13:47 <br/>
 * @author   陈勋
 * @version  
 * @since    JDK 1.7
 * @see 	 
 */
public class CustomUserManagerFactory implements SessionFactory {

	
	private CustomUserManager customUserManager;
	
	@Override
	public Class<?> getSessionType() {
		return CustomUserManager.class;
	}

	@Override
	public Session openSession() {
		return customUserManager;
	}

	public CustomUserManager getCustomUserManager() {
		return customUserManager;
	}

	public void setCustomUserManager(CustomUserManager customUserManager) {
		this.customUserManager = customUserManager;
	}

}

