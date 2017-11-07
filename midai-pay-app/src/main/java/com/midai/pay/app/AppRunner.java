/**
 * Project Name:midai-pay-app
 * File Name:AppRunner.java
 * Package Name:com.midai.pay.app
 * Date:2016年9月8日下午3:19:02
 * Copyright (c) 2016, www midaigroup com Technology Co., Ltd. All Rights Reserved.
 *
*/

package com.midai.pay.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ImportResource;

/**
 * ClassName:AppRunner <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年9月8日 下午3:19:02 <br/>
 * @author   陈勋
 * @version  
 * @since    JDK 1.7
 * @see 	 
 */
@SpringBootApplication
@ImportResource("classpath*:META-INF/spring/*.xml")
public class AppRunner {
    
    public static void main(String[] args) {
        
        SpringApplication.run(AppRunner.class, args);
    }

}
