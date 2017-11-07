package com.midai.pay.web.controller.system;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.midai.pay.user.entity.SystemMCC;
import com.midai.pay.user.service.SystemMccService;
import com.midai.pay.user.vo.SystemMccVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api("行业和mcc")
@RestController
@RequestMapping("/system/mcc")
public class SystemMccController {
	
	@Reference
	private SystemMccService systemMccService;
	
	@ApiOperation(value="行业和mcc")
	@GetMapping("/loadData/{code}")
	public List<SystemMccVo>  loadAllCity(@PathVariable("code") @ApiParam("编码,加载行业列表时传0值") String code){
		List<SystemMCC> plist = systemMccService.loadData(code);
		
		List<SystemMccVo> sal = new ArrayList<SystemMccVo>();
		if(plist!=null && plist.size()>0) {
			SystemMccVo sav;
			for(SystemMCC p : plist) {
				sav = new SystemMccVo();
				sav.setId( p.getId());
				sav.setName(p.getName());
				sav.setMcc(p.getMcc());
				
				sal.add(sav);
			}
		}
		 return sal;
	}
	
}
