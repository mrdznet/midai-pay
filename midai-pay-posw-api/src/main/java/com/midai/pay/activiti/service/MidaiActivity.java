/**
 * Project Name:midai-pay-posw-service
 * File Name:MidaiActivity.java
 * Package Name:com.midai.pay.user.config
 * Date:2016年12月8日上午11:14:19
 * Copyright (c) 2016, Shanghai Law Cloud Technology Co., Ltd. All Rights Reserved.
 *
*/

package com.midai.pay.activiti.service;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.beans.factory.annotation.Required;

/**
 * ClassName:MidaiActivity <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年12月8日 上午11:14:19 <br/>
 * @author   陈勋
 * @version  
 * @since    JDK 1.7
 * @see 	 
 */
@Retention(RetentionPolicy.RUNTIME) 
@Target({ElementType.METHOD})
@Documented
public @interface MidaiActivity {
	
	public String task() default "#{taskId}";

}

