package com.midai.pay.web.controller.frozen;

import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.midai.framework.common.ExcelUtil;
import com.midai.framework.query.PageVo;
import com.midai.pay.frozen.query.CustomerFrozenQuery;
import com.midai.pay.frozen.service.CustomerFrozenService;
import com.midai.pay.frozen.vo.CustomerFrozenVo;
import com.midai.pay.user.service.SystemUserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("商户冻结管理")
@RestController
@RequestMapping("/frozenreason")
public class CustomerFrozenController {
	private static final Logger LOGGER=LoggerFactory.getLogger(CustomerFrozenController.class);

	@Reference
	private CustomerFrozenService miIplaFrozenreasonService;
	
	@Reference
	private SystemUserService systemUserService;
	
	@ApiOperation("商户冻结管理查询")
	@PostMapping("/list")
	public PageVo<CustomerFrozenVo> find(@RequestBody CustomerFrozenQuery query){
		
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication() .getPrincipal();
		String inscode = systemUserService.getInscode(userDetails.getUsername());
		query.setInscode(inscode);	
		
	PageVo<CustomerFrozenVo> pageVo=new PageVo<>();
	pageVo.setRows(miIplaFrozenreasonService.findQueryMiIplafrozenreason(query));
	pageVo.setTotal(miIplaFrozenreasonService.findQueryMiIplafrozenreasonCount(query));
	return pageVo;	
	}
	@ApiOperation("商户冻结管理excel导出总记录数")
	@PostMapping("/miIplafrozenreasonCount")
	public int miIplafrozenreasonCount(@RequestBody CustomerFrozenQuery query){
		
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication() .getPrincipal();
		String inscode = systemUserService.getInscode(userDetails.getUsername());
		query.setInscode(inscode);	
		
		int result=miIplaFrozenreasonService.ExcelDownMiIplafrozenreasonCount(query);
		if(result >0){
			LOGGER.info("导出商户冻结表有记录数");
		}
		else{
			LOGGER.info("导出商户冻结表数据为空");
		}
		
		return result;
	}
	
	@ApiOperation("商户冻结管理excel导出")
	@GetMapping("/excelExport")
	public void miIplaFrozenreasonExcelExport(HttpServletRequest request ,HttpServletResponse response,CustomerFrozenQuery query){
		
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication() .getPrincipal();
		String inscode = systemUserService.getInscode(userDetails.getUsername());
		query.setInscode(inscode);	
		
		List<CustomerFrozenVo> list=miIplaFrozenreasonService.ExcelDownMiIplafrozenreason(query);
		if(list==null || list.isEmpty()){
			throw  new RuntimeException("导出商户冻结表为空");
		}
		try{
			ExcelUtil.listToExcel(list, getFieldMap(), "导出商户冻结表" ,response,null);
			LOGGER.info("导出商户冻结表成功");
		}catch(Exception e){
			LOGGER.info("导出商户冻结表失败");
			e.printStackTrace();
		}
		
	}
	
	@ApiOperation("交易冻结管理excel导出有复选框")
	@GetMapping("/excelExport/selected")
    public void miIplaFrozenreasonSelectExcelExport(HttpServletRequest request,HttpServletResponse response,@RequestParam("ids")List<String>ids ){
		if(ids==null || ids.size()<=0){
			throw new RuntimeException("交易冻结管理excel导出非法:值为:"+ids);
		}
		
		List<CustomerFrozenVo> list=miIplaFrozenreasonService.ExcelDownSelectMiIplafrozenreason(ids);
		if(list==null || list.isEmpty()){
			throw  new RuntimeException("导出交易冻结表为空");
		}
		try{
			ExcelUtil.listToExcel(list, getFieldMap(), "导出交易冻结表" ,response,null);
			LOGGER.info("导出交易冻结表成功");
		}catch(Exception e){
			LOGGER.info("导出交易冻结表失败");
			e.printStackTrace();
		}
		
	} 
	
	@ApiOperation("商户冻结和新增冻结商户")
	@PostMapping("/customerfrozen")
	public int customerfrozen(@Valid @RequestParam String frozenReason, @RequestBody String[] mercIds){
		
		if(mercIds==null || mercIds.length==0){
			throw  new RuntimeException("无选中冻结数据");
		}
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication() .getPrincipal();
    	return miIplaFrozenreasonService.customerfrozen(frozenReason,mercIds, userDetails.getUsername());
	}
	
	@ApiOperation("商户解冻")
	@PostMapping("/customerunfrozen")
	public int customerunfrozen(@Valid @RequestBody String[] mercIds){
		if(mercIds==null || mercIds.length==0){
			throw new RuntimeException("无选中解冻数据");
		}
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication() .getPrincipal();
		return miIplaFrozenreasonService.customerunfrozen(mercIds, userDetails.getUsername());
	}
	
	private LinkedHashMap<String, String>getFieldMap(){
		LinkedHashMap<String, String> FieldMap=new LinkedHashMap<String,String>();
		FieldMap.put("mercId", "商户小票号");
		FieldMap.put("mercName", "商户名称");
		FieldMap.put("frozenTime", "冻结时间 ");
		FieldMap.put("unfrozenTime", "解冻日期");
		FieldMap.put("frozenReason", "冻结原因");
		FieldMap.put("frozenPerson", "冻结操作人");
		FieldMap.put("unfrozenPerson", "解结操作人");
		
		return FieldMap;
	}
	
}
