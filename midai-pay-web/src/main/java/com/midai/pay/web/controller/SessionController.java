/**
 * Project Name:midai-pay-web
 * File Name:SesssionController.java
 * Package Name:com.midai.pay.web.controller
 * Date:2016年10月19日上午10:57:14
 * Copyright (c) 2016, www midaigroup com Technology Co., Ltd. All Rights Reserved.
 *
*/

package com.midai.pay.web.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * ClassName:SesssionController <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年10月19日 上午10:57:14 <br/>
 * @author   陈勋
 * @version  
 * @since    JDK 1.7
 * @see 	 
 */
@Controller
public class SessionController {
    
    @RequestMapping(value = "/session/invalidate")  
    @ResponseBody  
    public String invalidateSession(HttpServletRequest reqeust,HttpServletResponse response) {  
         String ajaxHeader = reqeust.getHeader("X-Requested-With");  
            boolean isAjax = "XMLHttpRequest".equals(ajaxHeader);  
            if (isAjax) { 
            	response.setStatus(408);           	
                return "invalidSession";  
            } else {  
                try {  
                    response.sendRedirect("/login.html");  
                } catch (IOException e) {  
                    // TODO Auto-generated catch block  
                    e.printStackTrace();  
                }  
            }  
        return "";  
    }  

}

