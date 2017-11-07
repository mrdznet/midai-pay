/**
 * Project Name:midai-pay-user-service
 * File Name:TestTrantransaction.java
 * Package Name:com.midai.pay.user.test
 * Date:2016年9月2日下午9:49:05
 * Copyright (c) 2016, www midaigroup com Technology Co., Ltd. All Rights Reserved.
 *
*/

package com.midai.pay.user.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.midai.pay.user.service.UserService;




/**
 * ClassName:TestTrantransaction <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年9月2日 下午9:49:05 <br/>
 * @author   陈勋
 * @version  
 * @since    JDK 1.7
 * @see 	 
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestTrantransaction {
    
    @Autowired
    private UserService userService;
    
    @Test
    public void test(){
        userService.testTrantransaction();
    }

}

