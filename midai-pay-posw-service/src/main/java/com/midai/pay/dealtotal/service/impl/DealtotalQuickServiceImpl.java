package com.midai.pay.dealtotal.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.midai.framework.common.BaseServiceImpl;
import com.midai.pay.dealtotal.entity.DealtotalQuick;
import com.midai.pay.dealtotal.mapper.DealtotalQuickMapper;
import com.midai.pay.dealtotal.query.DealtotalQuickQuery;
import com.midai.pay.dealtotal.service.DealtotalQuickService;
import com.midai.pay.dealtotal.vo.DealtotalQuickQueryVo;
import com.midai.pay.dealtotal.vo.DealtotalQuickStateUpdateVo;
import com.midai.pay.dealtotal.vo.DealtotalQuickVo;
import com.midai.reqbean.QRCodePayReqBean;
import com.midai.resbean.QRCodePayResBean;

import tk.mybatis.mapper.entity.Example;

@Service
public class DealtotalQuickServiceImpl extends BaseServiceImpl<DealtotalQuick> implements DealtotalQuickService{

	private final DealtotalQuickMapper quickmapper;
	
	public DealtotalQuickServiceImpl(DealtotalQuickMapper quickmapper) {
		super(quickmapper);
		
		this.quickmapper = quickmapper;
	}

	
	@Override
	public boolean save(DealtotalQuickVo vo) {
		DealtotalQuick entity = new DealtotalQuick();
		
		BeanUtils.copyProperties(vo, entity);
		entity.setCreateTime(new Date());
		entity.setUpdateTime(new Date());
		entity.setPayResult("99");
		entity.setPayDesc("等待付款");
		entity.setT0RespCode("99");
		entity.setT0RespDesc("等待付款");
		return quickmapper.insert(entity) > 0;
	}


	@Override
	@Transactional
	public String updateState(DealtotalQuickStateUpdateVo vo) {
		quickmapper.updateState(vo);
		Example example = new Example(DealtotalQuick.class);
		example.createCriteria().andEqualTo("seqId", vo.getSeqId());


		List<DealtotalQuick> dealtotalQuicks = quickmapper.selectByExample(example);
		if (!dealtotalQuicks.isEmpty()) {
			return dealtotalQuicks.get(0).getNotifyUrl();
		}

		return null;
	}


	@Override
	public List<DealtotalQuickQueryVo> queryList(DealtotalQuickQuery query) {
		return quickmapper.queryList(query);
	}


	@Override
	public int queryCount(DealtotalQuickQuery query) {
		return quickmapper.queryCount(query);
	}


	@Override
	public Boolean saveOrderData(QRCodePayReqBean qrCodePayReqBean, QRCodePayResBean agentAuthResBean, String mercNo, String mercName, String agentNo, Integer source, Double rate, String remarks) {
		DealtotalQuickVo dealtotalQuickVo = new DealtotalQuickVo();
		dealtotalQuickVo.setSeqId(qrCodePayReqBean.getSendSeqId());
		dealtotalQuickVo.setOrgaId(qrCodePayReqBean.getOrganizationId());
		dealtotalQuickVo.setTransAmt(Integer.parseInt(qrCodePayReqBean.getTransAmt()));
		dealtotalQuickVo.setTransFee(Integer.parseInt(qrCodePayReqBean.getFee()));
		dealtotalQuickVo.setCardNo(qrCodePayReqBean.getCardNo());
		dealtotalQuickVo.setCardOwer(qrCodePayReqBean.getName());
		dealtotalQuickVo.setCardOwerId(qrCodePayReqBean.getIdNum());
		dealtotalQuickVo.setNote(qrCodePayReqBean.getBody());
		dealtotalQuickVo.setInMobile(qrCodePayReqBean.getMobile());
		dealtotalQuickVo.setTransChannel(qrCodePayReqBean.getPayPass());
		dealtotalQuickVo.setUpChannel("钱宝");
		dealtotalQuickVo.setType(1);
		dealtotalQuickVo.setMercNo(mercNo);
		dealtotalQuickVo.setMercName(mercName);
		dealtotalQuickVo.setAgentNo(agentNo);
		dealtotalQuickVo.setSource(source);
		dealtotalQuickVo.setTransRate(rate);
		dealtotalQuickVo.setNotifyUrl(qrCodePayReqBean.getNotifyUrl());
		dealtotalQuickVo.setRemarks(remarks);
		return save(dealtotalQuickVo);
	}
	
}
