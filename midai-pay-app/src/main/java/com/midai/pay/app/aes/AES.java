package com.midai.pay.app.aes;

import java.io.PrintStream;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AES
{
  public static final String KEY_ALGORITHM = "AES";
  public static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";
  public static final String VIPARA = "0102030405060708";
  public static final String ENCODING = "UTF-8";

  public static byte[] encrypt(String content, String password)
  {
    try
    {
      SecretKeySpec skeySpec = getKey(password);
      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
      IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes());

      cipher.init(1, skeySpec, iv);
      byte[] encrypted = cipher.doFinal(content.getBytes());
      return encrypted;
    }
    catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (IllegalBlockSizeException e) {
      e.printStackTrace();
    } catch (BadPaddingException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public static byte[] encrypt1(String content, String password) {
    try {
      SecretKeySpec skeySpec = getKey1(password);
      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

      byte[] ivByte = { -54, -118, -120, 120, -15, 69, -55, -71, -77, -61, 26, 31, 21, -61, 74, 109 };
      IvParameterSpec iv = new IvParameterSpec(ivByte);

      cipher.init(1, skeySpec, iv);
      byte[] encrypted = cipher.doFinal(content.getBytes());
      return encrypted;
    }
    catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (IllegalBlockSizeException e) {
      e.printStackTrace();
    } catch (BadPaddingException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public static byte[] decrypt(byte[] content, String password)
  {
    try
    {
      SecretKeySpec skeySpec = getKey(password);
      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
      IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes());

      cipher.init(2, skeySpec, iv);
      byte[] original = cipher.doFinal(content);
      return original;
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (IllegalBlockSizeException e) {
      e.printStackTrace();
    } catch (BadPaddingException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
  public static byte[] decrypt1(byte[] content, String password) {
    try {
      SecretKeySpec skeySpec = getKey1(password);
      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

      byte[] ivByte = { -54, -118, -120, 120, -15, 69, -55, -71, -77, -61, 26, 31, 21, -61, 74, 109 };
      IvParameterSpec iv = new IvParameterSpec(ivByte);

      cipher.init(2, skeySpec, iv);
      byte[] original = cipher.doFinal(content);
      return original;
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (IllegalBlockSizeException e) {
      e.printStackTrace();
    } catch (BadPaddingException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public static String bytesToHexString(byte[] src)
  {
    StringBuilder stringBuilder = new StringBuilder("");
    if ((src == null) || (src.length <= 0)) {
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

  public static byte[] hexStringToBytes(String hexString)
  {
    if ((hexString == null) || (hexString.equals(""))) {
      return null;
    }
    hexString = hexString.toUpperCase();
    int length = hexString.length() / 2;
    char[] hexChars = hexString.toCharArray();
    byte[] d = new byte[length];
    for (int i = 0; i < length; i++) {
      int pos = i * 2;
      d[i] = (byte)(charToByte(hexChars[pos]) << 4 | charToByte(hexChars[(pos + 1)]));
    }
    return d;
  }
  private static byte charToByte(char c) {
    return (byte)"0123456789ABCDEF".indexOf(c);
  }

  public static String encryptString(String srcStr, String password)
  {
    String resultStr = "";
    byte[] result = encrypt(srcStr, password);
    resultStr = bytesToHexString(result);

    return resultStr;
  }

  public static String encryptString1(String srcStr, String password) {
    String resultStr = "";
    byte[] result = encrypt1(srcStr, password);
    resultStr = bytesToHexString(result);

    return resultStr;
  }

  public static String decryptString(String destStr, String password)
  {
    String resultStr = "";
    byte[] result = hexStringToBytes(destStr);
    result = decrypt(result, password);
    resultStr = new String(result);

    resultStr = resultStr.replace("&", "&amp;");

    resultStr = resultStr.replace("'", "&apos;");

    return resultStr;
  }

  public static String decryptString1(String destStr, String password) {
    String resultStr = "";
    byte[] result = hexStringToBytes(destStr);
    result = decrypt1(result, password);
    resultStr = new String(result);

    resultStr = resultStr.replace("&", "&amp;");

    resultStr = resultStr.replace("'", "&apos;");
    resultStr = resultStr.replace("ufeff", "");
    return resultStr;
  }

  private static SecretKeySpec getKey(String strKey)
  {
    byte[] arrBTmp = strKey.getBytes();

    byte[] arrB = new byte[16];

    for (int i = 0; (i < arrBTmp.length) && (i < arrB.length); i++) {
      arrB[i] = arrBTmp[i];
    }

    SecretKeySpec skeySpec = new SecretKeySpec(arrB, "AES");

    return skeySpec;
  }

  private static SecretKeySpec getKey1(String strKey)
  {
    byte[] arrBTmp = { -86, 112, -51, 119, 18, 95, -61, 4, -2, -65, 94, 62, -92, -23, -81, -67 };
    byte[] arrB = new byte[16];

    for (int i = 0; (i < arrBTmp.length) && (i < arrB.length); i++) {
      arrB[i] = arrBTmp[i];
    }

    SecretKeySpec skeySpec = new SecretKeySpec(arrB, "AES");

    return skeySpec;
  }

  public static void main(String[] args) {
    String content = "2013-05-10";
    String password = "dynamicode";
    String result = "";

    System.out.println("加密前 ：" + content);
    result = encryptString(content, CryptUtils.encryptToMD5(password));
    System.out.println("加密后：" + result);
    System.out.println("加密后长度：" + result.length());
  }
}