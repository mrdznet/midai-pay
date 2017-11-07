/**
 * Project Name:midai-pay-user-service
 * File Name:ServiceRunner.java
 * Package Name:com.midai.pay.user
 * Date:2016年9月1日下午2:09:21
 * Copyright (c) 2016, www midaigroup com Technology Co., Ltd. All Rights Reserved.
 *
*/

package com.midai.pay;

import org.activiti.spring.boot.JpaProcessEngineAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ImportResource;

import com.midai.framework.config.dubbo.DubboAutoConfiguration;

/**
 * ClassName:ServiceRunner <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年9月1日 下午2:09:21 <br/>
 * @author   陈勋
 * @version  
 * @since    JDK 1.7
 * @see 	 
 */
@SpringBootApplication(exclude={JpaProcessEngineAutoConfiguration.class,DubboAutoConfiguration.class})
@ImportResource("classpath*:META-INF/spring/*.xml")
public class PoswRunner {

    public static void main(String[] args) {
        SpringApplication.run(PoswRunner.class, args);
    }
}

