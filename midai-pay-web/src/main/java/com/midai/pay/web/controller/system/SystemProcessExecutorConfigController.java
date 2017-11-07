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
import com.midai.pay.user.service.SystemProcessExecutorConfigService;
import com.midai.pay.user.vo.SystemProcessExecutorConfigSVo;
import com.midai.pay.web.vo.system.SystemProcessExecutorConfigVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("流程列表")
@RestController
@RequestMapping("/system/process")
public class SystemProcessExecutorConfigController {


	@Reference
	private SystemProcessExecutorConfigService systemProcessExecutorConfigService;

	@ApiOperation(value="流程列表")
	@PostMapping("/load")
	public List<SystemProcessExecutorConfigVo> load(){
		Map<String, List<SystemProcessExecutorConfigSVo>> map = systemProcessExecutorConfigService.getAllMap();
		List<SystemProcessExecutorConfigVo> svList = new ArrayList<SystemProcessExecutorConfigVo>();
		Set<String>  set = map.keySet();
		Iterator<String> it = set.iterator();
		while(it.hasNext()) {
			String key = it.next();
			SystemProcessExecutorConfigVo spcv = new SystemProcessExecutorConfigVo();
			spcv.setName(key);
			spcv.setList(map.get(key));
			svList.add(spcv);
		}
		return svList;
	}

}
