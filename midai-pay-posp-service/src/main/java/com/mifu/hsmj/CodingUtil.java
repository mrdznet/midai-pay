package com.mifu.hsmj;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

public class CodingUtil {

	public static byte[] str2bcd(String str) {
		if (str.length() % 2 != 0) {
			str = "0" + str;
		}
		byte[] bytes = new byte[str.length() / 2];
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = (byte) Integer.parseInt(str.substring(2 * i, 2 * i + 2), 16);
		}
		return bytes;
	}

	public static String bcd2str(byte[] in) {
		StringBuilder sb = new StringBuilder(in.length);
		for (int i = 0; i < in.length; i++) {
			String sTemp = Integer.toHexString(0xFF & in[i]);
			if (sTemp.length() < 2) {
				sb.append(0);
			}
			sb.append(sTemp.toUpperCase());
		}
		return sb.toString();
	}

	public static byte[] str2bcdR(String s) {
		if (s.length() % 2 != 0) {
			s = s + "0";
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		char[] cs = s.toCharArray();
		int high = 0;
		int low = 0;
		for (int i = 0; i < cs.length; i += 2) {
			if ((cs[i] >= '0') && (cs[i] <= '9'))
				high = cs[i] - '0';
			else {
				high = cs[i] - '0' - 7;
			}

			if ((cs[(i + 1)] >= '0') && (cs[(i + 1)] <= '9'))
				low = cs[(i + 1)] - '0';
			else {
				low = cs[(i + 1)] - '0' - 7;
			}
			baos.write(high << 4 | low);
		}
		return baos.toByteArray();
	}

	public static byte[] str2bcdL(String s) {
		if (s.length() % 2 != 0) {
			s = "0" + s;
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		char[] cs = s.toCharArray();
		int high = 0;
		int low = 0;
		for (int i = 0; i < cs.length; i += 2) {
			if ((cs[i] >= '0') && (cs[i] <= '9'))
				high = cs[i] - '0';
			else {
				high = cs[i] - '0' - 7;
			}

			if ((cs[(i + 1)] >= '0') && (cs[(i + 1)] <= '9'))
				low = cs[(i + 1)] - '0';
			else {
				low = cs[(i + 1)] - '0' - 7;
			}
			baos.write(high << 4 | low);
		}
		return baos.toByteArray();
	}

	public static byte[] hexStringToByteArray2(String digits) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		for (int i = 0; i < digits.length(); i += 2) {
			char c1 = digits.charAt(i);
			if (i + 1 >= digits.length()) {
				throw new IllegalArgumentException("hexUtil.odd");
			}
			char c2 = digits.charAt(i + 1);
			byte b = 0;
			if ((c1 >= '0') && (c1 <= '9'))
				b = (byte) (b + (c1 - '0') * 16);
			else if ((c1 >= 'a') && (c1 <= 'f'))
				b = (byte) (b + (c1 - 'a' + 10) * 16);
			else if ((c1 >= 'A') && (c1 <= 'F'))
				b = (byte) (b + (c1 - 'A' + 10) * 16);
			else
				throw new IllegalArgumentException("hexUtil.bad");
			if ((c2 >= '0') && (c2 <= '9'))
				b = (byte) (b + (c2 - '0'));
			else if ((c2 >= 'a') && (c2 <= 'f'))
				b = (byte) (b + (c2 - 'a' + 10));
			else if ((c2 >= 'A') && (c2 <= 'F'))
				b = (byte) (b + (c2 - 'A' + 10));
			else
				throw new IllegalArgumentException("hexUtil.bad");
			baos.write(b);
		}
		return baos.toByteArray();
	}

	public static byte[] hexStringToByteArray(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
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

	public static String byteArrayToHexString(byte data[]) {
		String result = "";
		for (int i = 0; i < data.length; i++) {
			int v = data[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				result += "0";
			}
			result += hv;
		}
		return result;
	}

	private static char convertDigitToHexChar(int value) {
		value &= 15;
		if (value >= 10) {
			return (char) (value - 10 + 65);
		}
		return (char) (value + 48);
	}

	public static String byteArrayToHexString2(byte[] bytes) {
		StringBuffer sb = new StringBuffer(bytes.length * 2);
		for (int i = 0; i < bytes.length; i++) {
			sb.append(convertDigitToHexChar(bytes[i] >> 4));
			sb.append(convertDigitToHexChar(bytes[i] & 0xF));
		}
		return sb.toString();
	}

	public static String stringToHexString(String s) {
		String str = "";
		for (int i = 0; i < s.length(); i++) {
			int ch = (int) s.charAt(i);
			String s4 = Integer.toHexString(ch);
			str = str + s4;
		}
		return str;
	}

	public static String hexStringToString(String s) {
		try {
			return new String(hexStringToByteArray(s), "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

}
