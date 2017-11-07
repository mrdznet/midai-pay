/**
 * Project Name:midai-pay-web
 * File Name:BaseControllerAdvice.java
 * Package Name:com.midai.pay.web.controller
 * Date:2016年9月1日下午1:08:14
 * Copyright (c) 2016, www midaigroup com Technology Co., Ltd. All Rights Reserved.
 *
*/

package com.midai.pay.web.config;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.midai.pay.activiti.service.MidaiActivityException;


/**
 * ClassName:BaseControllerAdvice <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年9月1日 下午1:08:14 <br/>
 * @author   陈勋
 * @version  
 * @since    JDK 1.7
 * @see 	 
 */
@ControllerAdvice(annotations=RestController.class)
public class BaseControllerAdvice {
    
    @ExceptionHandler(Throwable.class)
    @ResponseBody
    ResponseEntity<ErrorMessage> handleControllerException(HttpServletRequest request, Throwable ex) {   	
        HttpStatus status = getStatus(request);
        ErrorMessage msg=new ErrorMessage();
        msg.setErrorMsg(ex.getMessage());
        ResponseEntity<ErrorMessage> res=new ResponseEntity<ErrorMessage>(msg, status);
        return res;
    }

    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.valueOf(statusCode);
    }

}

