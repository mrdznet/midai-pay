package com.midai.pay.mobile.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.midai.haibei.customer.service.HaibeiCustomerBankUpdateProcessService;
import com.midai.pay.customer.entity.BoCustomer;
import com.midai.pay.customer.entity.BoCustomerTemp;
import com.midai.pay.customer.mapper.BoCustomerMapper;
import com.midai.pay.customer.mapper.BoCustomerTempMapper;
import com.midai.pay.customer.service.BoCustomerImgService;
import com.midai.pay.customer.service.BoCustomerService;
import com.midai.pay.mobile.AppBaseEntity;
import com.midai.pay.mobile.service.MobileUserUpdateCardService;

/**
 * 
 * 项目名称：midai-pay-posw-service 类名称：FreezeMoneyServiceImpl 类描述： 创建人：wrt
 * 创建时间：2016年11月3日 下午4:26:51 修改人：wrt 修改时间：2016年11月3日 下午4:26:51 修改备注：
 * 
 * @version
 * 
 */
@Service
public class MobileUserUpdateCardServiceImpl implements MobileUserUpdateCardService {

	private Logger logger = LoggerFactory.getLogger(MobileUserUpdateCardServiceImpl.class);

	@Reference
	private BoCustomerService bcs;
	@Reference
	private BoCustomerImgService bcis;
	@Reference
	private HaibeiCustomerBankUpdateProcessService hbcbups;

	@Autowired
	private BoCustomerTempMapper bctmapper;
	@Autowired
	private BoCustomerMapper bcmapper;

	@Override
	public Object execute(String content) {
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
			// 判断传人参数
			if (!json.has("PHONENUMBER") || !json.has("ACCOUNTNAME") || !json.has("ACCOUNTNO")
					|| !json.has("CONFIRMACCOUNTNO") || !json.has("PROVINCEID") || !json.has("CITYID")
					|| !json.has("BANKID") || !json.has("BRANCHBANKNAME") || !json.has("PICPATH") ) {
				return new AppBaseEntity("01", "参数定义错误");
			}

			String userId = json.getString("PHONENUMBER").trim();// 手机号
			String accountName = json.getString("ACCOUNTNAME");// 收款账户名
			String accountNo = json.getString("ACCOUNTNO");// 收款账号
			String confirmAccountNo = json.getString("CONFIRMACCOUNTNO");// 确认收款账号
			String bankId = json.getString("BANKID");// 所属银行
			String branchbankname = json.getString("BRANCHBANKNAME");// 支行名称
			String provinceid = json.getString("PROVINCEID");// 省
			String cityid = json.getString("CITYID");// 市

			// 判断手机号
			if (userId.isEmpty()) {
				return new AppBaseEntity("02", "手机号码不能为空");
			}
			// 判断更新账号是否一致
			if (!accountNo.equals(confirmAccountNo)) {
				return new AppBaseEntity("03", "确认账户输入不一致");
			}

			logger.info("#####################mobile:" + userId + "#####################");

			BoCustomer bc = bcs.getCustomerByMobile(userId);

			if (bc == null) {
				return new AppBaseEntity("06", "该手机号不存在");
			}

			Integer state = 0;
			String mercId = null, mercName = null, idcardpicpath2 = null;
			String oldAccountName = "", oldAccountNo = "", oldBankId = "", oldBranchbankname = "", oldProvinceid = "",
					oldCityid = "";

			state = bc.getState();
			mercId = bc.getMercId();
			mercName = bc.getMercName();

			if (StringUtils.isEmpty(mercName)) {
				mercName = bc.getAccountName();
			}

			oldAccountName = json.getString("ACCOUNTNAME");// 原收款账户名
			oldAccountNo = bc.getAccountNo();// 原收款账号
			oldBankId = json.getString("BANKID");// 原所属银行
			oldBranchbankname = json.getString("BRANCHBANKNAME");// 原支行名称
			oldProvinceid = json.getString("PROVINCEID");// 省
			oldCityid = json.getString("CITYID");// 市

			String picPath = json.getString("PICPATH");

			if (StringUtils.isEmpty(picPath)) {
				return new AppBaseEntity("04", "请上传身份证和银行卡合照照片");
			}

			// 判断修改的跟当前的数据不能一样
			if (oldAccountNo.equals(accountNo) && oldAccountName.equals(accountName) && oldBankId.equals(bankId)
					&& oldBranchbankname.equals(branchbankname) && oldProvinceid.equals(provinceid)
					&& oldCityid.equals(cityid)) {
				return new AppBaseEntity("07", "修改数据不能跟原来一致");
			}

			// 判断当前状态是否为审核通过
			if (state != 4 && state != 7) {
				return new AppBaseEntity("04", "该商户状态没审核通过不允许修改");
			}

			BoCustomerTemp bct = new BoCustomerTemp();
			BeanUtils.copyProperties(bc, bct);
			bct.setMobile(userId);
			bct.setMercId(mercId);
			bct.setMercName(mercName);
			bct.setProvinceId(provinceid);
			bct.setCityId(cityid);
			bct.setAccountName(accountName);
			bct.setAccountNo(accountNo);
			bct.setConfirmaccountNo(confirmAccountNo);
			bct.setBankId(bankId);
			bct.setBranchBankName(branchbankname);
			bct.setPicPath(picPath);
			// 更新商户状态为已提交0:初始|1:已提交|2:正在审核|3:审核未通过|4:审核通过
			// 5:修改已提交|6:修改正在审核|7:更新审核未通过

			try {
				hbcbups.haibeiApply(bct);
			} catch (Exception e) {
				pcode = "06";
				pmsg = "提交海贝商户修改申请失败！";
			}
			pcode = "00";
			pmsg = "修改申请提交成功";
			
		}
		return new AppBaseEntity(pcode, pmsg);
	}

}
