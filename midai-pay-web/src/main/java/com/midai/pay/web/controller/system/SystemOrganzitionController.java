/**
 * Project Name:midai-pay-web
 * File Name:SystemOrganzitionController.java
 * Package Name:com.midai.pay.web.controller
 * Date:2016年9月13日下午2:48:40
 * Copyright (c) 2016, www midaigroup com Technology Co., Ltd. All Rights Reserved.
*/

package com.midai.pay.web.controller.system;

import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.midai.framework.query.PageVo;
import com.midai.pay.user.entity.SystemOrganizationModel;
import com.midai.pay.user.query.SystemCommNodeQuery;
import com.midai.pay.user.service.SystemOrganizationService;
import com.midai.pay.user.vo.SystemCommNodeVo;
import com.midai.pay.web.vo.system.SystemOrganizationVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * ClassName:SystemOrganzitionController [组织机构管理] <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2016年9月13日 下午2:48:40 <br/>
 * 
 * @author 屈志刚
 * @version
 * @since JDK 1.7
 * @see
 */
@Api("组织机构管理")
@RestController
@RequestMapping("/system/org")
public class SystemOrganzitionController {

	@Reference
	private SystemOrganizationService systemOrganizationService;

	@ApiOperation("查询所有节点")
	@PostMapping(path = "/loadall")
	public List<SystemCommNodeVo> loadAllNodes() {
		return systemOrganizationService.getAllOrgAndUserNodes();
	}

	@ApiOperation("新增组织机构")
	@PostMapping(path = "/save")
	public int save(@Valid @RequestBody SystemOrganizationVo org, BindingResult result) {
		SystemOrganizationModel systemOrg = new SystemOrganizationModel();
		BeanUtils.copyProperties(org, systemOrg);
		systemOrg.setCreateTime(new Date());
		return systemOrganizationService.insertOrg(systemOrg);
	}

	@ApiOperation("根据id删除组织机构")
	@PostMapping(path = "/del/{id}")
	public int del(@PathVariable int id) {
		if (id == 0) {
			throw new RuntimeException("不能删除根节点");
		}
		return systemOrganizationService.deleteById(id);
	}

	@ApiOperation("修改组织机构")
	@PostMapping(path = "/update")
	public int update(@Valid @RequestBody SystemOrganizationVo org, BindingResult result) {
		int num = 0;
		SystemOrganizationModel systemOrg = new SystemOrganizationModel();
		BeanUtils.copyProperties(org, systemOrg);
		if (!systemOrganizationService.checkOrgExists(systemOrg.getOrganizationName(), systemOrg.getOrganizationId(),
				systemOrg.getParentId())) {
			num = systemOrganizationService.update(systemOrg);
		} else {
			throw new RuntimeException("该组织机构名称已存在");
		}
		return num;
	}

	@ApiOperation("编辑组织机构")
	@PostMapping("/edit/{id}")
	public SystemOrganizationVo edit(@PathVariable int id) {

		SystemOrganizationVo sv = new SystemOrganizationVo();
		SystemOrganizationModel systemOrg = systemOrganizationService.findById(id);
		BeanUtils.copyProperties(systemOrg, sv);
		return sv;
	}

	@ApiOperation("分页查询")
	@PostMapping(path = "/querybypid")
	public PageVo<SystemCommNodeVo> queryByParentId(@RequestBody SystemCommNodeQuery query) {

		String str = query.getPid();
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		PageVo<SystemCommNodeVo> pageVo = new PageVo<SystemCommNodeVo>();
		if (!isNum.matches()) {
			return pageVo;
		}
		query.setOrder(" t.update_time desc ");
		pageVo.setRows(systemOrganizationService.queryOrgAndUserList(query));
		pageVo.setTotal(systemOrganizationService.queryOrgAndUserCount(query));
		return pageVo;

	}

	@ApiOperation("异步加载子节点")
	@PostMapping("/loadnodes/{orgid}")
	public List<SystemCommNodeVo> loadNodes(@PathVariable @ApiParam("orgid 父节点ID") int orgid) {
		return systemOrganizationService.loadNodes(orgid);
	}

	@ApiOperation("父级目录")
	@PostMapping("/findorgcatalogue/{id}")
	public String findOrgCatalogue(@PathVariable int id) {
		return systemOrganizationService.findOrgCatalogue(id);
	}
	
	@ApiOperation("批量删除")
    @PostMapping("/batchdel/{ids}")
	public int batchDel(@PathVariable @ApiParam("用户id字符串，逗号分隔") String ids) {
		int num = 0 ;
		if(StringUtils.isNotEmpty(ids)) {
			StringBuffer sb = new StringBuffer("");
			for(String id : ids.split(",")) {
				String cids = systemOrganizationService.findThemselvesAndChildrenIds(Integer.parseInt(id.trim()));
				if(StringUtils.isNotEmpty(cids)) {
					sb.append(cids).append(",");
				}
			}
			if(sb.length() > 0) {
				sb.setLength(sb.length()-1);
				num = systemOrganizationService.batchDel(sb.toString());
			}
		}
		return num;
	}
}
