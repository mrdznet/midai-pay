/**
 * Project Name:midai-pay-web
 * File Name:UserController.java
 * Package Name:com.midai.pay.web.controller
 * Date:2016年9月1日下午12:45:02
 * Copyright (c) 2016, www midaigroup com Technology Co., Ltd. All Rights Reserved.
 *
*/

package com.midai.pay.web.controller.system;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.midai.pay.customer.service.CustomerProcessService;
import com.midai.pay.user.entity.SystemUser;
import com.midai.pay.user.service.SystemUserService;
import com.midai.pay.user.vo.SystemUserSVo;
import com.midai.pay.web.config.MidaiPayMessageProperties;
import com.midai.pay.web.config.OSSProperties;
import com.midai.pay.web.vo.system.SystemUserVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * ClassName:UserController <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * @author   wrt
 * @version  
 * @since    JDK 1.7
 * @see 	 
 */
@Api("用户管理")
@RestController
@RequestMapping("/system/user")
public class SystemUserController {
	
	@Autowired
	private MidaiPayMessageProperties midaiPayMessageProperties;
    
    @Reference
    private SystemUserService userService;
    @Value("${system.user.password}")
    private String password;
    
    @Reference
    private CustomerProcessService customerProcessService;
    
	@Autowired
	private OSSProperties ossProperties;
	
    @ApiOperation("根据账号查询用户")
    @GetMapping("/load/{loginname}")
    public SystemUserVo loadByUserLoginname(@PathVariable @ApiParam("登陆账号")  String loginname){
    	SystemUserVo sv = new SystemUserVo();
        SystemUser su = userService.loadByUserLoginname(loginname);
        BeanUtils.copyProperties(su, sv);
        return sv;
    }

    @ApiOperation("根据ID查询用户,忽略状态")
    @GetMapping("/findkeyignorestatus/{id}")
    public SystemUserVo findByPrimaryKey(@PathVariable @ApiParam("用户ID")  Integer id){
    	SystemUserVo sv = new SystemUserVo();
        SystemUser su = userService.findByPrimaryKey(id);
        BeanUtils.copyProperties(su, sv);
        return sv;
    }
    
    @ApiOperation("保存用户")
    @PostMapping("/save")
    public int save(@RequestBody SystemUserVo uv ,BindingResult result){
    	
    	SystemUserSVo suv = new SystemUserSVo();
    	SystemUser su = new SystemUser();
    	BeanUtils.copyProperties(uv, su);
    	Md5PasswordEncoder md5=new Md5PasswordEncoder();
    	su.setPassword(md5.encodePassword(password, null));
    	suv.setSu(su);
    	suv.setRoleIds(uv.getRoleIds());
    	suv.setOrgNames(uv.getOrgNames());
        return userService.insertSystemUserVo(suv);
    }
    
    @ApiOperation("根据id删除用户")
    @DeleteMapping(path="/del/{id}")
    public int del(@PathVariable @ApiParam("用户ID") Integer id){       
        return userService.delete(id);
    }
    
    @ApiOperation("编辑用户")
    @PostMapping("/edit/{id}")
    public SystemUserVo edit(@PathVariable @ApiParam("用户ID") Integer id){   
    	SystemUserVo sv = new SystemUserVo();
    	SystemUserSVo svo = userService.findSystemUserVo(id);
    	BeanUtils.copyProperties(svo, sv);
    	BeanUtils.copyProperties(svo.getSu(), sv);
        return sv;
    }
    
    @ApiOperation("修改用户")
    @PostMapping("/update")
    public int update(@RequestBody SystemUserVo uv, BindingResult result){
    	SystemUserSVo suv = new SystemUserSVo();
    	SystemUser su = new SystemUser();
    	BeanUtils.copyProperties(uv, su);
    	suv.setSu(su);
    	suv.setRoleIds(uv.getRoleIds());
      return  userService.update(suv);
    }
    
    @ApiOperation("初始化密码")
    @PostMapping("/initpwd/{id}")
    public int initPassword(@PathVariable@ApiParam("用户ID") Integer id) {
    	SystemUser su = new SystemUser();
    	su.setId(id);
    	Md5PasswordEncoder md5=new Md5PasswordEncoder();
    	su.setPassword(md5.encodePassword(password, null));
    	return userService.update(su);
    }
    
    @ApiOperation("修改密码")
    @PostMapping("/updatepwd/{pwd}/{opwd}")
    public int updatePassword(@PathVariable @ApiParam("密码") String pwd, @PathVariable @ApiParam("原密码") String opwd) {
    	UserDetails u = currentUser();
    	Md5PasswordEncoder md5=new Md5PasswordEncoder();
    	String opwdmd = md5.encodePassword(opwd, null);
    	if(opwdmd.equals(u.getPassword())) {
    		SystemUser su = userService.loadByUserLoginname(u.getUsername());
    		su.setPassword(md5.encodePassword(pwd, null));
    		return userService.updatePassword(su);
    	} else {
    		throw new RuntimeException("原密码错误");
    	}
    }
    
    @ApiOperation("批量删除")
    @PostMapping("/batchdel/{ids}")
    public int batchDel(@PathVariable @ApiParam("用户id字符串，逗号分隔") String ids) {
    	int num = 0;
    	if(StringUtils.isNotEmpty(ids)) {
    		return userService.updateBatch(ids);
    	}
    	return num;
    }
    
    @ApiOperation("修改用户信息")
    @PostMapping("/updateuserinfo")
    public int updateOnlyUserInfo(@RequestBody SystemUserVo uv, BindingResult result) {
    	SystemUser user = new SystemUser();
    	BeanUtils.copyProperties(uv, user);
    	int num = userService.updateOnlyUserInfo(user);
    	return num;
    }
    
    private UserDetails currentUser(){      
        return (UserDetails) SecurityContextHolder.getContext().getAuthentication() .getPrincipal();      
    }
    
    @ApiOperation("获取当前登录用户和角色")
    @GetMapping("/getusername")
    public SystemUserVo currentUserName(){ 
    	UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication() .getPrincipal();
    	SystemUserVo vo=new SystemUserVo();
    	
    	SystemUserSVo suv =userService.findVoByLoginName(userDetails.getUsername());
    	
    	BeanUtils.copyProperties(suv.getSu(),vo);
    	BeanUtils.copyProperties(suv,vo);
    	vo.setName(userDetails.getUsername());	 
    	
    	
    	String url=midaiPayMessageProperties.getHost()+":"+midaiPayMessageProperties.getPort();
    	if(midaiPayMessageProperties.getApplication()!=null&&midaiPayMessageProperties.getApplication().trim().length()>0){
    		url=url+"/"+midaiPayMessageProperties.getApplication();
    	}

    	vo.setMidaiPayMessage(url);
    	
    	Map<String, Map<String, List<String>>> resource = userService.findAllResource(userDetails.getUsername()); // 获取所有用户资源
    	vo.setFirResource(resource.get("fir"));
    	vo.setSecResource(resource.get("sec"));
    	
    	vo.setHost(ossProperties.getHost());
    	vo.setHostImg(ossProperties.getImg());
    	
    	return vo;      
    }
    
    @ApiOperation("获取当前登录用户待审总数")
    @GetMapping("/counttasks")
    public int countPendingTask(){
    	UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication() .getPrincipal();
    	
    	return customerProcessService.countPendingTask(userDetails.getUsername());
    }
    
    @ApiOperation("检查手机号码唯一性")
    @GetMapping("/selectbymobile/{mobile}")
	public int selectBymobile(@PathVariable("mobile") @ApiParam("商户手机号")  String mobile)
	{
		if (mobile==null) {
			throw new RuntimeException("请传入商户手机号");
		}
		return userService.selectBymobile(mobile);
	}
}

