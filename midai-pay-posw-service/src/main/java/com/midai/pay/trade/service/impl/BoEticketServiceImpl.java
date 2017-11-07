package com.midai.pay.trade.service.impl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.midai.framework.common.BaseServiceImpl;
import com.midai.framework.common.PageEnum;
import com.midai.pay.common.constants.Constants;
import com.midai.pay.common.utils.SmsModeIdHelper;
import com.midai.pay.common.utils.SmsSender;
import com.midai.pay.customer.entity.BoCustomer;
import com.midai.pay.mobile.utils.MassageUtil;
import com.midai.pay.mobile.utils.SmsUtil;
import com.midai.pay.settlemoney.entity.BoGetMoney;
import com.midai.pay.settlemoney.mapper.BoGetMoneyMapper;
import com.midai.pay.trade.entity.BoEticket;
import com.midai.pay.trade.entity.DealCustomer;
import com.midai.pay.trade.mapper.BoEticketMapper;
import com.midai.pay.trade.mapper.TradeReviewMapper;
import com.midai.pay.trade.service.BoEticketService;
import com.midai.pay.trade.vo.EticketReviewVo;
import com.midai.pay.user.service.SystemUserService;

@Service
@Transactional
public class BoEticketServiceImpl extends BaseServiceImpl<BoEticket> implements BoEticketService {

	private Logger logger = LoggerFactory.getLogger(BoEticketServiceImpl.class);
	
	private final BoEticketMapper boEticketMapper;
	
	public BoEticketServiceImpl(BoEticketMapper mapper) {
		super(mapper);
		this.boEticketMapper = mapper;
	}
	
	@Autowired
	private TradeReviewMapper tradeReviewMapper;
	@Autowired
	private BoGetMoneyMapper boGetMoneyMapper;
	
	@Autowired
	private SmsUtil smsUtil;
	
	@Autowired
	private MassageUtil massageUtil;
	
	@Autowired
	private SystemUserService systemUserService;
	
	@Autowired
	private SmsSender smsSender;
	
	/**
	 * 小票审核通过
	 */
	@Transactional
	@Override
	public int saveReviewThrough(String hostTransSsn, String user) {
		String hostTransSsn_ = StringUtils.removeEnd(hostTransSsn, ",").replaceAll(",", "','");
		List<String> hostTransSsnList = boEticketMapper.findByHostTransSsn(hostTransSsn_);
		
		Map<String, String> map = new HashMap<String, String>();
		for(String ssn : hostTransSsnList){
			map.put(ssn, ssn);
		}
		
		String[] tempStrArr = StringUtils.removeEnd(hostTransSsn, ",").split(",");
		
		//如小票表没有,则插入
		BoEticket eticket = null;
		for(String str : tempStrArr){
			if(!map.containsKey(str)){
				eticket = new BoEticket();
				eticket.setHostTransSsn(str);
				eticket.setState(2);
				eticket.setCreateDate(new Date());
				eticket.setCreateUser(user);
				
				boEticketMapper.insertEticket(eticket);
			}
		}
		
		//更新小票状态
		EticketReviewVo vo = new EticketReviewVo();
		vo.setHostTransSsn(hostTransSsn_);
		vo.setState(1);
		vo.setCreateUser(user);
		boEticketMapper.updateReview(vo);
		
		//生成提现记录
		List<DealCustomer> dealList = tradeReviewMapper.findAllByHostTransSsn(hostTransSsn_);
		BoGetMoney gm = null;
		
		double tixianFeeamt = 0; // 提现手续费
		
		double transAmt = 0;	// 交易金额
		double mchntSingleFee = 0;	//单笔手续费
		double mchntRate = 0.0; //商户扣率
		DecimalFormat df = new DecimalFormat("#");
		
		for(DealCustomer deal : dealList){
			gm = new BoGetMoney();
			gm.setLogNo(deal.getHostTransSsn());
			gm.setMercId(deal.getMercId());
			gm.setTixianDatetime(deal.getTransTime());
			gm.setTxdesc("");
			
			transAmt = deal.getTransAmt();
			mchntRate = deal.getMchntRate();	
			mchntSingleFee = deal.getMchntSingleFee();
			
			tixianFeeamt =  mchntRate/100 * transAmt + mchntSingleFee;
			
			gm.setTixianFeeamt(Integer.valueOf(df.format(tixianFeeamt))); // 提现手续费
			gm.setTixianAmt(Integer.valueOf(df.format(transAmt - tixianFeeamt)));
			gm.setCreateTime(new Date());
			
			boGetMoneyMapper.insertBoGetMoney(gm);
		}
		
		massageUtil.sendMsgByResource(PageEnum.jyjs.toString(), systemUserService.getInscode(user)); //给交易结算发消息
		
		return 1;
	}

	/**
	 * 小票审核拒绝
	 */
	@Override
	@Transactional
	public int saveReviewNoThrough(EticketReviewVo reviewVo) {
		String hostTransSsn = StringUtils.removeEnd(reviewVo.getHostTransSsn(), ",").replaceAll(",", "','");
		List<String> hostTransSsnList = boEticketMapper.findByHostTransSsn(hostTransSsn);
		
		Map<String, String> map = new HashMap<String, String>();
		for(String ssn : hostTransSsnList){
			map.put(ssn, ssn);
		}
		
		String[] tempStrArr = StringUtils.removeEnd(reviewVo.getHostTransSsn(), ",").split(",");
		
		//如小票表没有,则插入
		BoEticket eticket = null;
		BoCustomer ct = null;
		for(String str : tempStrArr){
			if(!map.containsKey(str)){
				eticket = new BoEticket();
				eticket.setHostTransSsn(str);
				eticket.setState(2);
				eticket.setCreateDate(new Date());
				boEticketMapper.insertEticket(eticket);
			}
			
			//是否发短信
			if(null!=reviewVo.getSendMsg() && reviewVo.getSendMsg().equals(1)){
				ct = boEticketMapper.findCustomerByHostTransSsn(str);
				 
				switch (ct.getInscode()) {
				case Constants.HAIBEI_INSCODE:
					ArrayList<String> list=new ArrayList<String>();
					list.add(ct.getMercName());list.add(str);list.add(reviewVo.getReason());
					
					try {
						smsSender.sendNotice(ct.getMobile(), list, SmsModeIdHelper.getSmsModeId(10));
					} catch (Exception e) {
						e.printStackTrace();
						logger.info("************************小票审核拒绝, 海贝发送短信失败*******************************");
					}
					break;

				default:
					StringBuilder bs=new StringBuilder();
					bs.append("尊敬的").append(ct.getMercName()).append("用户:您交易号"+str+"审核 被拒绝。原因："+reviewVo.getReason());
					
					smsUtil.sendSMS(ct.getMobile(), bs.toString(), 0, ct.getInscode());
					break;
				}
			}
		}
		
		//更新小票状态
		reviewVo.setHostTransSsn(hostTransSsn);
		reviewVo.setState(2);
		boEticketMapper.updateReview(reviewVo);
		
		return 1;
	}


}
