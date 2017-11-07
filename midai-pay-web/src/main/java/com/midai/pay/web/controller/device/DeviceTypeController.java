package com.midai.pay.web.controller.device;

import java.util.List;

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
import com.midai.pay.device.query.BoDeviceTypeQuery;
import com.midai.pay.device.service.BoDeviceTypeService;
import com.midai.pay.device.vo.DeviceTypeVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;

@Api("设备类型管理")
@RestController
@RequestMapping("/deviceType")
public class DeviceTypeController {
	
	@Reference
	private BoDeviceTypeService boDeviceTypeService;
	
	@ApiOperation("设备类型列表")
    @PostMapping("/queryList")
	public PageVo<DeviceTypeVo> queryList(@RequestBody BoDeviceTypeQuery query) {
		PageVo<DeviceTypeVo> pageVo = new PageVo<DeviceTypeVo>();
		pageVo.setRows(boDeviceTypeService.queryList(query));
		pageVo.setTotal(boDeviceTypeService.queryCount());
		return pageVo;
	}
	
	@ApiOperation("设备类型新增页面")
    @GetMapping("/getInsertInfo")
	public DeviceTypeVo getInsertInfo() {
		return boDeviceTypeService.getInsertInfo();
	}
	
	@ApiOperation("设备类型新增")
    @PostMapping("/insertDeviceType")
	public JSONObject insert(@Valid @RequestBody DeviceTypeVo deviceTypeVo, BindingResult result) {
		JSONObject jsonObject = new JSONObject();
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication() .getPrincipal();
		ReturnVal<String> resultMsg = boDeviceTypeService.insertDeviceType(deviceTypeVo, userDetails.getUsername());
    	if(resultMsg.isSuccess()) {
    		jsonObject.put("result","success");
    	} else {
    		jsonObject.put("result","fail");
    	}
    	return jsonObject;
	}
	
	@ApiOperation("删除设备类型")
    @PostMapping("/deleteDeviceType")
	public JSONObject delete(@Valid @RequestBody Integer[] ids, BindingResult result) {
		JSONObject jsonObject = new JSONObject();
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication() .getPrincipal();
		ReturnVal<String> resultMsg = boDeviceTypeService.deleteDeviceType(ids, userDetails.getUsername());
    	if(resultMsg.isSuccess()) {
    		jsonObject.put("result","success");
    	} else {
    		jsonObject.put("result","fail");
    	}
    	return jsonObject;
	}
	
	@ApiOperation("加载所有终端类型")
    @GetMapping("/allDeviceType")
	public List<DeviceTypeVo> allDeviceType(){
		
		return boDeviceTypeService.allDeviceType();
	}
}
