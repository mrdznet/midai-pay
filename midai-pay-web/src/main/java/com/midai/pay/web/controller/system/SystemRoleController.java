/**
 * Project Name:midai-pay-web
 * File Name:UserController.java
 * Package Name:com.midai.pay.web.controller
 * Date:2016年9月1日下午12:45:02
 * Copyright (c) 2016, www midaigroup com Technology Co., Ltd. All Rights Reserved.
 *
*/

package com.midai.pay.web.controller.system;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.midai.framework.query.PageVo;
import com.midai.pay.user.entity.SystemRole;
import com.midai.pay.user.query.SystemCommNodeQuery;
import com.midai.pay.user.service.SystemRoleService;
import com.midai.pay.user.vo.SystemCommNodeVo;
import com.midai.pay.user.vo.SystemRoleSVo;
import com.midai.pay.web.vo.system.SystemRoleVo;

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
@Api("角色管理")
@RestController
@RequestMapping("/system/role")
public class SystemRoleController {
    
    @Reference
    private SystemRoleService roleService;
    
    @ApiOperation("查询所有节点")
    @PostMapping("/loadall")
    public List<SystemCommNodeVo> loadAll() {
    	return roleService.loadAll();
    }
    
    @ApiOperation("分页查询")
    @PostMapping(path="/querybypid")
    public PageVo<SystemCommNodeVo> queryByParentId(@RequestBody  SystemCommNodeQuery query){
    	
    	String str = query.getPid();
		Pattern pattern = Pattern.compile("[1-9][0-9]*");
		Matcher isNum = pattern.matcher(str);
		PageVo<SystemCommNodeVo> pageVo = new PageVo<SystemCommNodeVo>();
		if (!isNum.matches()) {
			return pageVo;
		}
    	query.setOrder(" r.update_time desc ");
		pageVo.setRows(roleService.queryRoleList(query));
		pageVo.setTotal(roleService.queryRoleCount(query));
		return pageVo;
    	
    }
    
    @ApiOperation("角色列表")
    @PostMapping("/list/{orgid}")
    public List<SystemRoleVo> list(@PathVariable  @ApiParam("组织机构ID")  int orgid) {
    	List<SystemRole> list = roleService.list(orgid);
    	List<SystemRoleVo> result = new ArrayList<SystemRoleVo>();
    	for(SystemRole r : list) {
    		SystemRoleVo sv = new SystemRoleVo();
    		BeanUtils.copyProperties(r, sv);
    		result.add(sv);
    	}
    	return result;
    }
    
    
    @ApiOperation("保存角色")
    @PostMapping("/save")
    public int save(@Valid @RequestBody SystemRoleVo rv ,BindingResult result) {
    	SystemRoleSVo srv = new SystemRoleSVo();
    	BeanUtils.copyProperties(rv, srv);
    	SystemRole sr = new SystemRole();
    	BeanUtils.copyProperties(rv, sr);
    	srv.setSr(sr);
    	return roleService.save(srv);
    }
    
    @ApiOperation("编辑角色")
    @PostMapping("/edit/{roleid}/{orgid}")
    public SystemRoleVo edit(@PathVariable @ApiParam("角色ID") int roleid, @PathVariable @ApiParam("组织机构ID")   int orgid) {
    	SystemRoleSVo srsv = roleService.getSystemRoleVo(roleid);
    	SystemRoleVo srv = new SystemRoleVo();
    	BeanUtils.copyProperties(srsv.getSr(), srv);
    	srv.setSmvIds(srsv.getSmvIds());
    	srv.setSpecIds(srsv.getSpecIds());
    	srv.setOrgid(orgid);
    	return srv;
    }
    
    @ApiOperation("更新角色")
    @PostMapping("/update")
    public int update(@Valid @RequestBody  SystemRoleVo rv ,BindingResult result) {
    	SystemRoleSVo srsv = new SystemRoleSVo();
    	BeanUtils.copyProperties(rv, srsv);
    	SystemRole srv = new SystemRole();
    	BeanUtils.copyProperties(rv, srv);
    	srsv.setSr(srv);
    	return roleService.updateBySystemRoleVo(srsv);
    }
    
    @ApiOperation("删除角色")
    @PostMapping("/del/{id}")
    public int del(@PathVariable @ApiParam("角色ID") int id) {
    	return roleService.delete(id);
    }
    
    @ApiOperation("批量删除角色")
    @PostMapping("/dels/{ids}")
    public int dels(@Valid @ApiParam("角色ID") @RequestBody Integer[] ids){
    	if(ids==null || ids.length==0){
			throw new RuntimeException("无选中删除数据");
		}
    	return roleService.deletes(ids);
    }
    
    @ApiOperation("异步加载子节点")
    @PostMapping("/loadnodes/{orgid}")
    public List<SystemCommNodeVo> loadNodes(@PathVariable @ApiParam("orgid父节点ID") int orgid) {
    	return roleService.loadNodes(orgid);
    }
}

