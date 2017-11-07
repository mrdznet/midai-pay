package com.midai.pay.web.controller.device;

import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.midai.pay.device.query.BoDeviceInOutStorageQuery;
import com.midai.pay.device.service.BoDeviceInOutStorageService;
import com.midai.pay.device.vo.BoDeviceInOutStorageVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;



@Api("设备管理")
@RestController
@RequestMapping("/device/inout")
public class BoDeviceInOutStorageController {
	
	 private static final Logger LOGGER= LoggerFactory.getLogger(BoDeviceInOutStorageController.class);
  
	 
	  
	 @Reference
	 private BoDeviceInOutStorageService boDeviceInOutStorageService;
	
	 
    @ApiOperation("出入库明细查询")
    @PostMapping("/list")
    public PageVo<BoDeviceInOutStorageVo> List(@RequestBody BoDeviceInOutStorageQuery query){
   
    PageVo<BoDeviceInOutStorageVo> pagevo=new 	PageVo<BoDeviceInOutStorageVo>();
    pagevo.setRows(boDeviceInOutStorageService.findQueryBoDevice(query));
    pagevo.setTotal(boDeviceInOutStorageService.findQueryBoDeviceCount(query));
    return pagevo;   	
    }
    
    @ApiOperation("出入库明细excel导出总记录数")
    @PostMapping("/inoutstorageStatisticsCount")
    public int inoutstorageStatisticsCount(@RequestBody BoDeviceInOutStorageQuery query ){
    
    	int result=boDeviceInOutStorageService.ExcelDownloadBoDeviceCount(query);
    	if(result > 0){
    		LOGGER.info("导出的设备表有记录数");
		}else{
			LOGGER.info("导出设备表的数据为空");	
		}
    	return result;
    	
    }
    
    @ApiOperation("出入库明细excel导出")
    @GetMapping("/excelExport")
    public void inoutstorageStatisticsExcelExport(HttpServletRequest request ,HttpServletResponse response ,BoDeviceInOutStorageQuery query){
    	
    	List<BoDeviceInOutStorageVo>list=boDeviceInOutStorageService.ExcelDownBoDevice(query);
    	if (list==null || list.isEmpty()) {
    		throw new RuntimeException("设备表为空");
    	}
    	   try {
    			ExcelUtil.listToExcel(list, getFieldMap(), "设备表", response, null);
    			LOGGER.info("导出设备表成功");
    		} catch (ExcelException e) {
    		   LOGGER.info("导出设备表失败");
    			e.printStackTrace();
    		}
    	
    }
    
    
    @ApiOperation("出入库明细excel导出有复选框条件")
    @GetMapping("/excelExport/selected")
    public void inoutstorageStatisticsSelectExcelExport(HttpServletResponse response ,HttpServletRequest request,@RequestParam("deviceNos")List<String> deviceNos){
    	if(deviceNos==null || deviceNos.size()<=0){
    		throw new RuntimeException("出入库明细excel导出查询参数非法:值为:"+deviceNos);
    	}
    	
    	List<BoDeviceInOutStorageVo> list=boDeviceInOutStorageService.ExcelDownSelectBoDevice(deviceNos);
    	if(list==null || list.isEmpty()){
    		throw new  RuntimeException("设备表为空");
    	}
    	try {
			ExcelUtil.listToExcel(list, getFieldMap(), "设备表", response, null);
			LOGGER.info("导出设备表成功");
		} catch (ExcelException e) {
		   LOGGER.info("导出设备表失败");
			e.printStackTrace();
		}
    }
    
     
    private LinkedHashMap<String, String>getFieldMap(){
    	
    	LinkedHashMap<String, String>fieldMap=new LinkedHashMap<String, String>();   	
    	fieldMap.put("deviceNo","编号机身号");
    	fieldMap.put("agentName", "源代理商名称");
    	fieldMap.put("destagentName", "目标代理商名称");
    	fieldMap.put("createTime","创建时间");
    	fieldMap.put("appUser", "操作员");
    	fieldMap.put("operateState", "状态");
    	return fieldMap;
    }

}
