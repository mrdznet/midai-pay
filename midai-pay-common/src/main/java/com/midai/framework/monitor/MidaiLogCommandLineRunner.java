/**
 * Project Name:midai-pay-common
 * File Name:MidaiLogCommandLineRunner.java
 * Package Name:com.midai.framework.monitor
 * Date:2016年11月14日上午9:47:03
 * Copyright (c) 2016, Shanghai Law Cloud Technology Co., Ltd. All Rights Reserved.
 *
*/

package com.midai.framework.monitor;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;

import lombok.extern.slf4j.Slf4j;

/**
 * ClassName:MidaiLogCommandLineRunner <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年11月14日 上午9:47:03 <br/>
 * @author   陈勋
 * @version  
 * @since    JDK 1.7
 * @see 	 
 */
@Slf4j
@Order(1)
public class MidaiLogCommandLineRunner implements CommandLineRunner {
	
	private boolean enableTraceLog;

	@Override
	public void run(String... args) throws Exception {
		MidaiLogTraceService.setEnable(enableTraceLog);
		
	}

	public boolean isEnableTraceLog() {
		return enableTraceLog;
	}

	public void setEnableTraceLog(boolean enableTraceLog) {
		this.enableTraceLog = enableTraceLog;
	}
	
	

}

