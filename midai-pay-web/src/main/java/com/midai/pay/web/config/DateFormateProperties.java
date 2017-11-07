/**
 * Project Name:midai-pay-web
 * File Name:DateFormateProperties.java
 * Package Name:com.midai.pay.web.config
 * Date:2016年9月1日下午5:22:50
 * Copyright (c) 2016, www midaigroup com Technology Co., Ltd. All Rights Reserved.
 *
*/

package com.midai.pay.web.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.midai.framework.config.mybatis.JdbcProperties;

import lombok.Data;
/**
 * ClassName:DateFormateProperties <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年9月1日 下午5:22:50 <br/>
 * @author   陈勋
 * @version  
 * @since    JDK 1.7
 * @see 	 
 */
@Data
@ConfigurationProperties(prefix = DateFormateProperties.PREFIX)
public class DateFormateProperties {

    public static final String PREFIX="spring.dateformat"; 
    
    private String pattern;
}

