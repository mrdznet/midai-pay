package com.mifu.hsmj;

import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;

public class DesUtil {

  /**
   * DES加密
   * 
   * @param data
   *          数据
   * @param key
   *          密钥
   * @return
   */
  public static byte[] encode(byte[] data, byte[] key) {
    try {
      SecureRandom sr = new SecureRandom();
      DESKeySpec dks = new DESKeySpec(key);
      SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
      SecretKey sk = keyFactory.generateSecret(dks);
      Cipher encryptCipher = Cipher.getInstance("DES/ECB/NoPadding");
      encryptCipher.init(Cipher.ENCRYPT_MODE, sk, sr);
      return encryptCipher.doFinal(data);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public static byte[] encryptDES(byte[] strdate, byte[] strkey) throws Exception {
    SecureRandom sr = new SecureRandom();
    DESKeySpec dks = new DESKeySpec(strkey);
    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
    SecretKey key = keyFactory.generateSecret(dks);
    Cipher encryptCipher = Cipher.getInstance("DES/ECB/NoPadding");
    encryptCipher.init(Cipher.ENCRYPT_MODE, key, sr);
    return encryptCipher.doFinal(strdate);
  }

  /**
   * DES解密
   * 
   * @param key
   *          密钥
   * @param data
   *          密文
   * @return
   */
  public static byte[] decode(byte[] data, byte[] key) {
    try {
      DESKeySpec dks = new DESKeySpec(key);
      SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
      Key secretKey = keyFactory.generateSecret(dks);
      Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
      cipher.init(Cipher.DECRYPT_MODE, secretKey, new SecureRandom());
      return cipher.doFinal(data);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * DES解密
   * 
   * @param key
   *          密钥
   * @param data
   *          密文
   * @return
   */
  public static byte[] decode2(String data, String key) {
    try {
      byte[] k = CodingUtil.hexStringToByteArray(key);
      byte[] km = new byte[24];
      System.arraycopy(k, 0, km, 0, 16);
      System.arraycopy(k, 0, km, 16, 8);
      Cipher cipher = Cipher.getInstance("DESede/ECB/NoPadding");
      DESedeKeySpec dks = new DESedeKeySpec(km);
      SecretKey sk = SecretKeyFactory.getInstance("DESede").generateSecret(dks);
      cipher.init(Cipher.DECRYPT_MODE, sk);
      return cipher.doFinal(CodingUtil.hexStringToByteArray(data));
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public static byte[] decode3(String data, String key) {
    try {
      byte[] k = ByteUtil.hexStringToByteArray(key);
      byte[] km = new byte[24];
      System.arraycopy(k, 0, km, 0, 16);
      System.arraycopy(k, 0, km, 16, 8);
      Cipher cipher = Cipher.getInstance("DESede/ECB/NoPadding");
      DESedeKeySpec dks = new DESedeKeySpec(km);
      SecretKey sk = SecretKeyFactory.getInstance("DESede").generateSecret(dks);
      cipher.init(Cipher.DECRYPT_MODE, sk);
      return cipher.doFinal(ByteUtil.hexStringToByteArray(data));
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * DES加密
   * 
   * @param data
   *          数据
   * @param key
   *          密钥
   * @return
   */
  public static byte[] encode2(byte[] data, byte[] key) {
    try {
      byte[] km = new byte[24];
      System.arraycopy(key, 0, km, 0, 16);
      System.arraycopy(key, 0, km, 16, 8);
      Cipher cipher = Cipher.getInstance("DESede/ECB/NoPadding");
      DESedeKeySpec dks = new DESedeKeySpec(km);
      SecretKey k = SecretKeyFactory.getInstance("DESede").generateSecret(dks);
      cipher.init(Cipher.ENCRYPT_MODE, k);
      byte[] result = cipher.doFinal(data);
      return result;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public static byte[] compute3DES(byte[] data, byte[] key) {
    try {
      byte[] km = new byte[24];
      System.arraycopy(key, 0, km, 0, 16);
      System.arraycopy(key, 0, km, 16, 8);
      Cipher cipher = Cipher.getInstance("DESede/ECB/NoPadding");
      DESedeKeySpec dks = new DESedeKeySpec(km);
      SecretKey k = SecretKeyFactory.getInstance("DESede").generateSecret(dks);
      cipher.init(Cipher.ENCRYPT_MODE, k);
      byte[] result = cipher.doFinal(data);
      return result;
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("" + e);
    }
  }

  public static void main(String[] args) {
//    System.out.println(CodingUtil.byteArrayToHexString(encode2(CodingUtil.hexStringToByteArray("10D4912120000000"),CodingUtil.hexStringToByteArray("CE806E0875DB10A5AC0D8A9A7B92998A"))));
		
	  String key = "4661FC95D1D6FAB29D29BD50191B070A";
	  String eMasKey = "04663DAFD25870A6EF0B951BF5BA8255";
	  System.out.println(CodingUtil.byteArrayToHexString(decode2(eMasKey, key)));
  
  }

}
