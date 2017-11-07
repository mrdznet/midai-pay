/**
 * Project Name:midai-pay-web
 * File Name:WebRunner.java
 * Package Name:com.midai.pay.web
 * Date:2016年9月1日下午1:44:33
 * Copyright (c) 2016, www midaigroup com Technology Co., Ltd. All Rights Reserved.
 *
*/

package com.midai.pay.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

/**
 * ClassName:WebRunner <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年9月1日 下午1:44:33 <br/>
 * @author   陈勋
 * @version  
 * @since    JDK 1.7
 * @see 	 
 */

@SpringBootApplication
@ImportResource("classpath*:META-INF/spring/*.xml")
public class WebRunner {
    
    public static void main(String[] args) {
        
        SpringApplication.run(WebRunner.class, args);
        
    }

}

