/**
 * Project Name:midai-car-web-manage
 * File Name:MidaiValidateAop.java
 * Package Name:com.midai.car.interceptor
 * Date:2016年8月9日上午11:04:50
 * Copyright (c) 2016, www midaigroup com Technology Co., Ltd. All Rights Reserved.
 *
*/

package com.midai.pay.web.config;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

/**
 * ClassName:MidaiValidateAop <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年8月9日 上午11:04:50 <br/>
 * @author   陈勋
 * @version  
 * @since    JDK 1.7
 * @see 	 
 */

@Aspect
@Configuration
public class MidaiValidateAop {
	
	
	@Before(value = "execution(* *(..,org.springframework.validation.BindingResult))")
	public void   validate(JoinPoint  jp) throws Throwable{
		Object[] arr=jp.getArgs();
		if(arr!=null){
			for(Object obj:arr){
				if(obj instanceof BindingResult ){
					BindingResult br=(BindingResult) obj;
					if(br.hasErrors()){
						StringBuffer sb=new StringBuffer();
						for(ObjectError error: br.getAllErrors()){
						    if(error instanceof FieldError){
						        FieldError fe=(FieldError) error;
						        sb.append(fe.getField());
						        sb.append(":");
						    }
						        sb.append(error.getDefaultMessage());
						        sb.append(",");
							
						}
						String str=sb.toString();
						if(str.endsWith(",")){
							str=str.substring(0,str.length()-1);
						}
						throw new IllegalArgumentException(str);
					}
				}
			}
		}
		
	}

}

