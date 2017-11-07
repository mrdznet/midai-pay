package com.midai.pay.web.controller.autopaymoney;

import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
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
import com.midai.framework.common.ExcelException;
import com.midai.framework.common.ExcelUtil;
import com.midai.framework.query.PageVo;
import com.midai.pay.autopaymoney.query.BoAutoPayMoneyQuery;
import com.midai.pay.autopaymoney.service.AutoPayMoneyService;
import com.midai.pay.autopaymoney.vo.AutoPayMoneyVo;
import com.midai.pay.user.service.SystemUserService;
import com.midai.pay.web.controller.agent.AgentController;

@Api("打款相关")
@RestController
@RequestMapping("/autopay")
public class AutoPayMoneyController {

	@Reference
	public AutoPayMoneyService autoPayMoneyService;
	
	@Reference
	public SystemUserService systemUserService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AgentController.class);
	
	@ApiOperation("自动打款列表")
	@PostMapping("/list1")
	public PageVo<AutoPayMoneyVo> List1(@RequestBody BoAutoPayMoneyQuery query)
	{
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication() .getPrincipal();
		String inscode = systemUserService.getInscode(userDetails.getUsername());
		query.setInscode(inscode);
		PageVo<AutoPayMoneyVo> vo=new PageVo<AutoPayMoneyVo>();
		vo.setRows(autoPayMoneyService.findQueryAutoPayMoney1(query));
		vo.setTotal(autoPayMoneyService.findQueryAutoPayMoneyCount1(query));
		return vo;
	}
	@ApiOperation("打款结果查询")
	@PostMapping("/list")
	public PageVo<AutoPayMoneyVo> List(@RequestBody BoAutoPayMoneyQuery query)
	{
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication() .getPrincipal();
		String inscode = systemUserService.getInscode(userDetails.getUsername());
		query.setInscode(inscode);
		PageVo<AutoPayMoneyVo> vo=new PageVo<AutoPayMoneyVo>();
		vo.setRows(autoPayMoneyService.findQueryAutoPayMoney(query));
		vo.setTotal(autoPayMoneyService.findQueryAutoPayMoneyCount(query));
		return vo;
	}
	@ApiOperation("打款结果excel下载")
	@GetMapping("/excelExport")
	public JSONObject excelExport(HttpServletRequest request, HttpServletResponse response, BoAutoPayMoneyQuery query) {
	
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication() .getPrincipal();
		String inscode = systemUserService.getInscode(userDetails.getUsername());
		query.setInscode(inscode);
		JSONObject jsonObject = new JSONObject();
		Integer count = autoPayMoneyService.excelQueryAutoPayMoneyCount(query);
		if(count <= 0) {
			LOGGER.info("导出数据为空！");
			jsonObject.put("result","fail");
			return jsonObject;
		}
		List<AutoPayMoneyVo> list = autoPayMoneyService.excelQueryAutoPayMoney(query);
		if(list == null || list.isEmpty()) {
			LOGGER.info("导出数据为空！");
			jsonObject.put("result","fail");
			return jsonObject;
		}
		try {
			ExcelUtil.listToExcel(list, getFieldMap(), "打款结果查询表", response, null);
			LOGGER.info("打款结果数据导出成功！");
		} catch (ExcelException e) {
			LOGGER.info("打款结果数据导出失败！");
			e.printStackTrace();
		}
		return jsonObject;
	}
	
	private LinkedHashMap<String, String> getFieldMap() {
		LinkedHashMap<String, String> fieldMap = new LinkedHashMap<String, String>();
		fieldMap.put("mercId", "商户小票号");
		fieldMap.put("mercName", "商户名称");
		fieldMap.put("tixianDatetime", "提现日期");
		fieldMap.put("payTime", "结算日期");
		fieldMap.put("payMoney", "结算金额");
		fieldMap.put("mchntSingleFee", "提现手续费");
		fieldMap.put("bankName", "开户行");
		fieldMap.put("accountName", "收款人名称");
		fieldMap.put("accountNo", "收款人卡号");
		fieldMap.put("payStatestr", "处理结果");
		fieldMap.put("payChannel", "代付渠道");
		fieldMap.put("channelCode", "通道返回码");
		fieldMap.put("errorMsg", "备注");
		
		fieldMap.put("agentName", "代理商名称");
		fieldMap.put("agentLevel", "代理商等级");
		return fieldMap;
	}
	
	@ApiOperation("自动打款")
	@PostMapping("/pay/{logon}/{dfway}")
	public int pay(@PathVariable("logon") @ApiParam("流水号")String logon, @PathVariable("dfway") @ApiParam("通道号")String dfway){
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		return autoPayMoneyService.autoPay(logon, dfway, userDetails.getUsername());
	}
	
	@ApiOperation("打款实时结果查询")
	@GetMapping("/payQuery")
	public int payQuery(){
		autoPayMoneyService.payQuery();
		return 1;
	}
	
	@ApiOperation("打款失败查询")
	@PostMapping("/errList")
	public PageVo<AutoPayMoneyVo> errList(@RequestBody BoAutoPayMoneyQuery query){
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String inscode = systemUserService.getInscode(userDetails.getUsername());
		query.setInscode(inscode);
		
		PageVo<AutoPayMoneyVo> vo=new PageVo<AutoPayMoneyVo>();
		vo.setRows(autoPayMoneyService.findErrList(query));
		vo.setTotal(autoPayMoneyService.findErrListCount(query));
		return vo;
	}
	
	@ApiOperation("标记打款状态")
	@PostMapping("/signPay/{logon}/{state}")
	public int signPay(@PathVariable("logon") @ApiParam("流水号")String logon, @PathVariable("state") @ApiParam("打款状态: 0-失败; 1-成功")Integer state){
		if(null != state && StringUtils.isNotEmpty(logon)){
			boolean isSuccess = false;
			if(state.equals(1)) isSuccess = true;
			
			autoPayMoneyService.signPay(logon, isSuccess);
		}
		return 1;
	}
	
}
