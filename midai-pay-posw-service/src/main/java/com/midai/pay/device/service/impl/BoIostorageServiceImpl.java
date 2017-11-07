package com.midai.pay.device.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.midai.framework.common.BaseServiceImpl;
import com.midai.pay.agent.service.AgentService;
import com.midai.pay.customer.mapper.BoCustomerDeviceMapper;
import com.midai.pay.device.entity.BoIostorage;
import com.midai.pay.device.mapper.BoAgentDeviceMapper;
import com.midai.pay.device.mapper.BoDeviceMapper;
import com.midai.pay.device.mapper.BoIostorageMapper;
import com.midai.pay.device.service.BoIostorageService;
import com.midai.pay.mobile.mapper.MobileUserDefineCustomerDeviceMapper;
import com.midai.pay.mobile.vo.DeviceCustomerVo;
import com.midai.pay.user.entity.SystemUser;
import com.midai.pay.user.service.SystemUserService;

/**   
*    
* 项目名称：midai-pay-posw-service   
* 类名称：BoIostorageServiceImpl   
* 类描述：   
* 创建人：wrt   
* 创建时间：2016年10月20日 下午5:28:14   
* 修改人：wrt   
* 修改时间：2016年10月20日 下午5:28:14   
* 修改备注：   
* @version    
*    
*/
@Service
public class BoIostorageServiceImpl extends BaseServiceImpl<BoIostorage> implements BoIostorageService {

    private final BoIostorageMapper mapper;
    @Autowired
    private MobileUserDefineCustomerDeviceMapper uamapper;
    @Autowired
    private BoAgentDeviceMapper agdmapper;
    @Autowired
    private BoCustomerDeviceMapper bcdmapper;
    @Autowired
    private BoDeviceMapper bbmapper;
    @Reference
    private SystemUserService sus;
    @Reference
    private AgentService as;
    
    public BoIostorageServiceImpl(BoIostorageMapper mapper) {
        super(mapper);
        this.mapper=mapper;
    }

    /**
	 * 更新库存记录,标识库存终端设备已被使用
	 * @param bodyNo 机身号
	 * @return
	 */
    @Transactional
	@Override
	public int afterStore(String bodyNo,  Integer operatorId) {

		List<DeviceCustomerVo> mcdv = uamapper.getMobileCustomerDeviceVobyBodyNo(bodyNo);
		int num = 0;
		if(mcdv!=null && mcdv.size()>0) {
			for(DeviceCustomerVo dco : mcdv) {
				BoIostorage boi = new BoIostorage();
				boi.setDeviceNo(dco.getBodyno());
				boi.setCreateTime(new Date());
				boi.setAgentId(dco.getAgentid());
				boi.setAgentName(dco.getAgentname());
				boi.setDestmercId(dco.getCustomerid());
				boi.setState("使用");
				boi.setOperatorId(operatorId);
				int s = 0;
				if((s=mapper.insert(boi)) > 0) {
					num += s;
					//更新中间表，标识代理商终端被使用状态
					num += agdmapper.updateAgentDeviceStateByDeviceId(dco.getBodyno(), 1);
					//设置设备表商户id和设备状态
					num += bbmapper.updateCustomerIdAndStateByBodyNos(dco.getBodyno(), dco.getCustomerid(), 2);
				}
			}
		}
		return num;
	}

    @Transactional
	@Override
	public int beforeStoreNew(int boid, String mobile, Integer operatorId) {
		
		int num = 0;
		List<DeviceCustomerVo> dcvs = uamapper.getMobileCustomerDeviceVobyDoid(boid);
		if(dcvs!=null && dcvs.size()>0) {
			for(DeviceCustomerVo dco : dcvs) {
				BoIostorage boi = new BoIostorage();
				boi.setDeviceNo(dco.getBodyno());
				boi.setCreateTime(new Date());
				boi.setAgentId(dco.getAgentid());
				boi.setAgentName(dco.getAgentname());
				boi.setDestmercId(dco.getCustomerid());
				boi.setState("解绑");
				boi.setOperatorId(operatorId);
				int s = 0;
				if((s=mapper.insert(boi)) > 0) {
					num += s;
					//更新代理商中间表
					num += agdmapper.updateAgentDeviceStateByDeviceId(dco.getBodyno(), 0);
					//删除原有商户表绑定的机器
					num += bcdmapper.deleteByPrimaryKey(boid);
					//将设备表customerid地段置为空
					num += bbmapper.updateCustomerIdAndStateByBodyNos(dco.getBodyno(), "", 2);
				}
			}
		}
		return num;
	}

	@Override
	public int batchInsertBIo(List<String> deviceNos, String userName,String sourceAgentName, String sourceAgentId, String destagentAgentName, String destagentAgentId, String status) {
		SystemUser u = sus.loadByUserLoginname(userName);
		List<BoIostorage> list = new ArrayList<BoIostorage>();
		for(String deviceNo : deviceNos) {
			BoIostorage io = new BoIostorage();
			if(StringUtils.isNotEmpty(sourceAgentName)) {
				io.setAgentName(sourceAgentName);
			}
			if(StringUtils.isNotEmpty(sourceAgentId)) {
				io.setAgentId(sourceAgentId);
			}
			if(StringUtils.isNotEmpty(destagentAgentName)) {
				io.setDestagentName(destagentAgentName);
			}
			if(StringUtils.isNotEmpty(destagentAgentId)) {
				io.setDestagentId(destagentAgentId);
			}
			io.setCreateTime(new Date());
			io.setOperatorId(u.getId());
			io.setState(status);
			io.setDeviceNo(deviceNo);
			list.add(io);		
		}
		Map<String, List<BoIostorage>> map = new HashMap<String, List<BoIostorage>>();
		map.put("list", list);
		return mapper.batchInsertBIo(map);
	}

    
}
