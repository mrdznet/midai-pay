package com.midai.pay.mobile.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.midai.pay.customer.entity.BoCustomer;
import com.midai.pay.customer.mapper.BoCustomerMapper;
import com.midai.pay.customer.service.BoCustomerService;
import com.midai.pay.mobile.AppBaseEntity;
import com.midai.pay.mobile.entity.CustomerDeviceEntity;
import com.midai.pay.mobile.entity.LoginEntity;
import com.midai.pay.mobile.entity.MobileVersionEntity;
import com.midai.pay.mobile.mapper.CustomerDeviceMapper;
import com.midai.pay.mobile.service.MobileUserService;
import com.midai.pay.mobile.service.MobileVersionService;
import com.midai.pay.mobile.utils.CryptUtils;
import com.midai.pay.mobile.utils.ZUtil;
import com.midai.pay.sms.entity.BoSMS;
import com.midai.pay.sms.mapper.BoSMSMapper;
import com.midai.pay.user.entity.SystemOrganizationModel;
import com.midai.pay.user.entity.SystemUser;
import com.midai.pay.user.mapper.SystemUserMapper;
import com.midai.pay.user.service.SystemOrganizationService;
import com.midai.pay.user.service.SystemUserService;

@Service
public class MobileUserLoginServiceImpl implements MobileUserService {

	private Logger logger = LoggerFactory.getLogger(MobileUserLoginServiceImpl.class);

	@Reference
	private SystemUserService users;
	@Reference
	private BoCustomerService bcs;
	@Reference
	private MobileVersionService mvs;
	@Reference
	private SystemOrganizationService orgs;
	@Autowired
	private CustomerDeviceMapper mapper;
	@Autowired
	private BoSMSMapper bmp;
	@Value("${system.merchant.org}")
	private String merchantOrg;
	@Value("${system.haibei.org}")
	private String haibeiOrg;
	@Autowired
	private SystemUserMapper ump;
	@Autowired
	private BoCustomerMapper bcmp;

	@Override
	public Object login(String content, String code) {

		String pcode = "";
		String pmsg = "";
		JSONObject json = null;
		LoginEntity lentity = new LoginEntity();
		boolean isErr = true;
		try {
			json = new JSONObject(content);
			isErr = false;
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.getMessage());
		}
		if (!isErr) {
			// 判断传人参数
			if (!json.has("PHONENUMBER") || !json.has("PASSWORD") || !json.has("IP")) {
				return new AppBaseEntity("01", "参数定义错误");
			}
			if (code.equals("399102") && (!json.has("os") || !json.has("version") || !json.has("companyNo"))) {
				return new AppBaseEntity("01", "参数定义错误");
			}

			// 海贝检测版本信息
			if (code.equals("399102")) {
				Object obj = mvs.checkMobileVersion(content);
				if (obj instanceof MobileVersionEntity) {
					BeanUtils.copyProperties((MobileVersionEntity) obj, lentity);
				} else {
					AppBaseEntity app = (AppBaseEntity) obj;
					if (!app.getRSPCOD().equals("00")) {
						return obj;
					}
				}
			}

			String mercId = "", mercname = "", state = "0", opstate = "0", merunid = "0";
			String userId = "", passwd = "";

			userId = json.getString("PHONENUMBER").trim();
			passwd = json.getString("PASSWORD");
			// ip=json.getString("IP");

			// 判断用户输入登录名和密码
			if (userId.isEmpty() || passwd.isEmpty()) {
				return new AppBaseEntity("02", "用户名或密码不允许为空");
			}

			SystemUser user = users.getByLoginnameIgnoreStatus(userId);

			if (user == null) {
				return new AppBaseEntity("04", "用户名不存在");
			}

			// 海贝比较inscode
			if (code.equals("399102")) {
				String companyNo = json.getString("companyNo").trim();
				String aginscode = users.getAgentInscodeByUserId(userId);
				if (!companyNo.equals(aginscode)) {
					return new AppBaseEntity("02", "注册的不是此App!");
				}
			}

			if (user.getStatus().equals(Integer.parseInt("2"))) {
				return new AppBaseEntity("06", "该用户已被注销");
			}

			String insertPwd = CryptUtils.encryptToMD5(passwd);
			if (!insertPwd.equalsIgnoreCase(user.getPassword())) {
				return new AppBaseEntity("05", "密码输入错误");
			}

			List<CustomerDeviceEntity> list = mapper.getCustomerDeviceEntityByUserId(userId);
			List<Map<String, String>> listd = new ArrayList<Map<String, String>>();
			for (CustomerDeviceEntity c : list) {
				Map<String, String> map_s = new HashMap<String, String>();
				merunid = c.getUNID();
				mercId = c.getMERCID();
				mercname = c.getMERCNAME();
				state = c.getSTATE();
				opstate = c.getOPSTATE();
				map_s.put("DEVICENO", c.getDEVICENO());
				map_s.put("DEVICETYPE", c.getDEVICETYPE());
				map_s.put("ISFIRST", c.getISFIRST());
				listd.add(map_s);
			}
			lentity.setRSPCOD("00");
			lentity.setRSPMSG("登录成功！");
			lentity.setMERUNID(merunid);
			lentity.setMERCID(mercId);
			lentity.setMERCNAME(mercname);
			if (code.equals("399102")) {
				lentity.setSTATE0(state);
			} else {
				lentity.setSTATE(state);
			}
			lentity.setOPSTATE(opstate);
			lentity.setDEVICE(listd);
			return lentity;

		}
		return new AppBaseEntity(pcode, pmsg);
	}

	@Transactional
	@Override
	public Object registerExecute(String content) {

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
			pcode = "99";
			pmsg = "数据异常，请检查！";
		}
		if (!isErr) {

			// 判断传人参数
			if (!json.has("PHONENUMBER") || !json.has("PASSWORD") || !json.has("CPASSWORD") || !json.has("MSGCODE")) {
				return new AppBaseEntity("01", "参数定义错误");
			}

			String userId = json.getString("PHONENUMBER").trim();
			String passwd = json.getString("PASSWORD");
			String cpasswd = json.getString("CPASSWORD");
			String mscode = json.getString("MSGCODE");
			// 判断手机号
			if (userId.isEmpty()) {
				return new AppBaseEntity("02", "手机号码不能为空");
			}
			if (!passwd.equals(cpasswd)) {
				// 判断密码
				return new AppBaseEntity("03", "两次输入密码不一致");
			}
			if (!ZUtil.isMobileNO(userId)) {
				return new AppBaseEntity("04", "手机号码有误，不符合规则");
			}

			// 判断验证码是否有效
			String currenttime = ZUtil.getDTimes();
			BoSMS sms = bmp.getByPhoneNumCur(userId, currenttime, 1);
			if (sms == null) {
				return new AppBaseEntity("05", "验证码不存在或已过期");
			}

			String code = sms.getSmscontent();
			int smid = sms.getId();
			int smstate = sms.getState();
			if (smstate == 1) {
				return new AppBaseEntity("05", "验证码已被使用,请重新获取验证码");
			}
			if (!mscode.equals(code)) {
				return new AppBaseEntity("05", "验证码有误");
			}

			// 判断手机号是否重复
			if (users.checkExistsUser(userId, null)) {
				return new AppBaseEntity("06", "该手机号已被使用");
			}

			// 获取部门编号
			SystemOrganizationModel som = new SystemOrganizationModel();
			som = orgs.getOrgByNameAndPid(merchantOrg, 0, 4);
			if (som == null) {
				return new AppBaseEntity("07", "未获取到部门数据，请联系技术");
			}

			int num = 0;
			SystemUser user = new SystemUser();
			user.setCreateTime(new Date());
			user.setLoginname(userId);
			user.setUsername(userId);
			user.setMobile(userId);
			user.setOrgid(som.getOrganizationId());
			user.setStatus(1);
			user.setPassword(CryptUtils.encryptToMD5(passwd));
			user.setIsmanger(2);
			num += ump.insert(user);
			// SystemRole orle =
			// roles.getSystemRoleByName(merchantOrg);
			// SystemUserRole ur = new SystemUserRole();
			// ur.setRoleid(orle.getId());
			// ur.setUserid(uid);
			// urs.insert(ur);
			// 让验证码失效
			num += bmp.updateStateById(smid);
			if (num <= 0) {
				pcode = "09";
				pmsg = "注册失败";
			} else {
				pcode = "00";
				pmsg = "注册成功";
			}

		}
		return new AppBaseEntity(pcode, pmsg);
	}

	@Override
	public Object modifyPassword(String content) {
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
			if (!json.has("PHONENUMBER") || !json.has("MSGCODE") || !json.has("OLDPASSWORD")
					|| !json.has("NEWPASSWORD")) {
				pcode = "01";
				pmsg = "参数定义错误";
			} else {
				String userId = "", oldPassword = "", newPassword = "", mscode = "";
				userId = json.getString("PHONENUMBER").trim();
				mscode = json.getString("MSGCODE");
				oldPassword = json.getString("OLDPASSWORD");
				newPassword = json.getString("NEWPASSWORD");
				// 如果传入验证码不为空，则为用户通过忘记密码修改密码
				int smid = 0;
				if (!mscode.isEmpty()) {
					String currenttime = ZUtil.getDTimes();
					BoSMS sms = bmp.getByPhoneNumCur(userId, currenttime, 2);
					String code = null;
					if (sms != null) {
						code = sms.getSmscontent();
						smid = sms.getId();
					}
					if (code == null || code.equals("")) {
						return new AppBaseEntity("02", "验证码不存在或已过期");
					} else if (!mscode.equals(code)) {
						return new AppBaseEntity("03", "验证码有误");
					}
				}

				SystemUser user = users.getByLoginNameAndStatus(userId, 1);
				if (user == null) {
					return new AppBaseEntity("04", "账户不存在");
				} 
				
				if (mscode.isEmpty()) {
					if (!CryptUtils.encryptToMD5(oldPassword).equalsIgnoreCase(user.getPassword())) {
						return new AppBaseEntity("06", "账户密码修改失败，原始密码不正确");
					}
				}
				
				if (updatePWDAndStatus(userId, newPassword, smid)) {
					pcode = "00";
					pmsg = "密码修改成功";
				} else {
					pcode = "99";
					pmsg = "账户密码修改失败，参数异常，请联系客服";
				}
			
			}
		}
		return new AppBaseEntity(pcode, pmsg);

	}

	@Transactional(propagation = Propagation.NESTED)
	private boolean updatePWDAndStatus(String userId, String newPassword, Integer smid) {
		SystemUser user = new SystemUser();
		user.setPassword(CryptUtils.encryptToMD5(newPassword));
		user.setLoginname(userId);
		if (users.updatePassword(user) > 0) {
			if (smid > 0) {
				bmp.updateStateById(smid);
			}
			return true;
		}
		return false;
	}

	@Transactional
	@Override
	public Object registerHaiBeiExecuteOne(String content) {
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
			// 判断传人参数 新增AGENTTYPE=0(米付) 1(海贝)
			if (!json.has("PHONENUMBER") || !json.has("PASSWORD") || !json.has("CPASSWORD") || !json.has("MSGCODE")
					) {	// || !json.has("EXTEND1")
				return new AppBaseEntity("01", "参数定义错误");
			}

			String userId = json.getString("PHONENUMBER").trim();
			String passwd = json.getString("PASSWORD");
			String cpasswd = json.getString("CPASSWORD");
			String mscode = json.getString("MSGCODE");
			String EXTEND1 = "";

/*			SystemUser tjuser = null;
			if (json.has("EXTEND1")) {
				EXTEND1 = json.getString("EXTEND1");
				// 判断推荐号码是否注册过
				tjuser = users.getByUserIdOrMobile(EXTEND1);
				if (tjuser == null) {
					return new AppBaseEntity("06", "该推荐号码未注册或未审核通过!");
				}
			}*/

			// 判断手机号
			if (userId.isEmpty()) {
				return new AppBaseEntity("02", "手机号码不能为空");
			}
			// 判断密码
			if (!passwd.equals(cpasswd)) {
				return new AppBaseEntity("03", "两次输入密码不一致");
			}
			if (!ZUtil.isMobileNO(userId)) {
				return new AppBaseEntity("04", "手机号码有误，不符合规则");
			}

			// 判断验证码是否有效
			String currenttime = ZUtil.getDTimes();
			BoSMS sms = bmp.getByPhoneNumCur(userId, currenttime, 1);

			if (sms == null) {
				return new AppBaseEntity("05", "验证码不存在或已过期");
			}

			String code = sms.getSmscontent();
			int smid = sms.getId();
			int smstate = sms.getState();
			if (smstate == 1) {
				return new AppBaseEntity("05", "验证码已被使用,请重新获取验证码");
			}
			if (!mscode.equals(code)) {
				return new AppBaseEntity("05", "验证码有误");
			}

			// 判断手机号是否重复
			if (users.checkExistsUser(userId, null)) {
				return new AppBaseEntity("06", "该手机号已被使用");
			}

			// 获取部门编号
			SystemOrganizationModel som = new SystemOrganizationModel();
			som = orgs.getOrgByNameAndPid(haibeiOrg, 0, 3);
			if (som == null) {
				return new AppBaseEntity("07", "未获取到部门数据，请联系技术");
			}

			int num = 0;
			SystemUser user = new SystemUser();
			user.setCreateTime(new Date());
			user.setLoginname(userId);
			user.setUsername(userId);
			user.setMobile(userId);
/*			if (tjuser != null) {
				user.setOrgid(tjuser.getOrgid());
			} else {
				user.setOrgid(som.getOrganizationId());
			}*/
			
			user.setOrgid(som.getOrganizationId());
			
			user.setStatus(1);
			user.setPassword(CryptUtils.encryptToMD5(passwd));
			user.setIsmanger(2);
			num += ump.insert(user);
			// 让验证码失效
			num += bmp.updateStateById(smid);
			if (num <= 0) {
				pcode = "09";
				pmsg = "注册失败";
			} else {
				pcode = "00";
				pmsg = "注册成功";
			}
		}
		return new AppBaseEntity(pcode, pmsg);

	}

	@Override
	public Object registerHaiBeiExecuteTwo(String content) {
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
			// 判断传人参数 新增AGENTTYPE=0(米付) 1(海贝)
			if (!json.has("PHONENUMBER") || !json.has("MSGCODE") ) { // || !json.has("EXTEND1")
				return new AppBaseEntity("01", "参数定义错误");
			}

			String userId = json.getString("PHONENUMBER").trim();
			String mscode = json.getString("MSGCODE");
			String EXTEND1 = "";

/*			SystemUser tjuser = null;
			if (json.has("EXTEND1")) {
				EXTEND1 = json.getString("EXTEND1");
				// 判断推荐号码是否注册过
				tjuser = users.getByUserIdOrMobile(EXTEND1);
				if (tjuser == null) {
					return new AppBaseEntity("06", "该推荐号码未注册或未审核通过!");
				}
			}*/

			// 判断手机号
			if (userId.isEmpty()) {
				return new AppBaseEntity("02", "手机号码不能为空");
			}
			if (!ZUtil.isMobileNO(userId)) {
				return new AppBaseEntity("04", "手机号码有误，不符合规则");
			}

			// 判断验证码是否有效
			String currenttime = ZUtil.getDTimes();
			BoSMS sms = bmp.getByPhoneNumCur(userId, currenttime, 1);

			if (sms == null) {
				return new AppBaseEntity("05", "验证码不存在或已过期");
			}

			String code = sms.getSmscontent();
			int smstate = sms.getState();
			if (smstate == 1) {
				return new AppBaseEntity("05", "验证码已被使用,请重新获取验证码");
			}
			if (!mscode.equals(code)) {
				return new AppBaseEntity("05", "验证码有误");
			}

			// 判断手机号是否重复
			if (users.checkExistsUser(userId, null)) {
				return new AppBaseEntity("06", "该手机号已被使用");
			}

			pcode = "00";
			pmsg = "校验成功,请设置密码";
		}
		return new AppBaseEntity(pcode, pmsg);
	}

	@Transactional
	@Override
	public Object updatePhone(String content) {
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
			// 判断传人参数 新增AGENTTYPE=0(米付) 1(海贝)
			if (!json.has("PHONENUMBER") || !json.has("IDCARD") || !json.has("ACCOUNTNO") || !json.has("PASSWORD")
					|| !json.has("NEWPHONENUMBER")) {
				return new AppBaseEntity("01", "参数定义错误");
			}

			String userId = json.getString("PHONENUMBER").trim();// 原手机号
			String C_Idcard = json.getString("IDCARD");// 身份证号码
			String C_AccountNo = json.getString("ACCOUNTNO");// 收款银行账号
			String C_Password = json.getString("PASSWORD");// 登陆密码
			String newPhonenumber = json.getString("NEWPHONENUMBER");// 新手机号

			// 判断手机号
			if (StringUtils.isEmpty(userId)) {
				return new AppBaseEntity("02", "手机号码不能为空");
			}
			// 身份证号码
			if (StringUtils.isEmpty(C_Idcard)) {
				return new AppBaseEntity("02", "身份证号码不能为空");
			}
			// 身份证号码
			if (StringUtils.isEmpty(C_Password)) {
				return new AppBaseEntity("02", "登陆密码不能为空");
			}
			// 身份证号码
			if (StringUtils.isEmpty(C_AccountNo)) {
				return new AppBaseEntity("02", "收款银行账号不能为空");
			}
			// 身份证号码
			if (StringUtils.isEmpty(newPhonenumber)) {
				return new AppBaseEntity("02", "新手机号不能为空");
			}

			logger.info("#########################userid:" + userId + "##############");
			// 校验申请材料
			BoCustomer bc = bcs.getCustomerByMobile(userId);
			String mobile = null;
			Integer state = 0;
			String idCard = null, accountno = null;
			if (bc != null) {
				// 获取要修改的商户信息匹配
				mobile = bc.getMobile();
				state = bc.getState();
				idCard = bc.getIdCard();
				accountno = bc.getAccountNo();
			}

			if (StringUtils.isEmpty(mobile)) {
				return new AppBaseEntity("06", "该手机号不存在");
			}

			SystemUser user = users.getByLoginNameAndStatus(userId, 1);
			if (!user.getPassword().equals(CryptUtils.encryptToMD5(C_Password))) {
				return new AppBaseEntity("03", "用户密码校验不通过");
			}

			// 校验新手机号是否被使用过
			SystemUser newu = users.loadByUserLoginname(newPhonenumber);
			if (newu != null) {
				return new AppBaseEntity("03", "该修改手机号已被注册不能修改");
			}

			if (!C_Idcard.equals(idCard)) {
				return new AppBaseEntity("04", "身份证号与申请时不匹配,请重新输入");
			}
			if (!C_AccountNo.equals(accountno)) {
				return new AppBaseEntity("05", "银行卡号与申请时不匹配,请重新输入");
			}

			// 判断当前状态是否为审核通过
			if (state != 4) {
				return new AppBaseEntity("06", "该商户状态没审核通过不允许修改");
			}

			// 验证通过后执行变更手机号
			int num = bcmp.updateMobileByidCardAndMobile(newPhonenumber, userId, idCard);
			if (num <= 0) {
				return new AppBaseEntity("04", "修改商户手机号失败");
			}
			
			//修改用户表
			int r= ump.updateLoginNameAndMobile(newPhonenumber, userId);
			if(r<=0){
				return new AppBaseEntity("04", "更新账户表手机号失败");
			}
			pcode = "00";
			pmsg = "修改手机号成功";
		}
		return new AppBaseEntity(pcode, pmsg);
	}
}
