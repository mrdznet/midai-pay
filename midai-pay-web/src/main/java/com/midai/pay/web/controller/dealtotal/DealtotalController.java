package com.midai.pay.web.controller.dealtotal;

import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.midai.framework.common.ExcelException;
import com.midai.framework.common.ExcelUtil;
import com.midai.framework.query.PageVo;
import com.midai.pay.dealtotal.query.DealtotalQuery;
import com.midai.pay.dealtotal.service.DealtotalQuickService;
import com.midai.pay.dealtotal.service.DealtotalService;
import com.midai.pay.dealtotal.vo.DealtotalQuickVo;
import com.midai.pay.dealtotal.vo.DealtotalVo;
import com.midai.pay.user.service.SystemUserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * ClassName:User <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2016年9月21日 <br/>
 * 
 * @author zt
 * @version
 * @since JDK 1.7
 * @see
 */

@Api("交易历史明细")
@RestController
@RequestMapping("/dealtotal")
public class DealtotalController {

	private static final Logger LOGGER = LoggerFactory.getLogger(DealtotalController.class);

	@Reference
	private DealtotalService bodealtotalService;

	@Reference
	private SystemUserService systemUserService;

	@Reference
	private DealtotalQuickService dealtotalQuickService;

	@ApiOperation("历史交易明细查询")
	@PostMapping("/list")
	public PageVo<DealtotalVo> queryList(@RequestBody DealtotalQuery query) {

		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String inscode = systemUserService.getInscode(userDetails.getUsername());
		query.setInscode(inscode);

		PageVo<DealtotalVo> pageVo = new PageVo<DealtotalVo>();
		pageVo.setRows(bodealtotalService.queryList(query));
		pageVo.setTotal(bodealtotalService.queryCount(query));
		return pageVo;
	}

	@ApiOperation("代理商 交易查询")
	@PostMapping("/agentList")
	public PageVo<DealtotalVo> angetQueryList(@RequestBody DealtotalQuery query) {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		String allAgents = systemUserService.getAllChildAgents(userDetails.getUsername());
		query.setAgentId(allAgents);

		PageVo<DealtotalVo> pageVo = new PageVo<DealtotalVo>();
		pageVo.setRows(bodealtotalService.angetQueryList(query));
		pageVo.setTotal(bodealtotalService.angetQueryCount(query));
		
		return pageVo;
	}

	@ApiOperation("代理商交易交易excel导出总记录数")
	@PostMapping("/agentDealtotalStatisticsCount")
	public int agentDealtotalStatisticsCount(@RequestBody DealtotalQuery query) {

		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String allAgents = systemUserService.getAllChildAgents(userDetails.getUsername());
		query.setAgentId(allAgents);

		int result = bodealtotalService.AgentExcelDownBodealtotalCount(query);
		if (result > 0) {
			LOGGER.info("导出的历史交易明细表有记录数");
		} else {
			LOGGER.info("导出的历史交易明细表的数据为空");
		}

		return result;
	}

	@ApiOperation("代理商交易Excel导出")
	@GetMapping("/agentExcelExport")
	public void agentDealtotalStatisticsExcelExport(HttpServletRequest request, HttpServletResponse response,
			DealtotalQuery query) {

		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String allAgents = systemUserService.getAllChildAgents(userDetails.getUsername());
		query.setAgentId(allAgents);

		List<DealtotalVo> list = bodealtotalService.AgentExcelDownBodealtotal(query);
		if (list == null || list.isEmpty()) {
			throw new RuntimeException("历史交易明细表为空");
		}
		try {
			ExcelUtil.listToExcel(list, getFieldMap(), "商户信息表", response, null);
			LOGGER.info("导出历史交易明细表成功");
		} catch (ExcelException e) {
			LOGGER.info("导出历史交易明细表失败");
			e.printStackTrace();
		}
	}

	@ApiOperation("历史交易明细excel导出总记录数")
	@PostMapping("/dealtotalStatisticsCount")
	public int dealtotalStatisticsCount(@RequestBody DealtotalQuery query) {

		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String inscode = systemUserService.getInscode(userDetails.getUsername());
		query.setInscode(inscode);

		int result = bodealtotalService.ExcelDownBodealtotalCount(query);
		if (result > 0) {
			LOGGER.info("导出的历史交易明细表有记录数");
		} else {
			LOGGER.info("导出的历史交易明细表的数据为空");
		}

		return result;
	}

	@ApiOperation("历史交易明细表Excel导出")
	@GetMapping("/excelExport")
	public void dealtotalStatisticsExcelExport(HttpServletRequest request, HttpServletResponse response,
			DealtotalQuery query) {

		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String inscode = systemUserService.getInscode(userDetails.getUsername());
		query.setInscode(inscode);

		List<DealtotalVo> list = bodealtotalService.ExcelDownBodealtotal(query);
		if (list == null || list.isEmpty()) {
			throw new RuntimeException("历史交易明细表为空");
		}
		try {
			ExcelUtil.listToExcel(list, getFieldMap(), "商户信息表", response, null);
			LOGGER.info("导出历史交易明细表成功");
		} catch (ExcelException e) {
			LOGGER.info("导出历史交易明细表失败");
			e.printStackTrace();
		}
	}

	private LinkedHashMap<String, String> getFieldMap() {
		LinkedHashMap<String, String> fieldMap = new LinkedHashMap<String, String>();
		fieldMap.put("hostTransSsn", "交易流水号");
		fieldMap.put("respCdLoc", "返回码");
		fieldMap.put("respCdLocDsp", "返回信息");
		fieldMap.put("mchntCodeIn", "商户小票号");
		fieldMap.put("mchntName", "商户名称");
		fieldMap.put("mobile", "商户手机号");
		fieldMap.put("deviceNoIn", "机身号");
		fieldMap.put("transTime", "交易时间");
		fieldMap.put("transAmt", "交易金额");
		fieldMap.put("transCardNo", "银行卡号");
		fieldMap.put("cardIssName", "发卡行");
		fieldMap.put("cardKind", "卡类型");
		fieldMap.put("cardIssId", "发卡行编号");
		fieldMap.put("transCode", "交易类型");
		fieldMap.put("specialFeeType", "商户类型");
		fieldMap.put("transStatus", "交易状态");
		fieldMap.put("mchntCodeOut", "大商户");
		fieldMap.put("routInstIdCd", "通道机构");
		fieldMap.put("agentId", "代理商编号");
		fieldMap.put("agentName", "代理商名称");
		fieldMap.put("mchntSingleFee", "提现手续费");
		fieldMap.put("mchntRate", "商户扣率");

		fieldMap.put("tixianAmt", "结算金额");
		fieldMap.put("tixianFeeamt", "结算手续费");
		fieldMap.put("transFee", "通道手续费");
		fieldMap.put("channelMoney", "通道结算金额");
		fieldMap.put("profits", "利润");
		fieldMap.put("channelCode", "通道返回码");
		fieldMap.put("tixiLogno", "打款流水号");
		fieldMap.put("inscode", "顶级代理商");
		return fieldMap;
	}

	@ApiOperation("保存快捷交易记录")
	@PostMapping("/dealQuickSave")
	public void dealtotalStatisticsCount(@RequestBody DealtotalQuickVo vo) {
		dealtotalQuickService.save(vo);
	}
}
