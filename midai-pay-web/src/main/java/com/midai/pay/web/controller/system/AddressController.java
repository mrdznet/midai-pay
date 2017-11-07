package com.midai.pay.web.controller.system;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.midai.pay.user.entity.SystemArea;
import com.midai.pay.user.entity.SystemChina;
import com.midai.pay.user.entity.SystemCity;
import com.midai.pay.user.entity.SystemProvince;
import com.midai.pay.user.service.SystemAreaService;
import com.midai.pay.user.service.SystemChinaService;
import com.midai.pay.user.service.SystemCityService;
import com.midai.pay.user.service.SystemProvinceService;
import com.midai.pay.user.vo.SystemAddressVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api("地址列表")
@RestController
@RequestMapping("/system/address")
public class AddressController {


	@Reference
	private SystemProvinceService systemProvinceService;
	@Reference
	private SystemCityService systemCityService;
	@Reference
	private SystemAreaService systemAreaService;
	
	@Reference
	private SystemChinaService systemChinaService;
	
	@ApiOperation(value="省列表")
	@PostMapping("/loadprovince")
	public List<SystemAddressVo>  loadProvince(){
		List<SystemProvince> plist = systemProvinceService.findAll();
		List<SystemAddressVo> sal = new ArrayList<SystemAddressVo>();
		if(plist!=null && plist.size()>0) {
			for(SystemProvince p : plist) {
				SystemAddressVo sav = new SystemAddressVo();
				sav.setCode(p.getCode());
				sav.setName(p.getName());
				sal.add(sav);
			}
		}
		 return sal;
	}

	@ApiOperation(value="市列表")
	@PostMapping("/loadcity/{pcode}/{pflag}")
	public  List<SystemAddressVo> getCityByProvinceCode(@PathVariable("pcode") @ApiParam("省编码") String pcode, @PathVariable("pflag") @ApiParam("city 查询市，area 查询区")  String pflag) {
		
		List<SystemAddressVo> sal = new ArrayList<SystemAddressVo>();
		
		if(StringUtils.isNotEmpty(pcode)) {
			if(pflag.equals("area")) {
				List<SystemArea> areas = systemAreaService.findByProvinceCode(pcode);
				if(areas!=null && areas.size()>0) {
					for(SystemArea a : areas) {
						SystemAddressVo av = new SystemAddressVo();
						av.setCode(a.getCode());
						av.setName(a.getName());
						sal.add(av);
					}
				}
			}else if(pflag.equals("city")){
				List<SystemCity> citys = systemCityService.findByProvinceCode(pcode);
				if(citys!=null && citys.size()>0) {
					for(SystemCity c : citys) {
						SystemAddressVo sav = new SystemAddressVo();
						sav.setCode(c.getCode());
						sav.setName(c.getName());
						sal.add(sav);
					}
				}
			}
		} 
		
		return sal;
	}
//	--------------------------------------------------------------------------------------------------------
	
	@ApiOperation(value="加载省市区")
	@PostMapping("/loadAllData/{type}/{code}")
	public List<SystemAddressVo>  loadAllData(@PathVariable("type") @ApiParam("省 0, 市 1, 县2") String type, @PathVariable("code") @ApiParam("编码, 加载省列表时传0值") String code){
		List<SystemChina> plist = systemChinaService.loadAllData(code, type);
		List<SystemAddressVo> sal = new ArrayList<SystemAddressVo>();
		if(plist!=null && plist.size()>0) {
			for(SystemChina p : plist) {
				SystemAddressVo sav = new SystemAddressVo();
				sav.setCode(p.getCode());
				sav.setName(p.getName());
				sal.add(sav);
			}
		}
		 return sal;
	}

}
