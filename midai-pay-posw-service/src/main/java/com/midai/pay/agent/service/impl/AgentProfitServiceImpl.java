package com.midai.pay.agent.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.midai.framework.common.BaseServiceImpl;
import com.midai.framework.common.DoubleUtil;
import com.midai.pay.agent.common.enums.AgentProfitTransModeEnum;
import com.midai.pay.agent.common.enums.AgentProfitTransTypeEnum;
import com.midai.pay.agent.entity.Agent;
import com.midai.pay.agent.entity.AgentProfit;
import com.midai.pay.agent.mapper.AgentProfitMapper;
import com.midai.pay.agent.service.AgentProfitService;
import com.midai.pay.agent.service.AgentService;
import com.midai.pay.agent.service.JiguangPushService;
import com.midai.pay.agent.vo.AgentProfitComparable;
import com.midai.pay.agent.vo.AgentProfitDsicountAmtVo;
import com.midai.pay.common.pay.DateUtils;
import com.midai.pay.customer.entity.BoCustomer;
import com.midai.pay.customer.service.BoCustomerService;
import com.midai.pay.dealtotal.entity.Dealtotal;
import com.midai.pay.dealtotal.entity.DealtotalQuick;
import com.midai.pay.dealtotal.service.DealtotalQuickService;
import com.midai.pay.dealtotal.service.DealtotalService;

import tk.mybatis.mapper.entity.Example;

@Service
public class AgentProfitServiceImpl extends BaseServiceImpl<AgentProfit> implements AgentProfitService {

	private AgentProfitMapper mapper;

	@Reference
	private DealtotalService dealtotalService;
	@Reference
	private BoCustomerService boCustomerService;
	@Reference
	private AgentService agentService;
	@Reference
	private DealtotalQuickService dealtotalQuickService;
	@Reference
	private JiguangPushService pushService;

	@Override
	public int saveAgentSwingCardProfit(String hostTramsSsn, Integer transType) {
		if(transType.intValue() >= AgentProfitTransTypeEnum.TRANSTYPE_SCANWX.getCode().intValue()) {
			Boolean isExist = checkAgentProfitDate(hostTramsSsn);
			if(isExist) {
				return 0;
			}
		}
		BoCustomer customer = new BoCustomer();
		String customerNo = "";
		Integer transAmt = 0;
		Date settlementTime = null;
		String agentId = "";
		Integer singleFee = 0;
		Double rate = 0D;
		String transMode = "";

		Integer trant = 0;

		if (transType.equals(AgentProfitTransTypeEnum.TRANSTYPE_MPOS.getCode())) {

			Dealtotal dealtotal = dealtotalService.findByPrimaryKey(hostTramsSsn);
			customer = boCustomerService.getCustomerByMercId(dealtotal.getMchntCodeIn());
			customerNo = customer.getMercNo();
			transAmt = dealtotal.getTransAmt();
			settlementTime = dealtotal.getTransTime();
			agentId = dealtotal.getAgentId();
			singleFee = dealtotal.getMchntSingleFee();
			rate = dealtotal.getMchntRate();
			if (dealtotal.getCardKind().equals("01")) {
				transMode = AgentProfitTransModeEnum.TRANSTYPE_MPOS_JJK.getCode();
			} else {
				transMode = AgentProfitTransModeEnum.TRANSTYPE_MPOS_DJK.getCode();
			}
			trant = AgentProfitTransTypeEnum.TRANSTYPE_MPOS.getCode();
		} else if (transType.intValue() >= AgentProfitTransTypeEnum.TRANSTYPE_SCANWX.getCode().intValue()) {
			// TODO 根据手机号查询商户
			// 查询扫码交易表，赋值以上变量
			DealtotalQuick dq = new DealtotalQuick();
			dq.setSeqId(hostTramsSsn);
			dq = dealtotalQuickService.findOne(dq);
			agentId = dq.getAgentNo();
			customerNo = dq.getMercNo();
			transAmt = dq.getTransAmt();
			settlementTime = dq.getCreateTime();
			singleFee = 0;
			rate = dq.getTransRate();
			trant = Integer.parseInt(dq.getTransChannel());
			if (trant.equals(Integer.parseInt("1"))) {
				trant = 4;
				transMode = AgentProfitTransModeEnum.TRANSTYPE_SCAN_WIN.getCode();
			} else if(trant.equals(Integer.parseInt("2"))) {
				trant = 5;
				transMode = AgentProfitTransModeEnum.TRANSTYPE_SCAN_ZFB.getCode();
			}
		}

		// 提醒消息推送
		if (transType.intValue() >= AgentProfitTransTypeEnum.TRANSTYPE_SCANWX.getCode().intValue()) {
			Double fee = DoubleUtil.divHalfUp((double)transAmt, 100d, 2);
			customer.getMobile();
			String date = DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
			StringBuffer sb = new StringBuffer("您收到用户支付费用");
			sb.append(fee).append("元，收款时间为 ").append(date);
			pushService.push(customer.getMobile(), sb.toString());
		}

		// 查询直属代理商信息
		Agent agent = new Agent();
		agent.setAgentNo(agentId);
		agent = agentService.findOne(agent);
		String agentCode = agent.getCode();
		String[] codeArr = agentCode.split("-");
		List<String> codeList = new ArrayList<String>();
		for (int i = 0; i < codeArr.length; i++) {
			if (codeList.isEmpty()) {
				codeList.add(codeArr[i]);
			} else {
				codeList.add(codeList.get(codeList.size() - 1) + "-" + codeArr[i]);
			}
		}

		StringBuffer sb = new StringBuffer();

		for (String code : codeList) {
			sb.append("'" + code + "'").append(",");
		}

		List<Agent> agentList = null;
		if (sb.length() > 0) {
			sb.setLength(sb.length() - 1);
			agentList = agentService.selectAgentListByCodes(sb.toString());
		}
		List<AgentProfit> agentProfitList = new ArrayList<AgentProfit>();
		if (agentList != null) {
			for (Agent ag : agentList) {
				AgentProfit ap = new AgentProfit();
				BeanUtils.copyProperties(ag, ap);
				ap.setHostTransSsn(hostTramsSsn);
				ap.setMercNo(customerNo);
				ap.setAgentId(ag.getAgentNo());
				ap.setAgentName(ag.getName());
				ap.setParentAgentId(ag.getParentAgentId());
				ap.setParentAgentName(ag.getParentAgentName());
				ap.setTransAmt(transAmt);
				ap.setCode(ag.getCode());
				ap.setSettlementStatus(0);
				ap.setSettlementTime(settlementTime);

				ap.setTransType(trant);
				ap.setCreateTime(new Date());

				AgentProfitDsicountAmtVo appdav = new AgentProfitDsicountAmtVo();
				appdav.setCustomerTransAmt(transAmt);
				appdav.setCustomerRate(rate);
				appdav.setCustomerSingleFee(singleFee);
				appdav.setCustomerAgentId(agentId);
				// 手刷总利润
				setAgentProfitDsicountAmtVo(appdav, ag, transType, transMode);
				setAgentProfit(ap, appdav);
				ap.setProfitSub(0);
				ap.setProfitOut(0);
				ap.setProfitSubOut(0);
				agentProfitList.add(ap);
			}

			AgentProfitComparable com = new AgentProfitComparable();
			Collections.sort(agentProfitList, com);

			for (int i = 0; i < agentProfitList.size() - 1; i++) {
				AgentProfit ap1 = agentProfitList.get(i);
				AgentProfit ap2 = agentProfitList.get(i + 1);
				ap1.setProfit(ap1.getProfitTotal() - ap2.getProfitTotal());
				ap1.setProfitSub(ap1.getProfitTotal() - ap1.getProfit());

			}

		}

		int i = 0;
		if (agentProfitList != null && agentProfitList.size() > 0) {
			Map<String, List<AgentProfit>> map = new HashMap<String, List<AgentProfit>>();
			map.put("list", agentProfitList);
			i = mapper.batchInsert(map);
		}
		return i;
	}
	
	/**
	 * 判断是否已经处理过回调信息 
	 * @param hostTramsSsn 交易流水号
	 * @return true 已经处理过， false 未处理
	 */
	public Boolean checkAgentProfitDate(String hostTramsSsn){
		Example example = new Example(AgentProfit.class);
		example.createCriteria().andEqualTo("hostTransSsn", hostTramsSsn);
		List<AgentProfit> list = mapper.selectByExample(example);
		if(list != null && list.size() > 0) {
			return true;
		}
		
		return false;
	}

	/**
	 * 
	 * @param appdav
	 * @param ag
	 * @param transType
	 *            1 MPOS, 2 无卡， 3 传统POS， 4扫码
	 * @param transMode
	 *            1001 借记卡， 1002 贷记卡， 2001 无卡， 3001 借记卡， 3002 贷记卡， 4001 微信， 4002
	 *            支付宝，4003-银联扫码, 4004-花呗, 4005-京东白条
	 */
	private void setAgentProfitDsicountAmtVo(AgentProfitDsicountAmtVo appdav, Agent ag, Integer transType,
			String transMode) {

		Boolean islimit = false;
		Double limitAmt = 0D;
		Double rate = 0D;
		Double singleFee = 0D;

		if (transType.equals(AgentProfitTransTypeEnum.TRANSTYPE_MPOS.getCode())) {

			rate = ag.getSwingCardDebitRate();
			singleFee = ag.getSwingCardSettleFee();

			if (transMode.equals(AgentProfitTransModeEnum.TRANSTYPE_MPOS_JJK.getCode())) {

				islimit = true;
				limitAmt = ag.getSwingCardLimit();

			}
		} else if (transType.equals(AgentProfitTransTypeEnum.TRANSTYPE_NONCARD.getCode())) {

			rate = ag.getNonCardCreditRate();
			singleFee = 0D;
		} else if (transType.equals(AgentProfitTransTypeEnum.TRANSTYPE_POS.getCode())) {

			singleFee = ag.getSwingCardSettleFee();

			if (transMode.equals(AgentProfitTransModeEnum.TRANSTYPE_POS_JJK.getCode())) {

				islimit = true;
				limitAmt = ag.getPosDebitLimit();
				rate = ag.getPosDebitRate();
			} else if (transMode.equals(AgentProfitTransModeEnum.TRANSTYPE_POS_DJK.getCode())) {

				rate = ag.getPosDebitRate();
			}
		} else if (transType.intValue() >= AgentProfitTransTypeEnum.TRANSTYPE_SCANWX.getCode().intValue()) {

			singleFee = 0D;

			if (transMode.equals(AgentProfitTransModeEnum.TRANSTYPE_SCAN_WIN.getCode())) {

				rate = ag.getScanCodeWxRate();
			} else if (transMode.equals(AgentProfitTransModeEnum.TRANSTYPE_SCAN_ZFB.getCode())) {

				rate = ag.getScanCodeZfbRate();
			} else if (transMode.equals(AgentProfitTransModeEnum.TRANSTYPE_SCAN_YL.getCode())) {

				rate = ag.getScanCodeYlRate();
			} else if (transMode.equals(AgentProfitTransModeEnum.TRANSTYPE_SCAN_HB.getCode())) {

				rate = ag.getScanCodeMyhbRate();
			} else if (transMode.equals(AgentProfitTransModeEnum.TRANSTYPE_SCAN_JDBT.getCode())) {

				rate = ag.getScanCodeJdbtRate();
			}
		}

		appdav.setAgentIsLimit(islimit);
		appdav.setAgentLimitAmt(limitAmt);
		appdav.setAgentRate(rate);
		appdav.setAgentSingleFee(singleFee);

	}

	private void setAgentProfit(AgentProfit ap, AgentProfitDsicountAmtVo appdav) {

		Integer profitTotal = 0;
		Double agentTransKl = 0D;
		agentTransKl = DoubleUtil.divHalfUp(DoubleUtil.mul(appdav.getCustomerTransAmt(), appdav.getAgentRate()), 100D,
				2);
		if (appdav.getAgentIsLimit()) {

			if (agentTransKl > appdav.getAgentLimitAmt() * 100) {
				agentTransKl = appdav.getAgentLimitAmt() * 100;
			}
		} else {

			agentTransKl = DoubleUtil.divHalfUp(DoubleUtil.mul(appdav.getCustomerTransAmt(), appdav.getAgentRate()),
					100D, 2);
		}

		Double custmTransKl = DoubleUtil
				.divHalfUp(DoubleUtil.mul(appdav.getCustomerTransAmt(), appdav.getCustomerRate()), 100D, 2);
		Double transKlDValue = DoubleUtil.sub(custmTransKl, agentTransKl);
		Double poundageDValue = DoubleUtil.sub(appdav.getCustomerSingleFee(),
				DoubleUtil.mul(appdav.getAgentSingleFee(), 100));
		profitTotal = DoubleUtil.add(transKlDValue, poundageDValue).intValue();
		ap.setProfitTotal(profitTotal);
		if (ap.getAgentId().equals(appdav.getCustomerAgentId())) {

			ap.setProfit(profitTotal);
		} else {

			ap.setProfit(0);
		}
	}

	public AgentProfitServiceImpl(AgentProfitMapper mapper) {
		super(mapper);
		this.mapper = mapper;
	}

	@Override
	public List<AgentProfit> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AgentProfit findByPrimaryKey(Object key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int insert(AgentProfit t) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int deleteByPrimaryKey(Object key) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(AgentProfit t) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public AgentProfit findOne(AgentProfit t) {
		// TODO Auto-generated method stub
		return null;
	}

}
