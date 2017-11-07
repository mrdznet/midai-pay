package com.midai.pay.app.aes;




public class TdXOR {

	private static String DES = "DES/ECB/NoPadding"; 
	
	public static String Byte8XORData(String macData){
		byte [] macByte = str2Bcd(toHexString(macData));
		return Byte8XOR(macByte).toUpperCase();
	}
	
	public static String Byte8XOR(byte[] args) {
		byte[][] data = null; 

		
		int length = args.length;
	
		int end = length % 8;

		int width = length / 8;

		if (end != 0) {
			width++;
		}
		data = new byte[width][8];

		int colCount = 0;
		int linCount = 0;

		for (int i = 0; i < length; i++) {
			data[linCount][colCount] = args[i];
			colCount++;
			if (colCount == 8) {
				linCount++;
				colCount = 0;
			}
		}
		if (end > 0) {
			for (int j = end; j < 8; j++) {
				data[width - 1][j] = 0x00;
			}
		}

		byte[] tempData = data[0];

		for (int k = 1; k < width; k++) {
			byte[] secData = data[k];
			for (int i = 0; i < 8; i++) {
				tempData[i] = (byte) (tempData[i] ^ secData[i]);
			}
		}

		String result = bytesToHexString(tempData);
		return result;
	}

	/**
	 * 
	 * @param src
	 * @return
	 */
	public static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder();
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}
	
	/**
	 * 
	 * @param src
	 * @return
	 */
	public static String BytesToHexStr(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder();
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString().toUpperCase();
	}

	/**
	 * 
	 * @param asc
	 * @return
	 */
	public static byte[] str2Bcd(String asc) {
		int len = asc.length();
		int mod = len % 2;

		if (mod != 0) {
			asc = "0" + asc;
			len = asc.length();
		}

		byte abt[] = new byte[len];
		if (len >= 2) {
			len = len / 2;
		}

		byte bbt[] = new byte[len];
		abt = asc.getBytes();
		int j, k;

		for (int p = 0; p < asc.length() / 2; p++) {
			if ((abt[2 * p] >= '0') && (abt[2 * p] <= '9')) {
				j = abt[2 * p] - '0';
			} else if ((abt[2 * p] >= 'a') && (abt[2 * p] <= 'z')) {
				j = abt[2 * p] - 'a' + 0x0a;
			} else {
				j = abt[2 * p] - 'A' + 0x0a;
			}

			if ((abt[2 * p + 1] >= '0') && (abt[2 * p + 1] <= '9')) {
				k = abt[2 * p + 1] - '0';
			} else if ((abt[2 * p + 1] >= 'a') && (abt[2 * p + 1] <= 'z')) {
				k = abt[2 * p + 1] - 'a' + 0x0a;
			} else {
				k = abt[2 * p + 1] - 'A' + 0x0a;
			}

			int a = (j << 4) + k;
			byte b = (byte) a;
			bbt[p] = b;
		}
		return bbt;
	}
	
	public static String bcd2Str(byte[] bytes) {  
        StringBuffer temp = new StringBuffer(bytes.length * 2);  
        for (int i = 0; i < bytes.length; i++) {  
            temp.append((byte) ((bytes[i] & 0xf0) >>> 4));  
            temp.append((byte) (bytes[i] & 0x0f));  
        }  
        return temp.toString().substring(0, 1).equalsIgnoreCase("0") ? temp  
                .toString().substring(1) : temp.toString();  
    }  
	
	public static String SubMsgBodyHexXOR(byte [] args, int pos, int len){
		len = args.length - len - pos;
		byte[] dest = new byte[len];
		System.arraycopy(args, pos, dest, 0, len);
		String hexStr = Byte8XOR(dest).toUpperCase();
		System.out.println(hexStr);
		
		
		return "";
	}

	/**
	 * 
	 * @param s
	 * @return
	 */
	public static String toHexString(String s) {
		String str = "";
		for (int i = 0; i < s.length(); i++) {
			int ch = (int) s.charAt(i);
			String s4 = Integer.toHexString(ch);
			str = str + s4;
		}
		return str;
	}
	
	/**
	 * @param account
	 * @param passwd
	 * @return
	 */
	public static String PinEncrypt(String account,String passwd){
		String result="";
		String accountTemp1 = "";
		int passwdLen = passwd.length();
		if(passwdLen==0){
			passwd = "FFFFFF";
		}else if(passwdLen<6){
			for(int i=0;i<6-passwdLen;i++){
				passwd += "F";
			}
		}
		String passwdTemp1 = "0"+passwdLen+passwd+"FFFFFFFF";
		if(account!=null&&!"".equals(account)){
			int len = account.length();
			String accountTemp = account.substring(len-13,len-1);
			accountTemp1 = "0000"+accountTemp;
		}
		
		if(accountTemp1.equals("")){
			result = passwdTemp1;
		}
		else{
			byte [] accountByte = str2Bcd(accountTemp1);
			byte [] passwdByte = str2Bcd(passwdTemp1);
			
			byte [] resultByte = new byte [8];
			
			for(int i=0;i<8;i++){
				resultByte[i] = (byte) (accountByte[i] ^ passwdByte[i]);
			}
			result = bytesToHexString(resultByte);
		}
		
		return result.toUpperCase();
	}
	
	
	
	/**
	 * @param TMK
	 * @param PMK
	 * @param macData
	 * @return
	 */
	public static String Mac9609(String TMK,String PMK,String macData){
		byte [] tmkByte = str2Bcd(TMK);
		byte [] pmkByte = str2Bcd(PMK);
		
		byte [] MwPmkByte = TdDesUtil.DoubleDesDecrypt(tmkByte,pmkByte);
		
		String MwPmkString = bytesToHexString(MwPmkByte).toUpperCase();
		
		return Mac9609Mw(MwPmkString, macData);
	}

	/**
	 * @param key
	 * @param data
	 * @return
	 */
	public static String Mac9609Mw(String key,String macData){
		
		String mac="" ;
		
		byte [] keyByte =  str2Bcd(key); 
		
		byte [] dataByte = str2Bcd(toHexString(macData));  
		
		byte[][] data = null; 

		int length = dataByte.length;
		int end = length % 8;
		int width = length / 8;
		if (end != 0) {
			width++;
		}
		data = new byte[width][8];

		int colCount = 0;
		int linCount = 0; 

		for (int i = 0; i < length; i++) {
			data[linCount][colCount] = dataByte[i];
			colCount++;
			if (colCount == 8) {
				linCount++;
				colCount = 0;
			}
		}
		if (end > 0) {
			for (int j = end; j < 8; j++) {
				data[width - 1][j] = 0x00;
			}
		}

		byte[] tempData = data[0];
		
		for (int k = 1; k < width; k++) {
			byte[] secData = data[k];
			tempData  = TdDesUtil.DoubleDesEncrypt(keyByte,tempData);
			for (int i = 0; i < 8; i++) {
				tempData[i] = (byte) (tempData[i] ^ secData[i]);
			}
		}
		
		tempData = TdDesUtil.DoubleDesEncrypt(keyByte,tempData);
		
		mac = bytesToHexString(tempData).toUpperCase();
		
		return mac;
	}
	
	/**
	 * @param TMK
	 * @param PMK
	 * @param macData
	 * @return
	 */
	public static String MacEcb(String TMK,String PMK,String macData){
		byte [] tmkByte = str2Bcd(TMK);
		byte [] pmkByte = str2Bcd(PMK);
		
		byte [] MwPmkByte = TdDesUtil.DoubleDesDecrypt(tmkByte,pmkByte);
		
		String MwPmkString = bytesToHexString(MwPmkByte).toUpperCase();
		
		return MacEcbMw(MwPmkString, macData);
	}

	
	/**
	 * @param key
	 * @param macData
	 * @return
	 */
	public static String MacEcbMw(String key,String macData){
		String mac = "";
		byte [] keyByte =  str2Bcd(key);  
		
		byte [] dataByte = str2Bcd(toHexString(macData)); 
		
		String tempData = Byte8XOR(dataByte).toUpperCase();
		
		String hexTempData = toHexString(tempData);
		
		String leftData = hexTempData.substring(0,16);
		
		String rightData = hexTempData.substring(16);
		
		byte [] leftByte = str2Bcd(leftData);
		
		byte [] rightByte = str2Bcd(rightData);
		
		byte [] tempByte = TdDesUtil.DoubleDesEncrypt(keyByte, leftByte);
		
		for(int i=0;i<8;i++){
			tempByte[i] = (byte) (tempByte[i] ^ rightByte[i]);
		}
		
		tempByte = TdDesUtil.DoubleDesEncrypt(keyByte, tempByte);
		
		String tempByteString = toHexString(bytesToHexString(tempByte).toUpperCase());
		
		mac = tempByteString.substring(0,16);
		
		return mac;
	}
	
	/**
	 * @param key
	 * @param macData
	 * @return
	 */
	public static String MacEcb16Mw(String key,String macData){
		String mac = "";
		byte [] keyByte =  str2Bcd(key);  
		
		byte [] dataByte = str2Bcd(macData); 
		
		String tempData = Byte8XOR(dataByte).toUpperCase();
		
		String hexTempData = toHexString(tempData);
		
		String leftData = hexTempData.substring(0,16);
		
		String rightData = hexTempData.substring(16);
		
		byte [] leftByte = str2Bcd(leftData);
		
		byte [] rightByte = str2Bcd(rightData);
		
		byte [] tempByte = TdDesUtil.UnionDesEncrypt(keyByte, leftByte);
		
		for(int i=0;i<8;i++){
			tempByte[i] = (byte) (tempByte[i] ^ rightByte[i]);
		}
		
		tempByte = TdDesUtil.UnionDesEncrypt(keyByte, tempByte);
		
		String tempByteString = toHexString(bytesToHexString(tempByte).toUpperCase());
		
		mac = tempByteString.substring(0,16).toUpperCase();
		
		return mac;
	}
	
	
    
   
  
	public static void main(String[] args) throws Exception {
		String tmk = "8364EF34B5940420";
		String pmk = "4220073EF5D74D107F95DF316F02A4E0";
		String macData = "000000000000000000000000000200302004C030C0981100000000000000003200000602100012376222020200098642024D4912120214999132500097996222020200098642024D1561560000000000001003214999016000049120D000000000000D000000000000D0000000003630303030323231333130313530303531393230303033313536501CED7DCB85646226000000000000000013220000010005000000000000000000";
		String macDataB = "0200302004C030C0981100000000000000003200000602100012376222020200098642024D4912120214999132500097996222020200098642024D1561560000000000001003214999016000049120D000000000000D000000000000D0000000003630303030323231333130313530303531393230303033313536501CED7DCB8564622600000000000000001322000001000500";
		
		byte [] dat = str2Bcd(macData);
		
		SubMsgBodyHexXOR(dat, 13, 8);
		System.out.println(MacEcb16Mw(tmk, macDataB));
//		System.out.println(Mac9609(tmk, pmk, macData));
//		System.out.println(Byte8XORData(macData));
	}
}
