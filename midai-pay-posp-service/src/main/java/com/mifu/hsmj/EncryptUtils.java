package com.mifu.hsmj;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class EncryptUtils {
	public static final String ALGORITHM_DES = "DES";
	private static final byte[] bt8 = new byte[8];
	private static final IvParameterSpec iv = new IvParameterSpec(bt8);

	private static Key toKey(byte[] key) throws Exception {
		DESKeySpec dks = new DESKeySpec(key);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey secretKey = keyFactory.generateSecret(dks);

		return secretKey;
	}

	public static String decrypt1(String data, String key) throws Exception {
		return bcd2Str(decrypt1(str2Bcd(data), str2Bcd(key)));
	}

	public static byte[] decrypt1(byte[] data, byte[] key) throws Exception {
		Key k = toKey(key);
		Cipher cipher = Cipher.getInstance("DES/CBC/NoPadding");
		cipher.init(2, k, iv);
		return cipher.doFinal(data);
	}

	public static String encrypt1(String data, String key) throws Exception {
		if ((data == null) || ("".equals(data)))
			return "";
		return bcd2Str(encrypt1(str2Bcd(data), str2Bcd(key)));
	}

	public static byte[] encrypt1(byte[] data, byte[] key) throws Exception {
		Key k = toKey(key);
		Cipher cipher = Cipher.getInstance("DES/CBC/NoPadding");
		cipher.init(1, k, iv);
		return cipher.doFinal(data);
	}

	public static String decrypt2(String data, String key) throws Exception {
		return bcd2Str(decrypt2(str2Bcd(data), str2Bcd(key)));
	}

	public static byte[] decrypt2(byte[] data, byte[] key) throws Exception {
		byte[] leftData = byteArrayCopy(data, 0, 8);
		byte[] rightData = byteArrayCopy(data, 8, 8);
		byte[] leftKey = byteArrayCopy(key, 0, 8);
		byte[] rightKey = byteArrayCopy(key, 8, 8);
		byte[] temp = new byte[16];
		byte[] decrypt1 = decrypt1(encrypt1(decrypt1(leftData, leftKey), rightKey), leftKey);
		byte[] decrypt2 = decrypt1(encrypt1(decrypt1(rightData, leftKey), rightKey), leftKey);
		for (int i = 0; i < 8; i++) {
			temp[i] = decrypt1[i];
			temp[(8 + i)] = decrypt2[i];
		}
		return temp;
	}

	public static String encrypt2(String data, String key) throws Exception {
		return bcd2Str(encrypt2(str2Bcd(data), str2Bcd(key)));
	}

	public static byte[] encrypt2(byte[] data, byte[] key) throws Exception {
		byte[] leftData = byteArrayCopy(data, 0, 8);
		byte[] rightData = byteArrayCopy(data, 8, 8);
		byte[] leftKey = byteArrayCopy(key, 0, 8);
		byte[] rightKey = byteArrayCopy(key, 8, 8);
		byte[] temp = new byte[16];
		byte[] encrypt1 = encrypt1(decrypt1(encrypt1(leftData, leftKey), rightKey), leftKey);
		byte[] encrypt2 = encrypt1(decrypt1(encrypt1(rightData, leftKey), rightKey), leftKey);
		for (int i = 0; i < 8; i++) {
			temp[i] = encrypt1[i];
			temp[(8 + i)] = encrypt2[i];
		}
		return temp;
	}

	public static String decrypt3(String data, String key) throws Exception {
		return bcd2Str(decrypt3(str2Bcd(data), str2Bcd(key)));
	}

	public static byte[] decrypt3(byte[] data, byte[] key) throws Exception {
		byte[] lData = byteArrayCopy(data, 0, 8);
		byte[] mData = byteArrayCopy(data, 8, 8);
		byte[] rData = byteArrayCopy(data, 16, 8);
		byte[] lKey = byteArrayCopy(key, 0, 8);
		byte[] mKey = byteArrayCopy(key, 8, 8);
		byte[] rKey = byteArrayCopy(key, 16, 8);
		byte[] temp = new byte[data.length];
		byte[] decrypt1 = decrypt1(encrypt1(decrypt1(lData, lKey), mKey), rKey);
		byte[] decrypt2 = decrypt1(encrypt1(decrypt1(mData, lKey), mKey), rKey);
		byte[] decrypt3 = decrypt1(encrypt1(decrypt1(rData, lKey), mKey), rKey);

		for (int i = 0; i < 8; i++) {
			temp[i] = decrypt1[i];
			if (data.length == 16)
				temp[(8 + i)] = decrypt2[i];

			if (data.length == 24)
				temp[(16 + i)] = decrypt3[i];
		}
		return temp;
	}

	public static String encrypt3(String data, String key) throws Exception {
		return bcd2Str(encrypt3(str2Bcd(data), str2Bcd(key)));
	}

	public static byte[] encrypt3(byte[] data, byte[] key) throws Exception {
		byte[] lData = byteArrayCopy(data, 0, 8);
		byte[] mData = byteArrayCopy(data, 8, 8);
		byte[] rData = byteArrayCopy(data, 16, 8);
		byte[] lKey = byteArrayCopy(key, 0, 8);
		byte[] mKey = byteArrayCopy(key, 8, 8);
		byte[] rKey = byteArrayCopy(key, 16, 8);
		byte[] temp = new byte[24];
		byte[] encrypt1 = encrypt1(decrypt1(encrypt1(lData, lKey), mKey), rKey);
		byte[] encrypt2 = encrypt1(decrypt1(encrypt1(mData, lKey), mKey), rKey);
		byte[] encrypt3 = encrypt1(decrypt1(encrypt1(rData, lKey), mKey), rKey);
		for (int i = 0; i < 8; i++) {
			temp[i] = encrypt1[i];
			temp[(8 + i)] = encrypt2[i];
			temp[(16 + i)] = encrypt3[i];
		}
		return temp;
	}

	public static byte[] str2Bcd(String str) {
		if (str.length() % 2 != 0) {
			str = "0" + str;
		}
		byte[] bytes = new byte[str.length() / 2];
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = (byte) Integer.parseInt(str.substring(2 * i, 2 * i + 2), 16);
		}
		return bytes;
	}

	public static String bcd2Str(byte[] in) {
		StringBuilder sb = new StringBuilder(in.length);

		for (int i = 0; i < in.length; i++) {
			String sTemp = Integer.toHexString(0xFF & in[i]);
			if (sTemp.length() < 2)
				sb.append(0);
			sb.append(sTemp.toUpperCase());
		}
		return sb.toString();
	}

	public static String buQi(String str, int len, char c) {
		char[] chars = new char[len];
		for (int i = 0; i < len; i++) {
			chars[i] = (i < str.length() ? str.charAt(i) : c);
		}
		return new String(chars);
	}

	public static byte[] buQi(byte[] in, int len, byte c) {
		byte[] temp = new byte[len];
		for (int i = 0; i < len; i++) {
			if (i < in.length)
				temp[i] = in[i];
			else {
				temp[i] = c;
			}
		}
		return temp;
	}
	
	public static byte[] byteArrayCopy(byte[] source, int start, int len)
	  {
	    byte[] temp = new byte[len];
	    for (int i = 0; i < len; i++) {
	      if (start + i < source.length)
	        temp[i] = source[(start + i)];
	    }
	    return temp;
	  }
	
	public static void main(String[] args) throws Exception{
		String key = "5253953CECA41AB09365C68F00A3A6AF";	//903d95ffc6dbf4269594aad26dc86a35
		String data = "0000000000000000";
		System.out.println(encrypt2(data, key).substring(0,8));
	}
}