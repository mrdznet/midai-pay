/**
 * Project Name:spirng-boot-starter-dubbo
 * File Name:DubboServiceLatchCommandLineRunner.java
 * Package Name:com.midai.springboot.dubbo
 * Date:2016年7月26日下午5:40:12
 * Copyright (c) 2016, www midaigroup com Technology Co., Ltd. All Rights Reserved.
 *
 */

package com.midai.framework.config.dubbo;

import org.springframework.boot.CommandLineRunner;

/**
 * ClassName:DubboServiceLatchCommandLineRunner <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2016年7月26日 下午5:40:12 <br/>
 * 
 * @author 陈勋
 * @version
 * @since JDK 1.7
 * @see
 */
public class DubboServiceLatchCommandLineRunner implements CommandLineRunner {

	private String domain = "com.midai.dubbo.management";

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	@Override
	public void run(String... arg0) throws Exception {
		ShutdownManage latch=new ShutdownManage(getDomain());
       latch.await();
	}

}
