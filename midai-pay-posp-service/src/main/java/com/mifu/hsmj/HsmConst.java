package com.mifu.hsmj;

public class HsmConst {
	public static final int SECBUF_MAX_SIZE = 2048;

	public static final int T_SUCCESS = 0;
	public static final int T_FAIL = 1;

	public static final int ERR_CONFIG_FILE = 0x00000090;
	public static final int ERR_CONNECT_HSM = 0x00000091;
	public static final int ERR_SENDTO_HSM = 0x00000092;
	public static final int ERR_RECVFORM_HSM = 0x00000093;
	public static final int ERR_SESSION_END = 0x00000094;
	public static final int ERR_HANDLE_FAULT = 0x00000095;

	public static final int MAX_ZMK_INDEX = 999;
	public static final byte KEY_ZMK = 0x01; // 区域主密钥
	public static final byte KEY_TMK = 0x02; // 终端主密钥
	public static final byte KEY_PIK = 0x11; // PIN加密密钥
	public static final byte KEY_MAK = 0x12; // MAC计算密钥
	public static final byte KEY_DTK = 0x13; // 数据加密密钥
	public static final byte KEY_CVK = 0x21; // CVV计算密钥

	public static final byte MAC_XOR = 01; // XOR方式
	public static final byte MAC_ANSI99 = 02; // ANSI 9.9
	public static final byte MAC_ANSI919 = 03; // ANSI 9.19

	public static final byte PIN_ANXI98A = 01; // ANXI9.8带主帐号(ISO9564-1-0)
	public static final byte PIN_DOCUTE1 = 02; // Docutel ATM，1位长度＋n位PIN＋用户定义数据
	public static final byte PIN_IBM = 03; // Diebold and IBM ATM, n位PIN＋F
	public static final byte PIN_PLUS = 04; // Plus Network, 与格式1差别在于取主帐号最左12位
	public static final byte PIN_ISO9564 = 05; // ISO9564-1-1格式，1NP..PR...R
	public static final byte PIN_ANXI98 = 06; // ANXIX9.8不带主帐号

	/** *************** 算法 *************** */
	public static final int DES_ECB = 17; // DES_ECB
	public static final int DES_CBC = 33; // DES_CBC
	public static final int TRIDES_ECB_2A = 18; // 3DES_ECB(双长度)
	public static final int TRIDES_ECB_2B = 34; // 3DES_ECB(双长度)
	public static final int TRIDES_ECB_3A = 19; // 3DES_ECB(3长度)
	public static final int TRIDES_ECB_3B = 35; // 3DES_ECB(3长度)
	
	/** *************** PIN类型 *************** */
	public static final int PIN_TYPE_1 = 1; // 带主帐号
	public static final int PIN_TYPE_6 = 6; // 不带主帐号
	
	/** *************** PIN类型 *************** */
	public static final int AccNoLenMax = 19; 
	public static final int AccNoLenMin = 13; 
	



	/** ***************Error Code*************** */
	
	public static final int EKEY_LENGTH = 32;

	public static final int S_Key_Len = 1;
	public static final int D_Key_Len = 2;
	public static final int T_Key_Len = 3;

	public static final int ERROR_CONN = -1;
	public static final int ERROR_DATA_LEN = -2;
	public static final int ERROR_DEV_TIMES = -3;
	public static final int ERROR_KEY_FLAG = -4;
	public static final int ERROR_KEY_INDEX = -5;
	public static final int ERROR_KEY_TYPE = -6;
	public static final int ERROR_KEY_LEN = -7;
	public static final int ERROR_KEY_NO = -8;
	public static final int ERROR_KEY_LMK = -9;
	public static final int ERROR_MAC_LEN = -10;
	public static final int ERROR_MAC_TYPE = -11;
	public static final int ERROR_PINFORMAT = -12;
	public static final int ERROR_AccNo_Len = -13;
	public static final int ERROR_DEV_NUM = -0x55;
	public static final int ERROR_ALGORITHM = -0x77;
	public static final int ERROR_DES_TYPE = -0x78;
	
	public static final int ERROR_PIN_LEN= 34;
	


	public static final int KEY_TYPE = 1;
	public static final int PIK_TYPE = 2;
	public static final int MAC_TYPE = 3;
	public static final int DATA_TYPE = 4;
	

	public HsmConst() {
	}
}