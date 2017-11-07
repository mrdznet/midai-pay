package com.midai.pay.dealtotal.service.impl;

import java.text.DecimalFormat;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.dubbo.config.annotation.Service;
import com.midai.framework.common.BaseServiceImpl;
import com.midai.pay.common.constants.Constants;
import com.midai.pay.dealtotal.entity.Dealtotal;
import com.midai.pay.dealtotal.mapper.DealtotalMapper;
import com.midai.pay.dealtotal.query.DealtotalQuery;
import com.midai.pay.dealtotal.service.DealtotalService;
import com.midai.pay.dealtotal.vo.DealtotalVo;

/**
 * ClassName:User <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2016年9月21日 <br/>
 * 
 * @author zt
 * @version
 * @since JDK 1.7
 * @see
 */

@Service
public class DealtotalServiceImpl extends BaseServiceImpl<Dealtotal> implements DealtotalService {

	public DealtotalServiceImpl(DealtotalMapper mapper) {
		super(mapper);
		this.mapper = mapper;
	}

	private final DealtotalMapper mapper;

	@Override
	public List<DealtotalVo> queryList(DealtotalQuery query) {
		return mapper.queryList(query);
	}
	
	@Override
	public int queryCount(DealtotalQuery query) {
		return mapper.queryCount(query);
	}
	
	/**
	 * 代理商交易查询
	 */
	public List<DealtotalVo> angetQueryList(DealtotalQuery query){
		return mapper.angetQueryList(query);
	}
	
	public int angetQueryCount(DealtotalQuery query){
		  return mapper.angetQueryCount(query);
	}
	
	  /**
	   * 代理商交易查询Excel导出
	   */  
	@Override
	public List<DealtotalVo> AgentExcelDownBodealtotal(DealtotalQuery query) {
		List<DealtotalVo> dealList = mapper.AgentExcelDownBodealtotal(query);
		for (DealtotalVo vo : dealList) {
			this.calculateFee(vo);
		}
		return dealList;
	}

	@Override
	public int AgentExcelDownBodealtotalCount(DealtotalQuery query) {
		return mapper.AgentExcelDownBodealtotalCount(query);
	}
	
	
	@Override
	public List<DealtotalVo> ExcelDownBodealtotal(DealtotalQuery query) {
		List<DealtotalVo> dealList = mapper.ExcelDownBodealtotal(query);
		for (DealtotalVo vo : dealList) {
			this.calculateFee(vo);
		}
		return dealList;
	}

	@Override
	public int ExcelDownBodealtotalCount(DealtotalQuery query) {
		return mapper.ExcelDownBodealtotalCount(query);
	}

	@Override
	public int queryCountByTransnoAndTransdate(String transno, String transdate) {
		return mapper.queryCountByTransnoAndTransdate(transno, transdate);
	}

	@Override
	public int queryCountByTransno(String transno) {
		return mapper.queryCountByTransno(transno);
	}
	
	DealtotalVo calculateFee(DealtotalVo vo){
		/** 金额按"分"记, 再四舍五入
		 * 
		 * 1.通道手续费：分贷记卡借记卡
				贷记卡交易时，该值等于 交易金额*通道贷记卡手续费
				借记卡交易时，该值等于 min(交易金额*通道借记卡手续费，借记卡封顶值)
			2.通道结算金额：交易金额-通道手续费
			3.利润：交易金额*商户扣率+单笔手续费-通道手续费
		 */
		DecimalFormat df = new DecimalFormat("0.00");
		
		double transAmt = Double.valueOf(vo.getTransAmt()) * 100; //交易金额
		double transFee = 0.0, temp = 0.0; // 通道手续费
		
		if(vo.getCardKindState().equals("01")){ //借记卡
			temp = transAmt * (vo.getInstFeeRate() / 100);
			transFee = (temp > (vo.getInstFeeMax() * 100)) ? (vo.getInstFeeMax()*100) : temp;
			
		}else if(vo.getCardKindState().equals("02")){//贷记卡
			transFee = transAmt * (vo.getInstDfeeRate() / 100);
		}
		
		double channelMoney =  transAmt - transFee; //通道结算金额
		double profits = transAmt*(vo.getMchntRate()/100) - transFee; //利润
		
		vo.setTransFee(df.format(transFee/100));
		vo.setChannelMoney(df.format(channelMoney/100));
		vo.setProfits(df.format(profits/100));
		
		/**
		 * 未结算: 计算出 '结算金额' 和 '提现手续费'
		 */
		if(StringUtils.isEmpty(vo.getTixianAmt()) || StringUtils.isEmpty(vo.getTixianFeeamt())){
			double mchntRate = vo.getMchntRate();
			double mchntSingleFee = vo.getMchntSingleFee() * 100;
			double tixianFeeamt = mchntRate/100 * transAmt + mchntSingleFee;
			double tixianAmt = transAmt - tixianFeeamt;
			
			vo.setTixianFeeamt(df.format(tixianFeeamt/100));
			vo.setTixianAmt(df.format(tixianAmt/100));
		}
		
		/**
		 * 提现手续费：需扣除商户单笔扣率
		 */
		vo.setTixianFeeamt(df.format(Double.valueOf(vo.getTixianFeeamt()) - vo.getMchntSingleFee()));
		
		if(StringUtils.isNotEmpty(vo.getInscode())){
			if(vo.getInscode().equals(Constants.MIFU_INSCODE)){
				vo.setInscode("米付");
			}else if(vo.getInscode().equals(Constants.HAIBEI_INSCODE)){
				vo.setInscode("海贝");
			}else if(vo.getInscode().equals(Constants.ZHANGFU_INSCODE)){
				vo.setInscode("掌付");
			}
		}
		
		return vo;
	}
}
