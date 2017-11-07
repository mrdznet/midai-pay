package com.midai.pay.web.controller.device;

import javax.validation.Valid;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.midai.framework.query.PageVo;
import com.midai.pay.common.po.ReturnVal;
import com.midai.pay.device.query.BoDeviceModeQuery;
import com.midai.pay.device.service.BoDeviceModeService;
import com.midai.pay.device.vo.DeviceModeVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;

@Api("设备型号管理")
@RestController
@RequestMapping("/deviceMode")
public class DeviceModeController {
	
	@Reference
	private BoDeviceModeService boDeviceModeService;
	
	@ApiOperation("设备型号列表")
    @PostMapping("/queryList")
	public PageVo<DeviceModeVo> queryList(@RequestBody BoDeviceModeQuery query) {
		PageVo<DeviceModeVo> pageVo = new PageVo<DeviceModeVo>();
		pageVo.setRows(boDeviceModeService.queryList(query));
		pageVo.setTotal(boDeviceModeService.queryCount());
		return pageVo;
	}
	
	@ApiOperation("设备型号新增页面")
    @GetMapping("/getInsertInfo")
	public DeviceModeVo getInsertInfo() {
		return boDeviceModeService.getInsertInfo();
	}
	
	@ApiOperation("设备型号新增")
    @PostMapping("/insertDeviceType")
	public JSONObject insert(@Valid @RequestBody DeviceModeVo deviceModeVo, BindingResult result) {
		JSONObject jsonObject = new JSONObject();
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication() .getPrincipal();
		ReturnVal<String> resultMsg = boDeviceModeService.insertDeviceMode(deviceModeVo, userDetails.getUsername());
    	if(resultMsg.isSuccess()) {
    		jsonObject.put("result","success");
    	} else {
    		jsonObject.put("result","fail");
    	}
    	return jsonObject;
	}
	
	@ApiOperation("删除设备型号")
    @PostMapping("/deleteDeviceType")
	public JSONObject delete(@Valid @RequestBody Integer[] ids, BindingResult result) {
		JSONObject jsonObject = new JSONObject();
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication() .getPrincipal();
		ReturnVal<String> resultMsg = boDeviceModeService.deleteDeviceMode(ids, userDetails.getUsername());
    	if(resultMsg.isSuccess()) {
    		jsonObject.put("result","success");
    	} else {
    		jsonObject.put("result","fail");
    	}
    	return jsonObject;
	}

}
