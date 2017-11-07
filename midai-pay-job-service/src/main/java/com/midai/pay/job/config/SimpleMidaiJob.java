/**
 * Project Name:midai-pay-job
 * File Name:SimpleJob.java
 * Package Name:com.midai.pay.job.config
 * Date:2016年9月26日上午10:41:53
 * Copyright (c) 2016, www midaigroup com Technology Co., Ltd. All Rights Reserved.
 *
*/

package com.midai.pay.job.config;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.stereotype.Component;

/**
 * ClassName:SimpleJob <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年9月26日 上午10:41:53 <br/>
 * @author   陈勋
 * @version  
 * @since    JDK 1.7
 * @see 	 
 */
@Retention(RetentionPolicy.RUNTIME) 
@Target({ElementType.TYPE})
@Documented
@Component
public @interface SimpleMidaiJob {
    
    String value() ;
    
    int shardingTotalCount() ;
    
    String cron();

}

