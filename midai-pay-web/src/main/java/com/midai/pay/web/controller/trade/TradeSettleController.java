package com.midai.pay.web.controller.trade;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.midai.framework.query.PageVo;
import com.midai.pay.settlemoney.query.BoGetMoneyQuery;
import com.midai.pay.settlemoney.vo.SettleCustomerVo;
import com.midai.pay.trade.service.TradeSettleService;
import com.midai.pay.trade.vo.TradeEticketVo;
import com.midai.pay.user.service.SystemUserService;

@ApiModel("交易结算")
@RestController
@RequestMapping("/trade/settle")
public class TradeSettleController {

	@Reference
	public TradeSettleService tradeSettleService;
	
	@Reference
	private SystemUserService systemUserService;
	
	
	@ApiOperation("交易结算列表")
	@PostMapping("/list")
	public PageVo<SettleCustomerVo> List(@RequestBody BoGetMoneyQuery query)
	{
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication() .getPrincipal();
		String inscode = systemUserService.getInscode(userDetails.getUsername());
		query.setInscode(inscode);
		
		PageVo<SettleCustomerVo> pageVo=new PageVo<SettleCustomerVo>();
		pageVo.setRows(tradeSettleService.findqueryToTrade(query));
		pageVo.setTotal(tradeSettleService.findqueryToTradeCount(query));
		return pageVo;
	}
	
	@ApiOperation("交易结算批量通过")
	@PostMapping("/update")
	public int batchUpdate(@RequestBody @ApiParam(example="'a','b','c'") String lognos)
	{
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication() .getPrincipal();
		return tradeSettleService.bacthUpdateCheckState(lognos, userDetails.getUsername());
	}
	
	@ApiOperation("交易结算详情")
	@PostMapping("/select/{mercId}")
	public List<TradeEticketVo> getPageDetailList(@PathVariable("mercId") @ApiParam("商户小票号") String mercId)
	{
		return tradeSettleService.getPageDetailList(mercId);
	}
	
}
