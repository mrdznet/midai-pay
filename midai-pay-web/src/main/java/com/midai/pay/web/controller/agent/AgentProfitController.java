package com.midai.pay.web.controller.agent;

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

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.midai.framework.common.DoubleUtil;
import com.midai.framework.common.ExcelException;
import com.midai.framework.common.ExcelUtil;
import com.midai.framework.query.PageVo;
import com.midai.pay.agent.common.enums.AgentProfitCustomerGradeEnum;
import com.midai.pay.agent.common.enums.AgentProfitTransTypeEnum;
import com.midai.pay.agent.query.AgentProfitQuery;
import com.midai.pay.agent.service.AgentProfitDetailService;
import com.midai.pay.agent.service.AgentProfitStatisticsService;
import com.midai.pay.agent.vo.AgentProfitStatisticsVo;
import com.midai.pay.agent.vo.AgentProfitVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;

@Api("代理商分润")
@RestController
@RequestMapping("/agentprofit")
public class AgentProfitController {

	private static final Logger LOGGER = LoggerFactory.getLogger(AgentController.class);

	@Reference
	private AgentProfitStatisticsService agentProfitStatisticsService;
	@Reference
	private AgentProfitDetailService agentProfitDetailService;

	@ApiOperation("代理商分润统计分页")
	@PostMapping("/statistics/paginate")
	public PageVo<AgentProfitVo> queryList(@RequestBody AgentProfitQuery query) {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		query.setUserName(userDetails.getUsername());
		return agentProfitStatisticsService.paginateAgentProfit(query, true);
	}

	@ApiOperation("代理商分润统计查询子代理商")
	@PostMapping("/statistics/query")
	public List<AgentProfitVo> queryListByParentAgentId(@RequestBody AgentProfitVo pv) {
		if (pv == null || StringUtils.isEmpty(pv.getAgentId()) || pv.getSettlementTime() == null) {
			LOGGER.error("代理商编号或清算时间为空！");
			throw new RuntimeException("代理商编号或清算时间为空！");
		}
		AgentProfitQuery query = new AgentProfitQuery();
		query.setAgentId(pv.getAgentId());
		query.setSettlementTimeEnd(pv.getSettlementTime());
		query.setSettlementTimeStart(pv.getSettlementTime());
		query.setFlag(AgentProfitCustomerGradeEnum.GRADE_XJ.getCode());
		query.setPageSize(1000000);
		PageVo<AgentProfitVo> page = agentProfitStatisticsService.paginateAgentProfit(query, false);
		return page.getRows();
	}

	@ApiOperation("代理商分润统计统计查询结果")
	@PostMapping("/statistics/querystatisticssum")
	public AgentProfitStatisticsVo queryAgentProfitStatisticsInfo(@RequestBody AgentProfitQuery query) {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		query.setUserName(userDetails.getUsername());
		query.setPageSize(100000);
		PageVo<AgentProfitVo> page = agentProfitStatisticsService.paginateAgentProfit(query, false);
		List<AgentProfitVo> list = page.getRows();
		AgentProfitStatisticsVo apv = new AgentProfitStatisticsVo();
		apv.setTransCount(0);
		apv.setProfitTotalAmount(0D);
		apv.setProfitRetainedAmount(0D);
		if (list != null && list.size() > 0) {

			String agentNo = list.get(0).getAgentId();
			for (AgentProfitVo av : list) {
				if (agentNo.equals(av.getAgentId())) {

					apv.setProfitTotalAmount(DoubleUtil.add(apv.getProfitTotalAmount(), av.getProfitTotal()));
					apv.setProfitRetainedAmount(DoubleUtil.add(apv.getProfitRetainedAmount(), av.getProfit()));
					apv.setTransCount(apv.getTransCount() + av.getTransCount());
				} else {
					apv.setProfitTotalAmount(0D);
					apv.setProfitRetainedAmount(0D);
					apv.setTransCount(0);
					break;
				}
			}
		}
		return apv;
	}

	@ApiOperation("代理商分润统计确认分润")
	@PostMapping("/statistics/confirm")
	public JSONObject updateAgentProfit(@RequestBody List<AgentProfitVo> voList) {
		JSONObject jsonObject = new JSONObject();
		try {
			if (voList != null && voList.size() > 0) {
				for (AgentProfitVo apv : voList) {
					if (StringUtils.isEmpty(apv.getSettlementTime()) || StringUtils.isEmpty(apv.getCode())) {
						jsonObject.put("result", "fail");
						LOGGER.error("批量操作时间和CODE不能为空");
					}
				}
				agentProfitStatisticsService.updateAgentProfit(voList);
				jsonObject.put("result", "success");
			} else {
				jsonObject.put("result", "fail");
				LOGGER.error("批量操作不能传空值");
			}
		} catch (Exception e) {
			jsonObject.put("result", "fail");
			LOGGER.error("代理商分润统计确认分润失败", e);
		}
		return jsonObject;
	}

	@ApiOperation("代理商分润明细查询结果")
	@PostMapping("/detail/querydetailsum")
	public AgentProfitStatisticsVo queryAgentProfitDetailInfo(@RequestBody AgentProfitQuery query) {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		query.setUserName(userDetails.getUsername());
		query.setPageSize(100000);
		PageVo<AgentProfitVo> pv = agentProfitDetailService.paginateAgentProfitDetail(query, "exp");

		List<AgentProfitVo> list = pv.getRows();
		AgentProfitStatisticsVo apv = new AgentProfitStatisticsVo();
		apv.setTransCount(0);
		apv.setProfitTotalAmount(0D);
		apv.setProfitRetainedAmount(0D);

		if (list != null && list.size() > 0) {

			String agentNo = list.get(0).getAgentId();
			for (AgentProfitVo av : list) {
				if (agentNo.equals(av.getAgentId())) {

					apv.setProfitTotalAmount(DoubleUtil.add(apv.getProfitTotalAmount(), av.getProfitTotal()));
					apv.setProfitRetainedAmount(DoubleUtil.add(apv.getProfitRetainedAmount(), av.getProfit()));
					apv.setTransCount(apv.getTransCount() + 1);
				} else {
					apv.setProfitTotalAmount(0D);
					apv.setProfitRetainedAmount(0D);
					apv.setTransCount(0);
					break;
				}
			}
		}
		return apv;
	}

	@ApiOperation("代理商分润明细分页")
	@PostMapping("/detail/paginate")
	public PageVo<AgentProfitVo> queryDetailList(@RequestBody AgentProfitQuery query) {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		query.setUserName(userDetails.getUsername());
		return agentProfitDetailService.paginateAgentProfitDetail(query, null);
	}

	@ApiOperation("代理商分润明细数据导出（有条件）")
	@GetMapping("/detail/exportdetail")
	public JSONObject excelExportDetail(HttpServletRequest request, HttpServletResponse response,
			AgentProfitQuery query) {
		JSONObject jsonObject = new JSONObject();

		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		query.setUserName(userDetails.getUsername());

		query.setPageSize(10000000);
		PageVo<AgentProfitVo> pv = agentProfitDetailService.paginateAgentProfitDetail(query, "exp");

		List<AgentProfitVo> list = pv.getRows();

		if (list == null || list.isEmpty()) {
			LOGGER.info("导出数据为空！");
			jsonObject.put("result", "fail");
			return jsonObject;
		}
		
		for (AgentProfitVo av : list) {
			av.setTransTypeName(AgentProfitTransTypeEnum.getName(av.getTransType()));
		}
		
		try {
			ExcelUtil.listToExcel(list, getDetailFieldMap(), "代理商分润明细信息表", response, null);
			LOGGER.info("代理商分润明细信息导出成功！");
		} catch (ExcelException e) {
			LOGGER.error("代理商分润明细信息失败！", e);
		}
		return jsonObject;
	}

	@ApiOperation("代理商分润统计数据导出（有条件）")
	@GetMapping("/exportstatistics")
	public JSONObject excelExportStatistics(HttpServletRequest request, HttpServletResponse response,
			AgentProfitQuery query) {
		JSONObject jsonObject = new JSONObject();

		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		query.setUserName(userDetails.getUsername());
		List<AgentProfitVo> list = agentProfitStatisticsService.queryListByQuery(query);

		if (list == null || list.isEmpty()) {
			LOGGER.info("导出数据为空！");
			jsonObject.put("result", "fail");
			return jsonObject;
		}

		try {
			ExcelUtil.listToExcel(list, getStatisticsFieldMap(), "代理商分润统计信息表", response, null);
			LOGGER.info("代理商分润统计信息导出成功！");
		} catch (ExcelException e) {
			LOGGER.error("代理商分润统计信息失败！", e);
		}
		return jsonObject;
	}

	private LinkedHashMap<String, String> getStatisticsFieldMap() {
		LinkedHashMap<String, String> fieldMap = new LinkedHashMap<String, String>();
		fieldMap.put("settlementTime", "清算日期");
		fieldMap.put("agentId", "代理商编号");
		fieldMap.put("parentAgentId", "父代理商编号");
		fieldMap.put("transCount", "交易笔数");
		fieldMap.put("transAmt", "交易金额（元）");
		fieldMap.put("profitTotal", "总利润（元）");
		fieldMap.put("profit", "本级利润（元）");
		fieldMap.put("profitOut", "本级已发利润（元）");
		fieldMap.put("profitSubOut", "下级已发利润（元）");
		return fieldMap;
	}

	private LinkedHashMap<String, String> getDetailFieldMap() {
		LinkedHashMap<String, String> fieldMap = new LinkedHashMap<String, String>();
		fieldMap.put("settlementTime", "清算日期");
		fieldMap.put("agentId", "代理商编号");
		fieldMap.put("mercNo", "商户号");
		fieldMap.put("transCardNo", "卡号");
		fieldMap.put("hostTransSsn", "流水号");
		fieldMap.put("cardKind", "卡类型");
		fieldMap.put("profitTotal", "总利润（元）");
		fieldMap.put("profit", "本级利润（元）");
		fieldMap.put("transTypeName", "交易类型");
		return fieldMap;
	}
}
