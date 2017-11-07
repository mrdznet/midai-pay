/**
 * Project Name:demo-dubbo-provider
 * File Name:UserMapper.java
 * Package Name:com.midai.demo.mapper
 * Date:2016年7月27日下午6:31:38
 * Copyright (c) 2016, www midaigroup com Technology Co., Ltd. All Rights Reserved.
 *
*/

package com.midai.pay.user.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Select;
import org.springframework.jdbc.core.JdbcTemplate;

import com.midai.framework.config.mybatis.MyMapper;
import com.midai.pay.user.entity.User;


/**
 * ClassName:UserMapper <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年7月27日 下午6:31:38 <br/>
 * @author   陈勋
 * @version  
 * @since    JDK 1.7
 * @see 	 
 */
public interface UserMapper extends MyMapper<User> {
    
    @Select("select * from tbl_user where name =#{username}")
    
    public User selectByUsername(String username);

}

