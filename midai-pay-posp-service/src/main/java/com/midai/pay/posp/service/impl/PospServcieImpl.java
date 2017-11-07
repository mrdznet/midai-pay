/**
 * Project Name:midai-pay-posp-service
 * File Name:PospServcieImpl.java
 * Package Name:com.midai.pay.posp.service.impl
 * Date:2016年9月12日下午1:41:32
 * Copyright (c) 2016, www midaigroup com Technology Co., Ltd. All Rights Reserved.
 */

package com.midai.pay.posp.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.midai.framework.common.po.ResultVal;
import com.midai.pay.agent.common.enums.AgentProfitTransTypeEnum;
import com.midai.pay.agent.service.AgentProfitService;
import com.midai.pay.autopaymoney.service.ShenXinAutoPayService;
import com.midai.pay.customer.entity.BoCustomer;
import com.midai.pay.customer.entity.BoCustomerDevice;
import com.midai.pay.customer.service.BoCustomerDeviceService;
import com.midai.pay.customer.service.BoCustomerService;
import com.midai.pay.handpay.service.HandpayService;
import com.midai.pay.handpay.vo.WithdrawRequestBean;
import com.midai.pay.posp.config.CdTool;
import com.midai.pay.posp.config.HsmUtil;
import com.midai.pay.posp.config.MidaiPayHsmTemplate;
import com.midai.pay.posp.config.Pay8583Message;
import com.midai.pay.posp.config.Pay8583Util;
import com.midai.pay.posp.config.PayMiShuaProperties;
import com.midai.pay.posp.entity.ConSumptionEntity;
import com.midai.pay.posp.entity.ConSumptionResult;
import com.midai.pay.posp.entity.DealTotalPos;
import com.midai.pay.posp.entity.DevicePriEncKey;
import com.midai.pay.posp.entity.DeviceWkEnckey;
import com.midai.pay.posp.entity.Inst;
import com.midai.pay.posp.entity.InstKey;
import com.midai.pay.posp.entity.InstMerc;
import com.midai.pay.posp.entity.InstResultCode;
import com.midai.pay.posp.entity.MiShuaPayParam;
import com.midai.pay.posp.entity.MiShuaPayQueryParam;
import com.midai.pay.posp.entity.ParCardBin;
import com.midai.pay.posp.entity.PayMishua;
import com.midai.pay.posp.entity.SeqEntity;
import com.midai.pay.posp.entity.SignEntity;
import com.midai.pay.posp.entity.SignParam;
import com.midai.pay.posp.mapper.DealTotalMapper;
import com.midai.pay.posp.mapper.DealTotalPosMapper;
import com.midai.pay.posp.mapper.DevicePriEncKeyMapper;
import com.midai.pay.posp.mapper.DeviceWkEnckeyMapper;
import com.midai.pay.posp.mapper.InstKeyMapper;
import com.midai.pay.posp.mapper.InstMapper;
import com.midai.pay.posp.mapper.InstMercMapper;
import com.midai.pay.posp.mapper.InstResultCodeMapper;
import com.midai.pay.posp.mapper.ParCarBinMapper;
import com.midai.pay.posp.mapper.PayMishuaMapper;
import com.midai.pay.posp.service.PospService;
import com.midai.pay.route.service.RouteService;
import com.midai.pay.trade.entity.DealTotal;
import com.midai.pay.user.service.SystemMassageService;
import com.mifu.hsmj.CodingUtil;
import com.mifu.hsmj.DateUtil;
import com.mifu.hsmj.DesUtil;
import com.mifu.hsmj.EncryptUtils;
import com.mifu.hsmj.HsmApp;
import com.mifu.hsmj.HsmConst;
import com.mifu.hsmj.HsmMessage;
import com.mifu.hsmj.ThreeDes;

import tk.mybatis.mapper.entity.Example;

/**
 * ClassName:PospServcieImpl <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2016年9月12日 下午1:41:32 <br/>
 * 
 * @author 陈勋
 * @version
 * @since JDK 1.7
 * @see
 */
@Service(mock = "com.midai.pay.posp.service.mock.PospServiceMock")
public class PospServcieImpl implements PospService {

	private Logger logger = LoggerFactory.getLogger(PospServcieImpl.class);

	public static final String ERROR_CODE = "99";

	public static final String SUCCESS_CODE = "00";
	
	public static final String DEAL_MISHUA="10000012";
	
	//public static final String DEAL_SHENXIN="00000022";测试
	public static final String DEAL_SHENXIN="00000081";
	
	public static final String DEAL_XINKU="10000040";

	public static final String DEAL_JIDIAN_KEY = "4661FC95D1D6FAB29D29BD50191B070A";
	
	public static final String DEAL_JIDIAN = "301000000001";
	
	@Autowired
	private MidaiPayHsmTemplate hsmTemplate;

	@Autowired
	private Pay8583Message pay8583Message;

	@Reference
	private BoCustomerService boCustomerService;
	
	@Autowired
	private DevicePriEncKeyMapper devicePriEncKeyMapper;

	@Reference
	private BoCustomerDeviceService boCustomerDeviceService;

	@Autowired
	private DeviceWkEnckeyMapper deviceWkEnckeyMapper;

	@Autowired
	private InstMapper instMapper;

	@Autowired
	private InstKeyMapper instKeyMapper;

	@Autowired
	private InstMercMapper instMercMapper;

	@Autowired
	private ParCarBinMapper parCarBinMapper;

	@Autowired
	private DealTotalMapper dealTotalMapper;

	@Autowired
	private DealTotalPosMapper dealTotalPosMapper;

	@Reference
	private RouteService routeService;

	@Reference
	private SystemMassageService systemMassageService;

	@Autowired
	private InstResultCodeMapper instResultCodeMapper;
	
	@Autowired
	private PayMishuaMapper payMishuaMapper;
	
	@Autowired
	private PayMiShuaProperties payMishuaProperties; 
	
	@Reference
	private ShenXinAutoPayService shenXinAutoPayService;
	
	@Reference
	private HandpayService handpayService;

	@Reference
	private AgentProfitService agentProfitService;
	
	private Map<String, String> check(String phonenumber, String terminalnumber) {
		Map<String, String> map = new HashMap<>();
		
		BoCustomer bocustomer = new BoCustomer();
		bocustomer.setMobile(phonenumber);
		bocustomer = boCustomerService.findOne(bocustomer);
		if (bocustomer == null) {
			// 商户不存在
			map.put("code", ERROR_CODE);
			map.put("msg", "商户不存在");
			return map;
		}
		if (bocustomer.getState() != 4 && bocustomer.getState() != 5 && bocustomer.getState() != 6
				&& bocustomer.getState() != 7) {
			// 商户未实名认证
			map.put("code", ERROR_CODE);
			map.put("msg", " 商户未实名认证");
			return map;
		}
		
/*		if(bocustomer.getInscode().equals("101004") || bocustomer.getInscode().equals("101003")){
			map.put("code", ERROR_CODE);
			map.put("msg", " 该渠道已停止交易");
			return map;
		}*/
		
		
		
		BoCustomerDevice boCustomerDevice = new BoCustomerDevice();
		boCustomerDevice.setMercId(bocustomer.getMercId());
		boCustomerDevice.setBodyNo(terminalnumber);
		boCustomerDevice = boCustomerDeviceService.findOne(boCustomerDevice);
		if (boCustomerDevice == null) {
			// 设备不属于当前商户
			map.put("code", ERROR_CODE);
			map.put("msg", "设备不属于当前商户");
			return map;
		}

		//获取终端密钥
		DevicePriEncKey devicePriEncKey = devicePriEncKeyMapper.selectByPrimaryKey(terminalnumber);
		if (devicePriEncKey == null) {
			// 终端密钥不存在
			map.put("code", ERROR_CODE);
			map.put("msg", "终端密钥不存在");
			return map;
		}
		
		map.put("sekIndId", devicePriEncKey.getSekId());
		map.put("termTmk", devicePriEncKey.getPriKey());
		map.put("mercId", bocustomer.getMercId());
		map.put("mercName", bocustomer.getMercName());
		map.put("mercNo", bocustomer.getMercNo());
		map.put("deviceIdIn", String.valueOf(boCustomerDevice.getId()));
		map.put("agentId", bocustomer.getAgentId());
		map.put("agentName", bocustomer.getAgentName());
		map.put("feeRate", String.valueOf(bocustomer.getSwingCardCreditRate()));
		map.put("singleFee", String.valueOf(bocustomer.getSwingCardSettleFee()));
		map.put("inscode", bocustomer.getInscode());
		return map;
	}
	
	private boolean tradTimeCheck(){
		try {
			SimpleDateFormat parser = new SimpleDateFormat("HH:mm");
			Date start = parser.parse("9:00");
			Date end = parser.parse("18:00");
			Date currDate = parser.parse(parser.format(new Date()));
			
			if (currDate.after(start) && currDate.before(end)) {
				return true;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	    
		return false;
	}
	
	@Transactional
	@Override
	public SignEntity sgin(SignParam param) {
		SignEntity signEntity = new SignEntity();
		// validate
		Assert.notNull(param);
		Assert.hasText(param.getPHONENUMBER());
		Assert.hasText(param.getPSAMCARDNO());
		ObjectMapper objectMapper = new ObjectMapper();
		
		String paramStr = null;
		try {
			paramStr = objectMapper.writeValueAsString(param);
		} catch (JsonProcessingException e1) {
			logger.error("签到参数转化JSON数据出错！", e1);
		}
		logger.info("签到接口：" + paramStr);
		Map<String, String> map = check(param.getPHONENUMBER(), param.getPSAMCARDNO());
		if (ERROR_CODE.equals(map.get("code"))) {
			signEntity.setRSPCOD(ERROR_CODE);
			signEntity.setRSPMSG(map.get("msg"));
			return signEntity;
		}

		final String sekIndId = map.get("sekIndId");
		final String termTmk = map.get("termTmk");

		List<String> list1 = generateDataKey(Integer.parseInt(sekIndId), termTmk);
		List<String> list2 = generateDataKey(Integer.parseInt(sekIndId), termTmk);

		DeviceWkEnckey deviceWkEnckey = new DeviceWkEnckey();
		deviceWkEnckey.setDeviceNo(param.getPSAMCARDNO());
		deviceWkEnckey.setMercId(map.get("mercId"));
		deviceWkEnckey.setTermPikEncKey(list1.get(0));// pin域本地侧密文
		deviceWkEnckey.setTermMacEncKey(list1.get(3)); // mac域本地侧密文
		deviceWkEnckey.setTermTckEncKey(list2.get(0));// 磁道域本地侧密文

		int count = deviceWkEnckeyMapper.updateWkEncKey(deviceWkEnckey);
		// 不存在就新增记录
		if (count == 0) {
			deviceWkEnckey.setSekId(sekIndId);
			deviceWkEnckey.setCreateTime(new Date());
			deviceWkEnckeyMapper.insert(deviceWkEnckey);
		}

		signEntity.setPINKEY(list1.get(1) + list1.get(2));
		signEntity.setMACKEY(list1.get(4) + list1.get(5));
		signEntity.setENCRYPTKEY(list2.get(1) + list2.get(2));
		signEntity.setPHONENUMBER(param.getPHONENUMBER());
		
		String log = null;
		try {
			log = objectMapper.writeValueAsString(signEntity);
		} catch (JsonProcessingException e) {
			logger.error("签到结果转化JSON数据出错！", e);
		}
		logger.info("签到接口：" + log);
		signEntity.setRSPCOD(SUCCESS_CODE);
		signEntity.setTERMINALNUMBER(param.getPSAMCARDNO());
		signEntity.setRSPMSG("成功");
		return signEntity;
	}

	@Override
	public ConSumptionResult consumption(ConSumptionEntity param) {
		ConSumptionResult result = new ConSumptionResult();
		// validate
		Assert.notNull(param);
		Assert.hasText(param.getPHONENUMBER());
		Assert.hasText(param.getTERMINALNUMBER());
		Assert.hasText(param.getPSAMCARDNO());	//设备号
		Assert.hasText(param.getTTXNDT());
		
		//信息核查并获取商户基本信息
		Map<String, String> map = check(param.getPHONENUMBER(), param.getPSAMCARDNO());
		if (ERROR_CODE.equals(map.get("code"))) {
			result.setRSPCOD(ERROR_CODE);
			result.setRSPMSG(map.get("msg"));
			return result;
		}
		
		//路由, 获取外部商户
		String instMercId = routeService.route(param.getPHONENUMBER(), Double.parseDouble(param.getCTXNAT()));
		if(StringUtils.isEmpty(instMercId)){
			result.setRSPCOD(ERROR_CODE);
			result.setRSPMSG("通道选择异常");
			return result;
		}
		
		//获取签到后的工作密钥
		DeviceWkEnckey deviceWkEnckey = new DeviceWkEnckey();
		deviceWkEnckey.setMercId(map.get("mercId"));
		deviceWkEnckey.setDeviceNo(param.getPSAMCARDNO());
		deviceWkEnckey = deviceWkEnckeyMapper.selectOne(deviceWkEnckey);
		if (deviceWkEnckey == null) {
			// 没有签到
			result.setRSPCOD(ERROR_CODE);
			result.setRSPMSG("该设备没有签到");
			return result;
		}
		
		// 获取机构商户设备信息
		InstMerc instMerc = new InstMerc();
		instMerc.setInstMercId(instMercId);
		instMerc = instMercMapper.selectOne(instMerc);
		if (instMerc == null) {
			result.setRSPCOD(ERROR_CODE);
			result.setRSPMSG("支付渠道信息异常");
			return result;
		}
		
		//生成此通道的下个交易号(db函数生成)
		SeqEntity seqEntity = new SeqEntity();
		seqEntity.setInstCode(instMerc.getInstCode());
		String instSeq = instMapper.instSeqNextval(seqEntity);

		//去通道配置信息(初始化到db)
		Inst inst = instMapper.selectByPrimaryKey(instMerc.getInstCode());
		InstKey instKey = null;
		if (inst.getKeyMode() == 1) { //平台密钥
			instKey = instKeyMapper.selectByPrimaryKey(instMerc.getInstCode());// 获取通道的密钥信息
		} else if (inst.getKeyMode() == 2) { //终端密钥
			Example example = new Example(InstKey.class);
			example.createCriteria().andEqualTo("deviceId", instMerc.getInstDeviceId());
			List<InstKey> instKeyList = instKeyMapper.selectByExample(example);
			if (!instKeyList.isEmpty()) {
				instKey = instKeyList.get(0);
			}
		}
		if (instKey == null) {
			result.setRSPCOD(ERROR_CODE);
			result.setRSPMSG("商户密钥获取异常");
			return result;
		}
		
		String terType = "0";
		// 4.2 解密磁道，国内【磁条卡】不验证3磁道，所以忽略3磁道数据
		String trackStr = null;
		// 根据inmod判断是否是磁条卡还是芯片卡，051是芯片卡
		if ("051".equalsIgnoreCase(param.getInMod())) {
			trackStr = decpyptTrack(param.getTRACK(), deviceWkEnckey);
			terType = "5";
		} else {
			// 如果是磁条卡，手机APP提交得磁道参数是将2磁道和3磁道连接在一起作为一个字段进行提交，所以需截取前48个字符做2磁道解密
			trackStr = decpyptTrack(param.getTRACK().substring(0, 48), deviceWkEnckey);
		}

		param.setCRDNO(trackStr.substring(0, trackStr.indexOf("D")));

		// 4.1 转pin
		String finalPinBlockStr = transPin(param.getTPINBLK(), deviceWkEnckey.getSekId(),
				deviceWkEnckey.getTermPikEncKey(), instKey.getSekId(), instKey.getPinEncKey(), param.getCRDNO(),
				param.getCRDNO());

		ParCardBin parCardBin = new ParCardBin();
		// parCardBin.setCardBin(param.getCrdno().substring(0, 6));
		// parCardBin.setCardLen(param.getCrdno().length() + "");
		Example example = new Example(ParCardBin.class);
		example.createCriteria().andEqualTo("cardBin", param.getCRDNO().substring(0, 6)).andEqualTo("cardLen",
				param.getCRDNO().length());

		RowBounds rowBounds = new RowBounds(0, 1);
		List<ParCardBin> parCardBinList = parCarBinMapper.selectByExampleAndRowBounds(example, rowBounds);
		if (parCardBinList.isEmpty()) {
			result.setRSPCOD(ERROR_CODE);
			result.setRSPMSG("卡bin不存在");
			return result;
		}

		Integer transAmt = new Integer(param.getCTXNAT());
		parCardBin = parCardBinList.get(0);
		
		String cardkind = parCardBin.getCardKindId();
		
		//借记卡刷卡限额不能大于5000
		if("01".equals(cardkind) && transAmt > 500000) {
			result.setRSPCOD(ERROR_CODE);
			result.setRSPMSG("借记卡限额不能超过5000元");
			return result;
		}
		
		String hostTramsSsn = instMapper.transSsnNextval();

		DealTotal dealTotal = new DealTotal();
		dealTotal.setHostTransSsn(hostTramsSsn);
		dealTotal.setPayMode("1");
		dealTotal.setMchntCodeIn(map.get("mercId"));
		dealTotal.setMchntName(map.get("mercName"));
		dealTotal.setMercNo(map.get("mercNo"));
		dealTotal.setMobile(param.getPHONENUMBER());
		dealTotal.setTransTime(new Date());
		
		dealTotal.setTransAmt(transAmt);
		dealTotal.setTransCardNo(param.getCRDNO());
		dealTotal.setDeviceNoIn(param.getPSAMCARDNO());
		dealTotal.setDeviceIdIn(map.get("deviceIdIn"));
		dealTotal.setCardBin(parCardBin.getCardBin());
		dealTotal.setCardKind(cardkind);

		dealTotal.setCardIssId(parCardBin.getIssInsCode());
		dealTotal.setCardIssName(parCardBin.getCardNameCn());

		dealTotal.setTransCode("0200");
		dealTotal.setTransCodeName("消费");
		dealTotal.setTransStatus(1);

		dealTotal.setMchntCodeOut(instMerc.getInstDeviceId());
		dealTotal.setDeviceIdOut(instMerc.getInstDeviceId());
		dealTotal.setDeviceSsnOut(instSeq);
		dealTotal.setRoutInstIdCd(instMerc.getInstCode());

		dealTotal.setAgentId(map.get("agentId"));
		dealTotal.setAgentName(map.get("agentName"));
		dealTotal.setSignPath(param.getELESIGNA());
		dealTotal.setCreateTime(new Date());
		dealTotal.setUpdateTime(new Date());
		dealTotal.setInscode(map.get("inscode"));

		dealTotalMapper.insert(dealTotal);

		// 8583
		TreeMap<Integer, String> filedMap = new TreeMap<>();
		// 域2 入参CRDNO  <!-- 主帐号 -->
		filedMap.put(2, param.getCRDNO());
		// 域4 入参CTXNAT <!-- 交易金额 -->
		filedMap.put(4, param.getCTXNAT());
		
		// 域11 入参TSEQNO param.getTseqno() <!-- 系统跟踪号 -->
		filedMap.put(11, instSeq);	// instSeq

		// 域22 入参InMod  <!-- 服务点输入方式码 -->
		// 芯片卡与磁条卡在此域的方式不同
		if (!"051".equalsIgnoreCase(param.getInMod())) {
			filedMap.put(22, "021");  
		} else {
			filedMap.put(14, param.getCardValidDate());
			filedMap.put(22, param.getInMod());
			// 域23 入参CRDSQN  <!-- 卡片序列号 -->
			filedMap.put(23, param.getCRDSQN());
		}

		//米刷逻辑
		if(DEAL_MISHUA.equalsIgnoreCase(instMerc.getInstCode())){
			// 域31 10000012（米刷机构代码） 10000035 路由结果    <!-- 暂不使用(银联) -->
			filedMap.put(31, instMerc.getInstCode());
			// 域35 4步骤解密后明文  <!-- 第二磁道数据 -->
			trackStr = trackStr.substring(0, 37);
			filedMap.put(35, trackStr);
		}else if(DEAL_SHENXIN.equalsIgnoreCase(instMerc.getInstCode())){ //申鑫逻辑
			String trackKey = decpyptTrackKeyByHsm(instKey.getTckEncKey(),instKey.getSekId());
			trackStr = trackEncrypt(trackStr.substring(0,37),trackKey);
			filedMap.put(35, trackStr);
		}else if(DEAL_JIDIAN.equalsIgnoreCase(instMerc.getInstCode())){	//吉点
			SimpleDateFormat df_time = new SimpleDateFormat("yyyyMMddHHmmss");
			String dt = df_time.format(new Date());
			
			// 域12 <!-- 受卡方所在地时间 -->
			filedMap.put(12, dt.substring(8));
			// 域13 <!-- 受卡方所在地日期 -->
			filedMap.put(13, dt.substring(4,8));
			
			// 域31 10000012（米刷机构代码） 10000035 路由结果    <!-- 暂不使用(银联) -->
			filedMap.put(32, DEAL_JIDIAN);	//instMerc.getInstCode()
						
			// 域35  <!-- 第二磁道数据 -->
			String trackKey = decpyptTrackKeyByHsm(instKey.getTckEncKey(),instKey.getSekId());
			trackStr = CdTool.getTrans(trackStr.substring(0,37), trackKey).toUpperCase();
			filedMap.put(35, trackStr);
			
			// 域48  自定义域
			filedMap.put(48, "00");
			filedMap.put(60, "22" + DateUtil.getDate().substring(2) + "000" + terType + "0");
		}
		
		// 域41 5步骤term_id 01015848 stifeEntity.getTermId() 01069578 路由结果  <!-- 受卡机终端标识码 -->
		filedMap.put(41, instMerc.getInstDeviceId());
		// 域42 800290050650014（大商户号）800290057220244 800290057220557 路由结果  <!-- 受卡方标识码 -->
		filedMap.put(42, instMerc.getInstMercId());

		// 域52 3步骤转换后pinblock  <!-- 个人标识码数据 -->
		filedMap.put(52, finalPinBlockStr);

		// 域55 入参ICDAT  <!-- IC卡数据域 -->
		// 如果是芯片卡则需要输入此域
		if ("051".equalsIgnoreCase(param.getInMod())) {
			filedMap.put(55, param.getICDAT());
		}

		String messageId = null;
		if (DEAL_MISHUA.equals(instMerc.getInstCode())) {
			//域64, 66A7E640A02B75F44C7C7F455FD83137   <!-- MAC报文鉴别码 -->
			filedMap.put(64, instKey.getMacEncKey());
		
			messageId = "mishuai-0200";
		} else if (DEAL_XINKU.equals(instMerc.getInstCode())){
			//域64, 66A7E640A02B75F44C7C7F455FD83137   <!-- MAC报文鉴别码 -->
			filedMap.put(64, instKey.getMacEncKey());
			
			messageId = "xinku-0200";
		} else if(DEAL_SHENXIN.equals(instMerc.getInstCode())){
			//域64, 66A7E640A02B75F44C7C7F455FD83137   <!-- MAC报文鉴别码 -->
			filedMap.put(64, instKey.getMacEncKey());
			
			messageId = "shenxin-0200";
		} else if(DEAL_JIDIAN.equals(instMerc.getInstCode())){
			messageId = "jidian-0200";
			String macKey = decpyptTrackKeyByHsm(instKey.getMacEncKey(), instKey.getSekId());
			
			//域64, 66A7E640A02B75F44C7C7F455FD83137   <!-- MAC报文鉴别码 -->
			filedMap.put(64, macKey);
		}

		byte[] in = requestMiShuai(filedMap, hostTramsSsn, messageId, inst,dealTotal);
		
		result = updateStatus(hostTramsSsn, in, parCardBin, map.get("feeRate"), map.get("singleFee"), inst, param.getTTXNDT());
		logger.info("手刷报文返回结果：" + result);
		if(result!=null && result.getRSPCOD().equalsIgnoreCase(SUCCESS_CODE)) {
			//TODO 计算代理商手刷分润信息
			int i = agentProfitService.saveAgentSwingCardProfit(hostTramsSsn, AgentProfitTransTypeEnum.TRANSTYPE_MPOS.getCode());
		}
		
		//申鑫/吉点打款处理
		if(result!=null && result.getRSPCOD().equalsIgnoreCase(SUCCESS_CODE) &&
				(DEAL_SHENXIN.equals(instMerc.getInstCode()) || DEAL_JIDIAN.equals(instMerc.getInstCode())) ){
			shenXinAutoPayService.autoPay(hostTramsSsn, instMerc.getInstCode());
			
			if(DEAL_JIDIAN.equals(instMerc.getInstCode())){	// 吉点调用T0提现
				WithdrawRequestBean req = new WithdrawRequestBean();
				req.setSubMerId(instMercId);
				
				SimpleDateFormat df_time = new SimpleDateFormat("yyyyMMdd");
				req.setOrderNo(hostTramsSsn);
				req.setTransAmount(" ");	//分
				req.setTransDate(df_time.format(new Date()));
				handpayService.t0Withdraw(req);
			}
		}
		
		//推送系统消息, 手机号
//		logger.info("推送消息,手机号:"+param.getPHONENUMBER());
//		systemMassageService.sendMsgByRole(PageEnum.jysh.toString(),param.getPHONENUMBER());
		
		return result;
	}
    /**
     * 
     * correct:(冲正). <br/>
     *
     * @author 陈勋
     * @param filedMap
     * @param inst
     * @param dealTotal
     * @return
     * @since JDK 1.7
     */
	private boolean correct(TreeMap<Integer, String> filedMap, Inst inst, DealTotal dealTotal) {

		String correctSeq = instMapper.transSsnNextval();
		dealTotal.setHostTransSsn(correctSeq);
		dealTotal.setTransCode("0400");
		dealTotal.setTransCodeName("冲正");
		dealTotalMapper.insert(dealTotal);

		// 8583 冲正
		TreeMap<Integer, String> correctMap = new TreeMap<>();
		// 主帐号
		correctMap.put(2, filedMap.get(2));
		// 交易处理码
		correctMap.put(3, filedMap.get(3));
		// 交易金额
		correctMap.put(4, filedMap.get(4));
		// 系统跟踪号 	  
		correctMap.put(11, filedMap.get(11));
		// 服务点输入方式码
		correctMap.put(22, filedMap.get(22));
		// 卡序列号
		if (!StringUtils.isEmpty(filedMap.get(23))) {
			correctMap.put(23, filedMap.get(23));
		}

		// 服务点条件码
		correctMap.put(25, filedMap.get(25));
		// 服务点PIN获取码
		correctMap.put(26, filedMap.get(26));
		// AMOUNT,SETTLEMENT PROCESSING FEE
		correctMap.put(31, filedMap.get(31));
		// 受理机构标识码
		correctMap.put(32, filedMap.get(32));
		// 受卡机终端标识码
		correctMap.put(41, filedMap.get(41));
		// 受卡方标识码
		correctMap.put(42, filedMap.get(42));
		// 交易货币代码
		correctMap.put(49, filedMap.get(49));
		// 基于PBOC借贷记标准的IC卡数据域
		if (!StringUtils.isEmpty(filedMap.get(55))) {
			correctMap.put(55, filedMap.get(55));
		}

		// 自定义域
		correctMap.put(60, filedMap.get(60));

		correctMap.put(64, filedMap.get(64));
		
		String messageId = null;
		//米刷
		if(DEAL_MISHUA.equalsIgnoreCase(inst.getInstCode())){
			messageId = "mishuai-0400";
		}
		//申鑫
		else if(DEAL_SHENXIN.equalsIgnoreCase(inst.getInstCode())){
			messageId = "shenxin-0400";
		}else{
			throw new RuntimeException("冲正时无对应的报文模板");
		}
		
		byte[] out = pay8583Message.pack8583(filedMap, messageId);
		Socket iSocketHandle = null;
		InputStream iInputStream = null;
		OutputStream iOutputStream = null;
		byte[] in = new byte[HsmConst.SECBUF_MAX_SIZE];

		DealTotalPos dealTotalPos = new DealTotalPos();
		dealTotalPos.setHostTransSsn(correctSeq);
		dealTotalPos.setSend8583(Pay8583Util.bytesToHexString(out));
		dealTotalPos.setCreateTime(new Date());
		dealTotalPosMapper.insert(dealTotalPos);

		try {
			iSocketHandle = new Socket();
			InetSocketAddress hsmAddr = new InetSocketAddress(inst.getInstIp(), inst.getInstPort());
			iSocketHandle.connect(hsmAddr, 50000);
			iSocketHandle.setSoTimeout(60000);
			iInputStream = iSocketHandle.getInputStream();
			iOutputStream = iSocketHandle.getOutputStream();
			logger.info("请求:" + Pay8583Util.bytesToHexString(out));
			iOutputStream.write(out, 0, out.length);
			iInputStream.read(in, 0, HsmConst.SECBUF_MAX_SIZE);
			logger.info("响应:" + Pay8583Util.bytesToHexString(in));
			Map<Integer, String> correctResult = pay8583Message.unpack(in, "mishuai-0410");

			dealTotalPos.setRecieve8583(Pay8583Util.bytesToHexString(in));
			dealTotalPos.setUpdateTime(new Date());
			dealTotalPosMapper.updateByPrimaryKeySelective(dealTotalPos);

			if ("00".equals(correctResult.get(38))) {
				// 冲正成功
				dealTotal.setRespCdLoc("00");
				dealTotal.setRespCdLocDsp("冲正成功");
				dealTotal.setTransStatus(0);
				dealTotalMapper.updateByPrimaryKeySelective(dealTotal);
				return true;
			} else {
				// 冲正失败
				dealTotal.setRespCdLoc(correctResult.get(38));
				dealTotal.setRespCdLocDsp("冲正失败");
				dealTotal.setTransStatus(5);
				dealTotalMapper.updateByPrimaryKeySelective(dealTotal);
			}
		}catch(IOException e) {
			logger.error(e.getMessage());
			logger.error("消费冲正失败");
			dealTotal.setRespCdLoc("-1");
			dealTotal.setRespCdLocDsp("调用通道失败");
			dealTotal.setTransStatus(5);
			dealTotalMapper.updateByPrimaryKeySelective(dealTotal);
		} finally {
			close(iInputStream, iOutputStream, iSocketHandle);
		}

		return false;
	}

	private void close(InputStream iInputStream, OutputStream iOutputStream, Socket iSocketHandle) {

		if (iInputStream != null) {
			try {
				iInputStream.close();
			} catch (Exception e) {
			}
			iInputStream = null;
		}

		if (iOutputStream != null) {
			try {
				iOutputStream.close();
			} catch (Exception e) {
			}
			iOutputStream = null;
		}

		if (iSocketHandle != null) {
			try {
				iSocketHandle.close();
			} catch (Exception e) {
			}
			iSocketHandle = null;
		}

	}

	private byte[] requestMiShuai(TreeMap<Integer, String> filedMap, String hostTramsSsn, String type, Inst inst,
			DealTotal dealTotal)   {
		
		byte[] out = pay8583Message.pack8583(filedMap, type);
		
		//报文持久化
		DealTotalPos dealTotalPos = new DealTotalPos();
		dealTotalPos.setHostTransSsn(hostTramsSsn);
		dealTotalPos.setSend8583(Pay8583Util.bytesToHexString(out));
		dealTotalPos.setCreateTime(new Date());
		dealTotalPosMapper.insert(dealTotalPos);

		Socket iSocketHandle = null;
		InputStream iInputStream = null;
		OutputStream iOutputStream = null;
		byte[] in = new byte[HsmConst.SECBUF_MAX_SIZE];
		try {
			iSocketHandle = new Socket();
			InetSocketAddress hsmAddr = new InetSocketAddress(inst.getInstIp(), inst.getInstPort());
			iSocketHandle.connect(hsmAddr,50000);
			iSocketHandle.setSoTimeout(60000);
			iInputStream = iSocketHandle.getInputStream();
			iOutputStream = iSocketHandle.getOutputStream();
			logger.info("请求:" + Pay8583Util.bytesToHexString(out));
			iOutputStream.write(out, 0, out.length);
			iInputStream.read(in, 0, HsmConst.SECBUF_MAX_SIZE);
			logger.info("响应:" + Pay8583Util.bytesToHexString(in));			
			return in;

		} catch (IOException e) {
			// 冲正
			logger.warn(e.getMessage());
			logger.warn("消费交易失败,开始冲正...");
			int count = 0;
			do {
				if (correct(filedMap, inst, dealTotal)) {
					break;
				}
				count++;
				try {
					Thread.currentThread().sleep(1000 * 60 * count);
				} catch (InterruptedException ingore) {

				}

			} while (count < 3);

		} finally {
			close(iInputStream, iOutputStream, iSocketHandle);
		}
		return in;

	}
	
	
	private byte[] requestMiShuaiDaiFu(TreeMap<Integer, String> filedMap,String messageType)   {

		byte[] out = pay8583Message.pack8583(filedMap, messageType);

		Socket iSocketHandle = null;
		InputStream iInputStream = null;
		OutputStream iOutputStream = null;
		byte[] in = new byte[HsmConst.SECBUF_MAX_SIZE];
		try {
			iSocketHandle = new Socket();
			InetSocketAddress hsmAddr = new InetSocketAddress("117.185.7.189", 6667);
			iSocketHandle.connect(hsmAddr,50000);
			iSocketHandle.setSoTimeout(60000);
			iInputStream = iSocketHandle.getInputStream();
			iOutputStream = iSocketHandle.getOutputStream();
			logger.info("请求:" + Pay8583Util.bytesToHexString(out));
			iOutputStream.write(out, 0, out.length);
			iInputStream.read(in, 0, HsmConst.SECBUF_MAX_SIZE);
			logger.info("响应:" + Pay8583Util.bytesToHexString(in));			
			return in;

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			close(iInputStream, iOutputStream, iSocketHandle);
		}
		return in;
	}

	// 不在事务中
	private ConSumptionResult updateStatus(String hostTramsSsn, byte[] in, ParCardBin parCardBin, String mchntRate,
			String singleFee, Inst inst, String ttxndt) {

		ConSumptionResult returnValue = new ConSumptionResult();
		returnValue.setLOGNO(hostTramsSsn);
		
		//持久化接收报文
		DealTotalPos reciveDealTotalPos = new DealTotalPos();
		reciveDealTotalPos.setHostTransSsn(hostTramsSsn);
		reciveDealTotalPos.setRecieve8583(Pay8583Util.bytesToHexString(in));
		reciveDealTotalPos.setUpdateTime(new Date());
		dealTotalPosMapper.updateByPrimaryKeySelective(reciveDealTotalPos);

		//解包
		Map<Integer, String> result = pay8583Message.unpack(in, "mishuai-0210");
		
		DealTotal reciveDealTotal = new DealTotal();
		reciveDealTotal.setHostTransSsn(hostTramsSsn);
		reciveDealTotal.setUpdateTime(new Date());

		reciveDealTotal.setRespCdLoc(result.get(38));
		reciveDealTotal.setRespCdLocDsp(result.get(55));

		if ("00".equals(result.get(38))) {
			reciveDealTotal.setRespCdLocDsp("交易成功");
			reciveDealTotal.setTransStatus(0);
			reciveDealTotal.setMchntRate(Double.parseDouble(mchntRate));
			reciveDealTotal.setMishuaiSeq(result.get(36));
			double d=Double.parseDouble(StringUtils.isEmpty(singleFee)?"0":singleFee);
			reciveDealTotal.setMchntSingleFee( (int)(d*100));

			if ("01".equals(parCardBin.getCardKindId())) {
				reciveDealTotal.setInstFeeRate(inst.getInstFeeRate());
				reciveDealTotal.setInstFeeMax(inst.getInstFeeMax());
			} else {
				reciveDealTotal.setInstDfeeRate(inst.getInstDfreeRate());
			}
			returnValue.setRSPCOD(SUCCESS_CODE);
			returnValue.setRSPMSG("交易成功");
			returnValue.setTTXNDT(ttxndt);
		} else {
			returnValue.setRSPCOD(ERROR_CODE);
			// 查询相关code对应message提示
			Example instResultCodeExample = new Example(InstResultCode.class);
			instResultCodeExample.createCriteria().andEqualTo("code", result.get(38)).andEqualTo("instCode",
					inst.getInstCode());

			List<InstResultCode> codeList = instResultCodeMapper.selectByExample(instResultCodeExample);
			if (codeList.isEmpty()) {
				returnValue.setRSPMSG("不支持的错误类型");
				reciveDealTotal.setRespCdLocDsp("不支持的错误类型");
			} else {
				returnValue.setRSPMSG(codeList.get(0).getMessage());
				reciveDealTotal.setRespCdLocDsp(codeList.get(0).getMessage());
			}

			reciveDealTotal.setTransStatus(5);
		}

		//更新交易表
		int count = dealTotalMapper.updateByPrimaryKeySelective(reciveDealTotal);
		
		return returnValue;
	}

	/**
	 * 转pin
	 * 
	 * @param crytograph
	 *            pin密文
	 * @param stwekEntity
	 *            密钥类
	 * @param fromNo
	 *            原卡号
	 * @param toNo
	 *            目标卡号
	 * @return
	 */
	private String transPin(final String crytograph, final String sekId1, final String localKey, int sekId2,
			final String insKey, final String fromNo, final String toNo) {
		String command = "0406";// 转pin命令
		String index1 = String.format("%04x", Integer.parseInt(sekId1));// 区索引106，hex化定死

		String localkeyLength = "10";// 长度指标，定死
		// String localKey= =// "08B7E8AF63A1D21E53503932F09841F0"

		String index2 = String.format("%04x", sekId2);// 区索引106，hex化定死

		String inskeyLength = "10";// 长度指标，定死
		// String insKey = "5BF7D38327799C89D071D0C52E7B26CD";

		String pinBlockBefore = "01";// 转前pin格式，定死
		String pinBLockAfter = "01";// 转后pin格式，定死

		String pinBlock = crytograph;// pin密文 B5821A50ABB8D80A

		String cardNoBefore = fromNo;// 卡号 6236681210001162770
		String cardNoBeforeSpan = ";";// 分隔符，定死
		String cardNoAfter = toNo;// 卡号
		String cardNoAfterSpan = ";";// 分隔符，定死

		HsmMessage message = new HsmMessage();
		message.appendHex(command).appendHex(index1).appendHex(localkeyLength).appendHex(localKey).appendHex(index2)
				.appendHex(inskeyLength).appendHex(insKey).appendHex(pinBlockBefore).appendHex(pinBLockAfter)
				.appendHex(pinBlock).appendAsii(cardNoBefore).appendAsii(cardNoBeforeSpan).appendAsii(cardNoAfter)
				.appendAsii(cardNoAfterSpan);

		byte[] bSecBufferOut = hsmTemplate.exec(message);
		byte[] finalPinBlock = new byte[8];
		System.arraycopy(bSecBufferOut, 1, finalPinBlock, 0, finalPinBlock.length);
		String finalPinBlockStr = HsmApp.byte2hexWithoutBlank(finalPinBlock);// 打印转后pin

		return finalPinBlockStr;
	}

	/**
	 * 解密磁道
	 * 
	 * @param cryptograph
	 *            磁道密文
	 * @param stwekEntity
	 *            密钥类
	 * @return
	 */
	private String decpyptTrack(final String cryptograph, final DeviceWkEnckey stwekEntity) {

		String command2 = "71";// 磁道解密命令
		String index3 = String.format("%04x", Integer.parseInt(stwekEntity.getSekId()));// 区索引106，hex化定死

		String trackKey = stwekEntity.getTermTckEncKey();// 本地侧磁道密钥
															// 4AAA1497823E00FFA6CCC5A258785FD4
		String originVector = "0000000000000000";// 偏移量，定死
		String encDecFlg = "0000";// 加密算法，定死
		String mes = cryptograph;// 磁道密文
									// ABED764B4DE80BD43FE2B1E100A7312770B8D0F5BEA81BF7

		String mesLengthStr = String.format("%04x", mes.length() / 2);// 磁道密文长度，由mes得到，hex化

		HsmMessage message2 = new HsmMessage();
		message2.appendHex(command2).appendHex(index3).appendHex(trackKey).appendHex(originVector).appendHex(encDecFlg)
				.appendHex(mesLengthStr).appendHex(mes);

		byte[] bSecBufferOut = hsmTemplate.exec(message2);
		byte high = bSecBufferOut[1];
		byte low = bSecBufferOut[2];
		int length = high * 256 + low;
		byte[] trackVal = new byte[length];
		System.arraycopy(bSecBufferOut, 3, trackVal, 0, trackVal.length);
		String trackStr = HsmApp.byte2hexWithoutBlank(trackVal);// 磁道明文
		logger.info("trackVal----length---" + length);

		return trackStr;
	}
	
	/**
	 * 解密加密机保护的明文
	 * @param cryptograph
	 * @param stwekEntity
	 * @return
	 */
	private String decpyptTrackKeyByHsm(final String cryptograph, final int idx) {

		String command = "72";// 磁道解密命令
		String index = String.format("%04x", idx);// 区索引106，hex化定死

		String originVector = "0000000000000000";// 偏移量，定死
		String encDecFlg = "0000";// 加密算法，定死
		String mes = cryptograph;// 磁道密文
		String mesLengthStr = String.format("%04x", mes.length() / 2);// 磁道密文长度，由mes得到，hex化

		HsmMessage message = new HsmMessage();
		message.appendHex(command)
				.appendHex(index)
				.appendHex(originVector)
				.appendHex(encDecFlg)
				.appendHex(mesLengthStr)
				.appendHex(mes);

		byte[] bSecBufferOut = hsmTemplate.exec(message);
		byte high = bSecBufferOut[1];
		byte low = bSecBufferOut[2];
		int length = high * 256 + low;
		byte[] trackVal = new byte[length];
		System.arraycopy(bSecBufferOut, 3, trackVal, 0, trackVal.length);
		String trackStr = HsmApp.byte2hexWithoutBlank(trackVal);// 磁道明文
		logger.info("TrackKey----length---" + length);

		return trackStr;
	}

	public static void main(String[] args) {
		/*try {
			System.out.println(new String("龚贇".getBytes("utf8"),"gbk"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		/*String str = "3E61F5293B1583B807FAFCD77571C141CDBBFF0B63099272CBC616B35A7DEB9D484B69CF4A35C6F55EE8FF40AFA39484581E199C2AB2E39C80FCA5ED";
		System.out.println(str.substring(0, 40));
		System.out.println(str.substring(40, 80));
		System.out.println(str.substring(80, 120));
		
		System.out.println(str.substring(0, 40).substring(0, 32));*/
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
		System.out.println(sdf.format(new Date()));
	}
	
	@Override
	public ResultVal<String> miShuaPay(MiShuaPayParam param) {
		Assert.notNull(param);
		Assert.hasText(param.getCardNo());
		Assert.hasText(param.getBlankNo());
		Assert.hasText(param.getMoney());
		Assert.hasText(param.getName());
		Assert.hasText(param.getTranSsn());
		
		//通过ssn号获取8583报文记录，解析记录获取到交易序列号
		DealTotalPos dtp = dealTotalPosMapper.selectByPrimaryKey(param.getTranSsn());
		if(dtp==null){
			throw new RuntimeException("找不到代付所需原始交易的8583报文");
		}
		
		byte[] outBytes = new byte[dtp.getRecieve8583().length()/2];
		try {
			HsmApp.Str2Hex(dtp.getRecieve8583().getBytes("GBK"), outBytes, dtp.getRecieve8583().length());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		Map<Integer, String> result = pay8583Message.unpack(outBytes, "mishuai-0210");
		String seqNo = null;
		if ("00".equals(result.get(38))) {
			seqNo = result.get(36);
		}else{
			throw new RuntimeException("不成功的交易无法代付：ssn["+param.getTranSsn()+"]result["+result.get(38)+"]");
		}
		
		//组织代付8583报文进行提交
		TreeMap<Integer, String> filedMap = new TreeMap<>();
		// 主账号
		filedMap.put(2, param.getCardNo());
		// 交易处理码
		filedMap.put(3, "290000");
		// 交易金额
		filedMap.put(4, param.getMoney());
		
		// 系统跟踪号
		SeqEntity seqEntity = new SeqEntity();
		seqEntity.setInstCode(payMishuaProperties.getInstcode());
		String instSeq = instMapper.instSeqNextval(seqEntity);
		filedMap.put(11, instSeq);
		
		// AMOUNT,SETTLEMENT
		filedMap.put(31, payMishuaProperties.getInstcode());
		// 受卡机终端标识码
		filedMap.put(41, payMishuaProperties.getDeviceid());
		// 受卡方标识码
		filedMap.put(42, payMishuaProperties.getMercid());
		// 联行行号
		filedMap.put(44, param.getBlankNo());
		// 转入账户名称
		filedMap.put(56, param.getName());
		
		// 自定义域
		String batchNo = (new SimpleDateFormat("yyMMdd")).format(new Date());
		filedMap.put(60, "90"+batchNo);

		filedMap.put(63, seqNo);
		
		InstKey instKey = instKeyMapper.selectByPrimaryKey(payMishuaProperties.getInstcode());
		if(instKey==null){
			throw new RuntimeException("代付获取密钥失败:"+payMishuaProperties.getInstcode());
		}
		filedMap.put(64,instKey.getMacEncKey());
		
		byte[] out = pay8583Message.pack8583(filedMap, "mishuai-daifu");
		
		//发送前插入记录
		PayMishua pm = new PayMishua();
		pm.setDealSsn(dtp.getHostTransSsn());
		pm.setPaySsn(instSeq);
		pm.setPaySend(HsmApp.byte2hexWithoutBlank(out));
		pm.setCreateTime(new Date());
		pm.setUpdateTime(new Date());
		payMishuaMapper.insert(pm);

		Socket iSocketHandle = null;
		InputStream iInputStream = null;
		OutputStream iOutputStream = null;
		byte[] in = new byte[HsmConst.SECBUF_MAX_SIZE];
		try {
			iSocketHandle = new Socket();
			InetSocketAddress hsmAddr = new InetSocketAddress(payMishuaProperties.getHost(),payMishuaProperties.getPort());
			iSocketHandle.connect(hsmAddr,50000);
			iSocketHandle.setSoTimeout(60000);
			iInputStream = iSocketHandle.getInputStream();
			iOutputStream = iSocketHandle.getOutputStream();
			logger.info("请求:" + Pay8583Util.bytesToHexString(out));
			iOutputStream.write(out, 0, out.length);
			iInputStream.read(in, 0, HsmConst.SECBUF_MAX_SIZE);
			logger.info("响应:" + Pay8583Util.bytesToHexString(in));			
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			close(iInputStream, iOutputStream, iSocketHandle);
		}
		
		//返回后更新记录
		pm.setPayRecieve(HsmApp.byte2hexWithoutBlank(in));
		int count = payMishuaMapper.updatePayMishuaRecieve(pm);
		if(count<0){
			throw new RuntimeException(
				"未更新到代付报文发出记录:dealSsn["+pm.getDealSsn()+"]|paySsn["+pm.getPaySsn()+"]");
		}
		
		//解析代付8583报文，获取代付结果
		result = pay8583Message.unpack(in,"mishuai-0210");
		if("00".equals(result.get(38))){
			return ResultVal.SUCCESS(instSeq);
		}else{
			Example instResultCodeExample = new Example(InstResultCode.class);
			instResultCodeExample.createCriteria().
				andEqualTo("code", result.get(38)).
				andEqualTo("instCode",payMishuaProperties.getInstcode());
			List<InstResultCode> codeList = instResultCodeMapper.selectByExample(instResultCodeExample);
			
			if (codeList.isEmpty()){
				return ResultVal.FAIL(-1,"不支持的错误类型",result.get(38));
			} else {
				return ResultVal.FAIL(-1,codeList.get(0).getMessage(), result.get(38));
			}
		}
	}

	@Override
	public ResultVal<String> miShuaPayQuery(MiShuaPayQueryParam param) {
		//查找原始的代付报文记录
		Example payMishuaExample = new Example(PayMishua.class);
		payMishuaExample.createCriteria().
			andEqualTo("dealSsn",param.getDealSsn()).
			andEqualTo("paySsn",param.getPaySsn());
		List<PayMishua> payMishuaList = payMishuaMapper.selectByExample(payMishuaExample);
		PayMishua pm = null;
		if(payMishuaList.isEmpty()){
			throw new RuntimeException("找不到原始的代付报文：dealssn["+param.getDealSsn()+"]payssn["+param.getPaySsn()+"]");
		}else{
			pm = payMishuaList.get(0);
		}
		
		byte[] outBytes = new byte[pm.getPayRecieve().length()/2];
		try {
			HsmApp.Str2Hex(pm.getPayRecieve().getBytes("GBK"), outBytes,pm.getPayRecieve().length());
		}catch(UnsupportedEncodingException e){
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		Map<Integer, String> result = pay8583Message.unpack(outBytes, "mishuai-0210");
		String orginCardNo = null;
		String orginBatchNo = null;
		String orginSeqNo = null;
		if ("00".equals(result.get(38))){
			orginCardNo = result.get(1);
			orginSeqNo = result.get(10);
			orginBatchNo = result.get(59).substring(2, 8);
		}else{
			throw new RuntimeException("不成功的代付无法查询：dealssn["+param.getDealSsn()+"]payssn["+param.getPaySsn()+"]result["+result.get(38)+"]");
		}
		
		TreeMap<Integer, String> filedMap = new TreeMap<>();
		// 2主账号
		filedMap.put(2, orginCardNo);
		// 3交易处理码
		filedMap.put(3, "290000");
		// 11系统跟踪号
		//String seq = instMapper.transSsnNextval();
		SeqEntity seqEntity = new SeqEntity();
		seqEntity.setInstCode(payMishuaProperties.getInstcode());
		String instSeq = instMapper.instSeqNextval(seqEntity);
		filedMap.put(11, instSeq);
		
		// 31机构号
		filedMap.put(31, payMishuaProperties.getInstcode());
		
		// 41受卡机终端标识码
		filedMap.put(41, payMishuaProperties.getDeviceid());
		
		// 42受卡方标识码
		filedMap.put(42, payMishuaProperties.getMercid());

		// 自定义域
		String batchNo = (new SimpleDateFormat("yyMMdd")).format(new Date());
		filedMap.put(60,"91"+batchNo);
		
		// 原始信息域
		filedMap.put(61,orginBatchNo+orginSeqNo);

		InstKey instKey = instKeyMapper.selectByPrimaryKey(payMishuaProperties.getInstcode());
		if(instKey==null){
			throw new RuntimeException("代付获取密钥失败:"+payMishuaProperties.getInstcode());
		}
		filedMap.put(64,instKey.getMacEncKey());
		
		
		byte[] out = pay8583Message.pack8583(filedMap,"mishuai-daifu-chaxun");
		
		pm.setDealSsn(param.getDealSsn());
		pm.setPaySsn(param.getPaySsn());
		pm.setQuerySsn(instSeq);
		pm.setQuerySend(Pay8583Util.bytesToHexString(out));
		payMishuaMapper.updatePayMishuaQuery(pm);
		
		Socket iSocketHandle = null;
		InputStream iInputStream = null;
		OutputStream iOutputStream = null;
		byte[] queryOut = new byte[HsmConst.SECBUF_MAX_SIZE];
		try {
			iSocketHandle = new Socket();
			InetSocketAddress hsmAddr = new InetSocketAddress(payMishuaProperties.getHost(),payMishuaProperties.getPort());
			iSocketHandle.connect(hsmAddr,50000);
			iSocketHandle.setSoTimeout(60000);
			iInputStream = iSocketHandle.getInputStream();
			iOutputStream = iSocketHandle.getOutputStream();
			logger.info("请求:" + Pay8583Util.bytesToHexString(out));
			iOutputStream.write(out, 0, out.length);
			iInputStream.read(queryOut, 0, HsmConst.SECBUF_MAX_SIZE);
			logger.info("响应:" + Pay8583Util.bytesToHexString(queryOut));			
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			close(iInputStream, iOutputStream, iSocketHandle);
		}
		
		pm.setQueryRecieve(Pay8583Util.bytesToHexString(queryOut));
		payMishuaMapper.updatePayMishuaQueryRecieve(pm);
		
		//解析代付查询8583报文，获取代付查询结果
		result = pay8583Message.unpack(queryOut,"mishuai-0210");
		if("00".equals(result.get(38))){
			String realResult = result.get(62);
			String statusResult = realResult.substring(0, 2);
			String failReason = realResult.substring(2,4);
			if(statusResult.equalsIgnoreCase("00")){
				return ResultVal.SUCCESS("代付成功", "00");
			}else
			if(statusResult.equalsIgnoreCase("01")){
				return ResultVal.SUCCESS("待查证", "01");
			}else{
				String reason = null;
				if(failReason.equalsIgnoreCase("01")){
					reason = "户名错误";
				}else
				if(failReason.equalsIgnoreCase("02")){
					reason = "账号错误";
				}else
				if(failReason.equalsIgnoreCase("03")){
					reason = "状态不正常";
				}else
				if(failReason.equalsIgnoreCase("06")){
					reason = "收款行行号错误";
				}else
				if(failReason.equalsIgnoreCase("07")){
					reason = "账户类型不支持";
				}else
				if(failReason.equalsIgnoreCase("08")){
					reason = "交易金额超限";
				}else
				if(failReason.equalsIgnoreCase("09")){
					reason = "收款行处理失败";
				}else
				if(failReason.equalsIgnoreCase("16")){
					reason = "账户不允许交易";
				}else{
					reason = "未知错误";
				}
				return ResultVal.SUCCESS("["+failReason+"]"+reason, "02");
			}
		}else{
			return ResultVal.FAIL(-1, "代付查询失败", result.get(38));
		}
	}

	public String[] generateTermianlKey() {
		try {
			String command = "0420";
			String index1 = String.format("%04x", 106);
			String index2 = String.format("%04x", 101);
			String keyLength = String.format("%02x", 1);

			HsmMessage message = new HsmMessage();
			message.appendHex(command).appendHex(index1).appendHex(index2).appendHex(keyLength);

			byte[] bSecBufferOut = hsmTemplate.exec(message);

			if (bSecBufferOut[0] == 0x41) {
				byte[] bKeyUndZMK1 = new byte[24];
				byte[] bKeyUndZMK2 = new byte[24];
				byte[] bKeyCheckValue = new byte[8];
				System.arraycopy(bSecBufferOut, 1, bKeyUndZMK1, 0, 16);
				System.arraycopy(bSecBufferOut, 1 + 16, bKeyUndZMK2, 0, 16);
				System.arraycopy(bSecBufferOut, 33, bKeyCheckValue, 0, 8);
				/*
				 * System.out.println("1:"+HsmApp.byte2hexWithoutBlank(
				 * bKeyUndZMK1));
				 * System.out.println("2:"+HsmApp.byte2hexWithoutBlank(
				 * bKeyUndZMK2));
				 * System.out.println("3:"+HsmApp.byte2hexWithoutBlank(
				 * bKeyCheckValue));
				 */
				String[] str = new String[] { HsmUtil.BinToHex(bKeyUndZMK1, 0, 16),
						HsmUtil.BinToHex(bKeyUndZMK2, 0, 16), HsmUtil.BinToHex(bKeyCheckValue, 0, 8) };
				return str;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private List<String> generateDataKey(int sek_ind_id, String term_tmk) {

		String command = "0421";

		String index1 = String.format("%04x", sek_ind_id);
		String index2 = String.format("%04x", sek_ind_id);
		String keyLength1 = String.format("%02x", 1);
		String keyLength2 = String.format("%02x", 0);
		String keyLength3 = String.format("%02x", 1);

		HsmMessage message = new HsmMessage();
		message.appendHex(command).appendHex(index1).appendHex(index2).appendHex(keyLength1).appendHex(keyLength2)
				.appendHex(keyLength3).appendHex(term_tmk);

		byte[] bSecBufferOut = hsmTemplate.exec(message);

		byte[] pinKeyTerminal = new byte[16];
		byte[] pinCheckValue = new byte[8];
		byte[] pinKeyLocal = new byte[16];
		byte[] macKeyTerminal = new byte[8];
		byte[] macCheckValue = new byte[8];
		byte[] macKeyLocal = new byte[8];

		int index = 1;
		System.arraycopy(bSecBufferOut, 1, pinKeyLocal, 0, pinKeyLocal.length);
		index += pinKeyLocal.length;
		System.arraycopy(bSecBufferOut, index, pinKeyTerminal, 0, pinKeyTerminal.length);
		index += pinKeyTerminal.length;
		System.arraycopy(bSecBufferOut, index, pinCheckValue, 0, pinCheckValue.length);
		index += pinCheckValue.length;

		System.arraycopy(bSecBufferOut, index, macKeyLocal, 0, macKeyLocal.length);
		index += macKeyLocal.length;
		System.arraycopy(bSecBufferOut, index, macKeyTerminal, 0, macKeyTerminal.length);
		index += macKeyTerminal.length;
		System.arraycopy(bSecBufferOut, index, macCheckValue, 0, macCheckValue.length);
		index += macCheckValue.length;

		String tmp1 = HsmApp.byte2hexWithoutBlank(pinCheckValue);
		String tmp2 = HsmApp.byte2hexWithoutBlank(macCheckValue);

		List<String> list = new ArrayList<String>();
		list.add(HsmApp.byte2hexWithoutBlank(pinKeyLocal));
		list.add(HsmApp.byte2hexWithoutBlank(pinKeyTerminal));
		list.add(tmp1.substring(0, tmp1.length() / 2));
		list.add(HsmApp.byte2hexWithoutBlank(macKeyLocal));
		list.add(HsmApp.byte2hexWithoutBlank(macKeyTerminal));
		list.add(tmp2.substring(0, tmp2.length() / 2));
		return list;
	}

	private String trackEncrypt(final String data,final String key){
		StringBuilder sb = new StringBuilder(data);
		int covers = sb.length()%16;
		for(int i=0;i<16-covers;i++){
			sb.append("F");
		}
//		String[] result = new String[sb.length()/16];
		StringBuilder result = new StringBuilder();
		int length = sb.length()/16;
		for(int i=0;i<length;i++){
			String tdb2n = sb.substring(0, 16);
//			System.out.println(tdb2n);
//			result[i]=ThreeDes.encrypt(key,tdb2n).substring(0, 16);
			result.append(ThreeDes.encrypt(key,tdb2n).substring(0, 16));
			sb.delete(0, 16);
		}
		return result.toString();
	}

	@Override
	public void signToInst(String instCode,String mercId) {
		//获取机构对象
		Inst inst = instMapper.selectByPrimaryKey(instCode);
		if(inst==null){
			throw new RuntimeException("获取机构失败："+instCode);
		}
		
		//获取机构主密钥
		InstKey instKey = null;
		if (inst.getKeyMode() == 1) {	//平台密钥
			instKey = instKeyMapper.selectByPrimaryKey(inst.getInstCode());
			
			if(instKey == null){
				throw new RuntimeException("通过instCode获取主密钥失败"+instCode);
			}
			
		} else if (inst.getKeyMode() == 2) {	//终端密钥
			// instKey=new InstKey();
			// instKey.setDeviceId(instMerc.getInstDeviceId());
			Example example = new Example(InstKey.class);
			example.createCriteria().andEqualTo("deviceId", mercId);
			List<InstKey> instKeyList = instKeyMapper.selectByExample(example);
			if (!instKeyList.isEmpty()) {
				instKey = instKeyList.get(0);
			}
			
			if(instKey == null){
				throw new RuntimeException("通过mercId获取主密钥失败"+instCode);
			}
		}
		
		
		//发送8583报文向机构签到
		TreeMap<Integer, String> filedMap = new TreeMap<>();
		if(DEAL_SHENXIN.equalsIgnoreCase(instCode)){
			filedMap.put(11, "000100"); // <!-- 系统跟踪号 -->
			filedMap.put(32, inst.getInstCode()); //<!-- 受理方标识码 -->
			filedMap.put(60, "00000000004");	// <!-- 自定义域 -->
		}else{
			filedMap.put(11, "000001"); // <!-- 系统跟踪号 -->
			filedMap.put(32, "30100000001"); //<!-- 受理方标识码 -->
			filedMap.put(41, instCode.trim());	// <!-- 受卡机终端标识码 -->  终端代码 
			filedMap.put(42, mercId.trim());	// <!-- 受卡方标识码 --> 商户代码 
			filedMap.put(48, "00");	// <!-- 行业特定信息 --> 渠道信息
		}
		
		byte[] out = pay8583Message.pack8583_sign(filedMap,inst.getTpdu(),inst.getMsgHead(),"0800");
		
		Socket iSocketHandle = null;
		InputStream iInputStream = null;
		OutputStream iOutputStream = null;

		byte[] in = null;
		try {
			iSocketHandle = new Socket();
			InetSocketAddress hsmAddr = new InetSocketAddress(inst.getInstIp(), inst.getInstPort());
			iSocketHandle.connect(hsmAddr,50000);
			iSocketHandle.setSoTimeout(60000);
			iInputStream = iSocketHandle.getInputStream();
			iOutputStream = iSocketHandle.getOutputStream();
			logger.info("签到请求:" + Pay8583Util.bytesToHexString(out));
			iOutputStream.write(out, 0, out.length);
			
			byte[] sizeByteArray = new byte[2];
			iInputStream.read(sizeByteArray, 0, 2);
			int size = Integer.parseInt(Pay8583Util.bytesToHexString(sizeByteArray),16);
			
			byte[] intemp = new byte[size];
			iInputStream.read(intemp, 0, size);
			
			logger.info("签到响应:" + Pay8583Util.bytesToHexString(intemp));
			
			in = new byte[size+2];
			System.arraycopy(sizeByteArray,0,in,0,2);
			System.arraycopy(intemp,0,in,2,size);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (iInputStream != null) {
				try {
					iInputStream.close();
				} catch (Exception e) {
				}
				iInputStream = null;
			}
			if (iOutputStream != null) {
				try {
					iOutputStream.close();
				} catch (Exception e) {
				}
				iOutputStream = null;
			}
			if (iSocketHandle != null) {
				try {
					iSocketHandle.close();
				} catch (Exception e) {
				}
				iSocketHandle = null;
			}
		}
		Map<Integer, String> result = pay8583Message.unpack(in, "mishuai-0210"); //mishuai-0210
		if("00".equals(result.get(38))){
			logger.info("签到成功,62域返回:"+result.get(61));
		}else{
			logger.info("签到失败："+result.get(38));
			throw new RuntimeException("签到失败，39域返回:"+result.get(38));
		}
		
		//使用主密钥解密签到返回工作密钥
		String eKeys = result.get(61);
		
		String ePinKey = eKeys.substring(0, 40).substring(0,32); 
		String eMakKey = eKeys.substring(40,80).substring(0,32);
		String eTdKey = eKeys.substring(80,120).substring(0,32);
		
		logger.info("主密钥为:"+instKey.getPriEncKey());
		logger.info("Pin密钥密文:"+ePinKey);
		logger.info("Mak密钥密文:"+eMakKey);
		logger.info("Td密钥密文:"+eTdKey);
		
		String dPinKey = ThreeDes.decrypt(instKey.getPriEncKey(), ePinKey);
		String dMakKey = ThreeDes.decrypt(instKey.getPriEncKey(), eMakKey);
		String dTdKey = ThreeDes.decrypt(instKey.getPriEncKey(), eTdKey);
		
		logger.info("Pin密钥明文:"+dPinKey);
		logger.info("Mak密钥明文:"+dMakKey);
		logger.info("Td密钥明文:"+dTdKey);
		
		//调用加密机保护工作密钥
		String hsmPinKey = encryptByHsm(instKey.getSekId(),dPinKey);
		String hsmMakKey = encryptByHsm(instKey.getSekId(),dMakKey);
		String hsmTdKey = encryptByHsm(instKey.getSekId(), dTdKey); 
		
		logger.info("加密保护Pin密钥:"+hsmPinKey);
		logger.info("加密保护Mak密钥:"+hsmMakKey);
		logger.info("加密保护Td密钥:"+hsmTdKey);
		
		//保存工作密钥密文
		InstKey ik = new InstKey();
		ik.setInstCode(instCode);
		ik.setPinEncKey(hsmPinKey);
		ik.setMacEncKey(hsmMakKey);
		ik.setTckEncKey(hsmTdKey);
		int updated = instKeyMapper.updateInstKey(ik);
		if(updated<=0){
			throw new RuntimeException("更新通道工作密钥失败:"+ik.getInstCode());
		}
	}
	
	private String encryptByHsm(int sekId,String data){
		String command = "72";//磁道解密命令
		String index = String.format("%04x", sekId);//区索引106，hex化定死
		String originVector="0000000000000000";//偏移量，定死
		String encDecFlg="0100";//加密算法，定死
		String mes=data;
		String mesLengthStr=String.format("%04x", mes.length()/2);//磁道密文长度，由mes得到，hex化
		
		HsmMessage message = new HsmMessage();
		message.appendHex(command)
				.appendHex(index)
				.appendHex(originVector)
				.appendHex(encDecFlg)
				.appendHex(mesLengthStr)
				.appendHex(mes);
		
		byte[] bSecBufferOut  = hsmTemplate.exec(message);
		if(bSecBufferOut[0]==0x41){
			byte high = bSecBufferOut[1];
			byte low = bSecBufferOut[2];
			int length = high*256+low;
//			System.out.println(length);//输出长度
			byte[] trackVal = new byte[length];
			System.arraycopy(bSecBufferOut, 3, trackVal, 0, trackVal.length);
//			System.out.println(HsmApp.byte2hexWithoutBlank(trackVal));//输出磁道明文
			return HsmApp.byte2hexWithoutBlank(trackVal);
		}else{
			throw new RuntimeException("调用加密机保护密钥失败:"+data);
		}
	}

	@Override
	public boolean signToJidian(String mercId, String mercCode) {
		//获取机构对象
		Inst inst = instMapper.selectByPrimaryKey(DEAL_JIDIAN);
		if(inst==null){
			throw new RuntimeException("获取机构失败："+DEAL_JIDIAN);
		}
		
		//发送8583报文向机构签到
		TreeMap<Integer, String> filedMap = new TreeMap<>();
		filedMap.put(11, String.valueOf((int)(Math.random()*9+1)*100000)); //6位,  <!-- 系统跟踪号 -->
		filedMap.put(32, DEAL_JIDIAN); //<!-- 受理方标识码 -->
		filedMap.put(41, mercCode);	// <!-- 受卡机终端标识码 -->  终端代码  90006068
		filedMap.put(42, mercId);	// <!-- 受卡方标识码 --> 商户代码 880293829109865
		filedMap.put(48, "00");	// <!-- 行业特定信息 --> 渠道信息
		
		byte[] out = pay8583Message.pack8583_sign(filedMap,inst.getTpdu(),inst.getMsgHead(),"0800");
		
		Socket iSocketHandle = null;
		InputStream iInputStream = null;
		OutputStream iOutputStream = null;
		
		byte[] in = null;
		try {
			iSocketHandle = new Socket();
			InetSocketAddress hsmAddr = new InetSocketAddress(inst.getInstIp(), inst.getInstPort());
			iSocketHandle.connect(hsmAddr,50000);
			iSocketHandle.setSoTimeout(60000);
			iInputStream = iSocketHandle.getInputStream();
			iOutputStream = iSocketHandle.getOutputStream();
			logger.info("签到请求:" + Pay8583Util.bytesToHexString(out));
			iOutputStream.write(out, 0, out.length);
			
			byte[] sizeByteArray = new byte[2];
			iInputStream.read(sizeByteArray, 0, 2);
			int size = Integer.parseInt(Pay8583Util.bytesToHexString(sizeByteArray),16);
			
			byte[] intemp = new byte[size];
			iInputStream.read(intemp, 0, size);
			
			logger.info("签到响应:" + Pay8583Util.bytesToHexString(intemp));
			
			in = new byte[size+2];
			System.arraycopy(sizeByteArray,0,in,0,2);
			System.arraycopy(intemp,0,in,2,size);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (iInputStream != null) {
				try {
					iInputStream.close();
				} catch (Exception e) {
				}
				iInputStream = null;
			}
			if (iOutputStream != null) {
				try {
					iOutputStream.close();
				} catch (Exception e) {
				}
				iOutputStream = null;
			}
			if (iSocketHandle != null) {
				try {
					iSocketHandle.close();
				} catch (Exception e) {
				}
				iSocketHandle = null;
			}
		}
		Map<Integer, String> result = pay8583Message.unpack(in, "jidian-0210"); //mishuai-0210
		if("00".equals(result.get(38))){
			logger.info("签到成功,62域返回:"+result.get(61));
		}else{
			logger.info("签到失败："+result.get(38));
			throw new RuntimeException("签到失败，39域返回:"+result.get(38));
		}
		
		//使用主密钥解密签到返回工作密钥
		String eKeys = result.get(61);
		
		//主密钥和工作密钥密文
		String eMasKey = eKeys.substring(0, 40).substring(0,32); 
		String ePinKey = eKeys.substring(40,80).substring(0,32);
		String eMakKey = eKeys.substring(80,120).substring(0,32);
		String eTdKey = eKeys.substring(120,160).substring(0,32);
		
		//主密钥和工作密钥的校验值
		String eMasKey_check = eKeys.substring(0, 40).substring(32,40); 
		String ePinKey_check = eKeys.substring(40,80).substring(32,40);
		String eMakKey_check = eKeys.substring(80,120).substring(32,40);
		String eTdKey_check = eKeys.substring(120,160).substring(32,40);
		
		logger.info("初始密钥为:" + DEAL_JIDIAN_KEY);
		logger.info("eMas主密钥密文:"+ eMasKey + ", 校验值为:"+eMasKey_check);
		logger.info("Pin密钥密文:"+ ePinKey + ", 校验值为:"+ ePinKey_check);
		logger.info("Mak密钥密文:"+ eMakKey  + ", 校验值为:"+ eMakKey_check);
		logger.info("Td密钥密文:"+ eTdKey  + ", 校验值为:"+ eTdKey_check);
		
		try {
			String key = "0000000000000000";
			//通过初始密钥解密到主密钥明文, 并校验
			String dMasKey = CodingUtil.byteArrayToHexString(DesUtil.decode2(eMasKey, DEAL_JIDIAN_KEY)).toUpperCase();
			if(!EncryptUtils.encrypt2(key, dMasKey).substring(0,8).equals(eMasKey_check)){
				logger.info("主密钥校验失败");
				return false;
			}
			
			//通过主密钥明文解密到工作密钥明文, 并校验
			String dPinKey = CodingUtil.byteArrayToHexString(DesUtil.decode2(ePinKey, dMasKey)).toUpperCase();
			if(!EncryptUtils.encrypt2(key, dPinKey).substring(0,8).equals(ePinKey_check)){
				logger.info("Pin密钥校验失败");
				return false;
			}
			
			String dMakKey = CodingUtil.byteArrayToHexString(DesUtil.decode2(eMakKey, dMasKey)).toUpperCase();
			if(!EncryptUtils.encrypt2(key, dMakKey).substring(0,8).equals(eMakKey_check)){
				logger.info("Mak密钥校验失败");
				return false;
			}
			
			String dTdKey = CodingUtil.byteArrayToHexString(DesUtil.decode2(eTdKey, dMasKey)).toUpperCase();
			if(!EncryptUtils.encrypt2(key, dTdKey).substring(0,8).equals(eTdKey_check)){
				logger.info("Td密钥校验失败");
				return false;
			}
			
			logger.info("eMas主密钥明文:"+dMasKey);
			logger.info("Pin密钥明文:"+dPinKey);
			logger.info("Mak密钥明文:"+dMakKey);
			logger.info("Td密钥明文:"+dTdKey);
			
			
			//调用加密机保护工作密钥
			String hsmPinKey = encryptByHsm(106, dPinKey);
			String hsmMakKey = encryptByHsm(106, dMakKey);
			String hsmTdKey = encryptByHsm(106, dTdKey); 
			logger.info("加密保护Pin密钥:"+hsmPinKey);
			logger.info("加密保护Mak密钥:"+hsmMakKey);
			logger.info("加密保护Td密钥:"+hsmTdKey);
			
			
			//保存工作密钥密文
			InstKey ik = new InstKey();
			ik.setInstCode(DEAL_JIDIAN);
			ik.setDeviceId(mercCode);
			ik.setSekId(106);
			ik.setPriEncKey(DEAL_JIDIAN_KEY);

			ik.setPinEncKey(hsmPinKey);
			ik.setMacEncKey(hsmMakKey);
			ik.setTckEncKey(hsmTdKey);
			
			ik.setCreateTime(new Date());
			ik.setUpdateTime(new Date());
			int insert = instKeyMapper.insert(ik);
			if(insert<=0){
				throw new RuntimeException("更新通道工作密钥失败:"+ik.getInstCode());
			}
		} catch (Exception e) {
			logger.error("吉点商户签到失败");
			logger.error(e.toString());
			return false;
		}
		
		return true;
	}

	@Override
	public ConSumptionResult consumptionToJidian(ConSumptionEntity param) {
		ConSumptionResult result = new ConSumptionResult();
		
		return result;
	}
	
}
