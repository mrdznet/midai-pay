/**
 * Project Name:midai-pay-posw-service
 * File Name:MidaiActivityParam.java
 * Package Name:com.midai.pay.user.config
 * Date:2016年12月8日下午3:49:58
 * Copyright (c) 2016, Shanghai Law Cloud Technology Co., Ltd. All Rights Reserved.
 *
*/

package com.midai.pay.activiti.service;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ClassName:MidaiActivityParam <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年12月8日 下午3:49:58 <br/>
 * @author   陈勋
 * @version  
 * @since    JDK 1.7
 * @see 	 
 */
@Retention(RetentionPolicy.RUNTIME) 
@Target({ElementType.PARAMETER})
public @interface MidaiActivityParam {
	

}

