package com.midai.pay.customer.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.midai.pay.agent.entity.Agent;
import com.midai.pay.customer.service.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.midai.framework.common.BaseServiceImpl;
import com.midai.framework.common.DoubleUtil;
import com.midai.framework.common.po.ResultVal;
import com.midai.pay.agent.mapper.AgentMapper;
import com.midai.pay.common.constants.Constants;
import com.midai.pay.common.po.TasksInfo;
import com.midai.pay.common.util.CreateParseCode;
import com.midai.pay.customer.entity.BoCustomer;
import com.midai.pay.customer.entity.BoCustomerDevice;
import com.midai.pay.customer.entity.BoCustomerInst;
import com.midai.pay.customer.entity.BoCustomerReview;
import com.midai.pay.customer.mapper.BoCustomerDeviceMapper;
import com.midai.pay.customer.mapper.BoCustomerImgMapper;
import com.midai.pay.customer.mapper.BoCustomerMapper;
import com.midai.pay.customer.mapper.BoCustomerTempMapper;
import com.midai.pay.customer.query.BoCustomerCountQuery;
import com.midai.pay.customer.query.BoCustomerQuery;
import com.midai.pay.customer.query.CustomerApplyQuery;
import com.midai.pay.customer.query.CustomerPendingTaskQuery;
import com.midai.pay.customer.vo.AgentAuthReqBeanVo;
import com.midai.pay.customer.vo.BoCustomerAddVo;
import com.midai.pay.customer.vo.BoCustomerVo;
import com.midai.pay.customer.vo.CustomerApplyVo;
import com.midai.pay.customer.vo.CustomerBindDeviceVo;
import com.midai.pay.customer.vo.CustomerFirstReviewVo;
import com.midai.pay.customer.vo.CustomerImgVo;
import com.midai.pay.customer.vo.CustomerSecondReviewVo;
import com.midai.pay.device.entity.BoIostorage;
import com.midai.pay.device.mapper.BoDeviceMapper;
import com.midai.pay.device.mapper.BoIostorageMapper;
import com.midai.pay.device.vo.BoIostorageCustomerVo;
import com.midai.pay.device.vo.DeviceDetailVo;
import com.midai.pay.handpay.service.HandpayService;
import com.midai.pay.handpay.vo.InstiMerchantRequestBean;
import com.midai.pay.handpay.vo.InstiMerchantResponseBean;
import com.midai.pay.inst.entity.InstMercAll;
import com.midai.pay.inst.service.InstMercService;
import com.midai.pay.mobile.utils.SmsUtil;
import com.midai.pay.mobile.vo.CustomerExtendVo;
import com.midai.pay.oss.PayOssService;
import com.midai.pay.process.po.PendingTask;
import com.midai.pay.process.service.ProcessTypeEnum;
import com.midai.pay.user.entity.Bank;
import com.midai.pay.user.entity.BankBranch;
import com.midai.pay.user.entity.SystemChina;
import com.midai.pay.user.entity.SystemCity;
import com.midai.pay.user.entity.SystemMCC;
import com.midai.pay.user.entity.SystemProvince;
import com.midai.pay.user.mapper.SystemTblSeqMapper;
import com.midai.pay.user.mapper.SystemUserMapper;
import com.midai.pay.user.service.BankBranchService;
import com.midai.pay.user.service.BankService;
import com.midai.pay.user.service.SystemChinaService;
import com.midai.pay.user.service.SystemCityService;
import com.midai.pay.user.service.SystemMccService;
import com.midai.pay.user.service.SystemProvinceService;
import com.midai.reqbean.AgentAuthReqBean;
import com.midai.resbean.AgentAuthResBean;
import com.midai.service.AgentAuthService;
import com.midai.service.impl.QBAgentAuthServiceImpl;

import tk.mybatis.mapper.entity.Example;

/**
 * ClassName:User <br/>
 * Date: 2016年9月21日 <br/>
 * 
 * @author cjy
 * @version
 * @since JDK 1.7
 * @see
 */
@Service
public class BoCustomerServiceImpl extends BaseServiceImpl<BoCustomer> implements BoCustomerService {

	private Logger logger = LoggerFactory.getLogger(BoCustomerServiceImpl.class);

	private static final int FIRST_REVIEW_LEVEL = 0; // 初审

	private static final int SECOND_REVIEW_LEVEL = 1; // 风控

	private static final String INST_JD_CODE = "301000000001";

	public BoCustomerServiceImpl(BoCustomerMapper mapper) {
		super(mapper);
		this.mapper = mapper;
	}

	private final BoCustomerMapper mapper;

	@Autowired
	private BoDeviceMapper boDeviceMapper;

	@Autowired
	private BoCustomerReviewService reviewService;

	@Autowired
	private CustomerProcessService customerProcessService;

	@Autowired
	private BoCustomerImgMapper boCustomerImgMapper;

	@Autowired
	private BoIostorageMapper boIostorageMapper;

	@Autowired
	private PayOssService payOssService;

	@Autowired
	private SmsUtil smsUtil;

	@Autowired
	private AgentMapper agentMapper;

	@Autowired
	private BoCustomerTempMapper boCustomerTempMapper;

	@Autowired
	private SystemTblSeqMapper systemTblSeqMapper;

	@Autowired
	private BoCustomerInstService customerInstService;

	@Autowired
	private HandpayService handpayService;

	@Autowired
	private InstMercService instMercService;

	@Autowired
	private SystemChinaService systemChinaService;

	@Autowired
	private BankService bankService;

	@Autowired
	private BankBranchService bankBranchService;

	@Autowired
	private BoCustomerDeviceMapper boCustomerDeviceMapper;

	@Autowired
	private SystemProvinceService systemProvinceService;

	@Autowired
	private SystemCityService systemCityService;

	@Autowired
	private SystemUserMapper systemUserMapper;

	@Autowired
	private SystemMccService systemMccService;

	@Autowired
	private BoChannelService boChannelService;

	@Value("${mifu.oss.customer-dir}")
	private String customerdir;

	@Value("${customer.qrcode.addr}")
	private String customerQrCodeAddr;

	@Override
	@Transactional
	public int insertBoCustomer(BoCustomerAddVo vo, String name,int id) {
		
		BoCustomer boCustomer = vo.getBoCustomer();

		/**
		 * 商户基本信息
		 */
		// 生成商户编号
		boCustomer.setMercNo(createMercNo(boCustomer.getAgentId()));
		// 小票号
		boCustomer.setMercId(createMercId(boCustomer.getMercNo(), boCustomer.getCityId()));
		// inscode
		String inscode = agentMapper.findInscode(boCustomer.getAgentId());
		boCustomer.setInscode(inscode);

		boCustomer.setCreateTime(new Date());
		mapper.insertSelective(boCustomer);
		
		//新增商户信息，绑定设备
		List<Map<String, String>> deviceList = new ArrayList<>();
		String devicestr = vo.getDeviceStr();
		if(StringUtils.isNotEmpty(devicestr)){
			try {
				Gson gson = new Gson();
				deviceList = gson.fromJson(devicestr, List.class);
			} catch (JsonSyntaxException e) {
				e.printStackTrace();
				logger.info("gosn格式转换失败："+devicestr,e);
			}
		}
		String deviceNos = "";
		List<BoCustomerDevice> list = new ArrayList<>();
		List<BoIostorage> ioList = new ArrayList<>();
		if (deviceList != null && deviceList.size()>0) {
			for (Map<String,String> map : deviceList) {
				if(StringUtils.isNotEmpty(map.get("deviceNo"))){
					deviceNos = deviceNos + "'" + map.get("deviceNo") + "',";
					BoCustomerDevice  boCustomerDevice=new BoCustomerDevice();
					boCustomerDevice.setMercId(boCustomer.getMercId());
					boCustomerDevice.setMercNo(boCustomer.getMercNo());
					boCustomerDevice.setBodyNo(map.get("deviceNo"));
					boCustomerDevice.setState(1);
					boCustomerDevice.setBundingTime(new Date());
					boCustomerDevice.setCreateTime(new Date());
					boCustomerDevice.setIsFirst(Integer.parseInt(map.get("isFirst")));
					list.add(boCustomerDevice);
					//然后向tbl_bo_iostorage表插入一条绑定数据
			        /**
			         * 取值插入
			         * 调用
			         */
					BoIostorage boIostorage=new BoIostorage();
					//boIostorage.setDeviceId(deviceId);//对应设备表里面的id
					boIostorage.setDeviceNo(map.get("deviceNo"));//设备机身号
					boIostorage.setCreateTime(new Date());
					boIostorage.setUpdateTime(new Date());
					//boIostorage.setDeviceFlag(deviceFlag);//pos和sim的标示
					boIostorage.setAgentId(boCustomer.getAgentId());//源代理商编号
					boIostorage.setAgentName(boCustomer.getAgentName());//代理商名称
					//boIostorage.setDestagentId();//目标代理商编号
					//boIostorage.setDestagentName(destagentName);//目标代理商的名称
					boIostorage.setDestmercId(boCustomer.getMercNo());//目标商户的编号
					boIostorage.setState("绑定");//状态，绑定或者解绑
					boIostorage.setOperatorId(id);
					ioList.add(boIostorage);
				}
			}
			if (StringUtils.isNotEmpty(deviceNos)) {
				boIostorageMapper.insertList(ioList);
				boCustomerDeviceMapper.batchInsertBoCustomerDevice(list);
				deviceNos = deviceNos.substring(0, deviceNos.length()-1);
				boDeviceMapper.updateDeviceCustomerAndState(boCustomer.getMercNo(), deviceNos,2);
			}
			
			
		}

		/**
		 * OSS 图片
		 */
		boCustomerImgMapper.deleteImgByMercNo(boCustomer.getMercNo());
		List<CustomerImgVo> imgList = vo.getImgList();
		for (CustomerImgVo imgVo : imgList) {
			imgVo.setMercNo(boCustomer.getMercNo());
			boCustomerImgMapper.insertImg(imgVo);
		}

		/**
		 * 工作流
		 */
		customerProcessService.startCustomerApplyProcess(boCustomer.getMercNo(), name, Constants.CHANNEL_WEB);

		return 1;
	}

	@Override
	public List<BoCustomerVo> findQueryBoCustomer(BoCustomerQuery query) {
		String agentNo = systemUserMapper.findCurrentAgentNo(query.getLoginName());
		String an = (null == agentNo) ? "AGEi000460" : agentNo;
		query.setAgentNo(an);

		List<BoCustomerVo> voList = mapper.findQueryBoCustomer(query);

		for (BoCustomerVo vo : voList) {
			if (vo.getAgentId().equals(an) && (vo.getState()==4)) { // 直属商户 并且 审核通过
				vo.setMarkId("1");
			} else
				vo.setMarkId("0");
		}

		return voList;
	}

	@Override
	public int findQueryBoCustomerCount(BoCustomerQuery query) {
		String agentNo = systemUserMapper.findCurrentAgentNo(query.getLoginName());
		String an = (null == agentNo) ? "AGEi000460" : agentNo;
		query.setAgentNo(an);

		return mapper.findQueryBoCustomerCount(query);
	}

	@Override
	public int applyCount(CustomerApplyQuery query) {
		return mapper.applyCount(query);
	}

	@Override
	public List<CustomerApplyVo> applyQuery(CustomerApplyQuery query) {
		return mapper.applyQuery(query);
	}

	@Override
	public List<BoCustomerVo> ExcelDownloadBoCustomer(BoCustomerQuery query) {
		String agentNo = systemUserMapper.findCurrentAgentNo(query.getLoginName());
		String an = (null == agentNo) ? "AGEi000460" : agentNo;
		query.setAgentNo(an);

		return mapper.ExcelDownloadBoCustomer(query);
	}

	@Override
	public int ExcelDownloadBoCustomerCount(BoCustomerCountQuery query) {
		String agentNo = systemUserMapper.findCurrentAgentNo(query.getLoginName());
		String an = (null == agentNo) ? "AGEi000460" : agentNo;
		query.setAgentNo(an);

		return mapper.ExcelDownloadBoCustomerCount(query);
	}

	@Override
	@Transactional
	public int update(CustomerFirstReviewVo vo) {
		// 修改商户表信息
		vo.getCustomer().setBranchbankName(vo.getCustomer().getBranchbankName());

		// 获取父代理商inscode
		/*
		 * String inscode =
		 * agentMapper.findInscode(vo.getCustomer().getAgentId());
		 * vo.getCustomer().setInscode(inscode);
		 */

		// 修改商户图片信息
		boCustomerImgMapper.deleteImgByMercNo(vo.getCustomer().getMercNo());
		if (null != vo.getImgs()) {
			List<CustomerImgVo> imgList = vo.getImgs();
			for (CustomerImgVo imgVo : imgList) {
				imgVo.setMercNo(vo.getCustomer().getMercNo());
				boCustomerImgMapper.insertImg(imgVo);
			}
		}

		mapper.updateBoCustomer(vo.getCustomer());

		BoCustomer customer = mapper.selectByMercNo(vo.getCustomer().getMercNo());

		String instMercId = getInstMercId(customer.getMercId(), INST_JD_CODE);

		List<BoCustomerInst> InstList = getBoCustomerInstList(vo.getCustomer().getMercNo());
		// 修改后是否包含翰银
		boolean conjd = false;
		// 商戶通道的信息修改
		if (InstList.size() > 0) {
			customerInstService.deleteCustomerInst(vo.getCustomer().getMercNo());
		}

		if (StringUtils.isNotEmpty(vo.getInstCode())) {
			String[] instCode = vo.getInstCode().split(",");

			for (int i = 0; i < instCode.length; i++) {
				if (instCode[i].trim().equals(INST_JD_CODE)) {
					conjd = true;
				}
				customerInstService.insertCustomerInst(vo.getCustomer().getMercNo(), instCode[i].trim());
			}
		}

		// 新增/更新报备吉点信息
		if (conjd) {
			updatePrepareInformation(customer, instMercId);
		}

		// 钱宝报备

		if(vo.getCustomer().getMerAuto().equals(Integer.parseInt("1"))) {
			BoCustomer cus = mapper.selectByPrimaryKey(vo.getCustomer().getMercNo());
			Integer qrCodeFlag = cus.getQrCodeFlag();
			if(qrCodeFlag.equals(Integer.parseInt("0"))) {
				
				AgentAuthService agentAuthService = new QBAgentAuthServiceImpl();
				AgentAuthReqBean agentAuthReqBean = new AgentAuthReqBean();
				copyCus2AgentAuthReqBean(customer, agentAuthReqBean);
				logger.info("钱宝报备请求：" + agentAuthReqBean);
				AgentAuthResBean res  = agentAuthService.applyForAgent(agentAuthReqBean);
				
				if(res.getStatus().equals("success")) {
					int i = mapper.updateBoCustomerQrCodeFlag(customer.getMercNo(), 1);
				} else {
					String msg =  res.getMsg();
					if(msg.contains("：")) {
						msg = msg.substring(msg.lastIndexOf("：") + 1);
					}
					throw new RuntimeException("钱宝通道报备失败：" + msg);
				}
				logger.info("钱宝报备响应：" + res);
			}
			
		}
		
		return 1;
	}

	@Override
	public String getInstMercId(String mercId, String instCode) {
		// 以前是否包含翰银
		InstMercAll instMerc = instMercService.selectInstMerc(instCode, mercId);
		if (instMerc != null) {
			return instMerc.getInstMercId();
		}

		return null;
	}

	@Override
	public List<BoCustomerInst> getBoCustomerInstList(String mercNo) {

		return customerInstService.selectCustomerInst(mercNo);
	}

	@Override
	public boolean updatePrepareInformation(BoCustomer customer, String instMercId) {
		// 吉点报备
		// 更新报备信息
		// 有翰银需要调用
		Bank bank = bankService.findById(customer.getBankId());
		SystemChina province = systemChinaService.findByCode(customer.getPeovinceId());
		SystemChina city = systemChinaService.findByCode(customer.getCityId());

		InstiMerchantRequestBean instBean = new InstiMerchantRequestBean();
		if (StringUtils.isNotEmpty(instMercId)) {
			instBean.setSubMerId(instMercId);
		}
		instBean.setMerName(customer.getMercName());// 商户名称
		if (customer.getChannel().equals(Integer.parseInt("2"))) {

			instBean.setRealName(customer.getAccountName());// 法人姓名
		}
		instBean.setMerState(province.getName());// 商户所在省份
		instBean.setMerCity(city.getName());// 商户所在城市
		instBean.setMerAddress(customer.getBusinessAddress());

		instBean.setCertType("01");
		instBean.setCertId(customer.getIdCard());// 证件号
		instBean.setMobile(customer.getMobile());// 手机号
		instBean.setAccountId(customer.getAccountNo());// 开户账号
		instBean.setAccountName(customer.getAccountName());// 开户名
		instBean.setBankName(bank.getBankname());// 总行名称
		instBean.setBankCode(customer.getBankId());// 总行ID

		instBean.setOpenBankName(" ");
		instBean.setOpenBankState(" ");
		instBean.setOpenBankCity(" ");

		instBean.setT0drawFee(customer.getSwingCardSettleFee().toString());
		instBean.setT0drawRate("0");
		instBean.setT1consFee(customer.getSwingCardSettleFee().toString());
		instBean.setT1consRate(new BigDecimal(customer.getSwingCardCreditRate().toString())
				.divide(new BigDecimal(100), 4, BigDecimal.ROUND_HALF_UP).toString());

		String flag = null;
		if (StringUtils.isNotEmpty(instMercId)) {
			flag = "up";
		}
		ResultVal<InstiMerchantResponseBean> result = handpayService.instiMerchant(flag, instBean);
		InstiMerchantResponseBean responseBean = (InstiMerchantResponseBean) result.getValue();
		if (null == responseBean) {
			logger.error("吉点商户报备更新失败");
			throw new RuntimeException("吉点商户报备更新失败");
		}

		if (StringUtils.isEmpty(instMercId)) {

			InstMercAll instMerc = new InstMercAll();
			instMerc.setInstCode(INST_JD_CODE);
			instMerc.setInstMercId(responseBean.getSubMerId());
			instMerc.setIsstMercName("");
			instMerc.setInstDeviceId(responseBean.getTsn());
			instMerc.setMercId(customer.getMercId());
			int count = instMercService.selectInstMercAllCount(INST_JD_CODE, customer.getMercId());
			if (count > 0) {
				instMercService.deleteInstMercAll(INST_JD_CODE, customer.getMercId());
			}
			instMercService.insertInstMercAll(instMerc);
		}

		return true;
	}

	// 历史记录时间需要精确到年月日时分秒
	@Override
	public List<BoIostorageCustomerVo> findByDeviceNo(String deviceNo) {
		List<BoIostorageCustomerVo> vo = new ArrayList<BoIostorageCustomerVo>();

		List<BoIostorageCustomerVo> vos = boIostorageMapper.fetchByDeviceNo(deviceNo);
		if (vos != null && vos.size() > 0) {
			int num = 0;
			BoIostorageCustomerVo fir = vos.get(0);
			if (fir.getState().equals("解绑")) {
				BoIostorageCustomerVo firVo = new BoIostorageCustomerVo();
				firVo.setUpdateTime(firVo.getCreateTime());
				num = 1;
			}

			for (; num < vos.size() - 1; num += 2) {
				BoIostorageCustomerVo bcv = new BoIostorageCustomerVo();
				BoIostorageCustomerVo s = vos.get(num);
				if (s.getState().equals("使用") || s.getState().equals("绑定")) {
					bcv.setCreateTime(s.getCreateTime());
					bcv.setMercName(s.getMercName());
					bcv.setMercId(s.getMercId());
					bcv.setDestmercId(s.getDestmercId());
					bcv.setDeviceNo(s.getDeviceNo());
					bcv.setState(s.getState());
				}
				if (num + 1 <= vos.size()) {
					if (vos.get(num + 1).getState().equals("解绑")) {
						bcv.setUpdateTime(vos.get(num + 1).getCreateTime());
					}
				}
				vo.add(bcv);
			}

			if (num - 1 <= vos.size() - 1) {
				BoIostorageCustomerVo lastbcv = new BoIostorageCustomerVo();
				lastbcv.setCreateTime(vos.get(vos.size() - 1).getCreateTime());
			}
		}
		return vo;
	}

	// 生成商户号
	@Override
	public String createMercNo(String agentno) {
		int nextVal = systemTblSeqMapper.findCustomeNextValue();
		String increment = String.format("%08d", nextVal);
		return "Mi" + increment;
	}

	// 生成小票号
	@Override
	public String createMercId(String mercnostr, String cityid) {
		// cityidMF008+日期
		String pre = mercnostr.substring(0, 2);

		String orgNo = "";

		switch (pre) {
		case "HB":
			orgNo = Constants.HAIBEI_MERID;

			break;
		case "ZF":
			orgNo = Constants.ZHANGFU_MERID;

			break;
		default:
			orgNo = Constants.MIFU_MERID;
			break;
		}

		return cityid + orgNo + mercnostr.substring(2);
	}

	@Override
	public CustomerFirstReviewVo loadFirstReview(String mercNo) {
		CustomerFirstReviewVo vo = new CustomerFirstReviewVo();

		BoCustomer bCustomer = mapper.selectByMercNo(mercNo);
		// 默认加载代理商的扣率
		/*
		 * Agent agent =
		 * agentMapper.findByAgentNo(bCustomer.getAgentId().trim());
		 * bCustomer.setSwingCardLimit(agent.getSwingCardLimit());
		 * bCustomer.setSwingCardDebitRate(agent.getSwingCardDebitRate());
		 * bCustomer.setSwingCardCreditRate(agent.getSwingCardCreditRate());
		 * bCustomer.setNonCardDebitRate(agent.getNonCardDebitRate());
		 * bCustomer.setNonCardCreditRate(agent.getNonCardCreditRate());
		 * bCustomer.setPosDebitLimit(agent.getPosDebitLimit());
		 * bCustomer.setPosDebitRate(agent.getPosDebitRate());
		 * bCustomer.setPosCreditRate(agent.getPosCreditRate());
		 * bCustomer.setScanCodeWxRate(agent.getScanCodeWxRate());
		 * bCustomer.setScanCodeZfbRate(agent.getScanCodeZfbRate());
		 * bCustomer.setScanCodeYlRate(agent.getScanCodeYlRate());
		 * bCustomer.setScanCodeJdbtRate(agent.getScanCodeJdbtRate());
		 * bCustomer.setScanCodeOtherRate(agent.getScanCodeOtherRate());
		 */

		vo.setCustomer(bCustomer);

		vo.setReview(reviewService.findLatestReview(mercNo, FIRST_REVIEW_LEVEL));
		vo.setImgs(boCustomerImgMapper.findByMercNo(mercNo));
		vo.setInstCode("0");

		return vo;
	}

	@Override
	@Transactional
	public int updateFirstReview(CustomerFirstReviewVo vo, String userName) {
		BoCustomer boCustomer = vo.getCustomer();

		/**
		 * Step1: 保存初审审核结果
		 */
		BoCustomerReview review = vo.getReview();
		review.setReviewLevel(FIRST_REVIEW_LEVEL);
		review.setOperUser(userName);
		review.setCreateTime(new Date());
		review.setMercNo(boCustomer.getMercNo());
		reviewService.insertCustomerReview(review);

		boolean approval = review.getReviewResult().equals(1) ? true : false;

		if (approval) {

			/**
			 * Step2: 保存商户信息
			 */
			// 获取父代理商inscode
			String inscode = agentMapper.findInscode(boCustomer.getAgentId());
			boCustomer.setBranchbankName(vo.getCustomer().getBranchbankName());
			boCustomer.setInscode(inscode);
			mapper.updateBoCustomer(boCustomer);

			/**
			 * Step3: 更新商户资质图片
			 */
			boCustomerImgMapper.deleteImgByMercNo(boCustomer.getMercNo());
			if (null != vo.getImgs()) {
				List<CustomerImgVo> imgList = vo.getImgs();
				for (CustomerImgVo imgVo : imgList) {
					imgVo.setMercNo(boCustomer.getMercNo());
					boCustomerImgMapper.insertImg(imgVo);
				}
			}

			/**
			 * 商户和通道的绑定的信息
			 */
			List<BoCustomerInst> InstList = customerInstService.selectCustomerInst(boCustomer.getMercNo());
			if (InstList.size() > 0) {
				customerInstService.deleteCustomerInst(boCustomer.getMercNo());
			}

			if (StringUtils.isNotEmpty(vo.getInstCode())) {
				String[] instCode = vo.getInstCode().split(",");

				for (int i = 0; i < instCode.length; i++) {
					customerInstService.insertCustomerInst(vo.getCustomer().getMercNo(), instCode[i].trim());
				}
			}

		}

		/**
		 * Step4: 工作流;
		 */
		customerProcessService.runCustomerApplyProcess(vo.getTaskId(), vo.getCustomer().getMercNo(), userName, approval,
				"2");

		/**
		 * Step5: 审核不通过, 发短信给商户
		 */
		if (!approval) {
			StringBuilder bs_2 = new StringBuilder();
			bs_2.append("尊敬的").append(boCustomer.getMercName()).append("用户:您的商户申请审核 被拒绝。原因：" + review.getAdvice());
			smsUtil.sendSMS(boCustomer.getMobile(), bs_2.toString(), 0, "101002");
		}

		/**
		 * 拒绝时, 删除设备，并更新设备表中的代理商信息
		 */
		if (!approval) {

			// 更新设备表中的代理商信息
			BoCustomerDevice bcd = new BoCustomerDevice();
			bcd.setMercNo(boCustomer.getMercNo());
			List<BoCustomerDevice> list = boCustomerDeviceMapper.select(bcd);
			String deviceNos = "";
			for (BoCustomerDevice bd : list) {
				deviceNos += "'" + bd.getBodyNo() + "',";
			}
			if (StringUtils.isNotEmpty(deviceNos)) {
				deviceNos = deviceNos.substring(0, deviceNos.length() - 1);
			}
			if (StringUtils.isNotEmpty(deviceNos)) {

				boDeviceMapper.bacthUpdateCustomerIdAndState(deviceNos);
			}
			// 删除设备
			boCustomerDeviceMapper.deleteBoCustomerDeviceByMercNo(boCustomer.getMercNo());
		}

		return 1;
	}

	@Override
	public CustomerSecondReviewVo loadSecondReview(String mercNo) {
		CustomerSecondReviewVo vo = new CustomerSecondReviewVo();

		BoCustomer bCustomer = mapper.selectByMercNo(mercNo);
		vo.setCustomer(bCustomer);

		vo.setFirstRreviewList(reviewService.findReviewsByLevel(mercNo, FIRST_REVIEW_LEVEL));
		vo.setImgs(boCustomerImgMapper.findByMercNo(mercNo));

		vo.setBranchBankName(bCustomer.getBranchbankName());

		// 商户通道进行信息加载
		List<BoCustomerInst> instList = customerInstService.selectCustomerInst(mercNo);
		if (null != instList && instList.size() > 0) {
			StringBuffer buff = new StringBuffer();
			for (BoCustomerInst inst : instList) {
				buff.append(inst.getInstCode().trim()).append(",");
			}

			String instCode = buff.toString();
			vo.setInstCode(instCode.substring(0, instCode.length() - 1));
		}

		return vo;
	}

	@Override
	@Transactional
	public int updateSecondReview(BoCustomerReview vo, String userName) {
		/**
		 * Step1: 保存复审审核结果
		 */
		vo.setReviewLevel(SECOND_REVIEW_LEVEL);
		vo.setOperUser(userName);
		vo.setCreateTime(new Date());
		reviewService.insertCustomerReview(vo);

		// 去通道报备
		BoCustomer customer = mapper.selectByMercNo(vo.getMercNo());
		if (vo.getReviewResult().equals(0)) {
			List<BoCustomerInst> InstList = customerInstService.selectCustomerInst(customer.getMercNo());
			for (BoCustomerInst inst : InstList) {
				if (inst.getInstCode().equals("301000000001")) { // 吉点报备
					String provinceName = "";
					String cityName = "";
					SystemChina province = systemChinaService.findByCode(customer.getPeovinceId());
					SystemChina city = systemChinaService.findByCode(customer.getCityId());

					if (province == null) {
						SystemProvince sysProvince = systemProvinceService.getByCode(customer.getPeovinceId());
						provinceName = sysProvince.getName();
					} else {
						provinceName = province.getName();
					}
					if (city == null) {
						SystemCity sysCity = systemCityService.findByCode(customer.getCityId());
						cityName = sysCity.getName();
					} else {
						cityName = city.getName();
					}
					Bank bank = bankService.findById(customer.getBankId());

					// 有翰银需要调用
					InstiMerchantRequestBean instBean = new InstiMerchantRequestBean();
					instBean.setMerName(customer.getMercName());// 商户名称
					instBean.setRealName(customer.getAccountName());// 法人姓名
					instBean.setMerState(provinceName);// 商户所在省份
					instBean.setMerCity(cityName);// 商户所在城市
					instBean.setMerAddress(provinceName + cityName + customer.getBusinessAddress());// 商户所在详细地址

					instBean.setCertType("01");
					instBean.setCertId(customer.getIdCard());// 证件号
					instBean.setMobile(customer.getMobile());// 手机号
					instBean.setAccountId(customer.getAccountNo());// 开户账号
					instBean.setAccountName(customer.getAccountName());// 开户名
					instBean.setBankName(bank.getBankname());// 总行名称
					instBean.setBankCode(customer.getBankId());// 总行ID

					instBean.setOpenBankName(" ");
					instBean.setOpenBankState(" ");
					instBean.setOpenBankCity(" ");

					instBean.setT0drawFee(customer.getSwingCardSettleFee().toString());
					instBean.setT0drawRate("0");
					instBean.setT1consFee(customer.getSwingCardSettleFee().toString());
					instBean.setT1consRate(new BigDecimal(customer.getSwingCardCreditRate().toString())
							.divide(new BigDecimal(100), 4, BigDecimal.ROUND_HALF_UP).toString());

					ResultVal<InstiMerchantResponseBean> result = handpayService.instiMerchant(null, instBean);
					InstiMerchantResponseBean responseBean = (InstiMerchantResponseBean) result.getValue();
					if (null == responseBean) {
						logger.error("吉点商户报备更新失败");
						throw new RuntimeException("吉点商户报备失败");
					}

					InstMercAll instMerc = new InstMercAll();
					instMerc.setInstCode("301000000001");
					instMerc.setInstMercId(responseBean.getSubMerId());
					instMerc.setIsstMercName("");
					instMerc.setInstDeviceId(responseBean.getTsn());
					instMerc.setMercId(customer.getMercId());
					int count = instMercService.selectInstMercAllCount("301000000001", customer.getMercId());
					if (count > 0) {
						instMercService.deleteInstMercAll("301000000001", customer.getMercId());
					}
					instMercService.insertInstMercAll(instMerc);
				}
			}

			// 钱宝报备

			if(customer.getMerAuto().equals(Integer.parseInt("1"))) {

				AgentAuthService agentAuthService = new QBAgentAuthServiceImpl();
				AgentAuthReqBean agentAuthReqBean = new AgentAuthReqBean();
				copyCus2AgentAuthReqBean(customer, agentAuthReqBean);
				logger.info("钱宝报备请求：" + agentAuthReqBean);
				AgentAuthResBean res  = agentAuthService.applyForAgent(agentAuthReqBean);
				logger.info("钱宝报备响应：" + res);
				if(res.getStatus().equals("success")) {
					int i = mapper.updateBoCustomerQrCodeFlag(customer.getMercNo(), 1);
				} else {
					String msg =  res.getMsg();
					if(msg.contains("：")) {
						msg = msg.substring(msg.lastIndexOf("：") + 1);
					}
					throw new RuntimeException("钱宝通道报备失败：" + msg);
				}
				
			}

			// 生产商户二维码
			createCustomerQrCode(customer.getMercNo(), customer.getMercName());
		}

		/**
		 * 工作流
		 */
		boolean approval = vo.getReviewResult().equals(0) ? true : false;
		customerProcessService.runCustomerApplyProcess(vo.getTaskId(), vo.getMercNo(), userName, approval, null);

		/**
		 * 更新商户等级
		 */
		mapper.updateCustomerLevel(vo.getCustomerLevel(), vo.getMercNo());

		/**
		 * 发送短信
		 */
		BoCustomer bCustomer = mapper.selectByMercNo(vo.getMercNo());
		if (!approval) {
			StringBuilder bs_1 = new StringBuilder();
			bs_1.append("尊敬的").append(bCustomer.getMercName()).append("用户:您的商户申请审核 被拒绝。原因：" + vo.getAdvice());

			smsUtil.sendSMS(bCustomer.getMobile(), bs_1.toString(), 0, bCustomer.getInscode());
		} else {
			StringBuilder bs_2 = new StringBuilder();
			bs_2.append("尊敬的").append(bCustomer.getMercName()).append("用户:您的商户申请审核 已通过。");

			smsUtil.sendSMS(bCustomer.getMobile(), bs_2.toString(), 0, bCustomer.getInscode());
		}

		return 1;
	}

	@Override
	public void copyCus2AgentAuthReqBean(BoCustomer cus, AgentAuthReqBean auth) {
		SystemMCC mcc42 = systemMccService.findByPrimaryKey(Integer.parseInt(cus.getMcc()));
		BankBranch bb = new BankBranch();
		bb.setBranchbankcode(cus.getBranchbankId());
		bb = bankBranchService.findOne(bb);
		auth.setType("insData");
		auth.setMcc32(cus.getAreaCode()); // 收单机构32域
		auth.setTraderAreaCode(cus.getAreaCode()); // 商户号地区编码
		auth.setMcc42(mcc42.getMcc()); // 42域MCC
		auth.setBusLicenseName(cus.getMercName()); // 商户工商注册名称
		auth.setBusName(cus.getMercName()); // 营业名称
		auth.setBusLicense(cus.getBusinessLicense()); // 营业执照号码
		auth.setBusScope(mcc42.getName()); // 经营范围
		auth.setMainbusiness(mcc42.getName()); // 主营业务
		auth.setLegalName(cus.getAccountName()); // 法人代表
		auth.setCerType("01"); // 法人证件类型，默认01
		auth.setCerNum(cus.getIdCard()); // 法人代表身份证号
		auth.setContactName(cus.getLegalName()); // 法人代表名称
		auth.setContactPhone(cus.getMobile()); // 商户联系人电话
		auth.setAddCountryCode("CHN"); // 商户注册地址国家代码：默认CHN
		auth.setAddCoProvinceCode(cus.getPeovinceId()); // 商户注册地址省代码
		auth.setAddCoCityCode(cus.getCityId()); // 商户注册地址市代码
		auth.setAddCoAreaCode(cus.getAreaCode()); // 商户注册地址区县代码
		auth.setReqAddress(cus.getBusinessAddress()); // 商户注册地址
		auth.setSettleWay("1"); // 商户结算途径，默认1
		auth.setSettleCycle("0"); // 商户结算周期，默认0
		auth.setChargeType("00"); // 商户计费类型，默认00
		auth.setChargingGrade("0"); // 商户计费档次，默认0
		auth.setAccountName(cus.getLegalName()); // 结算账户名称
		auth.setBankNum(cus.getAccountNo()); // 结算账户帐号
		auth.setBankCodeThreeCode(bb.getBankcode()); // 结算账户开户行代码
		auth.setBankName(bb.getBranchbankname() ); // 结算账户开户行名称
		auth.setBankCode(bb.getBranchbankcode()); // 结算账户开户行支付系统行号
		auth.setHolidaysSettle("是"); // 节假日合并结算
		auth.setWxRfeeType("0"); // 手续费收取方式，默认0
		auth.setWxRrate(DoubleUtil.divHalfUp(cus.getScanCodeWxRate(), 100D, 4).toString()); // 手续费收取额度
		auth.setWxRtop("0"); // 手续费额度封顶
		auth.setWxRrateMin("0"); // 手续费额度保底
		auth.setZfbRfeeType("0"); // 手续费收取方式，默认0
		auth.setZfbRrate(DoubleUtil.divHalfUp(cus.getScanCodeZfbRate(), 100D, 4).toString()); // 手续费收取额度
		auth.setZfbRtop("0"); // 手续费额度封顶
		auth.setZfbRrateMin("0"); // 手续费额度保底
	}

	@Override
	public String createCustomerQrCode(String mercNo, String mercName) {

		// 文件名称
		SimpleDateFormat df = new SimpleDateFormat("YYYYMMddHHmmss");
		String fileName = mercNo + "_" + df.format(new Date()) + ".png";
		String imgPath = "";
		String cqcaddr = customerQrCodeAddr;
		cqcaddr += "?mercNo=" + mercNo + "&mercName=" + mercName;
		ByteArrayOutputStream baos = CreateParseCode.createQRCodeWithLogWithStream(cqcaddr, "");

		// oss上传文件
		if (baos != null) {
			try {

				imgPath = payOssService.imgUpload(new ByteArrayInputStream(baos.toByteArray()), fileName, customerdir);
				BoCustomer upCustomer = new BoCustomer();
				upCustomer.setMercNo(mercNo);
				upCustomer.setQrCodeAddr(imgPath);
				upCustomer.setQrCodeFlag(1);
				mapper.updateByPrimaryKeySelective(upCustomer);
			} finally {
				if (baos != null) {
					try {
						baos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

		}

		return imgPath;
	}

	@Override
	public CustomerSecondReviewVo loadByBoCustomer(String id) {
		CustomerSecondReviewVo vo = new CustomerSecondReviewVo();

		BoCustomer bCustomer = mapper.selectByMercNo(id);
		vo.setCustomer(bCustomer);

		vo.setImgs(boCustomerImgMapper.findByMercNo(id));
		vo.setBranchBankName(bCustomer.getBranchbankName());

		// 商户通道进行信息加载
		List<BoCustomerInst> instList = customerInstService.selectCustomerInst(id);
		if (null != instList && instList.size() > 0) {
			StringBuffer buff = new StringBuffer();
			for (BoCustomerInst inst : instList) {
				buff.append(inst.getInstCode().trim()).append(",");
			}

			String instCode = buff.toString();
			vo.setInstCode(instCode.substring(0, instCode.length() - 1));
		}

		return vo;
	}

	@Override
	public List<DeviceDetailVo> loadCustomerDevice(String mercNo) {
		return boDeviceMapper.findByCustomerId(mercNo);
	}

	@Override
	public int selectBymobile(String mobile) {
		return mapper.selectBymobile(mobile);
	}

	@Override
	public CustomerPendingTaskQuery dolist(CustomerPendingTaskQuery query, String userName) {
		// 获取待审的列表
		List<PendingTask> tasks = customerProcessService.customerPendingTaskPages(userName,
				(query.getPageNumber() - 1) * query.getPageSize(), query.getPageSize());

		// int orderIdCount =
		// customerProcessService.customerPendingTaskCount(userName);
		int orderIdCount = 0;

		List<TasksInfo> taskList = new ArrayList<TasksInfo>();

		/**
		 * 根据 task.getProcessKey() 区分是商户申请流程 或 开户行变更流程
		 */
		TasksInfo info = null;
		for (PendingTask task : tasks) {
			if (task.getProcessKey().equals(ProcessTypeEnum.merchant_process.toString())) { // 商户申请流程
				info = mapper.findByMercId(task.getBusinessKey().trim());

				if (null != info && info.getState().equals("2")) { // 复审中,
																	// 取初审提交时间
					info.setCreateTime(mapper.findFirstReviewTime(info.getMercNo()));
				}

			} else if (task.getProcessKey().equals(ProcessTypeEnum.merchant_bank_process.toString())) { // 商户变更开户行流程
				info = boCustomerTempMapper.findByMercId(task.getBusinessKey().trim());
			}

			if (null != info && StringUtils.isNotEmpty(info.getMercNo())) {
				info.setTaskId(task.getId());
				taskList.add(info);

				orderIdCount++;
			}
		}

		query.setResult(taskList);
		query.setTotal(orderIdCount);

		return query;
	}

	public BoCustomer getCustomerByMobile(String mobile) {
		Example example = new Example(BoCustomer.class);
		example.createCriteria().andEqualTo("mobile", mobile);
		List<BoCustomer> list = mapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public List<CustomerExtendVo> getCustomerExtendVoByMobile(String mobile) {

		return mapper.getCustomerExtendVoByMobile(mobile);
	}

	@Override
	public List<BoCustomer> getCustomerByDeviceNo(String deviceid) {

		return mapper.getCustomerByDeviceNo(deviceid);
	}

	@Override
	public BoCustomer getCustomerByMercId(String mercId) {
		Example example = new Example(BoCustomer.class);
		example.createCriteria().andEqualTo("mercId", mercId);
		List<BoCustomer> bcls = mapper.selectByExample(example);
		if (bcls != null && bcls.size() > 0) {
			return bcls.get(0);
		}
		return null;
	}

	@Override
	public String updatebymobile(String newMobile, String mercNo) {
		String mobile = mapper.selectByMercNo(mercNo).getMobile();
		if (mobile.equals(newMobile)) {
			return "手机号码与原先相同";
		}

		int num = mapper.selectBymobile(newMobile);
		if (num > 0) {
			return "该手机号已经注册";
		}

		return "success";
	}

	@Override
	public void customerEntry(AgentAuthReqBeanVo req) {
		BoCustomer boCustomer = new BoCustomer();

		// 基本信息
		String agentID = boChannelService.getAgentIDByOrgID(req.getInstCode());
		Agent agent = agentMapper.findByAgentNo(agentID);

		boCustomer.setAgentId(agentID);
		if (agent != null) {
			boCustomer.setAgentName(agent.getName());
		}
		boCustomer.setTopAgentId("AGEi000460");
		boCustomer.setTopAgentName("POS银");
		boCustomer.setMercNo(createMercNo(null)); // 生成商户编号
		boCustomer.setMercId(createMercId(boCustomer.getMercNo(), "1000")); // 小票号
		boCustomer.setInscode(req.getInstCode());
		boCustomer.setState(4);
		boCustomer.setCreateTime(new Date());

		boCustomer.setMcc(req.getMcc42());
		boCustomer.setMercName(req.getBusLicenseName());
		boCustomer.setBusinessLicense(req.getBusLicense());
		boCustomer.setScobus(req.getBusScope());
		boCustomer.setLegalName(req.getLegalName());
		boCustomer.setIdCard(req.getCerNum());
		boCustomer.setMobile(req.getContactPhone());

		boCustomer.setPeovinceId(req.getAddCoProvinceCode());
		boCustomer.setCityId(req.getAddCoCityCode());
		boCustomer.setAreaCode(req.getAddCoAreaCode());
		boCustomer.setBusinessAddress(req.getReqAddress());
		boCustomer.setAddress(req.getReqAddress());

		boCustomer.setAccountName(req.getAccountName());
		boCustomer.setAccountNo(req.getBankNum());
		boCustomer.setBranchbankId(req.getBankCode());
		boCustomer.setBranchbankName(req.getBankName());

		boCustomer.setScanCodeWxRate(DoubleUtil.mul(Double.valueOf(req.getWxRrate()), 100));
		boCustomer.setScanCodeZfbRate(DoubleUtil.mul(Double.valueOf(req.getZfbRrate()), 100));

		mapper.insertSelective(boCustomer);
	}
}
