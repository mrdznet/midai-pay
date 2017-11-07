package com.midai.pay.app.aes;

import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AES2 {

	public static final String KEY_ALGORITHM = "AES";
	
	public static final String CIPHER_ALGORITHM ="AES/CBC/PKCS5Padding";
	

	public static final String VIPARA   = "0102030405060708"; //iv参数设置 （ios、android、wp7、java 统一）
	                                 
	public static final String ENCODING = "UTF-8";

	private static String bytesToHexString(byte[] src){
		StringBuilder stringBuilder = new StringBuilder("");
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
	 * 16进制字符串转化成字节数组
	 * @param hexString
	 * @return
	 * 
	 */
	private static byte[] hexStringToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return d;
	}
	private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}

	/**
	 * 加密 后 将字节数组转化成16进制字符串
	 * @param srcStr   需要加密的内容
	 * @param password 加密的key
	 * @return
	 */
	public static String encrypt(String srcStr ,String password) {
		String resultStr ="";
		byte[] result = null;
		
		try {
			SecretKeySpec skeySpec = getKey(password);
			Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
			//IvParameterSpec iv = new IvParameterSpec(VIPARA.getBytes());
			
			byte[] ivByte = {(byte)0XCA,(byte)0X8A,(byte)0X88,(byte)0X78,(byte)0XF1,(byte)0X45,(byte)0XC9,(byte)0XB9,(byte)0XB3,(byte)0XC3,(byte)0X1A,(byte)0X1F,(byte)0X15,(byte)0XC3,(byte)0X4A,(byte)0X6D};
			IvParameterSpec iv = new IvParameterSpec(ivByte);
			
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
			result = cipher.doFinal(srcStr.getBytes());

		}catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		resultStr = bytesToHexString(result);
//		System.out.println("####加密时#########srcStr:"+srcStr+"##########password:"+password);
//		System.out.println("####加密后#########result:"+resultStr);
		return resultStr;

	}

	
	public static String decrypt(String destStr ,String password) {
		String resultStr = "";
		byte[] result = hexStringToBytes(destStr);
		//result = decrypt(result, password);
		
		try {
			SecretKeySpec skeySpec = getKey(password);
			Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
			//IvParameterSpec iv = new IvParameterSpec(VIPARA.getBytes());
			byte[] ivByte = {(byte)0XCA,(byte)0X8A,(byte)0X88,(byte)0X78,(byte)0XF1,(byte)0X45,(byte)0XC9,(byte)0XB9,(byte)0XB3,(byte)0XC3,(byte)0X1A,(byte)0X1F,(byte)0X15,(byte)0XC3,(byte)0X4A,(byte)0X6D};
			IvParameterSpec iv = new IvParameterSpec(ivByte);
			
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
			result = cipher.doFinal(result);
			
		}catch (NoSuchAlgorithmException e) {
			result = null;
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			result = null;
			e.printStackTrace();
		} catch (BadPaddingException e) {
			result = null;
			e.printStackTrace();
		} catch (Exception e) {
			result = null;
			e.printStackTrace();
		}
		resultStr = new String(result);
		
		resultStr = resultStr.replace("&", "&amp;");
		//resultStr = resultStr.replace(">", "&gt;");
		//resultStr = resultStr.replace("<", "&lt;");du
		resultStr = resultStr.replace("'", "&apos;");
		resultStr = resultStr.replace("ufeff", "");
		return resultStr;

	}


	private static SecretKeySpec getKey(String strKey) {
		//byte[] arrBTmp = strKey.getBytes();
		// byte[] arrBTmp = {(byte)0xB0,(byte)0x0D,(byte)0xDF,(byte)0x9D,(byte)0x93,(byte)0xE1,(byte)0x99,(byte)0xEF,(byte)0xEA,(byte)0xE9,(byte)0x67,(byte)0x80,(byte)0x5E,(byte)0x0A,(byte)0x52,(byte)0x28};
		byte[] arrBTmp = {(byte)0xAA,(byte)0x70,(byte)0xCD,0x77,0x12,0x5F,(byte)0xC3,0x04,(byte)0xFE,(byte)0xBF,0x5E,0x3E,(byte)0xA4,(byte)0xE9,(byte)0xAF,(byte)0xBD};//{0xAA,0x70,0xCD,0x77,0x12,0x5F,0xC3,0x04,0xFE,0xBF,0x5E,0x3E,0xA4,0xE9,0xAF,0xBD};
		byte[] arrB = new byte[16]; // 创建一个空的16位字节数组（默认值为0）

		for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {
			arrB[i] = arrBTmp[i];
		}
		

		SecretKeySpec skeySpec = new SecretKeySpec(arrB, KEY_ALGORITHM);

		return skeySpec;
	}
/*	private static void main(String[] args){

		String content = "2013-05-10"; 
		String password="dynamicode";
		String result = "";
		//加密 
		System.out.println("加密前 ：" + content); 
		result = encrypt(content, CryptUtils.encryptToMD5(password));
		System.out.println("加密后："+result);
		System.out.println("加密后长度："+result.length());
	}*/


}
