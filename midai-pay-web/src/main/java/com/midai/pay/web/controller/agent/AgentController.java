package com.midai.pay.web.controller.agent;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.midai.framework.common.ExcelException;
import com.midai.framework.common.ExcelUtil;
import com.midai.framework.query.PageVo;
import com.midai.pay.agent.query.AgentQuery;
import com.midai.pay.agent.service.AgentService;
import com.midai.pay.agent.vo.AgentAllVo;
import com.midai.pay.agent.vo.AgentImgVo;
import com.midai.pay.agent.vo.AgentMerchantRateVo;
import com.midai.pay.agent.vo.AgentNoAndNameVo;
import com.midai.pay.agent.vo.AgentUpdateVo;
import com.midai.pay.agent.vo.AgentVo;
import com.midai.pay.common.po.ReturnVal;
import com.midai.pay.user.entity.SystemUser;
import com.midai.pay.user.service.SystemUserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;

@Api("代理商")
@RestController
@RequestMapping("/agent")
public class AgentController {
	
	@Reference
	private AgentService agentService;
	
	@Value("${system.user.password}")
    private String password;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AgentController.class);
	
	@Reference
	private SystemUserService systemUserService;
	
	
	@ApiOperation("代理商列表")
    @PostMapping("/queryList")
	public PageVo<AgentVo> queryList(@RequestBody AgentQuery query) {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication() .getPrincipal();
		String inscode = systemUserService.getInscode(userDetails.getUsername());
		query.setInscode(inscode);
		
		PageVo<AgentVo> pageVo = new PageVo<AgentVo>();
		pageVo.setRows(agentService.queryList(query));
		pageVo.setTotal(agentService.queryCount(query));
		return pageVo;
	}
	
	@ApiOperation("代理商编号、代理商名称列表")
    @PostMapping("/queryNoAndNameList")
	public PageVo<AgentNoAndNameVo> queryNoAndNameList(@RequestBody AgentQuery query) {
		
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication() .getPrincipal();
		String inscode = systemUserService.getInscode(userDetails.getUsername());
		if (!StringUtils.isEmpty(inscode)) {
			SystemUser user = systemUserService.loadByUserLoginname(userDetails.getUsername());
			query.setMobile(user.getMobile());
		}
		
		PageVo<AgentNoAndNameVo> pageVo = new PageVo<AgentNoAndNameVo>();
		pageVo.setRows(agentService.queryNoAndNameList(query));
		pageVo.setTotal(agentService.queryNoAndNameCount(query));
		return pageVo;
	}
	
	@ApiOperation("目标代理商编号、代理商名称列表")
    @PostMapping("/queryNoAndNameTargetList")
	public PageVo<AgentNoAndNameVo> queryNoAndNameTargetList(@RequestBody AgentQuery query) {
		
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication() .getPrincipal();
		String inscode = systemUserService.getInscode(userDetails.getUsername());
		if (!StringUtils.isEmpty(inscode)) {
			SystemUser user = systemUserService.loadByUserLoginname(userDetails.getUsername());
			query.setMobile(user.getMobile());
			List<AgentNoAndNameVo> sourceAgents = agentService.queryNoAndNameList(query);
			AgentNoAndNameVo av = sourceAgents.get(0);
			query.setMobile("");
			query.setAgentNor(av.getAgentNo());
		}
		
		PageVo<AgentNoAndNameVo> pageVo = new PageVo<AgentNoAndNameVo>();
		query.setLimit(10000);
		Map<String, Object> map = agentService.queryChildAgentList(query);
		pageVo.setTotal((Integer)map.get("total"));
		pageVo.setRows((List<AgentNoAndNameVo>)map.get("rows"));
		return pageVo;
	}
	
	@ApiOperation("代理商编号、代理商名称、顶级代理商编号、顶级代理商名称列表")
    @PostMapping("/queryNoAndNameTopList")
	public PageVo<AgentNoAndNameVo> queryNoAndNameTopList(@RequestBody AgentQuery query) {
		
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication() .getPrincipal();
		String inscode = systemUserService.getInscode(userDetails.getUsername());
		query.setInscode(inscode);
		
		PageVo<AgentNoAndNameVo> pageVo = new PageVo<AgentNoAndNameVo>();
		pageVo.setRows(agentService.queryNoAndNameTopList(query));
		pageVo.setTotal(agentService.queryNoAndNameTopCount(query));
		return pageVo;
	}
	
	@ApiOperation("代理商新增页面")
    @GetMapping("/getInsertInfo")
	public AgentVo getInsertInfo() {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication() .getPrincipal();
		return agentService.getInsertInfo(userDetails.getUsername());
	}
	
	@ApiOperation("代理商新增")
    @PostMapping("/insertAgent")
	public JSONObject insert(@Valid @RequestBody AgentAllVo agentAllVo) {
		JSONObject jsonObject = new JSONObject();
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication() .getPrincipal();
		Md5PasswordEncoder md5=new Md5PasswordEncoder();
		AgentVo agentVo = agentAllVo.getAgentVo();
		List<AgentImgVo> imgVo = agentAllVo.getImgVo();
		
		ReturnVal<String> resultMsg = agentService.insertAgent(agentVo, userDetails.getUsername(),
					md5.encodePassword(agentVo.getMobile().substring(5), null), imgVo);
    	if(resultMsg.isSuccess()) {
    		jsonObject.put("result","success");
    	} else {
    		jsonObject.put("result","fail");
    	}
    	return jsonObject;
	}
	
	@ApiOperation("代理商更新页面")
    @GetMapping("/getUpdateInfo")
	public AgentAllVo getUpdateInfo(@RequestParam Integer id) {
		return agentService.getUpdatInfo(id);
	}
	
	@ApiOperation("代理商更新")
    @PostMapping("/updateAgent")
	public JSONObject updateBatchMerRate(@Valid @RequestBody AgentUpdateVo vo) {
		JSONObject jsonObject = new JSONObject();
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication() .getPrincipal();
		ReturnVal<String> resultMsg = agentService.updateAgent(vo, userDetails.getUsername());
		if(resultMsg.isSuccess()) {
    		jsonObject.put("result","success");
    	} else {
    		jsonObject.put("result","fail");
    	}
    	return jsonObject;
	}
	
	@ApiOperation("商户签约扣率列表")
    @PostMapping("/queryAgtMerRateList")
	public PageVo<AgentMerchantRateVo> queryAgtMerRateList(@RequestBody AgentQuery query) {
		PageVo<AgentMerchantRateVo> pageVo = new PageVo<AgentMerchantRateVo>();
		pageVo.setRows(agentService.queryAgtMerRateList(query));
		pageVo.setTotal(agentService.queryAgtMerRateCount(query));
		return pageVo;
	}
	
	@ApiOperation("批量修改代理商扣率")
    @PostMapping("/updateBatchAgentRate")
	public JSONObject updateBatchAgentRate(@Valid @RequestBody List<AgentVo> agentVoList, BindingResult result) {
		JSONObject jsonObject = new JSONObject();
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication() .getPrincipal();
		ReturnVal<String> resultMsg = agentService.updateBatchAgentRate(agentVoList, userDetails.getUsername());
		if(resultMsg.isSuccess()) {
    		jsonObject.put("result","success");
    	} else {
    		jsonObject.put("result","fail");
    	}
    	return jsonObject;
	}
	
	@ApiOperation("修改扣率")
    @PostMapping("/updateRate")
	public JSONObject updateBatchAgentRate(@RequestBody AgentVo agentVo) {
		JSONObject jsonObject = new JSONObject();
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication() .getPrincipal();
		agentVo.setUpdateUser(userDetails.getUsername());
		ReturnVal<String> resultMsg = agentService.updateRate(agentVo);
		if(resultMsg.isSuccess()) {
    		jsonObject.put("result","success");
    	} else {
    		jsonObject.put("result","fail");
    	}
    	return jsonObject;
	}
	
	/**
	 * 代理商列表数据导出（无复选框条件）
	 * @param request
	 * @param response
	 * @param query
	 */
	@ApiOperation("代理商列表数据导出（无复选框条件）")
	@GetMapping("/excelExport")
	public JSONObject excelExport(HttpServletRequest request, HttpServletResponse response, AgentQuery query) {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication() .getPrincipal();
		String inscode = systemUserService.getInscode(userDetails.getUsername());
		query.setInscode(inscode);
		
		query.setLoginName(userDetails.getUsername());
		
		JSONObject jsonObject = new JSONObject();
		Integer count = agentService.queryCount(query);
		if(count <= 0) {
			LOGGER.info("导出数据为空！");
			jsonObject.put("result","fail");
			return jsonObject;
		}
		List<AgentVo> list = agentService.excelExport(query);
		if(list == null || list.isEmpty()) {
			LOGGER.info("导出数据为空！");
			jsonObject.put("result","fail");
			return jsonObject;
		}
		try {
			ExcelUtil.listToExcel(list, getFieldMap(), "代理商表", response, null);
			LOGGER.info("代理商数据导出成功！");
		} catch (ExcelException e) {
			LOGGER.info("代理商数据导出失败！");
			e.printStackTrace();
		}
		return jsonObject;
	}
	
	/**
	 * 代理商列表数据导出（有复选框条件）
	 * @param request
	 * @param response
	 * @param query
	 */
	@ApiOperation("代理商列表数据导出（有复选框条件）")
	@GetMapping("/excelExportChecked")
	public JSONObject excelExportChecked(HttpServletRequest request, HttpServletResponse response, Integer[] ids) {
		JSONObject jsonObject = new JSONObject();
		if(ids == null || ids.length <= 0) {
			LOGGER.info("导出数据为空！");
			jsonObject.put("result","fail");
			return jsonObject;
		}
		List<AgentVo> list = agentService.excelExportChecked(ids);
		if(list == null || list.isEmpty()) {
			LOGGER.info("导出数据为空！");
			jsonObject.put("result","fail");
			return jsonObject;
		}
		try {
			ExcelUtil.listToExcel(list, getFieldMap(), "代理商表", response, null);
			LOGGER.info("代理商数据导出成功！");
		} catch (ExcelException e) {
			LOGGER.info("代理商数据导出失败！");
			e.printStackTrace();
		}
		return jsonObject;
	}
	
	/**
	 * 中英文字段名Map
	 * @return
	 */
	private LinkedHashMap<String, String> getFieldMap() {
		LinkedHashMap<String, String> fieldMap = new LinkedHashMap<String, String>();
		fieldMap.put("agentNo", "代理商编号");
		fieldMap.put("name", "代理商名称");
		fieldMap.put("parentAgentId", "父代理商编号");
		fieldMap.put("parentAgentName", "父代理商名称");
//		fieldMap.put("costRate", "成本扣率（%）");
//		fieldMap.put("merchantRate", "商户扣率（%）");
		fieldMap.put("custCount", "商户数量");
		fieldMap.put("createTime", "创建时间");
		return fieldMap;
	}
	
	@ApiOperation("代理商用户登录显示列表")
    @PostMapping("/agentList")
	public PageVo<AgentVo> agentList(@RequestBody AgentQuery query) {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication() .getPrincipal();
		String allAgents = systemUserService.getAllChildAgents(userDetails.getUsername());	
		query.setAllAgents(allAgents);
		query.setLoginName(userDetails.getUsername());
		PageVo<AgentVo> pageVo = new PageVo<AgentVo>();
		pageVo.setRows(agentService.queryList(query));
		pageVo.setTotal(agentService.queryCount(query));
		return pageVo;
	}
	
	@ApiOperation("代理商用户登录代理商列表数据导出（无复选框条件）")
	@GetMapping("/agentListExcelExport")
	public JSONObject agentListExcelExport(HttpServletRequest request, HttpServletResponse response, AgentQuery query) {
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication() .getPrincipal();
		String allAgents = systemUserService.getAllChildAgents(userDetails.getUsername());	
		query.setAllAgents(allAgents);
		query.setLoginName(userDetails.getUsername());
		
		JSONObject jsonObject = new JSONObject();
		Integer count = agentService.queryCount(query);
		if(count <= 0) {
			LOGGER.info("导出数据为空！");
			jsonObject.put("result","fail");
			return jsonObject;
		}
		List<AgentVo> list = agentService.excelExport(query);
		if(list == null || list.isEmpty()) {
			LOGGER.info("导出数据为空！");
			jsonObject.put("result","fail");
			return jsonObject;
		}
		try {
			ExcelUtil.listToExcel(list, getFieldMap(), "代理商表", response, null);
			LOGGER.info("代理商数据导出成功！");
		} catch (ExcelException e) {
			LOGGER.info("代理商数据导出失败！");
			e.printStackTrace();
		}
		return jsonObject;
	}
	
	@ApiOperation("获取代理商信息")
    @GetMapping("/getAgentInfo")
	public AgentVo getAgentInfo(@RequestParam String agentNo) {
		return agentService.getAgentInfo(agentNo);
	}
}
