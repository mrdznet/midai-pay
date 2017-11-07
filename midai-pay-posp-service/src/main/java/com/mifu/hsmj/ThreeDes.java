package com.mifu.hsmj;


import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/*字符串 DESede(3DES) 加密
 * ECB模式/使用PKCS7方式填充不足位,目前给的密钥是192位
 * 3DES（即Triple DES）是DES向AES过渡的加密算法（1999年，NIST将3-DES指定为过渡的
 * 加密标准），是DES的一个更安全的变形。它以DES为基本模块，通过组合分组方法设计出分组加
 * 密算法，其具体实现如下：设Ek()和Dk()代表DES算法的加密和解密过程，K代表DES算法使用的
 * 密钥，P代表明文，C代表密表，这样，
 * 3DES加密过程为：C=Ek3(Dk2(Ek1(P)))
 * 3DES解密过程为：P=Dk1((EK2(Dk3(C)))
 * */
public class ThreeDes {

	/**
	 * @param args在java中调用sun公司提供的3DES加密解密算法时，需要使
	 * 用到$JAVA_HOME/jre/lib/目录下如下的4个jar包：
	 *jce.jar
	 *security/US_export_policy.jar
	 *security/local_policy.jar
	 *ext/sunjce_provider.jar 
	 */
	
	private static final String Algorithm = "DESede"; //定义加密算法,可用 DES,DESede,Blowfish
    //keybyte为加密密钥，长度为24字节    
	//src为被加密的数据缓冲区（源）
	public static byte[] encryptMode(byte[] keybyte,byte[] src){
		 try {
			//生成密钥
			SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);
			//加密
			Cipher c1 = Cipher.getInstance(Algorithm);
			c1.init(Cipher.ENCRYPT_MODE, deskey);
			return c1.doFinal(src);//在单一方面的加密或解密
		} catch (java.security.NoSuchAlgorithmException e1) {
			// TODO: handle exception
			 e1.printStackTrace();
		}catch(javax.crypto.NoSuchPaddingException e2){
			e2.printStackTrace();
		}catch(java.lang.Exception e3){
			e3.printStackTrace();
		}
		return null;
	}
	
	//keybyte为加密密钥，长度为24字节    
	//src为加密后的缓冲区
	public static byte[] decryptMode(byte[] keybyte,byte[] src){
		try {
			//生成密钥
			SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);
			//解密
			Cipher c1 = Cipher.getInstance("DESede/ECB/NoPadding");
			c1.init(Cipher.DECRYPT_MODE, deskey);
			return c1.doFinal(src);
		} catch (java.security.NoSuchAlgorithmException e1) {
			// TODO: handle exception
			e1.printStackTrace();
		}catch(javax.crypto.NoSuchPaddingException e2){
			e2.printStackTrace();
		}catch(java.lang.Exception e3){
			e3.printStackTrace();
		}
		return null;		
	}
	
    //转换成十六进制字符串
	public static String byte2Hex(byte[] b){
		String hs="";
		String stmp="";
		for(int n=0; n<b.length; n++){
			stmp = (java.lang.Integer.toHexString(b[n]& 0XFF));
			if(stmp.length()==1){
				hs = hs + "0" + stmp;				
			}else{
				hs = hs + stmp;
			}
			if(n<b.length-1)hs=hs+":";
		}
		return hs.toUpperCase();		
	}
	
	
	public static void main(String[] args) {
		//添加新安全算法,如果用JCE就要把它添加进去
//		Security.addProvider(new com.sun.crypto.provider.SunJCE());
		String szSrc = "This is a 3DES test. 测试";
		
		//String data = "EB8B970F05C99272B147ED5A1F8BE145";
		//String key = "DD71FDD099F90ECF42314D8476DF6E73";
		//data = decrypt(key, data);
		//key = "11111111111111111111111111111111";
		//encrypt(key,data);
		
	/*	String data = "1234567890123456789=05082017819991683";
		String key = "2C5EEDDF81F1DBA0CA68DC8CD00DCA21";
		for(String s:trackEncrypt(data, key)){
			System.out.println(s);
		}*/
		String key = "4661fc95d1d6fab29d29bd50191b070a";
		String eMasKey = "04663DAFD25870A6EF0B951BF5BA8255";
		String res = ThreeDes.decrypt(key, eMasKey);
		System.out.println(res);
	}
	
	public static String[] trackEncrypt(final String data,final String key){
		StringBuilder sb = new StringBuilder(data);
		int covers = sb.length()%16;
		for(int i=0;i<16-covers;i++){
			sb.append("F");
		}
		String[] result = new String[sb.length()/16];
		for(int i=0;i<result.length;i++){
			String tdb2n = sb.substring(0, 16);
			System.out.println(tdb2n);
			result[i]=encrypt(key,tdb2n).substring(0, 16);
			sb.delete(0, 16);
		}
		return result;
	}
	
	
	
	
	public static String encrypt(String key,String data){
		byte[] encoded = HsmConverts.hexStringToByte(data);
		byte[] keyByte =  makeKey(key);
		byte[] result = encryptMode(keyByte,encoded);
		String str = HsmConverts.bytesToHexString(result);
//		System.out.println("加密后的字符串:"+str);
		return str;
	}
	
	public static String decrypt(String key,String data){
		byte[] encoded = HsmConverts.hexStringToByte(data);
		byte[] keyByte =  makeKey(key);
		byte[] result = decryptMode(keyByte,encoded);
		String str = HsmConverts.bytesToHexString(result);
//		System.out.println("解密后的字符串:"+str);
		return str;
	}
	
	private static byte[] makeKey(String key){
		byte[] bKey = HsmConverts.hexStringToByte(key);
		
		byte[] newKey = new byte[24];
		System.arraycopy(bKey, 0, newKey, 0, 16);
		System.arraycopy(bKey, 0, newKey, 16, 8);
		
		return newKey;
	}
	
}
