package com.midai.pay.changjie.service.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.midai.pay.changjie.bean.G20007Dto_QryVirAcctPay;

import util.Cj;
import util.CjSignHelper;
import util.CjSignHelper.VerifyResult;
import util.ServiceLocation;
import util.U;



/**
 * 虚拟账户垫资付款結果查询 = "G20007";
 * @author SongCheng
 *
 */
public class G20007_QryVirAcctPay {
	private static Logger LOG = Logger.getLogger(G20007_QryVirAcctPay.class);
	private CjMsgSendServiceImpl cjMsgSendService = null;

	public G20007_QryVirAcctPay() {
		cjMsgSendService = ServiceLocation.getCjMsgSendService();
	}//constructor

	public G20007Dto_QryVirAcctPay do_虚拟账户垫资付款結果查询(G20007Dto_QryVirAcctPay data) {
		buildCjmsgAndSend(data);
		return data;
	}//method

	/** 组织Cj报文，并发送 */
	private void buildCjmsgAndSend(G20007Dto_QryVirAcctPay data) {
		try {
			String cjReqmsg = buildCjmsg(data);

			// 签名
			CjSignHelper singHelper = new CjSignHelper();
			String signMsg = singHelper.signXml$Add(cjReqmsg);

			String cjRespmsg = cjMsgSendService.sendAndGetString(signMsg);

			// 验证签名
			VerifyResult verifyResult = singHelper.verifyCjServerXml(cjRespmsg);
			if (!verifyResult.result) {
				throw new Exception("验证CJ返回数据的签名失败！" + verifyResult.errMsg);
			}

			parseCjMsgToDto(cjRespmsg, data);

		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}//method

	private void parseCjMsgToDto(String cjRespmsg, G20007Dto_QryVirAcctPay data) throws Exception {
		Document reqDoc = DocumentHelper.parseText(cjRespmsg);

		Element msgEl = reqDoc.getRootElement();

		Element infoEl = msgEl.element("INFO");
		data.setRetCode(infoEl.elementText("RET_CODE"));
		data.setErrMsg(infoEl.elementText("ERR_MSG"));
		data.setTimestamp(infoEl.elementText("TIMESTAMP"));

		Element bodyEl = msgEl.element("BODY");
		if (bodyEl != null) {
			data.bizRetcode = bodyEl.elementText("RET_CODE");
			data.bizErrmsg = bodyEl.elementText("ERR_MSG");
			data.payAcctNo = bodyEl.elementText("PAY_ACCT_NO");
			String payAcctBalance = bodyEl.elementText("PAY_ACCT_BALANCE");
			if (StringUtils.isNumeric(payAcctBalance)) {
				data.payAcctBalance = Long.parseLong(payAcctBalance);
			}
			data.recAcctNo = bodyEl.elementText("REC_ACCT_NO");
			data.recAcctName = bodyEl.elementText("REC_ACCT_NAME");
			String amount = bodyEl.elementText("AMOUNT");
			if (StringUtils.isNumeric(amount)) {
				data.amount = Long.parseLong(amount);
			}
			String charge = bodyEl.elementText("CHARGE");
			if (StringUtils.isNumeric(charge)) {
				data.charge = Long.parseLong(charge);
			}
			data.summary = bodyEl.elementText("SUMMARY");
			data.postscript = bodyEl.elementText("POSTSCRIPT");
			data.corpFlowNo = bodyEl.elementText("CORP_FLOW_NO");
			data.corpOrderNo = bodyEl.elementText("CORP_ORDER_NO");
			data.remark1 = bodyEl.elementText("REMARK1");
			data.remark2 = bodyEl.elementText("REMARK2");
			data.remark3 = bodyEl.elementText("REMARK3");
			data.remark4 = bodyEl.elementText("REMARK4");
			data.remark5 = bodyEl.elementText("REMARK5");
		}//if

		LOG.info("响应信息：retcode[" + data.getRetCode() + "], errmsg[" + data.getErrMsg() + "], bizRetcode[" + data.bizRetcode + "], bizErrmsg[" + data.bizErrmsg + "]");
		LOG.info("响应信息：" + U.build2StringShortStyle(data));
	}//method

	public String buildCjmsg(G20007Dto_QryVirAcctPay data) {
		Document doc = DocumentHelper.createDocument();
		Element msgEl = doc.addElement("MESSAGE");

		Element infoEl = msgEl.addElement("INFO");
		infoEl.addElement("TRX_CODE").setText(Cj.XMLMSG_TRANS_CODE_虚拟账户单笔付款結果查询);
		infoEl.addElement("VERSION").setText(Cj.XMLMSG_VERSION_01);
		infoEl.addElement("MERCHANT_ID").setText(U.nvl(data.getMertid()));
		infoEl.addElement("REQ_SN").setText(U.nvl(data.getReqSn()));
		infoEl.addElement("TIMESTAMP").setText(Cj.currTransDatetime_string());
		infoEl.addElement("SIGNED_MSG").setText("");

		Element bodyEl = msgEl.addElement("BODY");
		bodyEl.addElement("QRY_REQ_SN").setText(U.nvl(data.qryReqSn));

		String xml = Cj.formatXml_UTF8(doc);
		LOG.info("产生G20007虚拟账户垫资付款結果查询：" + U.substringByByte(xml, 512));
		return xml;
	}//method

}//class
