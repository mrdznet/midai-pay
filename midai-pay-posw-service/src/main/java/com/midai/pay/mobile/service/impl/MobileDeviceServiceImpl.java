package com.midai.pay.mobile.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.midai.pay.customer.entity.BoCustomer;
import com.midai.pay.customer.entity.BoCustomerDevice;
import com.midai.pay.customer.mapper.BoCustomerDeviceMapper;
import com.midai.pay.customer.service.BoCustomerDeviceService;
import com.midai.pay.customer.service.BoCustomerService;
import com.midai.pay.customer.service.MobileDeviceService;
import com.midai.pay.device.service.BoIostorageService;
import com.midai.pay.mobile.AppBaseEntity;
import com.midai.pay.mobile.mapper.MobileUserDefineCustomerDeviceMapper;
import com.midai.pay.mobile.utils.UserDefineException;
import com.midai.pay.mobile.vo.DeviceCustomerVo;
import com.midai.pay.user.entity.SystemUser;
import com.midai.pay.user.service.SystemUserService;

import tk.mybatis.mapper.entity.Example;

@Service
public class MobileDeviceServiceImpl implements MobileDeviceService {

	private Logger logger = LoggerFactory.getLogger(MobileDeviceServiceImpl.class);

	@Autowired
	private MobileUserDefineCustomerDeviceMapper mdcdmapper;
	@Autowired
	private BoCustomerDeviceMapper bcdmapper;
	@Reference
	private BoCustomerService bcs;
	@Reference
	private BoCustomerDeviceService bcds;
	@Reference
	private BoIostorageService biss;
	@Reference
	private SystemUserService sus;

	@Transactional
	@Override
	public Object addDevice(String content) {
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
			if (!json.has("PHONENUMBER") || !json.has("DEVICENO") || !json.has("ISFIRST")) {
				return new AppBaseEntity("01", "参数定义错误");
			} 

			String phonenumber = json.getString("PHONENUMBER");// 手机号
			String deviceno = json.getString("DEVICENO");// 设备号
			int isfirst = 0;
			isfirst = json.getInt("ISFIRST");
			// 判断该设备是否已被其他人使用
			String deviceId = null, _customerid = null, _agentid = null;
			List<DeviceCustomerVo> dcvs = mdcdmapper.getMobileCustomerDeviceAgentVobyBodyNo(deviceno);
			if (dcvs != null && dcvs.size() > 0) {
				DeviceCustomerVo dcv = dcvs.get(0);
				deviceId = dcv.getDeviceid(); //设备机身号
				_customerid = dcv.getCustomerid();
				_agentid = dcv.getAgentid();
			}
			if (StringUtils.isEmpty(deviceId)) {
				return new AppBaseEntity("03", "该终端不存在或未入库，请联系终端部门！");
			} 

			String unid = null, agentid = null, merid = "";
			int state = 0, opstate = 0;
			BoCustomer bc = bcs.getCustomerByMobile(phonenumber);
			if (bc != null) {
				unid = bc.getMercNo();
				agentid = bc.getAgentId();
				state = bc.getState();
				opstate = bc.getOpstate();
				merid = bc.getMercId();
			}
			// 判断商户审核状态
			if (state != 4) {
				return new AppBaseEntity("04", "商户信息审核未通过，不允许绑定终端！");
			} 
			// 判断商户开通状态
			if (opstate != 1) {
				return new AppBaseEntity("05", "商户未开通，不允许绑定终端！");
			} 
			// 判断终端代理商是否一致
			if (_agentid != null && agentid != null && !_agentid.equals(agentid)) {
				return new AppBaseEntity("06", "该终端对应代理商与商户原有代理商不一致！");
			} 
			// 判断终端商户是否被使用
			if (_customerid != null && !_customerid.isEmpty() && isfirst == 0) {
				return new AppBaseEntity("07", "该终端已被使用！");
			} 
			
			BoCustomerDevice bcd = new BoCustomerDevice();
			bcd.setMercNo(unid);
			bcd.setBodyNo(deviceno);
			bcd.setCreateTime(new Date());
			bcd.setIsFirst(isfirst);
			bcd.setMercId(merid);
			bcd.setState(1);
			SystemUser user = sus.loadByUserLoginname(phonenumber);
			try {
				update(bcd, isfirst, _customerid, user.getId());
				biss.afterStore(bcd.getBodyNo(), user.getId());
				pcode = "00";
				pmsg = "成功";
			} catch (UserDefineException e) {
				return e.getApp();
			}
		}
		return new AppBaseEntity(pcode, pmsg);
	}

	
	private void update(BoCustomerDevice bcd, Integer isfirst, String _customerid, Integer operatorId) throws UserDefineException {

		String unid = bcd.getMercNo();
		String deviceno = bcd.getBodyNo();
		//如果商户已绑定设备，更改设备的首选项
		List<BoCustomerDevice> bcdl =  bcds.getByMercNo(unid);
		if(bcdl != null && bcdl.size() > 0) {
			BoCustomerDevice bd = new BoCustomerDevice();
			bd.setIsFirst(0);
			Example example = new Example(BoCustomerDevice.class);
			example.createCriteria().andEqualTo("mercNo", unid);
			bcdmapper.updateByExampleSelective(bd, example);
			bd.setIsFirst(1);
			example.clear();
			example.createCriteria().andEqualTo("bodyNo", deviceno);
			bcdmapper.updateByExampleSelective(bd, example);
		} else {
			isfirst = 1;
		}
		
		bcd.setIsFirst(isfirst);
		
		if (_customerid == null) {
			bcdmapper.insert(bcd);
			// 设备出库
			try {
				biss.afterStore(bcd.getBodyNo(), operatorId);
			} catch (Exception e) {
				logger.error(e.getMessage());
				throw new UserDefineException("{'RSPCOD':'08','RSPMSG':'设备出库失败！'}", new AppBaseEntity("08", "设备出库失败！"));
			}
		}
	
	}


	@Override
	public Object mulDevice(String content) {
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
			if (!json.has("PHONENUMBER") || !json.has("DEVICENO")) {
				pcode = "01";
				pmsg = "参数定义错误";
			} else {
				int state=0,opstate=0;
				String phonenumber=json.getString("PHONENUMBER");//手机号
				String deviceno=json.getString("DEVICENO");//设备号
				BoCustomer bc = bcs.getCustomerByMobile(phonenumber);
				if(bc != null) {
					state = bc.getState();
					opstate = bc.getOpstate();
				}
				// 判断商户审核状态
				if (state != 4) {
					pcode = "04";
					pmsg = "商户信息审核未通过，不允许解绑终端！";
				} else
				// 判断商户开通状态
				if (opstate != 1) {
					pcode = "05";
					pmsg = "商户未开通，不允许解绑终端！";
				} else {
					List<BoCustomerDevice> bds = bcds.getByodyNo(deviceno);
					SystemUser user = sus.loadByUserLoginname(phonenumber);
					if(bds != null && bds.size() > 0) {
						if(biss.beforeStoreNew(bds.get(0).getId(), deviceno, user.getId()) > 0) {
							pcode = "00";
							pmsg = "设备解绑成功!";
						} else {
							pcode = "01";
							pmsg = "设备解绑失败！";
						}
					}
				}
			}
		}
		return new AppBaseEntity(pcode, pmsg);
	}
}
