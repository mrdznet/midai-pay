package com.midai.pay.autopaymoney.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.midai.framework.common.BaseServiceImpl;
import com.midai.framework.common.po.ResultVal;
import com.midai.pay.autopaymoney.entity.BoAutoPayMoney;
import com.midai.pay.autopaymoney.entity.PayChannelEnum;
import com.midai.pay.autopaymoney.entity.PayInfo;
import com.midai.pay.autopaymoney.entity.PaymoneyStateEnum;
import com.midai.pay.autopaymoney.mapper.AutoPayMoneyMapper;
import com.midai.pay.autopaymoney.query.BoAutoPayMoneyQuery;
import com.midai.pay.autopaymoney.service.AutoPayMoneyService;
import com.midai.pay.autopaymoney.service.JuZhenAutoHitPayService;
import com.midai.pay.autopaymoney.vo.AutoPayMoneyVo;
import com.midai.pay.changjie.bean.G10001Bean;
import com.midai.pay.changjie.bean.G20001Bean;
import com.midai.pay.changjie.service.CjMsgSendService;
import com.midai.pay.common.pay.Constant;
import com.midai.pay.common.pay.PrimaryGenerater;
import com.midai.pay.common.po.SqlParam;
import com.midai.pay.posp.entity.MiShuaPayParam;
import com.midai.pay.posp.entity.MiShuaPayQueryParam;
import com.midai.pay.posp.service.PospService;

import net.sf.json.JSONObject;

@Service(timeout=300000)
public class AutoPayMoneyServiceImpl extends BaseServiceImpl<BoAutoPayMoney> implements AutoPayMoneyService {

	private Logger logger = LoggerFactory.getLogger(AutoPayMoneyServiceImpl.class);
	
	public static SimpleDateFormat sf = new SimpleDateFormat("yyMMddHHmmssSSS");
	
	private final AutoPayMoneyMapper autoPayMoneyMapper;

	public AutoPayMoneyServiceImpl(AutoPayMoneyMapper mapper) {
		super(mapper);
		this.autoPayMoneyMapper = mapper;
	}
	
	@Reference
	private CjMsgSendService cjMsgSendService;
	
	@Reference
    private PospService pospService;
	
	@Autowired
	private JuZhenAutoHitPayService juZhenAutoHitPayService;
	
	
	@Override
	public List<AutoPayMoneyVo> findQueryAutoPayMoney1(BoAutoPayMoneyQuery query) {
		return autoPayMoneyMapper.findQueryAutoPayMoney1(query);
	}

	@Override
	public int findQueryAutoPayMoneyCount1(BoAutoPayMoneyQuery query) {
		return autoPayMoneyMapper.findQueryAutoPayMoneyCount1(query);
	}

	@Override
	public List<AutoPayMoneyVo> findQueryAutoPayMoney(BoAutoPayMoneyQuery query) {
		return autoPayMoneyMapper.findQueryAutoPayMoney(query);
	}

	@Override
	public int findQueryAutoPayMoneyCount(BoAutoPayMoneyQuery query) {
		return autoPayMoneyMapper.findQueryAutoPayMoneyCount(query);
	}
	
	@Override
	@Transactional(isolation=Isolation.SERIALIZABLE)
	public int autoPay(String logon, String dfway, String currUser) {
		int retState = 0;
		
		String tixiLogno = StringUtils.removeEnd(logon, ",").replaceAll(",", "','");
		List<PayInfo> payList = autoPayMoneyMapper.findPayInfo(tixiLogno);


		BoAutoPayMoney payMoney;

		for (PayInfo pay : payList) {
			
			String respCode = null, respInfo = null,
					responts = null, tseq = null;
			
			String orderid = pay.getTixiLogno();// 打款流水号
			String tixianamt = String.valueOf(pay.getPayMoney());// 提现金额
			String accountno = pay.getAccountNo();
			String accountname = pay.getAccountName();// 开户名
			String bankname = pay.getBankName();// 开户行
			String mercid = pay.getMercId();// 商户号
			String ylcode = pay.getTixiLogno();// 钜阵需要参考号
			String bandId = pay.getBankId();// 总行行号-清算行号
			String branchBranchId = pay.getBranchbankId();// 支行行号-开户行号
			String bkName = pay.getBkName();// 总行名称
			
			// 查询当前记录状态
			int operState = autoPayMoneyMapper.updatePayState(orderid); //更新状态为处理中
			
			if(operState <= 0){
				throw new RuntimeException("{\"errorMsg\":\"["+orderid+"]打款已在处理中！\"}");
			}
			
			int paystateReturn = PaymoneyStateEnum.PAY_STATE_PROCESS.getState();
			
			// 调用钜真代付的方法
			if (dfway.equals(PayChannelEnum.CHANNEL_JZ.getChannel())) {
				tseq = PrimaryGenerater.generaterpaylogno(orderid) + orderid; // 矩阵代付流水号
				String paymoney = PrimaryGenerater.generaterpaymoney(tixianamt) + tixianamt; // 矩阵打款金额:固定15位，精确到分.例如000000000000050
				
				try{
					responts = juZhenAutoHitPayService.jzpayamt(tseq, accountno, accountname, paymoney, bankname, mercid, ylcode, dfway, "");
				}catch(Exception e){
					respInfo = "请去通道方确认";
					paystateReturn = PaymoneyStateEnum.PAY_STATE_ERROR.getState();
					
					logger.info("调用畅捷支付超时");
					logger.error(e.toString());
				}
				
				if (responts != null && !responts.isEmpty()) {
					logger.info("responts 返回=" + responts);
					JSONObject json = JSONObject.fromObject(responts);
					respCode = json.getString("respCode");
					respInfo = json.getString("respInfo");
					
					logger.info("调用钜阵打款方法得到的流水号：" + tseq + ",钜阵返回流水号：" + tseq);
				}
			}

			// 畅捷支付代付接口
			if (dfway.equals(PayChannelEnum.CHANNEL_CJ.getChannel())) {
				G10001Bean g10001Bean = new G10001Bean();
				g10001Bean.setCurrency("CNY");// 货币类型
				g10001Bean.setAccountType("00");// 产品编码 ,00-银行卡，01-存折，02-信用卡
				g10001Bean.setAccountProp("0");// 对公对私,1-对公，0-对私
				g10001Bean.setIdType("0");// 开户证件类型,0-身份证
//				g10001Bean.setSubMertid("mdf001");// 二级商户
				g10001Bean.setAmount(Integer.parseInt(tixianamt));// 金额
				g10001Bean.setAccountNo(accountno);// 账号
				g10001Bean.setAccountName(accountname);// 账户名称
				g10001Bean.setBankName(bankname);// 开户行名称
				g10001Bean.setBankCode(branchBranchId);// 开户行号-支行号branchBranchId
				g10001Bean.setDrctBankCode(bandId);// 清算行号-总行号bandId
				bkName = bankname;// 开户行名、开户行号、清算行号对公必填，对私开户行名和总行名称一致，开户行号、清算行号选填
				g10001Bean.setBankGeneralName(bkName);// 银行通用名称
				g10001Bean.setProvince("");// 开户行所在省
				g10001Bean.setCity("");// 开户行所在市
				g10001Bean.setProtocolNo("");// 协议号
				g10001Bean.setId("");// 证件号
				g10001Bean.setTel("");// 手机号
				g10001Bean.setCorpFlowNo(orderid);// 外部企业流水号-米付流水号
				g10001Bean.setSummary("米付");// 备注
				g10001Bean.setPostscript("代付");// 用途
				
				try{
					responts = cjMsgSendService.G10002SendMessage(g10001Bean);
				}catch(Exception e){
					respInfo = "请去通道方确认";
					paystateReturn = PaymoneyStateEnum.PAY_STATE_ERROR.getState();
					
					logger.info("调用畅捷支付超时");
					logger.error(e.toString());
				}

				if (responts != null && !responts.isEmpty()) {
					logger.info("responts 返回=" + responts);
					JSONObject json = JSONObject.fromObject(responts);
					respCode = json.getString("retCode");
					if (respCode.equals("0000")) {
						respInfo = "受理成功";
					} else if (respCode.equals("2000")) {
						respInfo = "受理处理中";
					} else {
						respInfo = json.getString("errMsg");
					}
					tseq = json.getString("reqSn");
					
					logger.info("调用畅捷通打款方法的打款流水号：" + orderid + ",钜阵返回流水号：" + tseq);
				}
			}
			
			//米刷代付接口
			if(dfway.equals(PayChannelEnum.CNANNEL_MS.getChannel())){
				MiShuaPayParam rpp = new MiShuaPayParam();
				rpp.setCardNo(accountno);
				rpp.setBlankNo(branchBranchId);
				rpp.setName(accountname);
				rpp.setMoney(tixianamt);
				rpp.setTranSsn(orderid);
				StringBuilder sb = new StringBuilder();
				
				if(StringUtils.isEmpty(rpp.getCardNo())){
					sb.append("账号不能为空;");
				}
				if(StringUtils.isEmpty(rpp.getBlankNo())){
					sb.append("支行号不能为空;");
				}
				if(StringUtils.isEmpty(rpp.getName())){
					sb.append("账户名不能为空");
				}
				if(StringUtils.isEmpty(rpp.getMoney())){
					sb.append("代付金额不能为空");
				}
				if(StringUtils.isEmpty(rpp.getTranSsn())){
					sb.append("代付内部序列号不能为空");
				}
				
				ResultVal<String> rv = null;
				
				if(StringUtils.isNotEmpty(sb.toString())){
					respCode = "00";
					respInfo = sb.toString();
					paystateReturn = PaymoneyStateEnum.PAY_STATE_ERROR.getState();
				}else{
					try{
						rv = pospService.miShuaPay(rpp);
						if(rv.isSuccess()){
							//米刷代付受理成功
							tseq = rv.getValue();
							respCode = "00";
							respInfo = "受理成功";
						}else{
							//米刷代付受理失败
							respCode = rv.getValue();
							respInfo = rv.getMsg();
							paystateReturn = PaymoneyStateEnum.PAY_STATE_ERROR.getState();
						}
					}catch(Exception e){
						e.printStackTrace();
						respCode = "-1";
						respInfo = "请去通道方确认";
						paystateReturn = PaymoneyStateEnum.PAY_STATE_ERROR.getState();
					}
				}
				
				logger.info("调用米刷代付返回code["+respCode+"],msg["+respInfo+"]");
				
			}

			// 调用接口，得到银行返回的流水号
			if (StringUtils.isNotEmpty(tseq)) {
				if (dfway.equals("500302") && !respCode.equals("00000")) {
					logger.info("调用矩真代付返回code：" + respCode.equals("00000") + ",代付流水号：" + tseq);
					paystateReturn = PaymoneyStateEnum.PAY_STATE_ERROR.getState();// 结算失败
				}
				if (dfway.equals("G10002")) {
					if (respCode.equals(Constant.HEAD_RET_CODE_1000) || respCode.equals(Constant.HEAD_RET_CODE_2004)
							|| respCode.equals(Constant.HEAD_RET_CODE_2009)) {
						logger.info("调用畅捷的代付返回code：" + respCode.equals("00000") + ",代付流水号：" + tseq);
						paystateReturn = PaymoneyStateEnum.PAY_STATE_ERROR.getState();// 畅捷代付受理失败
					}
				}
			}
			
			// 更新打款表
			payMoney = new BoAutoPayMoney();
			payMoney.setTixiLogno(orderid);
			payMoney.setPayTime(new Date());
			payMoney.setPayPerson(currUser);
			//更新打款结果信息
			payMoney.setPayState(paystateReturn);
			payMoney.setChannelCode(tseq);
			payMoney.setPayChannel(dfway);
			payMoney.setErrorCode(respCode);
			payMoney.setErrorMsg(respInfo);
			//更新银行信息
			payMoney.setBankName(bankname);
			payMoney.setAccountName(accountname);
			payMoney.setAccountNo(accountno);
			payMoney.setBankCode(branchBranchId);
			
			autoPayMoneyMapper.updatePaySettle(payMoney);
			
			retState = (paystateReturn == PaymoneyStateEnum.PAY_STATE_PROCESS.getState()) ? 1 : 0;
		}
		
		return retState;
	}
	

	@Override
	public List<AutoPayMoneyVo> excelQueryAutoPayMoney(BoAutoPayMoneyQuery query) {
		return autoPayMoneyMapper.excelQueryAutoPayMoney(query);
	}

	@Override
	public int excelQueryAutoPayMoneyCount(BoAutoPayMoneyQuery query) {
		return autoPayMoneyMapper.excelQueryAutoPayMoneyCount(query);
	}

	@Override
	public List<AutoPayMoneyVo> queryPaginationList(Map<String, String> map) {
		return autoPayMoneyMapper.queryPaginationList(map);
	}

	@Override
	public void payQuery() {
		List<AutoPayMoneyVo> payList = autoPayMoneyMapper.findPendingPay(); //查询所有受理中的数据
		
		SqlParam param = null;
		G20001Bean dataReq = null, dataResp=null;
		
		for(AutoPayMoneyVo pay : payList){
			param = new SqlParam();
			param.setParam_1(pay.getTixiLogno());
			
			if (StringUtils.isNotEmpty(pay.getChannelCode()) && pay.getPayChannel().equals(PayChannelEnum.CHANNEL_JZ.getChannel())) { //TODO 钜真代付
			}
			
			if(StringUtils.isNotEmpty(pay.getChannelCode()) && pay.getPayChannel().equals(PayChannelEnum.CHANNEL_CJ.getChannel())) { //畅捷支付
				dataReq = new G20001Bean();
				dataReq.setQryReqSn(pay.getChannelCode());//查询交易流水号
				
				dataResp = cjMsgSendService.G20001SendMessage(dataReq);
				String rspCode = dataResp.getRetCode(); 
				
				param.setParam_2(dataResp.getErrMsg());
				param.setParam_3(rspCode);
				
				if(rspCode.equals(Constant.HEAD_RET_CODE_0000)){ //成功
					param.setParam_4(PaymoneyStateEnum.PAY_STATE_SUCCESS.getState());
				
				}else if(rspCode.equals(Constant.HEAD_RET_CODE_2000)){ //畅捷处理中
					param.setParam_4(PaymoneyStateEnum.PAY_STATE_PROCESS.getState());
				
				}else if(rspCode.equals(Constant.HEAD_RET_CODE_1000) || rspCode.equals(Constant.HEAD_RET_CODE_2004)
						|| rspCode.equals(Constant.HEAD_RET_CODE_2009)){ // 失败
					
					logger.info("调用畅捷查询, 处理失败： channel code：" + pay.getChannelCode() +", rsp code :" + rspCode + ", err msg:" + dataResp.getErrMsg());
					
					param.setParam_4(PaymoneyStateEnum.PAY_STATE_ERROR.getState());
				}
				
				autoPayMoneyMapper.updatePendingState(param);
			}
			
			if(StringUtils.isNotEmpty(pay.getChannelCode()) && pay.getPayChannel().equals(PayChannelEnum.CNANNEL_MS.getChannel())){
				MiShuaPayQueryParam qrpp = new MiShuaPayQueryParam();
				qrpp.setDealSsn(pay.getTixiLogno());
				qrpp.setPaySsn(pay.getChannelCode());
				ResultVal<String> rv = pospService.miShuaPayQuery(qrpp);
				
				param.setParam_2(rv.getMsg());
				param.setParam_3(rv.getValue());
				
				if(rv.isSuccess()){
					//若代付成功
					if(rv.getValue().equalsIgnoreCase("00")){
						param.setParam_4(PaymoneyStateEnum.PAY_STATE_SUCCESS.getState());
					}else
					//若代付正在处理中
					if(rv.getValue().equalsIgnoreCase("01")){
						param.setParam_4(PaymoneyStateEnum.PAY_STATE_PROCESS.getState());
					}else
					//若代付处理失败
					if(rv.getValue().equalsIgnoreCase("02")){
						param.setParam_4(PaymoneyStateEnum.PAY_STATE_ERROR.getState());
					}else{
						param.setParam_4(PaymoneyStateEnum.PAY_STATE_UNKNOWN.getState());
					}
				}else{
					//代付查询失败，保持处理中状态
					param.setParam_4(PaymoneyStateEnum.PAY_STATE_PROCESS.getState());
				}
				autoPayMoneyMapper.updatePendingState(param);
			}
		}
	}

	@Override
	public List<AutoPayMoneyVo> findErrList(BoAutoPayMoneyQuery query) {
		return autoPayMoneyMapper.findErrList(query);
	}

	@Override
	public int findErrListCount(BoAutoPayMoneyQuery query) {
		return autoPayMoneyMapper.findErrListCount(query);
	}

	/**
	 * 更新打款状态
	 */
	@Override
	public void signPay(String logon, boolean isSuccess){
		if(StringUtils.isNotEmpty(logon)){
			String tixiLogno = StringUtils.removeEnd(logon, ",").replaceAll(",", "','");
			
			SqlParam param = new SqlParam();
			param.setParam_2(tixiLogno);
			
			if(isSuccess){
				param.setParam_4(PaymoneyStateEnum.PAY_STATE_SUCCESS.getState());
			}else{
				param.setParam_4(PaymoneyStateEnum.PAY_STATE_ERROR.getState());
			}
			
			autoPayMoneyMapper.batchUpdatePayState(param);
		}
	}

	@Override
	public List<AutoPayMoneyVo> queryPaginationWithQuickPayList(Map<String, String> map) {
		return autoPayMoneyMapper.queryPaginationWithQuickPayList(map);
	}

	@Override
	public long queryTotalMoneyWithDate(Map<String, String> map) {
		Long totalMoney = autoPayMoneyMapper.queryAllPayMoneyWithDate(map);
		if (totalMoney == null) {
			return 0l;
		}
		return totalMoney;
	}

}
