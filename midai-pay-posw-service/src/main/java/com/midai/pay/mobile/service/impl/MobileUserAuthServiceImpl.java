package com.midai.pay.mobile.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.midai.pay.agent.entity.Agent;
import com.midai.pay.agent.service.AgentService;
import com.midai.pay.common.constants.Constants;
import com.midai.pay.customer.entity.BoCustomer;
import com.midai.pay.customer.entity.BoCustomerDevice;
import com.midai.pay.customer.entity.CustomerImg;
import com.midai.pay.customer.mapper.BoCustomerDeviceMapper;
import com.midai.pay.customer.mapper.BoCustomerImgMapper;
import com.midai.pay.customer.mapper.BoCustomerMapper;
import com.midai.pay.customer.service.BoCustomerService;
import com.midai.pay.customer.service.CustomerProcessService;
import com.midai.pay.device.entity.BoDevice;
import com.midai.pay.device.service.BoDeviceModeService;
import com.midai.pay.device.service.BoDeviceService;
import com.midai.pay.device.service.BoIostorageService;
import com.midai.pay.mobile.AppBaseEntity;
import com.midai.pay.mobile.entity.AuthUserInforEntity;
import com.midai.pay.mobile.entity.CustomerSimpleInfoEntity;
import com.midai.pay.mobile.mapper.CustomerDeviceExtendMapper;
import com.midai.pay.mobile.mapper.CustomerExtendMapper;
import com.midai.pay.mobile.mapper.MobileUserDefineCustomerDeviceMapper;
import com.midai.pay.mobile.service.MobileUserAuthService;
import com.midai.pay.mobile.utils.MassageUtil;
import com.midai.pay.mobile.utils.UserDefineException;
import com.midai.pay.mobile.vo.CustomerDeviceExtendVo;
import com.midai.pay.mobile.vo.CustomerExtendVo;
import com.midai.pay.mobile.vo.DeviceCustomerVo;
import com.midai.pay.trade.service.TradeReviewService;
import com.midai.pay.user.entity.SystemMCC;
import com.midai.pay.user.entity.SystemUser;
import com.midai.pay.user.mapper.SystemUserMapper;
import com.midai.pay.user.service.SystemMccService;
import com.midai.pay.user.service.SystemUserService;

/**   
*    
* 项目名称：midai-pay-posw-service   
* 类名称：MobileUserAuthServiceImpl   
* 类描述：   
* 创建人：wrt   
* 创建时间：2016年10月20日 下午5:28:03   
* 修改人：wrt   
* 修改时间：2016年10月20日 下午5:28:03   
* 修改备注：   
* @version    
*    
*/
@Service
public class MobileUserAuthServiceImpl implements MobileUserAuthService {

	private Logger logger = LoggerFactory.getLogger(MobileUserAuthServiceImpl.class);

	@Autowired
	private MobileUserDefineCustomerDeviceMapper mapper;
	@Autowired
	private BoCustomerMapper bcmapper;
	@Autowired
	private BoCustomerDeviceMapper bcdmapper;
	@Autowired
	private BoCustomerImgMapper bcimapper;
	@Autowired
	private CustomerExtendMapper cemapper;
	@Autowired
	private CustomerDeviceExtendMapper cdemapper;
	@Reference
	private AgentService agents;
	@Reference
	private BoIostorageService biss;
	@Reference
	private CustomerProcessService cps;
	@Reference
	private BoCustomerService bcs;
	@Reference
	private TradeReviewService trs;
	@Reference
	private BoDeviceService bds;
	@Reference
	private BoDeviceModeService bdms;
	@Reference
	private SystemUserService sus;
	@Autowired
	private SystemUserMapper sumapper;
	
	@Autowired
	private MassageUtil massageUtil;
	
	@Override
	public Object authentication(String content, String code) {

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
			// 判断传人参数（接口传入的必须参数）
			int mertype=1;
			if(code.equals("199725")) {
				
				if (!json.has("PHONENUMBER") || !json.has("USERNAME") || !json.has("IDNUMBER") || !json.has("MERNAME")
						|| !json.has("SCOBUS") || !json.has("MERADDRESS") || !json.has("ACCOUNTNAME")
						|| !json.has("ACCOUNTNO") || !json.has("PROVINCEID") || !json.has("CITYID") || !json.has("BANKID")
						|| !json.has("BRANCHBANKNAME") || !json.has("TERMNO") || !json.has("MERAUTO")) {
					return new AppBaseEntity("01", "参数定义错误");
				} 
			} else if(code.equals("300003")) {
				if(!json.has("MERTYPE")){
					return new AppBaseEntity("01", "参数定义错误");
				}
				
				if(StringUtils.isEmpty(json.getString("MERTYPE"))){
					return new AppBaseEntity("01", "商户类型不能为空!");
				}else if(!json.getString("MERTYPE").equals("1")&&!json.getString("MERTYPE").equals("3")){
					return new AppBaseEntity("01", "商户类型定义错误!");
				}else{
					mertype=json.getInt("MERTYPE");
				}
				
				if(mertype==1){//个人||!json.has("SCOBUS") ||!json.has("MERNAME")||!json.has("MERADDRESS")
					if(!json.has("PHONENUMBER") ||!json.has("USERNAME")||!json.has("IDNUMBER")
							||!json.has("ACCOUNTNAME")||!json.has("ACCOUNTNO")||!json.has("PROVINCEID")
							||!json.has("CITYID")||!json.has("BANKID")||!json.has("BRANCHBANKNAME")||!json.has("TERMNO")||
							!json.has("MERTYPE")
							||!json.has("AREAID") ||!json.has("BUSADDRES") ||!json.has("MCC")){//||!json.has("SCOBUS") ||!json.has("MERADDRESS") 新增 商户类型MERTYPE,经营地址,经营范围
						return new AppBaseEntity("01", "参数定义错误");
					}
				}else if(mertype==3){//普通商户
					if(!json.has("PHONENUMBER") ||!json.has("USERNAME")||!json.has("IDNUMBER")||!json.has("MERNAME")||!json.has("SCOBUS")
							||!json.has("MERADDRESS")||!json.has("ACCOUNTNAME")||!json.has("ACCOUNTNO")||!json.has("PROVINCEID")
							||!json.has("CITYID")||!json.has("BANKID")||!json.has("BRANCHBANKNAME")||!json.has("TERMNO")||
							!json.has("MERTYPE")){//新增 商户类型MERTYPE
						return new AppBaseEntity("01", "参数定义错误");
					}
				}
			}
			
			//商户新增可选字段by 20160713
			String businessLicense = "";
			String organizationno = "";
			String taxcertificateno = "";
			//营业执照号
			if(json.has("BUSINESSLICENSE")){
				businessLicense = json.getString("BUSINESSLICENSE");
			}
			//组织机构代码
			if(json.has("ORGANIZATIONNO")){
				organizationno = json.getString("ORGANIZATIONNO");
			}
			//税务登记号
			if(json.has("TAXCERTIFICATENO")){
				taxcertificateno = json.getString("TAXCERTIFICATENO");
			}
			

			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			logger.info("#################### 实名认证 开始：" + df.format(new Date()) + " ###############");
			logger.info("		**********************			");
			logger.info(content);
			logger.info("		**********************			");


			String phonenumber = json.getString("PHONENUMBER");// 手机号
			String username = json.getString("USERNAME");// 用户名
			String idcard = json.getString("IDNUMBER");// 身份证
			String mername=null,scobus=null,address=null;
			if(code.equals("199725") || (code.equals("300003") && mertype==3)) {
				mername = json.getString("MERNAME"); // 商户名称
				scobus = json.getString("SCOBUS");// 经营范围
				address = json.getString("MERADDRESS");// 地址
			} else if(code.equals("300003") && mertype!=3) {
//				mername="个人"; //商户名称
				try {
					mername = json.getString("MERNAME");
				} catch (Exception e) {
					mername = username;
				} // 商户名称
		    	//经营范围
				if(json.has("SCOBUS")){
					scobus=json.getString("SCOBUS");
				}
				//地址
				if(json.has("MERADDRESS")){
					address=json.getString("MERADDRESS");//地址
				}
				
			}
			Integer merauto =  0;
			if(code.equals("199725")) {
				merauto = json.getInt("MERAUTO");
			}
			String accountname = json.getString("ACCOUNTNAME");// 开户名
			String accountno = json.getString("ACCOUNTNO"); // 开户账户
			String provinceid = json.getString("PROVINCEID"); // 省市编码
			String cityid = json.getString("CITYID"); // 省市编码
			String bankid = json.getString("BANKID");// 总行名称
			String branchbankname = json.getString("BRANCHBANKNAME");// 支行名称
			String termno = json.getString("TERMNO");// 终端号
			
			if (StringUtils.isEmpty(phonenumber)) {
				return new AppBaseEntity("02", "手机号不允许为空");
			} 
			
			if (StringUtils.isEmpty(termno)) {
				return new AppBaseEntity("02", "设备编号不允许为空");
			} 
			
			char z = termno.charAt(0);
			int i = 0;
			while(z == '0') {
				i += 1;
				z = termno.charAt(i);
			}
			termno = termno.substring(i);
			
			// 判断该设备是否已被其他人使用
			//**从设备的角度
			String deviceId, _customerid, _state;
			List<DeviceCustomerVo> dcvs = mapper.getDeviceCustomerByTermNo(termno);
			if (dcvs != null && dcvs.size() > 0 && dcvs.get(0) != null) {
				DeviceCustomerVo dv = dcvs.get(0);
				deviceId = dv.getDeviceid() != null ? dv.getDeviceid():"" ;	//设备机身号
				_customerid = dv.getMercno() != null ? dv.getMercno():"";	//商户编号
//				_cdid = dv.getCdid() != null ? dv.getCdid().toString() : "0";	//设备商户表ID
				_state = dv.getState() != null ? dv.getState().toString() : "0";//商户状态 '状态:0:初审中|1:风控审|2:初审未通过|3:风控未通过|4:审核通过'

				//**从商户的角度
				String _bodyno="", merno = "";
				int state = 0,  _sboid = 0;
				List<DeviceCustomerVo> mobdvs = mapper.getDeviceCustomerByMobile(phonenumber);
				if (mobdvs != null && mobdvs.size() > 0 && mobdvs.get(0) != null) {
					DeviceCustomerVo dcv = mobdvs.get(0);
					merno = dcv.getMercno()!=null?dcv.getMercno():""; //商户编号
					_sboid = dcv.getSboid()!=null?dcv.getSboid():0; //设备商户表ID
					_bodyno = dcv.getBodyno()!=null?dcv.getBodyno():""; //机身号
					state = dcv.getState()!=null?dcv.getState():0;	//商户状态 '状态:0:初审中|1:风控审|2:初审未通过|3:风控未通过|4:审核通过'
				}

				if (!_state.equals("3") && StringUtils.isNotEmpty(_customerid) && !_customerid.equals(merno)) {
					return new AppBaseEntity("04", "该终端已被其他商户使用，请和终端部门联系");
				} 
				
				//查找代理商信息
				
				String _agentno = null, _agentname = null;
				List<Agent> ags = agents.getByAgentDeviceDeviceId(deviceId);
				
				String areaCode = json.getString("AREAID");
				String businessAddress = json.getString("BUSADDRES");
				String mcc = json.getString("MCC");
				if (ags != null && ags.size() > 0 && ags.get(0) != null) {

					Agent a = ags.get(0);
					_agentno = a.getAgentNo();
					_agentname = a.getName();
					BoCustomer bc = new BoCustomer();
					bc.setMercName(mername);
					bc.setIdCard(idcard);
					bc.setMerlinkman(username);
					if(code.equals("199725") || (code.equals("300003") && mertype==3)) {
						bc.setScobus(scobus);
					}
					bc.setAddress(address);
					bc.setIndustry(scobus);
					bc.setMcc(mcc);
					
					bc.setInscode(a.getInscode());
					bc.setAccountName(accountname);
					bc.setAccountNo(accountno);
					bc.setMobile(phonenumber);
					bc.setPeovinceId(provinceid);
					bc.setCityId(cityid);
					bc.setBankId(bankid);
					bc.setBranchbankName(branchbankname);
					bc.setAgentId(_agentno);
					bc.setAgentName(_agentname);
					bc.setDfeeRate(a.getMerchantRate()); 	//贷记卡扣率扣率为代理商的商户扣率
					bc.setFeeRate(a.getMerchantRate());		//借记卡扣率为代理商的商户扣率
					bc.setLegalName(username);
					bc.setMercNo(merno);
					bc.setTopAgentId(a.getCreateUser());
					bc.setTopAgentName(a.getUpdateUser());
					bc.setAreaCode(areaCode);
					bc.setBusinessAddress(businessAddress);
					if(code.equals("199725") ) {
						bc.setMerAuto(0);
						bc.setMerType(3); 	// 商户类型
					} else if(code.equals("300003") ) {
						bc.setMerAuto(0);
						bc.setMerType(mertype); 	// 商户类型
						bc.setCustomerLevel("A");
						bc.setShortName(mername);
						//商户可选字段
						bc.setBusinessLicense(businessLicense);
						bc.setOrganizationNo(organizationno);
						bc.setTaxcertificateNo(taxcertificateno);
					}
					if(StringUtils.isEmpty(merno)) {
						bc.setState(1); 	//商户状态 '状态:0:初审中|1:风控审|2:初审未通过|3:风控未通过|4:审核通过'
					}
					bc.setCreateTime(new Date());
					
					BoCustomerDevice bcd = new BoCustomerDevice();
					bcd.setDeviceId(deviceId);
					bcd.setBodyNo(termno);
					bcd.setMercNo(bc.getMercNo());
					bcd.setIsFirst(1);
					if(StringUtils.isEmpty(merno)) {
						bcd.setBundingTime(new Date());
						bcd.setCreateTime(new Date());
					}
					if(StringUtils.isEmpty(merno)) {
						bcd.setState(1);
					}
					try{
						SystemUser user = sus.loadByUserLoginname(phonenumber);
						updateState(state, bc, _sboid, _bodyno, bcd, a.getAgentNo(), user.getId());
						pcode = "00";
						pmsg = "实名基本信息认证成功!";
					} catch(UserDefineException ue) {
						pcode = ue.getApp().getRSPCOD();
						pmsg = ue.getApp().getRSPMSG();
					}
				
				} else {
					pcode = "05";
					pmsg = "该终端未出库";
				}
			
			} else {
				pcode = "03";
				pmsg = "该终端不存在或未入库，请联系终端部门";
			}

			logger.info("#################### 实名认证 结束：" + df.format(new Date()) + " ###############");

		}
		return new AppBaseEntity(pcode, pmsg);
	}

	@Transactional(propagation = Propagation.NESTED)
	public void updateState(int state, BoCustomer bc, int _sboid, String _bodyno, BoCustomerDevice bcd, String agentNo, Integer operatorId) throws UserDefineException {
		String merno = bc.getMercNo();
		logger.info("商户申请-------------updateState：" + merno);
		if (StringUtils.isEmpty(merno)) { // 商户第一次申请

			try {
				/**
				 * 生成商户编号
				 */
				merno = bcs.createMercNo(bc.getAgentId().substring(0, 2));
				bc.setMercNo(merno);
				bcd.setMercNo(bc.getMercNo());
				bc.setMercId(bcs.createMercId(bc.getMercNo(), bc.getCityId()));
				bcd.setMercId(bc.getMercId());
				sumapper.updateOrgIdByLoginName(bc.getMobile(), agentNo);
				bc.setMerAuto(0);
				bcmapper.insert(bc);
				bcdmapper.insert(bcd);
				cps.startCustomerApplyProcess(merno, bc.getMobile(), Constants.CHANNEL_APP);
			} catch (Exception e) {
				logger.info(e.getMessage());
				throw new UserDefineException("{'RSPCOD':'06','RSPMSG':'提交流程失败！'}", new AppBaseEntity("06", "提交流程失败！"));
			}

		} 


		logger.info("商户申请-------------state：" + state);
		if (state == 0 || state == 3) {// 只有审核不通过时才能再次提交

			// 1.更新商户信息表
			try {
				bcmapper.updateByPrimaryKeySelective(bc);
			} catch (Exception e) {
				logger.error(e.getMessage());
				throw new UserDefineException("{'RSPCOD':'08','RSPMSG':'更新商户失败1！'}", new AppBaseEntity("08", "更新商户失败1！"));
			}
			
			
			logger.info("商户申请-------------_bodyno：" + _bodyno);
			 //设备使用	
			if (StringUtils.isEmpty(_bodyno)) {

				try {
					biss.afterStore(bcd.getBodyNo(), operatorId);
				} catch (Exception e) {
					logger.error(e.getMessage());
					throw new UserDefineException("{'RSPCOD':'07','RSPMSG':'设备出库失败！'}", new AppBaseEntity("07", "设备出库失败！"));
				}

			} else if (StringUtils.isNotEmpty(_bodyno) && !bcd.getBodyNo().equals(_bodyno)) { // 2.1判断中间表是否更换如果换绑
				// 入库一次
				try {
					biss.beforeStoreNew(_sboid, bc.getMobile(), operatorId);
				} catch (Exception e) {
					logger.error(e.getMessage());
					throw new UserDefineException("{'RSPCOD':'09','RSPMSG':'更新入库失败2！'}", new AppBaseEntity("09", "更新入库失败2！"));
				}
				
				
				// 再出库一次//设备使用
				try {
					biss.afterStore(bcd.getBodyNo(), operatorId);
				} catch (Exception e) {
					logger.error(e.getMessage());
					throw new UserDefineException("{'RSPCOD':'11','RSPMSG':'设备出库失败2！'}", new AppBaseEntity("11", "设备出库失败2！"));
				}
			}
			
			
			// 3.重新激活一个工作流
			if (state == 3) {
				//商户设备表提交变更
				try {
					bcdmapper.insert(bcd);
				} catch (Exception e) {
					logger.error(e.getMessage());
					throw new UserDefineException("{'RSPCOD':'10','RSPMSG':'重新审核提交失败2！'}", new AppBaseEntity("10", "重新审核提交失败2！"));
				}
								
				cps.runCustomerApplyProcess(null, merno, bc.getMobile(), true, null);
			}
		}
	
	}

	@Override
	public Object authSubmitCompleted(String content, String code) {
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
			// 判断传人参数（接口传入的必须参数）
			if(!json.has("PHONENUMBER")){
				return new AppBaseEntity("01",  "参数定义错误");
			}
			
			if(code.equals("199726") && !json.has("TYPE")) {
				return new AppBaseEntity("01",  "参数定义错误");
			}

			String mobile = json.getString("PHONENUMBER");
			if(StringUtils.isNotEmpty(mobile)) {
				
				BoCustomer bc = bcs.getCustomerByMobile(mobile);
				if(bc != null) {
					//类型(1法人证件正面照, 2法人证件背面照,3银行正面照,4银行卡反面,5法人手持照片或场地照片)
					String merNo = bc.getMercNo();
					List<CustomerImg> imgs = new ArrayList<CustomerImg>();
					if(json.has("URL1") && StringUtils.isNotEmpty(json.getString("URL1"))) {
						imgs.add(createCustomerImg(1, json.getString("URL1"), merNo));
					}
					if(json.has("URL2") && StringUtils.isNotEmpty(json.getString("URL2"))) {
						imgs.add(createCustomerImg(2, json.getString("URL2"), merNo));
					}
					if(json.has("URL3") && StringUtils.isNotEmpty(json.getString("URL3"))) {
						imgs.add(createCustomerImg(3, json.getString("URL3"), merNo));
					}
					if(json.has("URL4") && StringUtils.isNotEmpty(json.getString("URL4"))) {
						imgs.add(createCustomerImg(4, json.getString("URL4"), merNo));
					}
					if(json.has("URL5") && StringUtils.isNotEmpty(json.getString("URL5"))) {
						imgs.add(createCustomerImg(5, json.getString("URL5"), merNo));
					}
					if(json.has("URL6") && StringUtils.isNotEmpty(json.getString("URL6"))) {
						imgs.add(createCustomerImg(6, json.getString("URL6"), merNo));
					}
					if(json.has("URL7") && StringUtils.isNotEmpty(json.getString("URL7"))) {
						imgs.add(createCustomerImg(7, json.getString("URL7"), merNo));
					}
					if(json.has("URL8") && StringUtils.isNotEmpty(json.getString("URL8"))) {
						imgs.add(createCustomerImg(8, json.getString("URL8"), merNo));
					}
					if(json.has("URL9") && StringUtils.isNotEmpty(json.getString("URL9"))) {
						imgs.add(createCustomerImg(9, json.getString("URL9"), merNo));
					}
					if(imgs.size() > 0) {
						bcimapper.insertList(imgs);
					}
					
					pcode = "00";
					pmsg = "实名基本信息认证提交成功,请等待审核!";
				} else {
					pcode = "03";
					pmsg = "商户信息未找到";
				}
			} else {
				pcode = "02";
				pmsg = "手机号码不能为空";
			}
		
		}
		return new AppBaseEntity(pcode, pmsg);	
	}

	private CustomerImg createCustomerImg(int type, String url, String merNo) {
		CustomerImg img1 = new CustomerImg();
		img1.setMercNo(merNo);
		img1.setType(type);
		img1.setUrl(url);
		img1.setCreateTime(new Date());
		img1.setUpdateTime(img1.getCreateTime());
		return img1;
	}
	
	@Override
	public Object getAuthSimpleInfo(String content, String code) {
		
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
			// 判断传人参数（接口传入的必须参数）
			if(!json.has("PHONENUMBER")){
				return new AppBaseEntity("01", "参数定义错误");	
			} 
			
			CustomerSimpleInfoEntity csie = new CustomerSimpleInfoEntity();
			String mobile=json.get("PHONENUMBER").toString();
			//第三步:第一节:查询商户信息	
			CustomerExtendVo cev = cemapper.getCustomerExtendVoByMobile(mobile);
			String mercno = "";
			if(cev != null) {
				mercno = cev.getMercNo();
			}
			csie.setMERCNAME(cev==null?"":cev.getMercName()==null?"":cev.getMercName());
			csie.setMERCID(cev==null?"":cev.getMercId()==null?"":cev.getMercId());
			csie.setACCOUNTNO(cev==null?"":cev.getAccountNo()==null?"":cev.getAccountNo());
			csie.setACCOUNTNAME(cev==null?"":cev.getAccountName()==null?"":cev.getAccountName());
			csie.setBRANCHBANKNAME(cev==null?"":cev.getBranchbankName()==null?"":cev.getBranchbankName());
			csie.setBALANCE(cev==null?"":cev.getBalance()==null?"":cev.getBalance().toString());
			csie.setSTATE(cev==null?"0":cev.getState()==null?"0":cev.getState().toString());
			csie.setOPSTATE(cev==null?"0":cev.getOpstate()==null?"0":cev.getOpstate().toString());
			csie.setACCOUNTSTATE(cev==null?"0":cev.getAccountstate()==null?"0":cev.getAccountstate());
			csie.setSUPERSTATE(cev==null?"0":cev.getSuperstate()==null?"0":cev.getSuperstate());
			csie.setMERAUTO(cev==null?"0":cev.getMerAuto()==null?"0":cev.getMerAuto().toString());
			csie.setMERLEV(cev==null?"A":cev.getCustomerLevel()==null?"A":cev.getCustomerLevel().toString());
			
			
			//检测商户状态，设备终端
			List<CustomerDeviceExtendVo> cdevs =  cdemapper.getCustomerDeviceExtendVoByMercNo(mercno);
			List<Map<String, String>> list = new ArrayList<Map<String, String>>();
			if(cdevs != null && cdevs.size() > 0) {
				for(CustomerDeviceExtendVo v : cdevs) {
					Map<String, String> map_s = new HashMap<String, String>(); 
					 map_s.put("DEVICENO", v.getDeviceno()==null?"":v.getDeviceno());
					 map_s.put("DEVICETYPE", v.getDevicetype()==null?"":v.getDevicetype());
					 map_s.put("ISFIRST", v.getIsfirst()==null?"":v.getIsfirst().toString());
					 map_s.put("DEVICESTATE", v.getStates()==null?"":v.getStates().toString());
					 map_s.put("IMAGEPATH", v.getImagepath()==null?"":v.getImagepath());
					 map_s.put("MONEY", v.getMoney()==null?"":v.getMoney().toString());
					 map_s.put("PAYSTATE", v.getPaystate()==null?"":v.getPaystate().toString());//1:需要付款，2：已付款
					 list.add(map_s);
				}
			}
			csie.setDEVICES(list);
			csie.setHFROZENAMT("0");
			csie.setTOTALABLEAMT("0");
			//冻结金额
			csie.setTOTALFROZENAMT("0");
			
			 //小票重签数量
			 int signnum= trs.countReSign(mobile);
			 csie.setSIGNNUM(signnum+"");
			 csie.setPROBABLEAMT("0");
			 csie.setRSPCOD("00");
			 csie.setRSPMSG("获取商户数据成功！");
			 return csie;
			
		}
		return new AppBaseEntity(pcode, pmsg);
	}

	@Override
	public Object getAuthUserInfor(String content, String code) {
		
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
			// 判断传人参数（接口传入的必须参数）
			if(!json.has("PHONENUMBER")){
				return new AppBaseEntity("01", "参数定义错误");
			} 

			if(json.get("PHONENUMBER") != null) {
				String mobile=json.get("PHONENUMBER").toString();
				//第一步:验证手机号码是否存在 
				if(!mobile.matches("^1[3|4|5|6|7|8|9][0-9]\\d{8}$")){
					return new AppBaseEntity("99", "手机信息加载失败!");
				} 

				List<CustomerExtendVo> ces = bcs.getCustomerExtendVoByMobile(mobile);
				AuthUserInforEntity auie = new AuthUserInforEntity();
				CustomerExtendVo cev = null;
				if(ces != null && ces.size() > 0) {
					cev = ces.get(0);
				}
				auie.setACCOUNTNAME(cev==null?"":cev.getAccountName()==null?"":cev.getAccountName());
				auie.setACCOUNTNO(cev==null?"":cev.getAccountNo()==null?"":cev.getAccountNo());
				auie.setADDRESS(cev==null?"":cev.getAddress()==null?"":cev.getAddress());
				auie.setBANKCODE(cev==null?"":cev.getBankcode()==null?"":cev.getBankcode());
				auie.setBANKNAME(cev==null?"":cev.getBankname()==null?"":cev.getBankname());
				auie.setBRANCHBANKNAME(cev==null?"":cev.getBranchbankName()==null?"":cev.getBranchbankName());
				auie.setBUSINESSLICENSE(cev==null?"":cev.getBusinesslicense()==null?"":cev.getBusinesslicense());
				auie.setCITYCODE(cev==null?"":cev.getCitycode()==null?"":cev.getCitycode());
				auie.setCITYNAME(cev==null?"":cev.getCityname()==null?"":cev.getCityname());
				auie.setIDCARD(cev==null?"":cev.getIdcard()==null?"":cev.getIdcard());
				auie.setMERAUTO(cev==null?"":cev.getMerAuto()==null?"":cev.getMerAuto().toString());
				auie.setMERCNAME(cev==null?"":cev.getMercName()==null?"":cev.getMercName());
				auie.setOPSTATE(cev==null?"":cev.getOpstate()==null?"":cev.getOpstate().toString());
				auie.setORGANIZATIONNO(cev==null?"":cev.getOrganizationno()==null?"":cev.getOrganizationno());
				auie.setPROVINCECODE(cev==null?"":cev.getProvincecode()==null?"":cev.getProvincecode());
				auie.setPROVINCENAME(cev==null?"":cev.getProvincename()==null?"":cev.getProvincename());
				auie.setSCOBUS(cev==null?"":cev.getScobus()==null?"":cev.getScobus());
				auie.setTAXCERTIFICATENO(cev==null?"":cev.getTaxcertificateno()==null?"":cev.getTaxcertificateno());
				auie.setTERMNO(cev==null?"":cev.getTermno()==null?"":cev.getTermno());
				auie.setAGETMERTYPE(cev==null?"":cev.getMerType()==null?"":cev.getMerType());
				//最后一步:如果成功
				auie.setRSPCOD("00");
				auie.setRSPMSG("进入实名认证!");
				return auie;
			
			} else {
				pcode = "02";
				pmsg = "手机号码不能为空";
			}
		
		}

		
		return new AppBaseEntity(pcode, pmsg);
	}

	@Override
	public Object checkFirstStep(String content) {

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
			// 判断传人参数（接口传入的必须参数）
			if(!json.has("TERMID") ||!json.has("PHONENUMBER")){
				pcode = "01";
				pmsg = "参数定义错误";
			} else {
				 String deviceid = json.getString("TERMID");//设备号TERMID
	            String mobile = json.getString("PHONENUMBER");//手机号PHONENUMBER	
	            
	            char z = deviceid.charAt(0);
				int i = 0;
				while(z == '0') {
					i += 1;
					z = deviceid.charAt(i);
				}
				deviceid = deviceid.substring(i);
				
	            List<BoDevice> bdls = bds.getByDeviceNo(deviceid);
	            if(bdls != null && bdls.size() > 0) {
	            	List<Agent> as = agents.getByDeviceNo(deviceid);
	            	if(as != null && as.size() > 0) {
	            		List<BoCustomer> bcls = bcs.getCustomerByDeviceNo(deviceid);
	            		BoCustomer bc = new BoCustomer();
	            		int state = 0;
	            		String bcmobile = "";
	            		if(bcls != null && bcls.size() > 0) {
	            			bc = bcls.get(0);
	            			state=bc.getState()!=null?bc.getState():0;
	            			bcmobile = StringUtils.isNotEmpty(bc.getMobile())?bc.getMobile().trim():"";
	            		}
	            		if(state!=3 && StringUtils.isNotEmpty(bcmobile) && !mobile.equals(bcmobile)){
	            			pcode = "99";
	            			pmsg = "此终端已被其它商户使用!";
	            		} else {
	            			Agent agent = bdms.getByDeviceNo(deviceid);
	            			Map<String, String> map = new HashMap<String, String>();
	            			if(agent != null) {
	            				map.put("DEVICEMODENAME", agent.getName());
	            			} else {
	            				map.put("DEVICEMODENAME", "");
	            			}
	            			map.put("AGETMERTYPE", "1");
	            			//最后一步:如果成功
	            			map.put("RSPCOD", "00");
	            			map.put("RSPMSG", "查询成功!");
	            			return map;
	            		} 
	            	} else {
	            		pcode = "99";
	            		pmsg = "终端未绑定代理商!";
	            	}
	            } else {
	            	pcode = "99";
					pmsg = "终端未入库,请联系代理商!";
	            }
			}
		}
		return new AppBaseEntity(pcode, pmsg);
	}

}
