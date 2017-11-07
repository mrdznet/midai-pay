package com.midai.pay.mobile.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.midai.pay.common.pay.ClientMain;
import com.midai.pay.common.pay.ConstantUtil;
import com.midai.pay.customer.entity.BoCustomer;
import com.midai.pay.customer.service.BoCustomerService;
import com.midai.pay.mobile.AppBaseEntity;
import com.midai.pay.mobile.service.WeChatPayService;
import com.midai.pay.mobile.utils.WeChatPayUtils;
import com.midai.pay.mobile.utils.ZUtil;
import com.midai.pay.wechat.entity.BoWechatEntity;
import com.midai.pay.wechat.mapper.BoWechatMapper;
import com.midai.pay.wechat.service.BoWechatService;

/**
 * 
 * 项目名称：midai-pay-posw-service 类名称：WeChatPayServiceImpl 类描述： 创建人：wrt
 * 创建时间：2016年11月4日 下午3:07:15 修改人：wrt 修改时间：2016年11月4日 下午3:07:15 修改备注：
 * 
 * @version
 * 
 */
@Service
public class WeChatPayServiceImpl implements WeChatPayService {

	private Logger logger = LoggerFactory.getLogger(WeChatPayServiceImpl.class);

	@Value("${mobile.wechatpay.codeurl}")
	private String code_url;
	// 商户私钥
	@Value("${mobile.wechatpay.prikey}")
	private String pri_key;
	@Value("${mobile.wechatpay.queryurl}")
	private String query_url;

	@Reference
	private BoCustomerService bcs;
	
	@Autowired
	private BoWechatMapper bwmapper;
	@Autowired
	private BoWechatService bws;
	
	@Override
	public Object changJieSign(String content) {

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
			if (!json.has("sign_type") ) {
				pcode = "01";
				pmsg = "参数定义错误";
			} else {
				Map<String, String> returnMap = new HashMap<String, String>();
				try {
					// 生成签名结果
					String mysign = "";
					logger.info("###################获取签名数据:" + content + "###########################");
					// json数据转换map
					Map<String, String> sParaTemp = WeChatPayUtils.jsonToObject(content);
					String signType = sParaTemp.get("sign_type");
					if(signType.equalsIgnoreCase("MD5") ||signType.equalsIgnoreCase("RSA") ) {
						
						// 除去数组中的空值和签名参数
						Map<String, String> sPara = WeChatPayUtils.paraFilter(sParaTemp);
						mysign = "";
						if ("MD5".equalsIgnoreCase(signType)) {
							mysign = WeChatPayUtils.buildRequestByMD5(sPara, pri_key, "UTF-8");
						} else if ("RSA".equalsIgnoreCase(signType)) {
							mysign = WeChatPayUtils.buildRequestByRSA(sPara, pri_key, "UTF-8");
						}
						// 签名结果与签名方式加入请求提交参数组中
						logger.info("###################sign:" + mysign + "###########################");
						// 给app返回内容
						returnMap.put("SIGN", mysign);
						returnMap.put("SIGN_TYPE", signType);
						returnMap.put("RSPCOD", "00");
						returnMap.put("RSPMSG", "签名成功！");
						return returnMap;
					} else {
						pcode = "02";
						pmsg = "加密类型不正确";
					}
				} catch (Exception e) {
					logger.error(e.getMessage());
					e.printStackTrace(System.err);
				}
			}
		}
		
		return new AppBaseEntity(pcode, pmsg);
	}

	@Override
	public Object getCodeImage(String content) {
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
			if (!json.has("out_trade_no")|| !json.has("trade_amount") || !json.has("url")) {
				pcode = "01";
				pmsg = "参数定义错误";
			} else {
				String outTradeNo = json.getString("out_trade_no");//订单号
				double tradeAmount = json.getDouble("trade_amount");//交易金额
				String url = json.getString("url");//接口地址
				//根据订单号获取金额
				String mercId = outTradeNo.substring(0,15);//商户号
				
				//判断金额是否正确
				if(!ConstantUtil.isNumber(""+tradeAmount)){
					pcode = "01";
					pmsg = "金额参数非法";
				} else {
					String tradeAmt = ConstantUtil.changeY2F(""+tradeAmount);//金额元转换为分
					String accountname = "";//开户名
					String accountno = "";//账号
					String merName = "";//商户名称
					String bankname ="";//开户行名称-支行名
					String bandId ="";//总行行号
					String insCode = "";//所属公司
					String mercidQuery = "";
					String weRate = "";//商户扫码扣率
					
					BoCustomer bc = bcs.getCustomerByMercId(mercId);
					if(bc != null) {
						accountno = bc.getAccountNo();//账号
						accountname = bc.getAccountName();//开户名
						merName = bc.getMercName();//商户名
						bankname = bc.getBranchbankName();//开户支行名称
						bandId = bc.getBankId();//总行行号-清算行号
						mercidQuery = bc.getMercId();
						//商户扫码扣率
						weRate = bc.getWechatRate()!=null? bc.getWechatRate().toString():"";
					}
					
					//判断商户信息
					if(mercidQuery==null || mercidQuery.equals("")){
						pcode = "01";
						pmsg = "请联系客服,检查商户信息或者扣率未配置";
					} else 
					//判断扫码扣率
					if(weRate==null || weRate.equals("")){
						pcode = "01";
						pmsg = "请联系客服,配置商户扫码扣率!";
					} else {
						String wetas [] = weRate.split("\\|");
						if(wetas.length !=2){
							pcode = "01";
							pmsg = "请联系客服,检查扣率配置!";
						} else {
							
							double rate1  = Double.parseDouble(wetas[0])/100;//扣率
							double rate2  = Integer.parseInt(wetas[1]);//扣率基础上+N元
							
							//获取到商户扫码扣率计算手续费
							double feeYuan =  ZUtil.mul(tradeAmount, rate1)+rate2;
							//手续费转为分单位
							int tmoney =(int)ZUtil.mul((feeYuan), Double.parseDouble("100"));
							logger.info("##################手续费:" + feeYuan + "=======fen====" + tmoney + "######################");
							
							ClientMain clientMain = new ClientMain();
							String httpResult = "";
							try {
								httpResult = clientMain.getMethodInvoke(url);
							} catch (Exception e) {
								logger.error(e.getMessage());
								e.printStackTrace();
							}
							String payUrl ="";// 扫码返回url
							String codeState = "";//扫码返回状态
							String errMsg ="";//扫码返回错误提示
							
							if (httpResult != null && !"".equals(httpResult)) {
								logger.info("##################扫码返回：" + httpResult + "######################");
								
								//{"_input_charset":"UTF-8","is_success":"T","pay_url":"https://pay.swiftpass.cn/pay/qrcode?uuid=weixin://wxpay/bizpayurl?pr=CB524CE","status":"0"}
								JSONObject fobject = new JSONObject(httpResult);
								payUrl = fobject.getString("pay_url");//返回的二维码URL
								codeState = fobject.getString("status");//0：成功；1：失败 
								if(codeState.equals("1")){
									errMsg = fobject.getString("err_msg");
								}
							}
							
							// 4存储交易数据
							BoWechatEntity wechat = new BoWechatEntity();
							wechat.setPaytime(ZUtil.getDTimes()); //原始支付日期
							wechat.setPayperson("admin");
							wechat.setPaystate(Integer.parseInt(ConstantUtil.CJ_STATE_10));//扫码返回待打款
							wechat.setChannelcode(""); //代付通道订单号
							wechat.setMercid(mercId); //商户号
							wechat.setErrorcode(codeState); //扫码返回状态
							wechat.setErrormsg(errMsg); //扫码失败才有错误信息
							wechat.setPaycount(1);
							wechat.setTixilogno(outTradeNo); //扫码订单号
							wechat.setPaymoney(Integer.parseInt(tradeAmt)); //扫码金额（交易金额：单位分）
							wechat.setAutohitflag("");
							wechat.setBnakname(bankname);
							wechat.setAccountname(accountname);
							wechat.setAccountno(accountno);
							wechat.setTixiflag(tmoney); //计算的交易手续费
							wechat.setPayremark(payUrl);//扫码URL
							wechat.setPaylogno(outTradeNo);//初始化为扫码订单号，生成后更新为通道返回订单号
							wechat.setMercname(merName);
							wechat.setTixiandatetime(ZUtil.getDTimes());//扫码日期
							wechat.setAutoflag("2");//,0,'普通商户',1,'自动到账商户',2微信来源
							wechat.setBankcode(bandId);//总行号
							wechat.setPaychannel("0");//默认初始代付通道为0
							wechat.setCreateTime(new Date());
							wechat.setUpdateTime(new Date());
							
							Map<String, String> m = new HashMap<String, String>();
							
							//扫码返回成功URL
							if(codeState.equals("0")){
								//插入扫码记录
								bwmapper.insert(wechat);
								m.put("TRANSID", outTradeNo);//订单号
								m.put("RSPCOD", "00");
								m.put("RSPMSG","二维码获取成功");
								m.put("PAYURL", payUrl);//二维码
							}else {
								m.put("RSPCOD","01");
								m.put("RSPMSG",errMsg);
							}
							return m;
						}
					}
					
				}
			}
		}
		return new AppBaseEntity(pcode, pmsg);
	}

	@Override
	public Object executeQuery(String content) {
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
			if (!json.has("out_trade_no") || !json.has("url") ) {
				pcode = "01";
				pmsg = "参数定义错误";
			} else {
				 String outTradeNo = json.getString("out_trade_no");//订单号
				 String url = json.getString("url");//接口地址
				 
				 ClientMain clientMain = new ClientMain();
				 String httpResult = "";
				try {
					httpResult = clientMain.getMethodInvoke(url);
				} catch (Exception e) {
					e.printStackTrace();
				}
				 System.out.println("=========查询返回===：" + httpResult);
				
				 String innerTradeNo ="";//支付订单号
				 String tradeDate = "";//交易状态对应的时间
				 String tradeStatus ="";//交易状态
				 String tradeAmount ="";
				 if (httpResult != null && !"".equals(httpResult)) {
					//解析返回数据
					JSONObject jsonRes =  new JSONObject(httpResult);
					innerTradeNo = jsonRes.getString("inner_trade_no");//支付平台交易订单号
					tradeDate = jsonRes.getString("trade_date");//交易时间
					tradeAmount  = jsonRes.getString("trade_amount");//交易金额
					String outerTradeNo  = jsonRes.getString("outer_trade_no");//我们发起的订单号
					tradeStatus = jsonRes.getString("trade_status");//交易状态
				 }
				 
				 BoWechatEntity bw =  bws.getBoWeChatByTixilogno(outTradeNo);
				 if(bw == null) {
					pcode = "03";
					pmsg = "原始扫码交易信息不存在！";
				 } else {
					 //根据商户号查询扣率信息，生产结算金额。或者在扫码的时候去取到商户的扣率提现算好结算金额
					 //TODO:
					 String mercId = outTradeNo.substring(0,15);//商户号
					 //判断返回状态
					 String code = "";
					 String reason = "";
					 int payState = 0;//打款状态
					 
					 
					 //判断返回状态
					 if(tradeStatus.equals("TRADE_SUCCESS")){
						reason ="交易成功";
						payState = 11;
						code = "301";
					 }else if(tradeStatus.equals("WAIT_PAY")){
						reason ="待付款";
						payState = 12;
						code = "100";
					 }else if(tradeStatus.equals("WAIT_BUYER_PAY")){
						reason ="付款中";
						payState = 13;
						code = "201";
					 }else if(tradeStatus.equals("TRADE_FINISHED")){
						reason ="交易结束";
						payState = 14;
						code ="401";
					 }else if(tradeStatus.equals("TRADE_CLOSED")){
						reason ="交易关闭";
						payState = 15;
						code ="999";
					 }else if(tradeStatus.equals("TRANSFER_SUCCESS")){
						reason ="交易成功";
						payState = 11;
						code = "301";
					 }else{
						reason ="状态异常";
						payState = 16;
						code = "000";
					 }
					 
					 //更新扫码订单状态
					 BoWechatEntity entity = new BoWechatEntity();
					 entity.setPaystate(payState);
					 entity.setPaytime(ZUtil.getDTimes());
					 entity.setPayperson("admin");
					 entity.setChannelcode(innerTradeNo);
					 entity.setCjcodechannel(innerTradeNo);
					 entity.setErrorcode(code);
					 entity.setErrormsg(reason);
					 entity.setAutohitflag(tradeStatus);
					 entity.setTixilogno(outTradeNo);
					 
					 
					 int updateCount = bwmapper.updateTixilognos(entity);
					 if(updateCount <0){
						pcode = "02";
						pmsg = "扫码返回更新失败！";
					 } else {
						 
						 // 生产返回信息
						 Map<String, String> m = new HashMap<String, String>();
						 m.put("TRANSID", outTradeNo);//订单号
						 m.put("RSPCOD", "00");
						 m.put("RSPMSG","查询成功");
						 m.put("INNERTRADENO", innerTradeNo);//支付订单号
						 m.put("TRADESTATUS",tradeStatus);
						 return m;
					 }

				 }
			}
		}
		return new AppBaseEntity(pcode, pmsg);
	}

}
