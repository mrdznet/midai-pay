package com.mifu.hsmj;

import java.io.*;
import java.util.Properties;

import com.midai.pay.posp.config.MidaiPayHsmProperties;

import java.net.*;

public class HsmSession {
	private String sMainIP;
	private String sBackIP;
	private int nPort;
	private int nTimeOut;

	private Socket iSocketHandle = null;
	private InputStream iInputStream = null;
	private OutputStream iOutputStream = null;

	private int iLastErr = -1;

	// 创建连接
	public HsmSession(MidaiPayHsmProperties midaiPayHsmProperties) {
		iLastErr = 0;

		//初始化加密机连接
		try {
			InitSession(midaiPayHsmProperties);
		}
		catch (Exception e) {
			iLastErr = HsmConst.ERR_CONFIG_FILE;
		}
	}

	private void InitSession(MidaiPayHsmProperties midaiPayHsmProperties) throws Exception {

		//1、获取配制文件

			sMainIP =  midaiPayHsmProperties.getMain();
			sBackIP = midaiPayHsmProperties.getBack();
			nPort=midaiPayHsmProperties.getPort();
			nTimeOut = midaiPayHsmProperties.getTimeout();


/* 		
 		System.out.println("MAIN:" + sMainIP);
 		System.out.println("BACK:" + sBackIP);
 		System.out.println("PORT:" + nPort);
 		System.out.println("TIMEOUT:" + nTimeOut);
*/
		//2、连接加密机
		connect(sMainIP,nPort,nTimeOut);
		if(iSocketHandle == null){
			connect(sBackIP,nPort,nTimeOut);
			if(iSocketHandle == null){
				throw new Exception("无法与加密机建立连接.");
			}
		}        
	}

    public void connect(String sIP,int iPort, int iTimeout){
        try {
            iSocketHandle = new Socket();
            InetSocketAddress hsmAddr = new InetSocketAddress(sIP, iPort);
            iSocketHandle.connect(hsmAddr, iTimeout);
            iSocketHandle.setSoTimeout(iTimeout);
            iSocketHandle.setKeepAlive(true);
            iSocketHandle.setReceiveBufferSize(HsmConst.SECBUF_MAX_SIZE);
            iSocketHandle.setTcpNoDelay(true);
            iSocketHandle.setSoLinger(true, 0);//time_wait
            iInputStream = iSocketHandle.getInputStream();
            iOutputStream = iSocketHandle.getOutputStream();
        }
        catch (IOException e) {
            ReleaseSession();
        }
	}

	// 发送数据到加密机
	public int SendData(byte[] byteOut, int nLength) {
//		HsmApp.OutputDataHex("Data Send to HSM:",byteOut,nLength);

		try {
			iOutputStream.write(byteOut, 0, nLength);
		}
		catch (Exception e) {
			return HsmConst.ERR_SENDTO_HSM;
		}
		catch (Error err) {
			return HsmConst.ERR_SENDTO_HSM;
		}
        
		return HsmConst.T_SUCCESS;
	}

	// 从加密机接收数据
	public int RecvData(byte[] byteIn) {
		int rcvLen = -1;

		try {
			rcvLen = iInputStream.read(byteIn, 0, HsmConst.SECBUF_MAX_SIZE);
		}
		catch (Exception e) {
			return HsmConst.ERR_RECVFORM_HSM;
		}
		catch (Error err) {
			return HsmConst.ERR_RECVFORM_HSM;
		}

//		HsmApp.OutputDataHex("Data Recv From HSM:",byteIn,rcvLen);
		return HsmConst.T_SUCCESS;
	}

	public void ReleaseSession() {
		if (iInputStream != null) {
			try {
				iInputStream.close();
			}
			catch (Exception e) {}
			iInputStream = null;
		}
	        
		if (iOutputStream != null) {
			try {
				iOutputStream.close();
			}
			catch (Exception e) {}
			iOutputStream = null;
		}
	        
		if (iSocketHandle != null) {
			try {
				iSocketHandle.close();
			}
			catch (Exception e) {}
			iSocketHandle = null;
		}
	}

	public int GetLastError() {
		return iLastErr;
	}


	public void SetErrCode(int nErrCode) {
		iLastErr = nErrCode;
		return;
	}

	public String ParseErrCode(int nErrCode) {
        String sMeaning;
        switch (nErrCode) {
            case 0:
                sMeaning = "0x" + Integer.toHexString(nErrCode) + "操作正确,状态正常";
                break;
            case HsmConst.ERR_CONFIG_FILE:
                sMeaning = "0x" + Integer.toHexString(nErrCode) + "配置文件异常";
                break;
            case HsmConst.ERR_CONNECT_HSM:
                sMeaning = "0x" + Integer.toHexString(nErrCode) + "连接密码机失败.";
                break;
            case HsmConst.ERR_SENDTO_HSM:
                sMeaning = "0x" + Integer.toHexString(nErrCode) + "发送数据至密码机失败.";
                break;
            case HsmConst.ERR_RECVFORM_HSM:
                sMeaning = "0x" + Integer.toHexString(nErrCode) + "接收密码机数据失败.";
                break;
            case HsmConst.ERR_SESSION_END:
                sMeaning = "0x" + Integer.toHexString(nErrCode) + "连接已关闭.";
                break;
            case HsmConst.ERR_HANDLE_FAULT:
                sMeaning = "0x" + Integer.toHexString(nErrCode) + "连接句柄状态异常.";
                break;
            default:
                sMeaning = "0x" + Integer.toHexString(nErrCode) + "异常操作,检查密码机日志.";
                break;
        }
        return sMeaning;
	}
}