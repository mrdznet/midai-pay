package com.mifu.hsmj;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.midai.pay.posp.config.MidaiPayHsmProperties;

public class HsmApp {
	
	private final MidaiPayHsmProperties midaiPayHsmProperties;
	private static Logger logger=LoggerFactory.getLogger(HsmApp.class);
	
	
	// 构造函数：
	public HsmApp(MidaiPayHsmProperties midaiPayHsmProperties) {
		Assert.notNull(midaiPayHsmProperties);
		this.midaiPayHsmProperties=midaiPayHsmProperties;
	}
	
	



	static public boolean Str2Hex(byte[] in, byte[] out, int len) {
		byte[] asciiCode = { 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f };

		if (len > in.length) {
			return false;
		}

		if (len % 2 != 0) {
			return false;
		}

		byte[] temp = new byte[len];

		for (int i = 0; i < len; i++) {
			if (in[i] >= 0x30 && in[i] <= 0x39) {
				temp[i] = (byte) (in[i] - 0x30);
			} else if (in[i] >= 0x41 && in[i] <= 0x46) {
				temp[i] = asciiCode[in[i] - 0x41];
			} else if (in[i] >= 0x61 && in[i] <= 0x66) {
				temp[i] = asciiCode[in[i] - 0x61];
			} else {
				return false;
			}
		}

		for (int i = 0; i < len / 2; i++) {
			out[i] = (byte) (temp[2 * i] * 16 + temp[2 * i + 1]);
		}

		return true;
	}

	static public boolean Hex2Str(byte[] in, byte[] out, int len) {
		byte[] asciiCode = { 0x41, 0x42, 0x43, 0x44, 0x45, 0x46 };

		if (len > in.length) {
			return false;
		}

		byte[] temp = new byte[2 * len];

		for (int i = 0; i < len; i++) {
			temp[2 * i] = (byte) ((in[i] & 0xf0) / 16);
			temp[2 * i + 1] = (byte) (in[i] & 0x0f);
		}

		for (int i = 0; i < 2 * len; i++) {
			if (temp[i] <= 9 && temp[i] >= 0) {
				out[i] = (byte) (temp[i] + 0x30);
			} else {
				out[i] = asciiCode[temp[i] - 0x0a];
			}
		}

		return true;
	}

	public static String byte2hex(byte[] b) { // 二行制转字符串
		String hs = "";
		String stmp = "";

		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1) {
				hs = hs + "0" + stmp;
			} else {
				hs = hs + stmp;

			}
			if (n < b.length - 1) {
				hs = hs + " ";
			}
		}

		return hs.toUpperCase();

	}
	
	public static String byte2hexWithoutBlank(byte[] b){
		return byte2hex(b).replaceAll(" ", "");
	}

	// 4字节长度
	/*
	 * private int HSM_LINK(HsmSession hSession, byte[] bSecBufferIn, int
	 * iSndLen, byte[] bSecBufferOut) { int nResult, nRetLen; byte[] tmpBuffer =
	 * new byte[HsmConst.SECBUF_MAX_SIZE]; // OutputDataHex("HSM_LINK
	 * Send:",bSecBufferIn,iSndLen); tmpBuffer[0] = (byte)((iSndLen >> 24) &
	 * 0xFF); tmpBuffer[1] = (byte)((iSndLen >> 16) & 0xFF); tmpBuffer[2] =
	 * (byte)((iSndLen >> 8) & 0xFF); tmpBuffer[3] = (byte)(iSndLen & 0xFF);
	 * 
	 * System.arraycopy(bSecBufferIn,0,tmpBuffer,4,iSndLen); // send message
	 * nResult = hSession.SendData(tmpBuffer, iSndLen + 4); if (nResult !=
	 * HsmConst.T_SUCCESS) { return nResult; }
	 * 
	 * //receive message nResult = hSession.RecvData(tmpBuffer); if(nResult !=
	 * HsmConst.T_SUCCESS) { return nResult; }
	 * 
	 * if (tmpBuffer[4] != 'A') { return tmpBuffer[5]; }
	 * 
	 * nRetLen = ((tmpBuffer[0] & 0xff )<< 24) | ((tmpBuffer[1] & 0xff) <<
	 * 16) | ((tmpBuffer[2] & 0xff) << 8) | (tmpBuffer[3] & 0xff);
	 * 
	 * System.arraycopy(tmpBuffer,4,bSecBufferOut,0,nRetLen); //
	 * OutputDataHex("HSM_LINK Receive:",bSecBufferOut,nRetLen); return
	 * HsmConst.T_SUCCESS; }
	 * 
	 */
	
	// 无长度 短连接
	public int HSM_LINK(byte[] bSecBufferIn, int iSndLen, byte[] bSecBufferOut) {
		int nResult, nRet;
		byte[] tmpBuffer = new byte[HsmConst.SECBUF_MAX_SIZE];
		HsmSession hSession;

		hSession = new HsmSession(midaiPayHsmProperties);

		// 如有错误号，直接返回
		nRet = hSession.GetLastError();
		if (nRet != 0) {
			logger.info(hSession.ParseErrCode(nRet));
			return nRet;
		}

		System.arraycopy(bSecBufferIn, 0, tmpBuffer, 0, iSndLen);
		// send message
		//OutputDataHex("indata ", bSecBufferIn, iSndLen);
		nResult = hSession.SendData(tmpBuffer, iSndLen);
		if (nResult != HsmConst.T_SUCCESS) {
			return nResult;
		}

		tmpBuffer = new byte[HsmConst.SECBUF_MAX_SIZE];
		
		// receive message
		nResult = hSession.RecvData(tmpBuffer);
		if (nResult != HsmConst.T_SUCCESS) {
			return nResult;
		}

		// OutputDataHex("outdata ", tmpBuffer, 17);
		
//		if(tmpBuffer[0] != 'A'){
//			return tmpBuffer[1];
//		}
		
		System.arraycopy(tmpBuffer, 0, bSecBufferOut, 0, HsmConst.SECBUF_MAX_SIZE);

		// OutputDataHex("outdata ", bSecBufferOut, 17);

		hSession.ReleaseSession();

		return HsmConst.T_SUCCESS;
	}


}
