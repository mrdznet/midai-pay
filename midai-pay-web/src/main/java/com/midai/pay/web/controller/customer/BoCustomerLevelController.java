package com.midai.pay.web.controller.customer;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.midai.framework.query.PageVo;
import com.midai.pay.common.po.ReturnVal;
import com.midai.pay.customer.query.BoCustomerLevelModeQuery;
import com.midai.pay.customer.service.BoCustomerLevelService;
import com.midai.pay.customer.vo.BoCustomerLevelVo;
import com.midai.pay.customer.vo.CustomerLevelSimpleVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.sf.json.JSONObject;

@Api("商户等级管理")
@RestController
@RequestMapping("/customer/level/")
public class BoCustomerLevelController {
	@Reference
	private BoCustomerLevelService boCustomerLevelService;
	
	
	@ApiOperation("列表查询")
    @PostMapping("query")
	public PageVo<BoCustomerLevelVo> queryList(@RequestBody BoCustomerLevelModeQuery query) {
		PageVo<BoCustomerLevelVo> pageVo = new PageVo<BoCustomerLevelVo>();
		pageVo.setRows(boCustomerLevelService.queryList(query));
		pageVo.setTotal(boCustomerLevelService.queryCount());
		
		return pageVo;
	}
	
	@ApiOperation("新增")
    @PostMapping("add")
	public JSONObject insert(@RequestBody BoCustomerLevelVo vo) {
		JSONObject jsonObject = new JSONObject();
		ReturnVal<String> resultMsg = boCustomerLevelService.insert(vo);
    	if(resultMsg.isSuccess()) {
    		jsonObject.put("result","success");
    	} else {
    		jsonObject.put("result","fail");
    		jsonObject.put("errorMsg", resultMsg.getMsg());
    	}
    	
    	return jsonObject;
	}
	
	@ApiOperation("修改-加载页面")
    @PostMapping("updateLoad/{level}")
	public BoCustomerLevelVo updateLoad(@PathVariable("level") @ApiParam("等级") String level){
		if(StringUtils.isEmpty(level)){
			throw new RuntimeException("请传入等级");
		}
		
    	return boCustomerLevelService.load(level);
	}
	
	@ApiOperation("修改-提交")
    @PostMapping("UpdateAdd")
	public JSONObject UpdateAdd(@RequestBody BoCustomerLevelVo vo) {
		JSONObject jsonObject = new JSONObject();
		
		int i = boCustomerLevelService.update(vo);
    	if(i > 0) {
    		jsonObject.put("result","success");
    	} else {
    		jsonObject.put("result","fail");
    	}
    	
    	return jsonObject;
	}
	
	@ApiOperation("删除")
    @PostMapping("delete/{level}")
	public JSONObject delete(@PathVariable("level") @ApiParam("等级，多个以逗号(,)分隔") String level){
		if(StringUtils.isEmpty(level)){
			throw new RuntimeException("请传入等级");
		}
		JSONObject jsonObject = new JSONObject();
		
		int i = boCustomerLevelService.delete(level);
    	if(i > 0) {
    		jsonObject.put("result","success");
    	} else {
    		jsonObject.put("result","fail");
    	}
    	
		return jsonObject;
	}
	
	@ApiOperation("获取全部level")
	@GetMapping("all")
	public List<CustomerLevelSimpleVo> all(){
		return boCustomerLevelService.getAll();
	}
}
