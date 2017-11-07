/**
 * Project Name:midai-pay-sercurity-service
 * File Name:SercurityRunner.java
 * Package Name:com.midai.pay.sercurity
 * Date:2016年9月12日上午10:56:53
 * Copyright (c) 2016, www midaigroup com Technology Co., Ltd. All Rights Reserved.
 *
*/

package com.midai.pay.posp;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

import com.midai.framework.config.dubbo.DubboAutoConfiguration;

/**
 * ClassName:SercurityRunner <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年9月12日 上午10:56:53 <br/>
 * @author   陈勋
 * @version  
 * @since    JDK 1.7
 * @see 	 
 */
@SpringBootApplication(exclude={DubboAutoConfiguration.class})
@ImportResource("classpath*:META-INF/spring/*.xml")
public class PospServicesRunner {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(PospServicesRunner.class, args);
       
    }
}

