package com.midai.pay.settlemoney.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.midai.framework.common.BaseServiceImpl;
import com.midai.framework.common.PageEnum;
import com.midai.pay.autopaymoney.entity.BoAutoPayMoney;
import com.midai.pay.autopaymoney.entity.PayInfo;
import com.midai.pay.autopaymoney.mapper.AutoPayMoneyMapper;
import com.midai.pay.customer.mapper.BoCustomerMapper;
import com.midai.pay.customer.vo.BoCustomerVo;
import com.midai.pay.mobile.utils.MassageUtil;
import com.midai.pay.settlemoney.entity.BoGetMoney;
import com.midai.pay.settlemoney.mapper.BoGetMoneyMapper;
import com.midai.pay.settlemoney.query.BoGetMoneyQuery;
import com.midai.pay.settlemoney.service.BoGetMoneyService;
import com.midai.pay.settlemoney.vo.SettleCustomerVo;
import com.midai.pay.user.service.SystemUserService;

@Service
@Transactional
public class BoGetMoneyServiceImpl  extends BaseServiceImpl<BoGetMoney> implements BoGetMoneyService{

	private final BoGetMoneyMapper boGetMoneyMapper;
	public BoGetMoneyServiceImpl(BoGetMoneyMapper mapper) {
		super(mapper);
		this.boGetMoneyMapper=mapper;
	}
	
	@Autowired
	private AutoPayMoneyMapper autoPayMoneyMapper;

	@Autowired 
	private BoCustomerMapper boCustomerMapper;
	
	@Autowired
	private MassageUtil massageUtil;
	
	@Autowired
	private SystemUserService systemUserService;
	
	@Override
	public List<SettleCustomerVo> findqueryBoGetMoney(BoGetMoneyQuery query) {
		return boGetMoneyMapper.findqueryBoGetMoney(query);
	}

	@Override
	public int findqueryBoGetMoneyCount(BoGetMoneyQuery query) {
		return boGetMoneyMapper.findqueryBoGetMoneyCount(query);
	}

	@Override
	public int batchUpdate(String logNos, String name) {
		
		//批量修改状态
		int num=boGetMoneyMapper.bacthUpdateSettleState(logNos);
		if (num > 0) {
			//插入数据
			Map<String, List<BoAutoPayMoney>> batchInsertData=new HashMap<String, List<BoAutoPayMoney>>();
			List<BoGetMoney> getMoneyList=boGetMoneyMapper.selectBoGetMoneyList(logNos);
			List<BoAutoPayMoney> autoPayMoneyList=new ArrayList<BoAutoPayMoney>();
			BoAutoPayMoney boAutoPayMoney=null;
			for (BoGetMoney boGetMoney : getMoneyList) {
				boAutoPayMoney=new BoAutoPayMoney();
				boAutoPayMoney.setTixiLogno(boGetMoney.getLogNo());
				boAutoPayMoney.setPayTime(new Date());
				boAutoPayMoney.setPayPerson(name);
				boAutoPayMoney.setMercId(boGetMoney.getMercId());
				boAutoPayMoney.setPayMoney(boGetMoney.getTixianAmt());
				//根据小票号查询所有信息
				BoCustomerVo vo=boCustomerMapper.findByMrecId(boGetMoney.getMercId());
				if (StringUtils.isEmpty(vo.getBranchBankName())) {	
					boAutoPayMoney.setBankName("");
				}
				boAutoPayMoney.setBankName(vo.getBranchBankName());
				boAutoPayMoney.setAccountName(vo.getAccountName());
				boAutoPayMoney.setAccountNo(vo.getAccountNo());
				boAutoPayMoney.setBankCode(vo.getBranchbankId());
				boAutoPayMoney.setMercName(vo.getMercName());
				boAutoPayMoney.setCreateTime(new Date());
				boAutoPayMoney.setCreateUser(name);
				autoPayMoneyList.add(boAutoPayMoney);
			}
			
			batchInsertData.put("list", autoPayMoneyList); 
			autoPayMoneyMapper.batchInsert(batchInsertData);
			
			massageUtil.sendMsgByResource(PageEnum.zddk.toString(), systemUserService.getInscode(name)); //发消息给自动打款
		}
		
		return 1;
	}

	public String pay(String logNos){
		String newlogno=StringUtils.removeEnd(logNos, ",").replaceAll(",", "','");
		
		List<PayInfo> payList = autoPayMoneyMapper.findPayInfo(newlogno);
		
		return null;	
	}
}
