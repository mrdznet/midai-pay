package com.midai.pay.web.controller.system;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.midai.pay.user.service.SystemModuleService;
import com.midai.pay.user.vo.SystemModuleSVo;
import com.midai.pay.web.vo.system.SystemModuleVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("菜单列表")
@RestController
@RequestMapping("/system/module")
public class SystemModuleController {


	@Reference
	private SystemModuleService systemModuleService;

	@ApiOperation(value="流程列表")
	@PostMapping("/load")
	public List<SystemModuleVo> load(){
		Map<SystemModuleSVo, List<SystemModuleSVo>> map = systemModuleService.getAllResultMap();
		Set<SystemModuleSVo> smsvList = map.keySet();
		List<SystemModuleVo> list = new ArrayList<SystemModuleVo>();
		Iterator<SystemModuleSVo> it = smsvList.iterator();
		
		while(it.hasNext()) {
			SystemModuleSVo ssv = it.next();
			SystemModuleVo smv = new SystemModuleVo();
			smv.setModuleId(ssv.getModuleId());
			smv.setModuleName(ssv.getModuleName());
			smv.setList(map.get(ssv));
			list.add(smv);
		}
		return list;
	}

}
