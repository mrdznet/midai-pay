package com.midai.pay.changjie.service.impl;

import java.net.URL;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Value;

import com.alibaba.dubbo.config.annotation.Service;
import com.google.gson.Gson;
import com.midai.pay.changjie.bean.G10001Bean;
import com.midai.pay.changjie.bean.G20001Bean;
import com.midai.pay.changjie.bean.G20004Bean;
import com.midai.pay.changjie.bean.G20015Bean;
import com.midai.pay.changjie.service.CjMsgSendService;

import util.Cj;
import util.CjSignHelper;
import util.CjSignHelper.VerifyResult;
import util.HttpPostBodyMethod;
import util.S;
import util.U;

/**
 * 向畅捷支付前置发送报文的服务类
 * @author SongCheng
 * 2015年9月19日下午7:24:18
 */
@Service
public class CjMsgSendServiceImpl implements CjMsgSendService{
	
	public static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(CjMsgSendServiceImpl.class);
	
	public static SimpleDateFormat sf = new SimpleDateFormat("yyMMddHHmmssSSS");
	
	public String sendAndGetString(String message) throws Exception {
		HttpClient client = new HttpClient();
		client.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, S.ENCODING_utf8);
		client.getParams().setSoTimeout(10 * 1000);

		URL url = new URL(Cj.CJPARAM_cjGatewayUrl);
		String urlstr = url.toString();
		LOG.info("提交地址" + urlstr);
		HttpPostBodyMethod post = new HttpPostBodyMethod(urlstr, message);

		int statusCode = client.executeMethod(post);
		if (statusCode != HttpStatus.SC_OK) {
			String err = "访问失败！！HTTP_STATUS=" + statusCode;
			LOG.error(err);
			LOG.error("返回内容为：" + post.getResponseBodyAsString());
			throw new HttpException(err);
		}//if

		String respData = post.getResponseBodyAsString();
		LOG.info("收到响应报文：" + respData);
		return respData;
	}//method

	public sendAndGetBytes_Response sendAndGetBytes(String message) throws Exception {
		HttpClient client = new HttpClient();
		client.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, S.ENCODING_utf8);
		client.getParams().setSoTimeout(10  * 1000);

		URL url = new URL(Cj.CJPARAM_cjGatewayUrl);
		String urlstr = url.toString();
		LOG.info("提交地址" + urlstr);
		HttpPostBodyMethod post = new HttpPostBodyMethod(urlstr, message);

		int statusCode = client.executeMethod(post);
		if (statusCode != HttpStatus.SC_OK) {
			String err = "访问失败！！HTTP_STATUS=" + statusCode;
			LOG.error(err);
			LOG.error("返回内容为：" + post.getResponseBodyAsString());
			throw new HttpException(err);
		}//if

		sendAndGetBytes_Response ret = new sendAndGetBytes_Response();

		ret.contentType = findHeaderAttr(post.getResponseHeader("Content-Type"));
		LOG.info("收到响应类型contentType：" + ret.contentType);

		ret.data = post.getResponseBody();
		ret.contentDisposition = findHeaderAttr(post.getResponseHeader("Content-Disposition"));
		ret.retcode = findHeaderAttr(post.getResponseHeader(Cj.PROP_NAME_RET_CODE));
		ret.errmsg = findHeaderAttr(post.getResponseHeader(Cj.PROP_NAME_ERR_MSG));
		//TODO 需要字符集转义
		return ret;
	}//method

	public class sendAndGetBytes_Response {
		public String retcode = "";
		public String errmsg = "";
		public byte[] data = {};
		public String contentType = "";
		public String contentDisposition = "";
	}//class

	private String findHeaderAttr(Header head) throws Exception {
		if (head == null)
			return "";
		String val = head.getValue();
		if (StringUtils.isBlank(val)) {
			return "";
		}
		String msg = URLDecoder.decode(val, S.ENCODING_utf8);
		LOG.info("收到HTTP-HEAD属性[" + head.getName() + "]=" + msg);
		return msg;
	}//method

	
	/** 组织Cj报文，并发送 */
	@Override
	public String G10002SendMessage(G10001Bean data) {
		String time = sf.format(new Date());
		Gson g = new Gson();
		try {
			data.setReqSn(Cj.CJPARAM_mertid + time + "01");
			
			data.setMertid(Cj.CJPARAM_mertid);
			data.setBusinessCode(Cj.CJPARAM_businessCode);
			data.setCorpAccNo(Cj.CJPARAM_corpAcctNo);
			data.setProductCode(Cj.CJPARAM_productCode);
			data.setSubMertid(Cj.CJPARAM_submertid_huaxia);
			
			String cjReqmsg = buildCjmsg(data);
			
			// 签名
			CjSignHelper singHelper = new CjSignHelper();
			String signMsg = singHelper.signXml$Add(cjReqmsg);
			
			// 发送报文
			String cjRespmsg = sendAndGetString(signMsg);

			// 验证签名
			VerifyResult verifyResult = singHelper.verifyCjServerXml(cjRespmsg);
			if (!verifyResult.result) {
				throw new Exception("验证CJ返回数据的签名失败！" + verifyResult.errMsg);
			}
			parseCjMsgToDto(cjRespmsg, data);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			data.setRetCode("cs_0000");//超时
		}
		
		return g.toJson(data);
	}
	
	private void parseCjMsgToDto(String cjRespmsg, G10001Bean data) throws Exception {
		Document reqDoc = DocumentHelper.parseText(cjRespmsg);

		Element msgEl = reqDoc.getRootElement();
		Element infoEl = msgEl.element("INFO");

		data.setRetCode(infoEl.elementText("RET_CODE"));
		data.setErrMsg(infoEl.elementText("ERR_MSG"));
		data.setTimestamp(infoEl.elementText("TIMESTAMP"));
		LOG.info("响应信息：retcode[" + data.getRetCode() + "], errmsg[" + data.getErrMsg() + "]");
	}//method

	private String buildCjmsg(G10001Bean data) {
		Document doc = DocumentHelper.createDocument();
		Element msgEl = doc.addElement("MESSAGE");

		Element infoEl = msgEl.addElement("INFO");
		infoEl.addElement("TRX_CODE").setText(Cj.XMLMSG_TRANS_CODE_单笔实时付款);
		infoEl.addElement("VERSION").setText(Cj.XMLMSG_VERSION_01);
		infoEl.addElement("MERCHANT_ID").setText(U.nvl(data.getMertid()));
		infoEl.addElement("REQ_SN").setText(U.nvl(data.getReqSn()));
		infoEl.addElement("TIMESTAMP").setText(U.getCurrentTimestamp());
		infoEl.addElement("SIGNED_MSG").setText("");

		Element bodyEl = msgEl.addElement("BODY");
		bodyEl.addElement("BUSINESS_CODE").setText(U.nvl(data.getBusinessCode()));
		bodyEl.addElement("CORP_ACCT_NO").setText(U.nvl(data.getCorpAccNo()));
		bodyEl.addElement("PRODUCT_CODE").setText(U.nvl(data.getProductCode()));
		bodyEl.addElement("ACCOUNT_PROP").setText(U.nvl(data.getAccountProp()));
		bodyEl.addElement("SUB_MERCHANT_ID").setText(U.nvl(data.getSubMertid()));
		bodyEl.addElement("BANK_GENERAL_NAME").setText(U.nvl(data.getBankGeneralName()));
		bodyEl.addElement("ACCOUNT_TYPE").setText(U.nvl(data.getAccountType()));
		bodyEl.addElement("ACCOUNT_NO").setText(U.nvl(data.getAccountNo()));
		bodyEl.addElement("ACCOUNT_NAME").setText(U.nvl(data.getAccountName()));
		bodyEl.addElement("PROVINCE").setText(U.nvl(data.getProvince()));
		bodyEl.addElement("CITY").setText(U.nvl(data.getCity()));
		bodyEl.addElement("BANK_NAME").setText(U.nvl(data.getBankName()));
		if(StringUtils.isNotBlank(data.getBankCode())){
			bodyEl.addElement("BANK_CODE").setText(U.nvl(data.getBankCode()));
		}
		if(StringUtils.isNotBlank(data.getDrctBankCode())){
			bodyEl.addElement("DRCT_BANK_CODE").setText(U.nvl(data.getDrctBankCode()));
		}
		bodyEl.addElement("PROTOCOL_NO").setText(U.nvl(data.getProtocolNo()));
		bodyEl.addElement("CURRENCY").setText(U.nvl(data.getCurrency()));
		bodyEl.addElement("AMOUNT").setText(U.nvl(data.getAmount()));
		bodyEl.addElement("ID_TYPE").setText(U.nvl(data.getIdType()));
		bodyEl.addElement("ID").setText(U.nvl(data.getId()));
		bodyEl.addElement("TEL").setText(U.nvl(data.getTel()));
		bodyEl.addElement("CORP_FLOW_NO").setText(U.nvl(data.getCorpFlowNo()));
		bodyEl.addElement("SUMMARY").setText(U.nvl(data.getSummary()));
		bodyEl.addElement("POSTSCRIPT").setText(U.nvl(data.getPostscript()));
		String xml = Cj.formatXml_UTF8(doc);
		LOG.info("产生G10002单笔实时收款：" + U.substringByByte(xml, 512));
		return xml;
	}//method
	
	/** 组织Cj报文，并发送 */
	@Override
	public G20001Bean G20001SendMessage(G20001Bean data) {
		try {
			String time = sf.format(new Date());
			
			data.setReqSn(Cj.CJPARAM_mertid + time + "01");
			
			data.setMertid(Cj.CJPARAM_mertid);
			
			String cjReqmsg = buildCjmsg(data);

			// 签名
			CjSignHelper singHelper = new CjSignHelper();
			String signMsg = singHelper.signXml$Add(cjReqmsg);
			
			// 发送报文
			String cjRespmsg = sendAndGetString(signMsg);

			// 验证签名
			VerifyResult verifyResult = singHelper.verifyCjServerXml(cjRespmsg);
			if (!verifyResult.result) {
				throw new Exception("验证CJ返回数据的签名失败！" + verifyResult.errMsg);
			}
			parseCjMsgToDto(cjRespmsg, data);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			data.setRetCode("cs_0000");//超时
		}
		return data;
	}//method
	
	private String buildCjmsg(G20001Bean data) {
		Document doc = DocumentHelper.createDocument();
		Element msgEl = doc.addElement("MESSAGE");

		Element infoEl = msgEl.addElement("INFO");
		infoEl.addElement("TRX_CODE").setText(Cj.XMLMSG_TRANS_CODE_实时交易结果查询);
		infoEl.addElement("VERSION").setText(Cj.XMLMSG_VERSION_01);
		infoEl.addElement("MERCHANT_ID").setText(U.nvl(data.getMertid()));
		infoEl.addElement("REQ_SN").setText(U.nvl(data.getReqSn()));
		infoEl.addElement("TIMESTAMP").setText(U.getCurrentTimestamp());
		infoEl.addElement("SIGNED_MSG").setText("");

		Element bodyEl = msgEl.addElement("BODY");
		bodyEl.addElement("QRY_REQ_SN").setText(U.nvl(data.getQryReqSn()));

		String xml = Cj.formatXml_UTF8(doc);
		LOG.info("产生G20001文件上传：" + U.substringByByte(xml, 512));
		return xml;
	}//method
	
	private void parseCjMsgToDto(String cjRespmsg, G20001Bean data) throws Exception {
		Document reqDoc = DocumentHelper.parseText(cjRespmsg);

		Element msgEl = reqDoc.getRootElement();
		Element infoEl = msgEl.element("INFO");

		data.setRetCode(infoEl.elementText("RET_CODE"));
		data.setErrMsg(infoEl.elementText("ERR_MSG"));
		data.setTimestamp(infoEl.elementText("TIMESTAMP"));
		
		Element bodyEl = msgEl.element("BODY");
		if (bodyEl == null) {
			return;
		}
		data.setCharge(StringUtils.isBlank(bodyEl.elementText("CHARGE"))?0:Long.parseLong(bodyEl.elementText("CHARGE")));
		data.setCorpAcctNo(bodyEl.elementText("CORP_ACCT_NO"));
		data.setCorpAcctName(bodyEl.elementText("CORP_ACCT_NAME"));
		data.setAccountNo(bodyEl.elementText("ACCOUNT_NO"));
		data.setAccountName(bodyEl.elementText("ACCOUNT_NAME"));
		data.setAmount(StringUtils.isBlank(bodyEl.elementText("AMOUNT"))?0:Long.parseLong(bodyEl.elementText("AMOUNT")));
		data.setSummary(bodyEl.elementText("SUMMARY"));
		data.setCorpFlowNo(bodyEl.elementText("CORP_FLOW_NO"));
		data.setPostscript(bodyEl.elementText("POSTSCRIPT"));
		data.setBodyRetCode(bodyEl.elementText("RET_CODE"));
		data.setBodyErrMsg(bodyEl.elementText("ERR_MSG"));
		LOG.info("响应信息：retcode[" + data.getRetCode() + "], errmsg[" + data.getErrMsg() + "]");
	}//method
	
	
	/** 组织Cj报文，并发送 */
	public G20004Bean G20004SendMessage(G20004Bean data) {
		try {
			
			data.setMertid(Cj.CJPARAM_mertid);
			data.setAccountNo(Cj.CJPARAM_corpAcctNo);
			
			String cjReqmsg = buildCjmsg(data);

			// 签名
			CjSignHelper singHelper = new CjSignHelper();
			String signMsg = singHelper.signXml$Add(cjReqmsg);
			
			// 发送报文
			String cjRespmsg = sendAndGetString(signMsg);

			// 验证签名
			VerifyResult verifyResult = singHelper.verifyCjServerXml(cjRespmsg);
			if (!verifyResult.result) {
				throw new Exception("验证CJ返回数据的签名失败！" + verifyResult.errMsg);
			}
			parseCjMsgToDto(cjRespmsg, data);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			data.setRetCode("cs_0000");//超时
		}
		return data;
	}//method
	
	private void parseCjMsgToDto(String cjRespmsg, G20004Bean data) throws Exception {
		Document reqDoc = DocumentHelper.parseText(cjRespmsg);

		Element msgEl = reqDoc.getRootElement();
		Element infoEl = msgEl.element("INFO");

		data.setRetCode(infoEl.elementText("RET_CODE"));
		data.setErrMsg(infoEl.elementText("ERR_MSG"));
		data.setTimestamp(infoEl.elementText("TIMESTAMP"));
		LOG.info("响应信息：retcode[" + data.getRetCode() + "], errmsg[" + data.getErrMsg() + "]");
		
		Element bodyEl = msgEl.element("BODY");
		if (bodyEl == null) {
			return;
		}
		
		Element pageEl = bodyEl.element("PAGE");
		if(null!=pageEl){
			data.setTtlPage(pageEl.elementText("TTL_PAGE"));
			data.setTtlCnt(pageEl.elementText("TTL_CNT"));
			data.setCurrPage(pageEl.elementText("CURR_PAGE"));
			data.setCurrCnt(pageEl.elementText("CURR_CNT"));
		}
		
		data.setRetDate(bodyEl.elementText("RET_DATA"));
		
	}//method
	
	private String buildCjmsg(G20004Bean data) throws ParseException{
		Document doc = DocumentHelper.createDocument();
		Element msgEl = doc.addElement("MESSAGE");

		Element infoEl = msgEl.addElement("INFO");
		infoEl.addElement("TRX_CODE").setText(Cj.XMLMSG_TRANS_CODE_交易明细查询);
		infoEl.addElement("VERSION").setText(Cj.XMLMSG_VERSION_01);
		infoEl.addElement("MERCHANT_ID").setText(U.nvl(data.getMertid()));
		infoEl.addElement("REQ_SN").setText(U.nvl(data.getReqSn()));
		infoEl.addElement("TIMESTAMP").setText(U.getCurrentTimestamp());
		infoEl.addElement("SIGNED_MSG").setText("");

		Element bodyEl = msgEl.addElement("BODY");

		bodyEl.addElement("ACCOUNT_NO").setText(U.nvl(data.getAccountNo()));
		
		/*SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
		String stime = data.getStartDay();
		Date sdate = sdf.parse(stime);		
		DateFormat dateFormat =new SimpleDateFormat("yyyyMMddHHmmss");
		String startTime= dateFormat.format(sdate);*/
		bodyEl.addElement("START_DAY").setText(data.getStartDay());
	/*	String etime = data.getEndDay();
		Date edate = sdf.parse(etime);
		String endTime = dateFormat.format(edate);*/
		bodyEl.addElement("END_DAY").setText(data.getEndDay());
		bodyEl.addElement("STATUS").setText(U.nvl(data.getStatus()));
		bodyEl.addElement("QRY_PAGE").setText(U.nvl(data.getQryPage()));				
		
		String xml = Cj.formatXml_UTF8(doc);
		LOG.info("产生G20004交易明细查询：" + U.substringByByte(xml, 1024));
		return xml;
	}
	/** 组织Cj报文，并发送 */
	public G20015Bean G20015SendMessage(G20015Bean data) {
		try {
			
			data.setMertid(Cj.CJPARAM_mertid);
			data.setAccountNo(Cj.CJPARAM_corpAcctNo);
			//data.setAccountName(Cj.CJPARAM_corpAcctNo);
			
			String cjReqmsg = buildCjmsg(data);

			// 签名
			CjSignHelper singHelper = new CjSignHelper();
			String signMsg = singHelper.signXml$Add(cjReqmsg);
			
			// 发送报文
			String cjRespmsg = sendAndGetString(signMsg);

			// 验证签名
			VerifyResult verifyResult = singHelper.verifyCjServerXml(cjRespmsg);
			if (!verifyResult.result) {
				throw new Exception("验证CJ返回数据的签名失败！" + verifyResult.errMsg);
			}
			parseCjMsgToDto(cjRespmsg, data);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			data.setRetCode("cs_0000");//超时
		}
		return data;
	}//method
	
	private void parseCjMsgToDto(String cjRespmsg, G20015Bean data) throws Exception {
		Document reqDoc = DocumentHelper.parseText(cjRespmsg);

		Element msgEl = reqDoc.getRootElement();
		Element infoEl = msgEl.element("INFO");

		data.setRetCode(infoEl.elementText("RET_CODE"));
		data.setErrMsg(infoEl.elementText("ERR_MSG"));
		data.setTimestamp(infoEl.elementText("TIMESTAMP"));
		LOG.info("响应信息：retcode[" + data.getRetCode() + "], errmsg[" + data.getErrMsg() + "]");
		
		Element bodyEl = msgEl.element("BODY");
		if (bodyEl == null) {
			return;
		}
		data.setUsableBalance(bodyEl.elementText("USABLE_BALANCE"));
		data.setFrozenBalance(bodyEl.elementText("FROZEN_BALANCE"));
		data.setConfirm_balance(bodyEl.elementText("CONFIRM_BALANCE"));
		data.setTotalBalance(bodyEl.elementText("TOTAL_BALANCE"));
		data.setBodyRetCode(bodyEl.elementText("RET_CODE"));
		data.setBodyErrMsg(bodyEl.elementText("ERR_MSG"));
	}//method
	
	private String buildCjmsg(G20015Bean data) throws ParseException{
		Document doc = DocumentHelper.createDocument();
		Element msgEl = doc.addElement("MESSAGE");

		Element infoEl = msgEl.addElement("INFO");
		infoEl.addElement("TRX_CODE").setText(Cj.XMLMSG_TRANS_CODE_银行余额查询);
		infoEl.addElement("VERSION").setText(Cj.XMLMSG_VERSION_01);
		infoEl.addElement("MERCHANT_ID").setText(U.nvl(data.getMertid()));
		infoEl.addElement("REQ_SN").setText(U.nvl(data.getReqSn()));
		infoEl.addElement("TIMESTAMP").setText(U.getCurrentTimestamp());
		infoEl.addElement("SIGNED_MSG").setText("");

		Element bodyEl = msgEl.addElement("BODY");

		bodyEl.addElement("CORP_ACCOUNT_NO").setText(U.nvl(data.getAccountNo()));
		//bodyEl.addElement("ACCOUNT_NAME").setText(U.nvl(data.getAccountName()));				
		
		String xml = Cj.formatXml_UTF8(doc);
		LOG.info("产生G50001银行余额查询：" + U.substringByByte(xml, 1024));
		return xml;
	}
	
	
	/** 组织Cj报文，并发送 */
	public sendAndGetBytes_Response G40003SendMessage(String billType, String billMonth, String billDay,String reqNo) {
		sendAndGetBytes_Response ret = null;
		try {
			
			String mertid = Cj.CJPARAM_mertid;
			
			String cjReqmsg = buildCjmsg(mertid,billType,billMonth,billDay,reqNo);

			// 签名
			CjSignHelper singHelper = new CjSignHelper();
			String signMsg = singHelper.signXml$Add(cjReqmsg);
			
			// 发送报文
			ret = sendAndGetBytes(signMsg);

		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			ret.retcode = "cs_0000";
		}
		return ret;
	}//method
	
	private String buildCjmsg(String mertid, String billType, String billMonth, String billDay,String reqNo) {
		Document doc = DocumentHelper.createDocument();
		Element msgEl = doc.addElement("MESSAGE");

		Element infoEl = msgEl.addElement("INFO");
		infoEl.addElement("TRX_CODE").setText(Cj.XMLMSG_TRANS_CODE_对账文件下载);
		infoEl.addElement("VERSION").setText(Cj.XMLMSG_VERSION_01);
		infoEl.addElement("MERCHANT_ID").setText(U.nvl(mertid));
		infoEl.addElement("REQ_SN").setText(U.nvl(reqNo));
		infoEl.addElement("TIMESTAMP").setText(U.getCurrentTimestamp());
		infoEl.addElement("SIGNED_MSG").setText("");

		Element bodyEl = msgEl.addElement("BODY");
		bodyEl.addElement("BILL_TYPE").setText(U.nvl(billType));
		
		if(StringUtils.isNotEmpty(billMonth)){
			bodyEl.addElement("BILL_MONTH").setText(U.nvl(billMonth));
		}
		if(StringUtils.isNotEmpty(billDay)){
			bodyEl.addElement("BILL_DAY").setText(U.nvl(billDay));
		}
		
		String xml = Cj.formatXml_UTF8(doc);
		LOG.info("产生G40003对账文件下载：" + U.substringByByte(xml, 512));
		return xml;
	}//method

	
}//class
