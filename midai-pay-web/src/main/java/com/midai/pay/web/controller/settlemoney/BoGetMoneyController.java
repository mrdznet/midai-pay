package com.midai.pay.web.controller.settlemoney;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.midai.framework.query.PageVo;
import com.midai.pay.settlemoney.query.BoGetMoneyQuery;
import com.midai.pay.settlemoney.service.BoGetMoneyService;
import com.midai.pay.settlemoney.vo.SettleCustomerVo;
import com.midai.pay.user.service.SystemUserService;

@Api("清分清算")
@RestController
@RequestMapping("/getmoney")
public class BoGetMoneyController {

	@Reference
	 public BoGetMoneyService boGetMoneyService;
	
	@Reference
	public SystemUserService systemUserService;
	
	@ApiOperation("清分清算列表查询")
	@PostMapping("/list")
	public PageVo<SettleCustomerVo> List(@RequestBody BoGetMoneyQuery query )
	{
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication() .getPrincipal();
		String inscode = systemUserService.getInscode(userDetails.getUsername());
		query.setInscode(inscode);
		PageVo<SettleCustomerVo> pageVo=new PageVo<SettleCustomerVo>();
		pageVo.setRows(boGetMoneyService.findqueryBoGetMoney(query));
		pageVo.setTotal(boGetMoneyService.findqueryBoGetMoneyCount(query));
		return pageVo;
	}
	
	@ApiOperation("通过")
	@PostMapping("/batchupdate")
	public int batchUpdate(@RequestBody @ApiParam(example="'a','b','c'") String logNos)
	{
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication() .getPrincipal();

		if (logNos==null) {
			throw new RuntimeException("参数传入失败");
		}
		return boGetMoneyService.batchUpdate(logNos,userDetails.getUsername());
	}
}
