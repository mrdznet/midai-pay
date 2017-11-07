package com.midai.pay.changjie.service.impl;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.midai.pay.changjie.bean.G10007Dto_VirAcctPay;

import util.Cj;
import util.CjSignHelper;
import util.CjSignHelper.VerifyResult;
import util.ServiceLocation;
import util.U;

/**
 * 虚拟账户垫资付款 = "G10007";
 *
 * @author SongCheng
 */
public class G10007_VirAcctPay {
	private static Logger LOG = Logger.getLogger(G10007_VirAcctPay.class);
	private CjMsgSendServiceImpl cjMsgSendService = null;

	public G10007_VirAcctPay() {
		cjMsgSendService = ServiceLocation.getCjMsgSendService();
	}//constructor

	public G10007Dto_VirAcctPay do_虚拟账户垫资付款(G10007Dto_VirAcctPay data) {
		// 将数据对象拼装成 CJ的XML报文
		buildCjmsgAndSend(data);
		return data;
	}//method

	/** 组织Cj报文，并发送 */
	private void buildCjmsgAndSend(G10007Dto_VirAcctPay data) {
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

	private void parseCjMsgToDto(String cjRespmsg, G10007Dto_VirAcctPay data) throws Exception {
		Document reqDoc = DocumentHelper.parseText(cjRespmsg);

		Element msgEl = reqDoc.getRootElement();
		Element infoEl = msgEl.element("INFO");

		data.setRetCode(infoEl.elementText("RET_CODE"));
		data.setErrMsg(infoEl.elementText("ERR_MSG"));
		data.setTimestamp(infoEl.elementText("TIMESTAMP"));
		LOG.info("响应信息：retcode[" + data.getRetCode() + "], errmsg[" + data.getErrMsg() + "]");
	}//method

	public String buildCjmsg(G10007Dto_VirAcctPay data) {
		Document doc = DocumentHelper.createDocument();
		Element msgEl = doc.addElement("MESSAGE");

		Element infoEl = msgEl.addElement("INFO");
		infoEl.addElement("TRX_CODE").setText(Cj.XMLMSG_TRANS_CODE_虚拟账户单笔付款);
		infoEl.addElement("VERSION").setText(Cj.XMLMSG_VERSION_01);
		infoEl.addElement("MERCHANT_ID").setText(U.nvl(data.getMertid()));
		infoEl.addElement("REQ_SN").setText(U.nvl(data.getReqSn()));
		infoEl.addElement("TIMESTAMP").setText(U.nvl(data.getTimestamp()));
		infoEl.addElement("SIGNED_MSG").setText("");

		Element bodyEl = msgEl.addElement("BODY");
		bodyEl.addElement("BUSINESS_CODE").setText(U.nvl(data.businessCode));
		bodyEl.addElement("PRODUCT_CODE").setText(Cj.XMLMSG_PRODUCT_CODE_虚拟账户垫资付款);
		bodyEl.addElement("PUBLIC_PERSONAL").setText(Cj.ACCOUNT_PROP_对私);
		bodyEl.addElement("SUB_MERCHANT_ID").setText(U.nvl(data.getSubMertid()));
		bodyEl.addElement("PAY_ACCT_NO").setText("");
		bodyEl.addElement("REC_ACCT_NO").setText(U.nvl(data.recAcctNo));
		bodyEl.addElement("REC_ACCT_NAME").setText(U.nvl(data.recAcctName));
		bodyEl.addElement("BANK_NAME").setText(U.nvl(data.bankName));
		bodyEl.addElement("BANK_CODE").setText(U.nvl(data.bankCode));
		bodyEl.addElement("PROVINCE").setText("");
		bodyEl.addElement("CITY").setText("");
		bodyEl.addElement("CURRENCY").setText(Cj.CURRENCY_人民币);
		bodyEl.addElement("AMOUNT").setText(U.nvl(data.amount));
		bodyEl.addElement("ID_TYPE").setText("");
		bodyEl.addElement("ID").setText("");
		bodyEl.addElement("TEL").setText("");
		bodyEl.addElement("SUMMARY").setText(U.nvl(data.summary));
		bodyEl.addElement("POSTSCRIPT").setText("");
		bodyEl.addElement("CORP_FLOW_NO").setText(U.nvl(data.corpFlowNo));
		bodyEl.addElement("CORP_ORDER_NO").setText(U.nvl(data.corpOrderNo));
		bodyEl.addElement("REMARK1").setText(U.nvl(data.remark1));
		bodyEl.addElement("REMARK2").setText(U.nvl(data.remark2));
		bodyEl.addElement("REMARK3").setText(U.nvl(data.remark3));
		bodyEl.addElement("REMARK4").setText(U.nvl(data.remark4));
		bodyEl.addElement("REMARK5").setText(U.nvl(data.remark5));

		String xml = Cj.formatXml_UTF8(doc);
		LOG.info("产生G10007虚拟账户垫资付款：" + U.substringByByte(xml, 512));
		return xml;
	}//method

}//class
