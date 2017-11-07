/**
 * Project Name:midai-pay-job
 * File Name:JobRunner.java
 * Package Name:com.midai.pay.job
 * Date:2016年9月26日上午11:14:54
 * Copyright (c) 2016, www midaigroup com Technology Co., Ltd. All Rights Reserved.
 *
*/

package com.midai.pay.job;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

/**
 * ClassName:JobRunner <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年9月26日 上午11:14:54 <br/>
 * @author   陈勋
 * @version  
 * @since    JDK 1.7
 * @see 	 
 */
@SpringBootApplication
@ImportResource("classpath*:META-INF/spring/*.xml")
public class JobRunner {
      
    public static void main(String[] args) {     
    	
        SpringApplication.run(JobRunner.class, args);      
    }


}

