/**
 * Project Name:midai-pay-web
 * File Name:MidaiPayUserDetailsService.java
 * Package Name:com.midai.pay.web.config
 * Date:2016年9月1日下午3:55:53
 * Copyright (c) 2016, www midaigroup com Technology Co., Ltd. All Rights Reserved.
 *
*/

package com.midai.pay.web.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Reference;
import com.midai.pay.user.entity.SystemUser;
import com.midai.pay.user.service.SystemUserService;

/**
 * ClassName:MidaiPayUserDetailsService <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年9月1日 下午3:55:53 <br/>
 * @author   陈勋
 * @version  
 * @since    JDK 1.7
 * @see 	 
 */
@Component("userDetailsService")
public class MidaiPayUserDetailsService implements UserDetailsService{

    @Reference
    private SystemUserService userService;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	SystemUser user= userService.loadByUserLoginname(username);
        if(user==null){
            return null;
        }
        List<GrantedAuthority> list=buildUserAuthority(user);
        return  buildUserForAuthentication(user,list);
        
       
    }
    
    /**
     * 返回验证角色
     * 
     * @param userRolesO
     * @return
     */
    private List<GrantedAuthority> buildUserAuthority(SystemUser user) {
        List<GrantedAuthority> result = new ArrayList<GrantedAuthority>();
        result.add(new SimpleGrantedAuthority("ROLE_USER"));   
        return result;
    }

    
    /**
     * 返回登录用户
     * 
     * @param user
     * @param authorities
     * @return
     */
    private org.springframework.security.core.userdetails.User buildUserForAuthentication(SystemUser user,
            List<GrantedAuthority> authorities) {
        return new org.springframework.security.core.userdetails.User
                (user.getLoginname(), user.getPassword(), authorities);
    }

}

