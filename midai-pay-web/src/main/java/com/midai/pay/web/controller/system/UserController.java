/**
 * Project Name:midai-pay-web
 * File Name:UserController.java
 * Package Name:com.midai.pay.web.controller
 * Date:2016年9月1日下午12:45:02
 * Copyright (c) 2016, www midaigroup com Technology Co., Ltd. All Rights Reserved.
 *
*/

package com.midai.pay.web.controller.system;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;

import javax.validation.Valid;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.midai.pay.user.entity.User;
import com.midai.pay.user.service.UserService;

/**
 * ClassName:UserController <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年9月1日 下午12:45:02 <br/>
 * @author   陈勋
 * @version  
 * @since    JDK 1.7
 * @see 	 
 */
@Api("用户管理")
@RestController
@RequestMapping("/user")
public class UserController {
    
    @Reference
    private UserService userService;
    
    @ApiOperation("查询全部用户")
    @GetMapping(path="/all")
    public List<User> listAll(){
        return userService.findAll();
    }
    @ApiOperation("根据id查询指定用户")
    @GetMapping("/{id}")
    public User findById(@PathVariable  int id){
        
        User user =userService.findByPrimaryKey(id);
        return user;
    }

    @ApiOperation("新增用户")
    @PostMapping
    public int add(@Valid @RequestBody User user ,BindingResult result){   
        return userService.insert(user);
    }
    
    @ApiOperation("根据id删除用户")
    @DeleteMapping(path="/{id}")
    public int del(@PathVariable int id){       
        return userService.deleteByPrimaryKey(id);
    }
    
    @ApiOperation("修改用户")
    @PutMapping
    public int update(@Valid @RequestBody User user,BindingResult result){
      return  userService.update(user);
    }
    
    @ApiOperation("测试事务")
    @GetMapping("/test")
    public void testTrantransaction(){
        userService.testTrantransaction();
    }
    
    @ApiOperation("当前用户")
    @GetMapping("/currentUser")
    public UserDetails currentUser(){      
        return (UserDetails) SecurityContextHolder.getContext().getAuthentication() .getPrincipal();      
    }
    

    
    
    
     
    
    
     
    
    

}

