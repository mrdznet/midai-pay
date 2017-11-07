package com.midai.pay.app.aes;

import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;


/**
 * 用于Epos项目的手机刷卡器的主密钥分散和解密使用
 * @author luoyang 
 *
 */
public class TdDesUtil {

	private static String DES = "DES/ECB/NoPadding";  
	private static String TriDes = "DESede/ECB/NoPadding";
	
	
	/**
	 * 对byte数组进行取反
	 * @param args
	 * @return
	 */
	public static byte [] getByteNot (byte[] args){
		byte [] result = new byte [args.length];
		
		for(int i=0;i<args.length;i++){
			result[i] = (byte) ~args[i];
		}
		return result;
	}
	
	 /**
     * des加密
     * @param key 密钥
     * @param data 明文数据 16进制且长度为16的整数倍
     * @return  密文数据
     */
    public static byte[] UnionDesEncrypt(byte key[], byte data[]) {  
  
        try {  
            KeySpec ks = new DESKeySpec(key);  
            SecretKeyFactory kf = SecretKeyFactory.getInstance("DES");  
            SecretKey ky = kf.generateSecret(ks);  
  
            Cipher c = Cipher.getInstance(DES);  
            c.init(Cipher.ENCRYPT_MODE, ky);  
            return c.doFinal(data);  
        } catch (Exception e) {  
            e.printStackTrace();  
            return null;  
        }  
    }  
  
    
    /**
     * des解密
     * @param key 密钥
     * @param data 密文数据 16进制且长度为16的整数倍
     * @return 明文数据
     */
    public static byte[] UnionDesDecrypt(byte key[], byte data[]) {  
  
        try {  
            KeySpec ks = new DESKeySpec(key);  
            SecretKeyFactory kf = SecretKeyFactory.getInstance("DES");  
            SecretKey ky = kf.generateSecret(ks);  
  
            Cipher c = Cipher.getInstance(DES);  
            c.init(Cipher.DECRYPT_MODE, ky);  
            return c.doFinal(data);  
        } catch (Exception e) {  
            e.printStackTrace();  
            return null;  
        }  
    }   
    
    /**
     * 双倍长密钥的加密
     * @param key 密钥 16字节
     * @param data 数据 8字节
     * @return 加密的数据
     * 双倍长的DES加密过程是先用前半部分进行DES加密，后半部分进行DES解密
     * 再用前半部分进行DES加密
     */
    public static byte[] DoubleDesEncrypt(byte key[],byte data[]){
    	
    	byte [] key1 = new byte[8]; // 前半部分
    	byte [] key2 = new byte[8]; // 后半部分
    	
    	//对密钥进行截断，分为前半部分和后半部分 
    	for(int i=0;i<key.length;i++){
    		if(i<8){
    			key1[i] = key[i];
    		}else{
    			key2[i-8]=key[i];
    		}
    	}
    	
    	byte [] result = new byte[data.length]; // 中间进行转换使用数据
    	
    	//1. 前半部分对数据进行加密
    	result = UnionDesEncrypt(key1,data); 
    	
    	//2. 后半部分对数据进行解密
    	result = UnionDesDecrypt(key2,result);
    	
    	//3. 前半部分对数据进行加密
    	result = UnionDesEncrypt(key1, result);
    	
    	return result;
    }
    
    /**
     * 双倍长密钥的解密
     * @param key 密钥16字节 
     * @param data 数据 8字节
     * @return 解密的数据
     * 双倍长的DES解密过程是先用前半部分进行DES解密，后半部分进行DES加密
     * 再用前半部分进行DES解密
     */
    public static byte[] DoubleDesDecrypt(byte key[],byte data[]){
    	byte [] key1 = new byte[8]; // 前半部分
    	byte [] key2 = new byte[8]; // 后半部分
    	
    	//对密钥进行截断，分为前半部分和后半部分 
    	for(int i=0;i<key.length;i++){
    		if(i<8){
    			key1[i] = key[i];
    		}else{
    			key2[i-8]=key[i];
    		}
    	}
    	
    	byte [] result = new byte[data.length]; // 中间进行转换使用数据
    	
    	//1. 前半部分对数据进行加密
    	result = UnionDesDecrypt(key1,data); 
    	
    	//2. 后半部分对数据进行解密
    	result = UnionDesEncrypt(key2,result);
    	
    	//3. 前半部分对数据进行加密
    	result = UnionDesDecrypt(key1, result);
    	
    	return result;
    }
    
    
    /**
     * 3des加密
     * @param key 密钥
     * @param data 明文数据 16进制且长度为16的整数倍
     * @return  密文数据
     */
    public static byte[] Union3DesEncrypt(byte key[], byte data[]) {  
        try {  
            byte[] k = new byte[24];  
  
            int len = data.length;  
            if(data.length % 8 != 0){  
                len = data.length - data.length % 8 + 8;  
            }  
            byte [] needData = null;  
            if(len != 0)  
                needData = new byte[len];  
              
            for(int i = 0 ; i< len ; i++){  
                needData[i] = 0x00;  
            }  
              
            System.arraycopy(data, 0, needData, 0, data.length);  
              
            if (key.length == 16) {  
                System.arraycopy(key, 0, k, 0, key.length);  
                System.arraycopy(key, 0, k, 16, 8);  
            } else {  
                System.arraycopy(key, 0, k, 0, 24);  
            }  
  
            KeySpec ks = new DESedeKeySpec(k);  
            SecretKeyFactory kf = SecretKeyFactory.getInstance("DESede");  
            SecretKey ky = kf.generateSecret(ks);  
  
            Cipher c = Cipher.getInstance(TriDes);  
            c.init(Cipher.ENCRYPT_MODE, ky);  
            return c.doFinal(needData);  
        } catch (Exception e) {  
            e.printStackTrace();  
            return null;  
        }  
  
    }  
    
    /**
     * 3des解密
     * @param key 密钥
     * @param data 密文数据 16进制且长度为16的整数倍
     * @return   明文数据
     */
    public static byte[] Union3DesDecrypt(byte key[], byte data[]) {  
        try {  
            byte[] k = new byte[24];  
  
            int len = data.length;  
            if(data.length % 8 != 0){  
                len = data.length - data.length % 8 + 8;  
            }  
            byte [] needData = null;  
            if(len != 0)  
                needData = new byte[len];  
              
            for(int i = 0 ; i< len ; i++){  
                needData[i] = 0x00;  
            }  
              
            System.arraycopy(data, 0, needData, 0, data.length);  
              
            if (key.length == 16) {  
                System.arraycopy(key, 0, k, 0, key.length);  
                System.arraycopy(key, 0, k, 16, 8);  
            } else {  
                System.arraycopy(key, 0, k, 0, 24);  
            }  
            KeySpec ks = new DESedeKeySpec(k);  
            SecretKeyFactory kf = SecretKeyFactory.getInstance("DESede");  
            SecretKey ky = kf.generateSecret(ks);  
  
            Cipher c = Cipher.getInstance(TriDes);  
            c.init(Cipher.DECRYPT_MODE, ky);  
            return c.doFinal(needData);  
        } catch (Exception e) {  
            e.printStackTrace();  
            return null;  
        }  
    }
	
    /**
     * 3DES的分散密钥，双倍长密钥
     * @param key 双倍长密钥16字节，32字符
     * @param factor 分散因子，8字节，16字符
     * @return 返回的是密钥 16字节，32字符
     * 过程： 1. 用双倍长密钥对传入的分散因子进行加密
     *       2. 对分散因子进行取反
     *       3. 用双倍长密钥对取反后的分散因子进行加密
     *       4. 将2次加密的数据进行拼接返回
     */
    public static String GenRandomKey(String key ,String factor){
    	byte [] keyByte = TdXOR.str2Bcd(key);
    	byte [] factorByte = TdXOR.str2Bcd(factor);
    	
    	//1 . 使用双倍长密钥对传入的分散因子进行加密
    	byte [] temp1 = DoubleDesEncrypt(keyByte, factorByte);
    	//2 . 对分散因子进行取反操作
    	byte [] factorByteTemp = getByteNot(factorByte);
    	//3 . 使用双倍长密钥对取反后的分散因子进行加密
    	byte [] temp2 = DoubleDesEncrypt(keyByte, factorByteTemp);
    	
    	String result = "";
    	//4 . 将2次加密的数据进行拼接返回
    	result = TdXOR.bytesToHexString(temp1)+TdXOR.bytesToHexString(temp2);
    	return result;
    }
    
    /**
     * 数据解密
     * @param key 密钥 支持单倍和多倍密钥
     * @param data 密文数据 16进制且长度为16的整数倍
     * @return 明文数据
     */
    public static String UnionDecryptData(String key, String data)
    {
     if((key.length() != 16) && (key.length() != 32) && (key.length() != 48))
     {
      return(null);
     }
     if(data.length()%16 != 0)
     {
      return"";
     }
     int lenOfKey = 0;
     lenOfKey = key.length();
     String strEncrypt = "";
     byte sourData[] = TdXOR.str2Bcd(data);
     switch(lenOfKey)
     {
     case 16:
      byte deskey8[] = TdXOR.str2Bcd(key);      
      byte encrypt[] = UnionDesDecrypt(deskey8, sourData);
      strEncrypt = TdXOR.bytesToHexString(encrypt);
      break;
     case 32:
     case 48:
      String newkey1 = "";
      if(lenOfKey == 32)
      {
       String newkey = key.substring(0, 16);
       newkey1 = key+newkey;
      }else
      {
       newkey1 = key;
      }
      byte deskey24[] = TdXOR.str2Bcd(newkey1);
      byte desEncrypt[] = Union3DesDecrypt(deskey24, sourData);
      strEncrypt = TdXOR.bytesToHexString(desEncrypt);
     }
     return strEncrypt;
    }
    
    /**
     * 数据加密
     * @param key 密钥 支持单倍和多倍密钥
     * @param data 密文数据 16进制且长度为16的整数倍
     * @return 明文数据
     */
    public static String UnionEncryptData(String key, String data)
    {
     if((key.length() != 16) && (key.length() != 32) && (key.length() != 48))
     {
      return(null);
     }
     if(data.length()%16 != 0)
     {
      return"";
     }
     int lenOfKey = 0;
     lenOfKey = key.length();
     String strEncrypt = "";
     byte sourData[] = TdXOR.str2Bcd(data);
     switch(lenOfKey)
     {
     case 16:
      byte deskey8[] = TdXOR.str2Bcd(key);      
      byte encrypt[] = UnionDesEncrypt(deskey8, sourData);
      strEncrypt = TdXOR.bytesToHexString(encrypt).toUpperCase();
      break;
     case 32:
     case 48:
      String newkey1 = "";
      if(lenOfKey == 32)
      {
       String newkey = key.substring(0, 16);
       newkey1 = key+newkey;
      }else
      {
       newkey1 = key;
      }
      byte deskey24[] = TdXOR.str2Bcd(newkey1);
      byte desEncrypt[] = Union3DesEncrypt(deskey24, sourData);
      strEncrypt = TdXOR.bytesToHexString(desEncrypt).toUpperCase();
     }
     return strEncrypt;
    }
    
    public static void main(String [] args){
    	String key = "11111111111111112222222222222222";
    	String factor ="376210300220015736D49121208688560000000000000000"; 
    	String factor1 = "36D4912120868856";
    	String data = "1F5E52F5B1E42EB79BE0496C6FF78AF319B9A096AC58F52F";
//    	
    	String result2 = GenRandomKey(key, factor);
//    	String result3 = GenRandomKey(result2, factor);
//    	String result1 = UnionDecryptData(result2,data);
//    	
    	System.out.println(result2.toUpperCase());
//    	System.out.println(result3.toUpperCase());
//    	System.out.println(result1.toUpperCase());
    	
    	
//    	String temp = "9800000000000002";
//    	
//    	byte [] tempByte = TdXOR.str2Bcd(temp);
//    	
//    	byte [] result = getByteNot(tempByte);
//    	
//    	String resultString = TdXOR.bytesToHexString(result);
//    	
//    	System.out.println(resultString);
    	
    }
}
