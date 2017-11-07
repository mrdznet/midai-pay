/**
 * Project Name:spirng-boot-starter-dubbo
 * File Name:ShutDownLatch.java
 * Package Name:com.midai.springboot.dubbo
 * Date:2016年7月26日下午3:55:03
 * Copyright (c) 2016, www midaigroup com Technology Co., Ltd. All Rights Reserved.
 *
*/

package com.midai.framework.config.dubbo;

import java.lang.management.ManagementFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.management.MBeanServer;
import javax.management.ObjectName;

/**
 * ClassName:ShutDownLatch <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年7月26日 下午3:55:03 <br/>
 * @author   陈勋
 * @version  
 * @since    JDK 1.7
 * @see 	 
 */
public class ShutdownManage implements ShutdownManageMBean {
	

	protected AtomicBoolean running=new AtomicBoolean(false);
	
	public long checkIntervalInSeconds=10;
	
	private String domain="com.midai.dubbo.lifecycles";
	
	public ShutdownManage(){
		
	}
	
	public ShutdownManage(String domain){
		this.domain=domain;
	}

	
	public void await() throws Exception{
		if(running.compareAndSet(false, true)){
			MBeanServer mBeanServer=ManagementFactory.getPlatformMBeanServer();
			mBeanServer.registerMBean(this, new ObjectName(domain,"name","ShutdownnLatch"));
			while(running.get()){
				TimeUnit.SECONDS.sleep(checkIntervalInSeconds);
			}
		}
	}
	
	@Override
	public String shutdown() {
		if(running.compareAndSet(true, false)){
			return "shutdown signal send, shutting down...";
		}else{
			return "shutdown signal had been sent, no need again and again and again...";
		}
		
		
	}
	
	

}

