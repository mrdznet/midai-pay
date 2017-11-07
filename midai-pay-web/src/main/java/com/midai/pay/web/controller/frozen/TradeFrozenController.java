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
import com.midai.pay.frozen.query.TradeFrozenQuery;
import com.midai.pay.frozen.service.TradeFrozenService;
import com.midai.pay.frozen.vo.TradeFrozenVo;
import com.midai.pay.user.service.SystemUserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("交易冻结管理")
@RestController
@RequestMapping("/tradefrozenreason")
public class TradeFrozenController {
	private static final Logger LOGGER= LoggerFactory.getLogger(TradeFrozenController.class);
	
	@Reference 
	private TradeFrozenService miIplaTradefrozenreasonService;
	
	@Reference 
	private SystemUserService systemUserService;
	
	@ApiOperation("交易冻结管理查询")
	@PostMapping("/list")
	public PageVo<TradeFrozenVo> findQueryMiIplaTradefrozenreason(@RequestBody TradeFrozenQuery query){
		
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication() .getPrincipal();
		String inscode = systemUserService.getInscode(userDetails.getUsername());
		query.setInscode(inscode);
		
		PageVo<TradeFrozenVo> pageVo=new PageVo<>();
		pageVo.setRows(miIplaTradefrozenreasonService.findQueryMiIplaTradefrozenreason(query));
		pageVo.setTotal(miIplaTradefrozenreasonService.findQueryMiIplaTradefrozenreasonCount(query));
		return pageVo;
	}
	
	@ApiOperation("交易冻结管理excel导出总记录数")
	@PostMapping("/miIplaTradefrozenreasonCount")
	public int miIplaTradefrozenreasonCount(@RequestBody TradeFrozenQuery query){
		int result=miIplaTradefrozenreasonService.ExcelDownMiIplaTradefrozenreasonCount(query);
		if(result >0){
			LOGGER.info("导出交易冻结表有记录数");
		}
		else{
			LOGGER.info("导出交易冻结表数据为空");
		}
		
		return result;
	}
	
	@ApiOperation("交易冻结管理excel导出")
	@GetMapping("/excelExport")
	public void miIplaTradefrozenreasonExcelExport(HttpServletRequest request ,HttpServletResponse response,TradeFrozenQuery query){
		
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication() .getPrincipal();
		String inscode = systemUserService.getInscode(userDetails.getUsername());
		query.setInscode(inscode);
		
		List<TradeFrozenVo> list=miIplaTradefrozenreasonService.ExcelDownMiIplaTradefrozenreason(query);
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
	
	@ApiOperation("交易冻结管理excel导出有复选框")
	@GetMapping("/excelExport/selected")
    public void miIplaTradefrozenreasonSelectExcelExport(HttpServletRequest request,HttpServletResponse response,@RequestParam("ids")List<String>ids ){
		if(ids==null || ids.size()<=0){
			throw new RuntimeException("交易冻结管理excel导出非法:值为:"+ids);
		}
		
		List<TradeFrozenVo> list=miIplaTradefrozenreasonService.ExcelDownSelectMiIplaTradefrozenreason(ids);
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
	
	
	@ApiOperation("交易批量冻结")
	@PostMapping("/updateTradefrozen")
	public  int  upadateTradefrozen(@Valid @RequestBody String[] lognos){
		if(lognos==null || lognos.length==0){
			throw  new RuntimeException("无选中冻结数据");
		}
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication() .getPrincipal();
    	return miIplaTradefrozenreasonService.updatetradeFrozen(lognos, userDetails.getUsername());
	    
	}

	
	
	@ApiOperation("交易批量解冻")
	@PostMapping("/updateTradeunfrozen")
	public int updateTradeunfrozen(@Valid @RequestBody Integer[] ids ){
		if(ids==null || ids.length==0){
			throw new RuntimeException("无选中解冻数据");
		}
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication() .getPrincipal();
		return miIplaTradefrozenreasonService.updatetradeunFrozen(ids, userDetails.getUsername());
		
		
		
	}

	
	
	private LinkedHashMap<String, String>getFieldMap(){
		LinkedHashMap<String, String> FieldMap=new LinkedHashMap<String,String>();
		
		FieldMap.put("hostTransssn","流水号");
		FieldMap.put("mercId", "商户小票号");
		FieldMap.put("mercName", "商户名称");
		FieldMap.put("transAmt", "交易金额");
		FieldMap.put("transTime", "交易时间");
		FieldMap.put("transCardNo", "交易卡号");
		FieldMap.put("frozenTime", "冻结时间 ");
		FieldMap.put("frozenReason", "冻结原因");
		FieldMap.put("frozenPerson", "冻结操作人");
		FieldMap.put("unfrozenPerson", "解结操作人");
		FieldMap.put("unfrozenTime", "解冻日期");
		return FieldMap;
	}
     	
}
