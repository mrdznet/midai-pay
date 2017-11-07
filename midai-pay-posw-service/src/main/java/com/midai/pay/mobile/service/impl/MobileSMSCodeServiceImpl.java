package com.midai.pay.mobile.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.midai.pay.common.utils.SmsModeIdHelper;
import com.midai.pay.common.utils.SmsSender;
import com.midai.pay.dealtotal.service.DealtotalService;
import com.midai.pay.mobile.AppBaseEntity;
import com.midai.pay.mobile.entity.SMSCodeEntity;
import com.midai.pay.mobile.service.MobileSMSCodeService;
import com.midai.pay.mobile.utils.SmsUtil;
import com.midai.pay.mobile.utils.ZUtil;
import com.midai.pay.sms.entity.BoSMS;
import com.midai.pay.sms.mapper.BoSMSMapper;
import com.midai.pay.user.entity.SystemUser;
import com.midai.pay.user.service.SystemUserService;

@Service
public class MobileSMSCodeServiceImpl implements MobileSMSCodeService {

	private Logger logger = LoggerFactory.getLogger(MobileSMSCodeServiceImpl.class);

	@Reference
	private SystemUserService users;
	@Reference
	private DealtotalService dss;
	@Autowired
	private SmsUtil smsUtil;
	@Autowired
	private BoSMSMapper boss;
	@Autowired
	private BoSMSMapper bsmsmapper;
	@Value("${mobile.ticket.sertch.url}")
	private String url;

	@Autowired
	private SmsSender smsSender;
	
	@Transactional
	@Override
	public Object execute(String content, String code) {

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
			if (!json.has("PHONENUMBER") || !json.has("TYPE")) {
				return new AppBaseEntity("01", "参数定义错误");
			} 

			String phonenumber = json.getString("PHONENUMBER");
			String stype = json.getString("TYPE");
			int type = 0;
			if (StringUtils.isNotEmpty(stype)) {
				type = Integer.parseInt(stype);
			}
			if (StringUtils.isNotEmpty(phonenumber)) {

				SystemUser user = users.loadByUserLoginname(phonenumber);

				if (type == 1 && user != null) { // 如果是已注册的用户，则不让重新发送注册码
					return new AppBaseEntity("04", "该用户已被注册，不能再发送短信");
				} 
				if (type == 2 && user == null) { // 如果是找回密码，该手机号必须为注册过的
					return new AppBaseEntity("05", "该手机号不存在，不能再发送短信");
				} 

				String smsCode = "";
				int r = 0;
				if(code.equals("199018")) {
					 smsCode = roundcode("");
					 // 发送短信
					 // 验证码：XXXXXX，您于2015-05-19
					 // 16：45：45进行手机验证操作。如有疑问请致电客服4008855881.请保密并确认本人操作
					 String inscode = "101";
					 if (json.has("INSCODE")) {
						 inscode = json.getString("INSCODE");
					 }
					 r = smsUtil.sendSMS(phonenumber,
							 "验证码：" + smsCode + ",您于" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
							 + "进行手机验证操作。如有疑问请致电客服CALLCENTER请保密并确认本人操作。",
							 type, inscode);
				} else {
					smsCode = roundcode("hb");
					//发送短信
					  //验证码：XXXXXX，您于2015-05-19 16：45：45进行手机验证操作。如有疑问请致电客服4008855881.请保密并确认本人操作
					HashMap<String, Object> result = new HashMap<String, Object>(); 
					  try {
						result= smsSender.sendVerifyCode(phonenumber,smsCode, SmsModeIdHelper.getSmsModeId(type));
						if(result != null && result.get("statusCode").toString().equals("000000")) {
							r=1;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				if (r < 1) {
					pcode = "02";
					pmsg = "短信发送失败";
				} else {
					SMSCodeEntity sms = new SMSCodeEntity();
					sms.setPHONENUMBER(phonenumber);
					if(code.equals("199018")) {
						sms.setSMSCODE(r + "");
					} else {
						sms.setSMSCODE("000000");
					}
					sms.setSMSCONTENT(smsCode);
					sms.setCREATETIMES(ZUtil.getDTimes());
					sms.setSTYPE(type + "");
					sms.setSTATE("0");
					// 发送成功，写入记录
					BoSMS bsms = new BoSMS();
					bsms.setPhonenumber(sms.getPHONENUMBER());
					bsms.setSmscode(sms.getSMSCODE());
					bsms.setSmscontent(sms.getSMSCONTENT());
					bsms.setState(Integer.parseInt(sms.getSTATE()));
					bsms.setStype(Integer.parseInt(sms.getSTYPE()));
					bsms.setCreateTime(new Date());
					bsms.setUpdateTime(bsms.getCreateTime());
					if (boss.insert(bsms) > 0) {
						pcode = "00";
						pmsg = "成功";
					} else {
						pcode = "03";
						pmsg = "写入短消息失败";

					}
				}

			} else {
				pcode = "05";
				pmsg = "手机号码不能为空";
			}
		
		}

		return new AppBaseEntity(pcode, pmsg);
	}

	private String roundcode(String type) {
		Random random = new Random();
		int codeLength = 6;
		StringBuffer code = new StringBuffer();
		if(!type.equals("hb")) {
			code.append("8");
		} else {
			codeLength += 1;
		}
		for (int i = 1; i < codeLength; i++) {
			code.append(random.nextInt(10));
		}
		return code.toString();
	}

	@Override
	public Object ticket(String content, String code) {
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
			if (!json.has("PHONENUMBER") || !json.has("TYPE") || !json.has("TRANSNO")) {
				return new AppBaseEntity("01", "参数定义错误");
			}

			if (code.equals("199019") && !json.has("TRANSDATE")) {
				return new AppBaseEntity("01", "参数定义错误");
			}

			String phonenumber = json.getString("PHONENUMBER");
			String type = json.getString("TYPE");
			String transno = json.getString("TRANSNO");// 交易流水
			String transdate = ""; // 交易时间
			if (code.equals("199019")) {
				transdate = json.getString("TRANSDATE");
			}
			logger.info("#######################交易流水transno:" + transdate + "#########################");
			if (!type.equals("4")) {
				return new AppBaseEntity("02", "短信类型传入错误！");
			}

			int count = 0;
			if (code.equals("199019")) {
				count = dss.queryCountByTransnoAndTransdate(transno, transdate);
			} else {
				count = dss.queryCountByTransno(transno);
			}
			if (count <= 0) {
				return new AppBaseEntity("03", "获取交易数据失败，请联系客服!");
			}
			
			// 发送短信
			String smsCode = "消费小票：" + url + transno + "，请点击查看";
			logger.info("####################发送短信:" + smsCode + "######################");
			int r = 0;
			if (code.equals("199019")) {
				String inscode = "101";
				if (json.has("INSCODE")) {
					inscode = json.getString("INSCODE");
				}
				r = smsUtil.sendSMS(phonenumber, smsCode, Integer.parseInt(type), inscode);
			} else {
				ArrayList<String> list = new ArrayList<String>();
				list.add(url + transno );
				try {
					 HashMap<String, Object> remap = smsSender.sendNotice(phonenumber, list, SmsModeIdHelper.getSmsModeId(8));
					 if(remap != null && remap.get("statusCode").toString().equals("000000")) {
						 r = 1;
					 }
				} catch (Exception e) {
					logger.error(e.getMessage());
				}
			}
			if (r < 1) {
				pcode = "04";
				pmsg = "短信发送失败";
			} else {
				if(code.equals("399019")) {
					
					BoSMS bsms = new BoSMS();
					bsms.setCreateTime(new Date());
					bsms.setPhonenumber(phonenumber);
					bsms.setSmscode("");
					bsms.setSmscontent("短信获取小票" + url);
					bsms.setStype(Integer.parseInt(type));
					bsms.setState(0);
					bsmsmapper.insert(bsms);
					
				}
				pcode = "00";
				pmsg = "成功";
			}
		

		}
		return new AppBaseEntity(pcode, pmsg);
	}

}
