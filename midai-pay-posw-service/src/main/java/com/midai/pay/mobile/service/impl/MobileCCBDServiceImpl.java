package com.midai.pay.mobile.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.midai.pay.mobile.AppBaseEntity;
import com.midai.pay.mobile.entity.MobileListsEntity;
import com.midai.pay.mobile.mapper.MobileCCBDMapper;
import com.midai.pay.mobile.service.MobileCCBDService;
import com.midai.pay.mobile.vo.DeviceModeTypeFactoryVo;
import com.midai.pay.user.entity.Bank;
import com.midai.pay.user.entity.SystemArea;
import com.midai.pay.user.entity.SystemChina;
import com.midai.pay.user.entity.SystemCity;
import com.midai.pay.user.entity.SystemMCC;
import com.midai.pay.user.entity.SystemProvince;
import com.midai.pay.user.service.BankService;
import com.midai.pay.user.service.SystemAreaService;
import com.midai.pay.user.service.SystemChinaService;
import com.midai.pay.user.service.SystemCityService;
import com.midai.pay.user.service.SystemMccService;
import com.midai.pay.user.service.SystemProvinceService;



/**   
*    
* 项目名称：midai-pay-posw-service   
* 类名称：MobileCCBDServiceImpl   
* 类描述：   
* 创建人：wrt   
* 创建时间：2016年10月31日 下午4:35:07   
* 修改人：wrt   
* 修改时间：2016年10月31日 下午4:35:07   
* 修改备注：   
* @version    
*    
*/
@Service
public class MobileCCBDServiceImpl implements MobileCCBDService {

	private Logger logger = LoggerFactory.getLogger(MobileCCBDServiceImpl.class);

	@Autowired
	private MobileCCBDMapper mapper;
	@Reference
	private BankService bs;
	@Reference
	private SystemProvinceService sps;
	@Reference
	private SystemAreaService sas;
	@Reference
	private SystemCityService scs;
	@Reference
	private SystemChinaService china;
	@Reference
	private SystemMccService mcc;
	
	@Override
	public Object getDeviceModes(String content, String code) {
		String pcode = "";
		String pmsg = "";
		JSONObject json = null;
		boolean isErr = true;
		try {
			json = new JSONObject(content);
			isErr = false;
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.getMessage());
		}
		if (!isErr) {
			String inscode="101002";
			if(json.has("INSCODE")){
				  inscode=json.getString("INSCODE");
			}
			Map<String, String> param = new HashMap<String, String>();
			param.put("postalCode", inscode);
			param.put("code", code);
			List<DeviceModeTypeFactoryVo> dmfs = mapper.getDeviceModeTypeFactoryVoByPostalCode(param);
			List<Map<String, String>> lists  = new ArrayList<Map<String, String>>();
			for(DeviceModeTypeFactoryVo dtf : dmfs) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("DEVICEMODENAME",dtf.getDeviceModeName());
				map.put("DEVICETYPENAME",dtf.getDeviceTypeName());	
				map.put("PICTURE",dtf.getImagePath()==null?"":dtf.getImagePath());
				lists.add(map);
			}
			MobileListsEntity entry = new MobileListsEntity();
			entry.setLISTS(lists);
			entry.setRSPCOD("00");
			entry.setRSPMSG("查询成功！");
			return entry;
		}
		return new AppBaseEntity(pcode, pmsg);
	}
	@Override
	public Object getBigBanks() {
		
		List<Bank> banks = bs.findAll();
		List<Map<String, String>> lists = new ArrayList<Map<String, String>>();
		for(Bank b : banks) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("BANKCODE",b.getBankcode());
			map.put("BANKNAME",b.getBankname());
			map.put("SHOWBANKNAME",b.getShowname());
			lists.add(map);
		}
		
		MobileListsEntity entry = new MobileListsEntity();
		entry.setLISTS(lists);
		entry.setRSPCOD("00");
		entry.setRSPMSG("查询成功！");
		return entry;
	}
	@Override
	public Object getProvinces() {
		List<SystemChina> proList = china.loadAllData("0", "0");
		
		List<Map<String, String>> lists = new ArrayList<Map<String, String>>();
		Map<String, String> map;
		for(SystemChina sp : proList) {
			map = new HashMap<String, String>();
			map.put("PROVINCECODE",sp.getCode());
			map.put("PROVINCENAME",sp.getName());
			lists.add(map);
		}
		MobileListsEntity entry = new MobileListsEntity();
		entry.setLISTS(lists);
		entry.setRSPCOD("00");
		entry.setRSPMSG("查询成功！");
		return entry;
	}
	@Override
	public Object getCitys(String content) {
		String pcode = "";
		String pmsg = "";
		JSONObject json = null;
		boolean isErr = true;
		try {
			json = new JSONObject(content);
			isErr = false;
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.getMessage());
		}
		if (!isErr) {
			// 判断传人参数
			if (!json.has("PARCOD")) {
				pcode = "01";
				pmsg = "参数定义错误";
			} else {
				List<SystemChina> proList = china.loadAllData(json.getString("PARCOD"), "1");
				
				List<Map<String, String>> lists = new ArrayList<Map<String, String>>();
				Map<String, String> map;
				for(SystemChina sp : proList) {
					map = new HashMap<String, String>();
					map.put("CITYCODE",sp.getCode());
					map.put("CITYNAME",sp.getName());
					lists.add(map);
				}
				MobileListsEntity entry = new MobileListsEntity();
				entry.setLISTS(lists);
				entry.setRSPCOD("00");
				entry.setRSPMSG("查询成功！");
				return entry;
			}
		}
		return new AppBaseEntity(pcode, pmsg);
	}
	@Override
	public Object getAreas(String content) {
		String pcode = "";
		String pmsg = "";
		JSONObject json = null;
		boolean isErr = true;
		try {
			json = new JSONObject(content);
			isErr = false;
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.getMessage());
		}
		if (!isErr) {
			// 判断传人参数
			if (!json.has("PARCOD")) {
				pcode = "01";
				pmsg = "参数定义错误";
			} else {
				List<SystemChina> proList = china.loadAllData(json.getString("PARCOD"), "2");
				
				List<Map<String, String>> lists = new ArrayList<Map<String, String>>();
				Map<String, String> map;
				for(SystemChina sp : proList) {
					map = new HashMap<String, String>();
					map.put("AREACODE",sp.getCode());
					map.put("AREANAME",sp.getName());
					lists.add(map);
				}
				MobileListsEntity entry = new MobileListsEntity();
				entry.setLISTS(lists);
				entry.setRSPCOD("00");
				entry.setRSPMSG("查询成功！");
				return entry;
			}
		}
		return new AppBaseEntity(pcode, pmsg);
	}
	@Override
	public Object getIndustry() {
		List<SystemMCC> proList = mcc.loadData("0");
		
		List<Map<String, String>> lists = new ArrayList<Map<String, String>>();
		Map<String, String> map;
		for(SystemMCC sp : proList) {
			map = new HashMap<String, String>();
			
			map.put("PARID", ""+sp.getId());
			map.put("PARNAME", sp.getName());
			lists.add(map);
		}
		MobileListsEntity entry = new MobileListsEntity();
		entry.setLISTS(lists);
		entry.setRSPCOD("00");
		entry.setRSPMSG("查询成功！");
		return entry;
	}
	@Override
	public Object getCategory(String content) {
		String pcode = "";
		String pmsg = "";
		JSONObject json = null;
		boolean isErr = true;
		try {
			json = new JSONObject(content);
			isErr = false;
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.getMessage());
		}
		if (!isErr) {
			// 判断传人参数
			if (!json.has("PARCOD")) {
				pcode = "01";
				pmsg = "参数定义错误";
			} else {
				List<SystemMCC> proList = mcc.loadData(json.getString("PARCOD"));
				List<Map<String, String>> lists = new ArrayList<Map<String, String>>();
				Map<String, String> map;
				for(SystemMCC sp : proList) {
					map = new HashMap<String, String>();
					
					map.put("PARID", ""+sp.getId());
					map.put("PARNAME", sp.getName());
					lists.add(map);
				}
				MobileListsEntity entry = new MobileListsEntity();
				entry.setLISTS(lists);
				entry.setRSPCOD("00");
				entry.setRSPMSG("查询成功！");
				return entry;
			}
		}
		
		return new AppBaseEntity(pcode, pmsg);
	}

}
