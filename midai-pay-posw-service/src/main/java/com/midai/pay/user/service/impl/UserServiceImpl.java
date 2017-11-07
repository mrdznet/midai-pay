/**
 * Project Name:chenxun-framework-start
 * File Name:UserServiceImpl.java
 * Package Name:com.chenxun.framework.service.impl
 * Date:2016年8月31日下午5:36:59
 * Copyright (c) 2016, www midaigroup com Technology Co., Ltd. All Rights Reserved.
 *
*/

package com.midai.pay.user.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.midai.framework.common.BaseServiceImpl;
import com.midai.framework.config.mybatis.MyMapper;
import com.midai.pay.user.entity.User;
import com.midai.pay.user.mapper.UserMapper;
import com.midai.pay.user.service.UserService;

/**
 * ClassName:UserServiceImpl <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年8月31日 下午5:36:59 <br/>
 * @author   陈勋
 * @version  
 * @since    JDK 1.7
 * @see 	 
 */
@Service
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService{

    public UserServiceImpl(UserMapper mapper) {           
        super(mapper);  
        this.mapper=mapper;
    }
    
    
    
    private final UserMapper mapper;

  
    @Override
    public User loadByUsername(String username) {
        return mapper.selectByUsername(username);
    }

    @Transactional
    @Override
    public void testTrantransaction() {
        
        User u=new User();
        u.setName("aaaa2132");
        u.setPassword("123213213");
        u.setCreateTime(new Date());
        mapper.insert(u);
        if(true){
            throw new RuntimeException("roll back");
        }
        
    }

    

   
}

