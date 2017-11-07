/**
 * Project Name:midai-pay-posp-service
 * File Name:MidaiPayHsmTemplate.java
 * Package Name:com.midai.pay.posp.config
 * Date:2016年11月16日上午9:25:32
 * Copyright (c) 2016, Shanghai Law Cloud Technology Co., Ltd. All Rights Reserved.
 *
*/

package com.midai.pay.posp.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mifu.hsmj.HsmApp;
import com.mifu.hsmj.HsmConst;
import com.mifu.hsmj.HsmMessage;

/**
 * ClassName:MidaiPayHsmTemplate <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2016年11月16日 上午9:25:32 <br/>
 * 
 * @author 陈勋
 * @version
 * @since JDK 1.7
 * @see
 */

public class MidaiPayHsmTemplate {

	private Logger logger = LoggerFactory.getLogger(MidaiPayHsmTemplate.class);

	private final HsmApp hsmApp;

	public MidaiPayHsmTemplate(MidaiPayHsmProperties midaiPayHsmProperties) {
		hsmApp = new HsmApp(midaiPayHsmProperties);
	};

	public void exec(HsmMessage message, MidaiPayHsmCallBack call) {
		int nRet;
		byte[] bSecBufferIn = message.makeMessage();
		byte[] bSecBufferOut = new byte[HsmConst.SECBUF_MAX_SIZE];

		logger.info("加密机调用:" + Pay8583Util.bytesToHexString(bSecBufferIn));

		nRet = hsmApp.HSM_LINK(bSecBufferIn, bSecBufferIn.length, bSecBufferOut);
		logger.info("加密机响应:" + Pay8583Util.bytesToHexString(bSecBufferOut));

		if (0 == nRet) {
			if (bSecBufferOut[0] == 0x41) {
				// 结果处理
				call.call(bSecBufferOut);
			}
		} else {
			throw new RuntimeException("加密机调用失败");
		}

	}
	
	public byte[] exec(HsmMessage message) {
		int nRet;
		byte[] bSecBufferIn = message.makeMessage();
		byte[] bSecBufferOut = new byte[HsmConst.SECBUF_MAX_SIZE];

		logger.info("加密机调用:" + Pay8583Util.bytesToHexString(bSecBufferIn));

		nRet = hsmApp.HSM_LINK(bSecBufferIn, bSecBufferIn.length, bSecBufferOut);
		logger.info("加密机响应:" + Pay8583Util.bytesToHexString(bSecBufferOut));
        
		if (0 == nRet) {
			if (bSecBufferOut[0] == 0x41) {
				// 结果处理
				return bSecBufferOut;
			}
		} else {
			throw new RuntimeException("加密机调用失败");
		}
		return bSecBufferOut;

	}

}
