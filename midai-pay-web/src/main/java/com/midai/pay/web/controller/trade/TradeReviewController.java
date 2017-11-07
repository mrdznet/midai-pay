package com.midai.pay.web.controller.trade;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.midai.framework.query.PageVo;
import com.midai.pay.trade.query.TradeReviewQuery;
import com.midai.pay.trade.service.BoEticketService;
import com.midai.pay.trade.service.TradeReviewService;
import com.midai.pay.trade.vo.EticketReviewVo;
import com.midai.pay.trade.vo.TradeReviewVo;
import com.midai.pay.user.service.SystemUserService;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@ApiModel("交易审核")
@RestController
@RequestMapping("/trade/review")
public class TradeReviewController {
	
	@Reference
	public TradeReviewService tradeReviewService;
	@Reference
	public BoEticketService boEticketService;
	
	@Reference
	private SystemUserService systemUserService;
	
	
	@ApiOperation("交易审核列表")
	@PostMapping("/list")
	public PageVo<TradeReviewVo> List(@RequestBody TradeReviewQuery query){
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication() .getPrincipal();
		String inscode = systemUserService.getInscode(userDetails.getUsername());
		query.setInscode(inscode);
		
		
		PageVo<TradeReviewVo> pageVo=new PageVo<TradeReviewVo>();
		
		pageVo.setRows(tradeReviewService.findQuery(query));
		pageVo.setTotal(tradeReviewService.findQueryCount(query));
		return pageVo;
	}
	
	@ApiOperation("小票审核通过")
	@PostMapping("/through")
	public int saveReviewThrough(@RequestBody @ApiParam(example="'a','b','c'") String hostTransSsn){
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		return boEticketService.saveReviewThrough(hostTransSsn, userDetails.getUsername());
	}
	
	@ApiOperation("小票审核通过")
	@PostMapping("/unthrough")
	public int saveReviewNoThrough(@RequestBody EticketReviewVo reviewVo){
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		reviewVo.setCreateUser(userDetails.getUsername());
		
		return boEticketService.saveReviewNoThrough(reviewVo);
	}
	
	@ApiOperation("通道小票生成")
	@PostMapping("/channelList")
	public PageVo<TradeReviewVo> channelList(@RequestBody TradeReviewQuery query){
		PageVo<TradeReviewVo> pageVo=new PageVo<TradeReviewVo>();
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		String inscode = systemUserService.getInscode(userDetails.getUsername());
		query.setInscode(inscode);
		
		if(null!=query && StringUtils.isNotEmpty(query.getHostTransSsn())){
			pageVo.setRows(tradeReviewService.channelEticketQuery(query, userDetails.getUsername()));
			pageVo.setTotal(tradeReviewService.channelEticketCount(query));
		}else{
			pageVo.setRows(new ArrayList<TradeReviewVo>());
			pageVo.setTotal(0);
		}
		
		return pageVo;
	}
	
	@ApiOperation("获取单个小票")
	@GetMapping("/pay/{logon}")
	public String eticket(@PathVariable("logon") @ApiParam("流水号")String logon){
		if(StringUtils.isNotEmpty(logon)){
			
			return tradeReviewService.eticketUrl(logon);
		}
		return null;
	}
}
