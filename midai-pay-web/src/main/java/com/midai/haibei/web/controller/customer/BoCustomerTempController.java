package com.midai.haibei.web.controller.customer;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.midai.haibei.customer.service.HaibeiCustomerBankUpdateProcessService;
import com.midai.pay.customer.entity.BoCustomerTemp;
import com.midai.pay.web.controller.customer.BoCustomerController;

@Api("商户管理")
@RestController
@RequestMapping("/haibei/customer")
public class BoCustomerTempController {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(BoCustomerController.class);
	
	@Reference 
	private HaibeiCustomerBankUpdateProcessService haibeiService;
	
	@ApiOperation("海贝修改商户入账行申请加载")
	@GetMapping("/load/{mercNo}")
	public BoCustomerTemp startload(@PathVariable("mercNo") @ApiParam("商户编号")  String mercNo)
	{
		return haibeiService.loadBoCustomerBymercNo(mercNo);
	}
	
	@ApiOperation("海贝修改商户入账行申请")
	@PostMapping("/apply")
	public String startApply(@RequestBody BoCustomerTemp boCustomerTemp)
	{ 
		int num=haibeiService.haibeiApply(boCustomerTemp);
		if (num > 0) {
			return "success";
		}else {			
			return "有申请正在进行中,不能再次进行申请呦!";
		}
	}
	
	@ApiOperation("海贝修改商户入账行审核加载")
	@GetMapping("/review/{mercNo}")
	public BoCustomerTemp reviewload(@PathVariable("mercNo") @ApiParam("商户编号")  String mercNo)
	{
		return haibeiService.loadBoCustomerTempBymercNo(mercNo);
	}
	
	@ApiOperation("海贝修改商户入账信息审核")
	@PostMapping("/review")
	public String reviewApply(@RequestBody BoCustomerTemp boCustomerTemp)
	{
		UserDetails userDetails = (UserDetails) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();
		return haibeiService.haibeiCheck(boCustomerTemp,userDetails.getUsername());
	}
	
}
