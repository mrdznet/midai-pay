/**
 * Project Name:midai-pay-job
 * File Name:JobProperties.java
 * Package Name:com.midai.pay.job.config
 * Date:2016年9月26日上午11:25:43
 * Copyright (c) 2016, www midaigroup com Technology Co., Ltd. All Rights Reserved.
 *
*/

package com.midai.pay.job.config;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * ClassName:JobProperties <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年9月26日 上午11:25:43 <br/>
 * @author   陈勋
 * @version  
 * @since    JDK 1.7
 * @see 	 
 */
@Data
@ConfigurationProperties(prefix = JobProperties.PREFIX)
public class JobProperties {
    
    public static final String PREFIX="elastic.job";
    
    
    private String namespace;
    
    private String serverList;
    
    private int baseSleepTimeMilliseconds;
    
    private int maxSleepTimeMilliseconds;
    
    private int maxRetries;
    
    private boolean disabled;

}

