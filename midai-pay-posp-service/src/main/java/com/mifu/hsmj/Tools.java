package com.mifu.hsmj;

import java.text.NumberFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tools {

	public static String leftFillZero(int num, int len) {
		NumberFormat nf = NumberFormat.getInstance();

		nf.setGroupingUsed(false);

		nf.setMaximumIntegerDigits(len);

		nf.setMinimumIntegerDigits(len);

		return nf.format(num);
	}

	public static String leftFillZero(long num, int len) {
		NumberFormat nf = NumberFormat.getInstance();

		nf.setGroupingUsed(false);

		nf.setMaximumIntegerDigits(len);

		nf.setMinimumIntegerDigits(len);

		return nf.format(num);
	}

	public static boolean matchDigital(String val) {
		Pattern pattern = Pattern.compile("\\d+");
		Matcher matcher = pattern.matcher(val);
		return matcher.matches();
	}

	public static boolean matchVersion(String val) {
		Pattern pattern = Pattern.compile("[0-9]{1,2}[.][0-9]{1,2}[.][0-9]{1,2}");
		Matcher matcher = pattern.matcher(val);
		return matcher.matches();
	}

	public static boolean matchOrgCode(String val) {
		Pattern pattern = Pattern.compile("[a-fA-Z0-9]{8,11}");
		Matcher matcher = pattern.matcher(val);
		return matcher.matches();
	}

	public static boolean matchPosCode(String val) {
		Pattern pattern = Pattern.compile("[a-fA-Z0-9]{8}");
		Matcher matcher = pattern.matcher(val);
		return matcher.matches();
	}

	public static boolean matchDigitalAndF(String val) {
		Pattern pattern = Pattern.compile("[A-F0-9]+");
		Matcher matcher = pattern.matcher(val);
		return matcher.matches();
	}

	public static boolean matchData(String val) {
		Pattern pattern = Pattern.compile(
				"(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)");
		Matcher matcher = pattern.matcher(val);
		return matcher.matches();
	}

	public static boolean matchRole(String val) {
		Pattern pattern = Pattern.compile("(0|1|2|3|5)");
		Matcher matcher = pattern.matcher(val);
		return matcher.matches();
	}

	public static boolean matchSuperAuth(String val) {
		Pattern pattern = Pattern.compile("(0|1)");
		Matcher matcher = pattern.matcher(val);
		return matcher.matches();
	}

	public static boolean matchMasterKey(String val) {
		Pattern pattern = Pattern.compile("([A-F0-9]{16}|[A-F0-9]{32})");
		Matcher matcher = pattern.matcher(val);
		return matcher.matches();
	}

	public static boolean matchNum(String val) {
		Pattern pattern = Pattern.compile("[0-9]{3}");
		Matcher matcher = pattern.matcher(val);
		return matcher.matches();
	}

	public static String addLeftSpace(String value, int length) {
		if (value == null) {
			value = "";
		}
		if (value.length() > length) {
			return value.substring(value.length() - length, value.length());
		}
		char[] c = new char[length];
		System.arraycopy(value.toCharArray(), 0, c, 0 + length - value.length(), value.length());

		for (int i = 0; i < length - value.length(); i++) {
			c[i] = ' ';
		}
		return new String(c);
	}

	public static String addRightSpace(String value, int length) {
		if (value == null) {
			value = "";
		}
		if (value.length() > length) {
			return value.substring(0, length - 1);
		}
		char[] c = new char[length];
		System.arraycopy(value.toCharArray(), 0, c, 0, value.length());
		for (int i = value.length(); i < c.length; i++) {
			c[i] = ' ';
		}
		return new String(c);
	}

	public static String addRightZero(String value, int length) {
		if (value == null) {
			value = "";
		}
		if (value.length() > length) {
			return value.substring(0, length - 1);
		}
		char[] c = new char[length];
		System.arraycopy(value.toCharArray(), 0, c, 0, value.length());
		for (int i = value.length(); i < c.length; i++) {
			c[i] = '0';
		}
		return new String(c);
	}

	public static String addLeftZero(String s, int length) {
		int old = s.length();
		if (length > old) {
			char[] c = new char[length];
			char[] x = s.toCharArray();
			if (x.length > length) {
				throw new IllegalArgumentException(
						new StringBuilder().append("Numeric value is larger than intended length: ").append(s)
								.append(" LEN ").append(length).toString());
			}

			int lim = c.length - x.length;
			for (int i = 0; i < lim; i++) {
				c[i] = '0';
			}
			System.arraycopy(x, 0, c, lim, x.length);
			return new String(c);
		}
		return s.substring(0, length);
	}

	public static int compare(Object obj1, Object obj2) {
		int result = 0;
		if ((obj1 == null) || (obj2 == null))
			return result;
		return Integer.valueOf(obj1.toString().split(" ")[0]).intValue()
				- Integer.valueOf(obj2.toString().split(" ")[0]).intValue();
	}

	public static String calcMac2(byte[] input) throws Exception {
		int length = input.length;
		int x = length % 8;
		int addLength = 0;
		byte[] src = null;
		if (x != 0) {
			addLength = 8 - x;
			src = new byte[length + addLength];
			System.arraycopy(input, 0, src, 0, length);
		} else {
			src = input;
		}

		byte[] xorInit = new byte[8];
		System.arraycopy(src, 0, xorInit, 0, 8);
		for (int i = 1; i < src.length / 8; i++) {
			byte[] xorTmp = new byte[8];
			System.arraycopy(src, i * 8, xorTmp, 0, xorTmp.length);
			byte[] t = bytesXOR(xorInit, xorTmp);
			xorInit = t;
		}

		String xorHexStrTmp = CodingUtil.byteArrayToHexString(xorInit);
		System.out.println(xorHexStrTmp);

		return xorHexStrTmp;
	}

	public static byte[] bytesXOR(byte[] src1, byte[] src2) {
		int length = src1.length;
		if (length != src2.length) {
			return null;
		}
		byte[] result = new byte[length];
		for (int i = 0; i < length; i++) {
			result[i] = ((byte) (src1[i] & 0xFF ^ src2[i] & 0xFF));
		}

		return result;
	}

}
