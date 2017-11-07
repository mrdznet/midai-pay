package com.midai.pay.customer.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.midai.framework.common.BaseServiceImpl;
import com.midai.pay.customer.entity.BoCustomer;
import com.midai.pay.customer.entity.BoCustomerDevice;
import com.midai.pay.customer.mapper.BoCustomerDeviceMapper;
import com.midai.pay.customer.mapper.BoCustomerMapper;
import com.midai.pay.customer.service.BoCustomerDeviceService;
import com.midai.pay.device.entity.BoIostorage;
import com.midai.pay.device.mapper.BoDeviceMapper;
import com.midai.pay.device.mapper.BoIostorageMapper;
import com.midai.pay.device.vo.DeviceDetailVo;

import tk.mybatis.mapper.entity.Example;

@Service
public class BoCustomerDeviceServiceImpl extends BaseServiceImpl<BoCustomerDevice> implements BoCustomerDeviceService {

	public BoCustomerDeviceServiceImpl(BoCustomerDeviceMapper mapper) {
		super(mapper);
		
		this.boCustomerDeviceMapper = mapper;
	}

	private final BoCustomerDeviceMapper boCustomerDeviceMapper;
	
	@Autowired
	private BoCustomerMapper boCustomerMapper;
	
	@Autowired
	private BoDeviceMapper boDeviceMapper;
	
	@Autowired
	private BoIostorageMapper boIostorageMapper;
	
	@Override
	@Transactional
	public DeviceDetailVo insert(String bodyNo,String mercNo,int isFirst,String typeName,String modeName,int id) {
		
		//追加之前先进行判断是否存在
		int num=boCustomerDeviceMapper.selectCustomerDeviceCount(mercNo,bodyNo);
		if (num > 0) {
			return null;
		}
		/**
		 * 首先插入tbl_bo_customer_device表一条绑定数据
		 * 更改设备表状态2已开通，绑定商户编号
		 * 然后向tbl_bo_iostorage表插入一条绑定数据
		 * 首选项不做考虑，全用默认值0，首选1是APP端选择的
		 */
		//首先插入tbl_bo_customer_device
		//通过mercNo获得小票号,state状态改为1绑定，绑定时间为当前时间
		BoCustomer boCustomer=boCustomerMapper.selectByMercNo(mercNo);
		BoCustomerDevice  boCustomerDevice=new BoCustomerDevice();
		boCustomerDevice.setMercId(boCustomer.getMercId());
		boCustomerDevice.setMercNo(mercNo);
		boCustomerDevice.setBodyNo(bodyNo);
		boCustomerDevice.setState(1);
		boCustomerDevice.setBundingTime(new Date());
		boCustomerDevice.setCreateTime(new Date());
		boCustomerDevice.setIsFirst(isFirst);
		boCustomerDeviceMapper.insertBoCustomerDevice(boCustomerDevice);
		//更新state 改为2已开通
		StringBuilder bodynoStr = new StringBuilder();		
		bodynoStr.append("'").append(bodyNo).append("'");	
		boDeviceMapper.updateByBodyNos(bodynoStr.toString(), 2);
		
		//更新商户编号
		
		boDeviceMapper.updateCustomerId(mercNo, bodyNo);

		//然后向tbl_bo_iostorage表插入一条绑定数据
        /**
         * 取值插入
         * 调用
         */
		BoIostorage boIostorage=new BoIostorage();
		BoCustomer customer=boCustomerMapper.selectByMercNo(mercNo);
		//boIostorage.setDeviceId(deviceId);//对应设备表里面的id
		boIostorage.setDeviceNo(bodyNo);//设备机身号
		boIostorage.setCreateTime(new Date());
		//boIostorage.setDeviceFlag(deviceFlag);//pos和sim的标示
		boIostorage.setAgentId(customer.getAgentId());//源代理商编号
		boIostorage.setAgentName(customer.getAgentName());//代理商名称
		//boIostorage.setDestagentId();//目标代理商编号
		//boIostorage.setDestagentName(destagentName);//目标代理商的名称
		boIostorage.setDestmercId(mercNo);//目标商户的编号
		boIostorage.setState("绑定");//状态，绑定或者解绑
		boIostorage.setOperatorId(id);
		
		boIostorageMapper.insertBoIostorage(boIostorage);
		
		//为了前端页面返回列表使用
		DeviceDetailVo deviceDetailVo=new DeviceDetailVo();
		deviceDetailVo.setDeviceNo(bodyNo);
		deviceDetailVo.setCreateTime(new Date());
		deviceDetailVo.setTypeName(typeName);
		deviceDetailVo.setModeName(modeName);
		deviceDetailVo.setIsFirst(isFirst);
		return deviceDetailVo;
	}

	@Override
	@Transactional
	public int batchDelete(String mercNo,String[] bodyNos,int userId) {
		/**
		 * 解绑时，要删除tbl_bo_customer_device的记录
		 * 添加历史记录表的数据tbl_bo_iostorage解绑记录
		 * 设备表里面的状态是不可逆的：1转为2,2不能转为1
		 */
		
		//第一步
		StringBuilder idStr = new StringBuilder();
		for (String id : bodyNos) {
			idStr.append("'").append(id).append("',");
		
		}
		idStr.setLength(idStr.length() - 1);
		
		boCustomerDeviceMapper.deleteBoCustomerDevice(mercNo, idStr.toString());
		
		//第二步添加历史记录表的数据tbl_bo_iostorage解绑记录
		
		for (String id : bodyNos) {
			//StringBuilder idStr2 = new StringBuilder();
			//idStr2.append("'").append(id).append("'");

			BoIostorage boIostorage=new BoIostorage();
			BoCustomer customer=boCustomerMapper.selectByMercNo(mercNo);
			//boIostorage.setDeviceId(deviceId);//对应设备表里面的id
			boIostorage.setDeviceNo(id);//设备机身号
			boIostorage.setCreateTime(new Date());
			//boIostorage.setDeviceFlag(deviceFlag);//pos和sim的标示
			boIostorage.setAgentId(customer.getAgentId());//源代理商编号
			boIostorage.setAgentName(customer.getAgentName());//代理商名称
			//boIostorage.setDestagentId(destagentId);//目标代理商编号
			//boIostorage.setDestagentName(destagentName);//目标代理商的名称
			boIostorage.setDestmercId(mercNo);//目标商户的编号
			boIostorage.setState("解绑");//状态，绑定或者解绑
			boIostorage.setOperatorId(userId);
			boIostorageMapper.insertBoIostorage(boIostorage);
		}
		
		//更新商户编号,删除商户编号
		boDeviceMapper.updateDeviceCustomer(null, idStr.toString());
				
		return 1;
	}

	@Override
	public List<BoCustomerDevice> getByMercNo(String mercNo) {
		Example example = new Example(BoCustomerDevice.class);
		example.createCriteria().andEqualTo("mercNo", mercNo);
		return boCustomerDeviceMapper.selectByExample(example);
	}
	@Override
	public List<BoCustomerDevice> getByodyNo(String bodyNo) {
		Example example = new Example(BoCustomerDevice.class);
		example.createCriteria().andEqualTo("bodyNo", bodyNo);
		return boCustomerDeviceMapper.selectByExample(example);
	}

	@Override
	public int updateIsFirst(String mercNo, String bodyNo) {
		return boCustomerDeviceMapper.updateIsFirst(mercNo, bodyNo);
	}

	@Override
	public List<BoCustomerDevice> getByodyNos(String bodyNos) {
		
		return boCustomerDeviceMapper.getByodyNos(bodyNos);
	}

	@Override
	public int deleteBybodyNOs(String bodyNOs) {
		
		return boCustomerDeviceMapper.deleteBybodyNOs(bodyNOs);
	}

	

	

}
