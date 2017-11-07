/**
 * Project Name:midai-pay-job
 * File Name:SImpleJobDemo.java
 * Package Name:com.midai.pay.job.service
 * Date:2016年9月26日上午10:37:01
 * Copyright (c) 2016, www midaigroup com Technology Co., Ltd. All Rights Reserved.
 *
*/

package com.midai.pay.job.service.impl;




import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.midai.pay.job.config.SimpleMidaiJob;

/**
 * ClassName:SImpleJobDemo <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年9月26日 上午10:37:01 <br/>
 * @author   陈勋
 * @version  
 * @since    JDK 1.7
 * @see 	 
 */
@SimpleMidaiJob(value="simpleJobDemo",cron="0/30 * * * * ?",shardingTotalCount=10)
public class SimpleJobDemo implements SimpleJob  {

	@Override
	public void execute(ShardingContext context) {
		switch (context.getShardingItem()) {
		   default:System.out.println(context.getShardingItem());
        }

		
		
	}

 
   
  
    
    

}

