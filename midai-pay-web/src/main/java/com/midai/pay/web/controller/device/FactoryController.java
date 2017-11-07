package com.midai.pay.web.controller.device;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.midai.framework.query.PageVo;
import com.midai.pay.device.query.BoFactoryQuery;
import com.midai.pay.device.service.BoFactoryService;
import com.midai.pay.device.vo.FactoryVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api("厂商管理")
@RestController
@RequestMapping("/factory")
public class FactoryController {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(FactoryController.class);
    
    @Reference
    private BoFactoryService factoryService;
    
	@ApiOperation("厂商列表")
    @PostMapping("/list")
	public PageVo<FactoryVo> applyQuery(@RequestBody BoFactoryQuery query){
		PageVo<FactoryVo> vo = new PageVo<FactoryVo>();
		vo.setRows(factoryService.factoryQuery(query));
		vo.setTotal(factoryService.factoryCount(query));
		
		return vo;
	}
    
	@ApiOperation("新增厂商")
    @PostMapping("/add")
	public int add(@RequestBody FactoryVo vo){
		return factoryService.add(vo);
	}
	
	@ApiOperation("删除厂商")
    @PostMapping("/del/{ids}")
	public int del(@PathVariable("ids") @ApiParam("厂商ID,多个以逗号分隔") String ids){
		if(StringUtils.isEmpty(ids)){
            throw new RuntimeException("厂商编号为空");
        }
		return factoryService.del(ids);
	}
	
	@ApiOperation("查询厂商id、名称")
    @GetMapping("/queryFactoryInfo")
	public List<FactoryVo> queryFactoryInfo() {
		return factoryService.queryFactoryInfo();
	}
}
