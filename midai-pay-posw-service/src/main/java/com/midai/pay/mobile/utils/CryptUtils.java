package com.midai.pay.mobile.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


public class CryptUtils {  
   /** 
    * 进行MD5加密 
    *  
    * @param info 
    *            要加密的信息 
    * @return String 加密后的字符串 
    */  
   public static final String AES_Password = "dynamicode";// AES密钥明文
   public static String encryptToMD5(String info) { 
      byte[] digesta = null;  
       try {  
           // 得到一个md5的消息摘要   
           MessageDigest alga = MessageDigest.getInstance("MD5");  
           // 添加要进行计算摘要的信息   
          // alga.update(info.getBytes());  
           alga.update(info.getBytes("utf-8"));
           // 得到该摘要   
           digesta = alga.digest();  
       } catch (NoSuchAlgorithmException e) {  
           e.printStackTrace();  
       } catch (UnsupportedEncodingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}  
       // 将摘要转为字符串   
       String rs = byte2hex(digesta);  
       return rs;  
   }  
   /** 
    * 进行SHA加密 
    *  
    * @param info 
    *            要加密的信息 
    * @return String 加密后的字符串 
    */  
   private static String encryptToSHA(String info) {  
       byte[] digesta = null;  
       try {  
           // 得到一个SHA-1的消息摘要   
           MessageDigest alga = MessageDigest.getInstance("SHA-1");  
           // 添加要进行计算摘要的信息   
           alga.update(info.getBytes());  
           // 得到该摘要   
           digesta = alga.digest();  
       } catch (NoSuchAlgorithmException e) {  
           e.printStackTrace();  
       }  
       // 将摘要转为字符串   
       String rs = byte2hex(digesta);  
       return rs;  
   }  
   // //////////////////////////////////////////////////////////////////////////   
   /** 
    * 创建密匙 
    *  
    * @param algorithm 
    *            加密算法,可用 DES,DESede,Blowfish 
    * @return SecretKey 秘密（对称）密钥 
    */  
   private static SecretKey createSecretKey(String algorithm) {  
       // 声明KeyGenerator对象   
       KeyGenerator keygen;  
       // 声明 密钥对象   
       SecretKey deskey = null;  
       try {  
           // 返回生成指定算法的秘密密钥的 KeyGenerator 对象   
           keygen = KeyGenerator.getInstance(algorithm);  
           // 生成一个密钥   
           deskey = keygen.generateKey();  
       } catch (NoSuchAlgorithmException e) {  
           e.printStackTrace();  
       }  
       // 返回密匙   
       return deskey;  
   }  
   /** 
    * 根据密匙进行DES加密 
    *  
    * @param key 
    *            密匙 
    * @param info 
    *            要加密的信息 
    * @return String 加密后的信息 
    */  
   private static String encryptToDES(SecretKey key, String info) {  
       // 定义 加密算法,可用 DES,DESede,Blowfish   
       String Algorithm = "DES";  
        // 加密随机数生成器 (RNG),(可以不写)   
        SecureRandom sr = new SecureRandom();  
        // 定义要生成的密文   
        byte[] cipherByte = null;  
        try {  
            // 得到加密/解密器   
            Cipher c1 = Cipher.getInstance(Algorithm);  
            // 用指定的密钥和模式初始化Cipher对象   
            // 参数:(ENCRYPT_MODE, DECRYPT_MODE, WRAP_MODE,UNWRAP_MODE)   
            c1.init(Cipher.ENCRYPT_MODE, key, sr);  
            // 对要加密的内容进行编码处理,   
            cipherByte = c1.doFinal(info.getBytes());  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        // 返回密文的十六进制形式   
        return byte2hex(cipherByte);  
    }  
    /** 
     * 根据密匙进行DES解密 
     *  
     * @param key 
     *            密匙 
     * @param sInfo 
     *            要解密的密文 
     * @return String 返回解密后信息 
     */  
   private static String decryptByDES(SecretKey key, String sInfo) {  
        // 定义 加密算法,   
        String Algorithm = "DES";  
        // 加密随机数生成器 (RNG)   
        SecureRandom sr = new SecureRandom();  
        byte[] cipherByte = null;  
        try {  
            // 得到加密/解密器   
            Cipher c1 = Cipher.getInstance(Algorithm);  
            // 用指定的密钥和模式初始化Cipher对象   
            c1.init(Cipher.DECRYPT_MODE, key, sr);  
            // 对要解密的内容进行编码处理   
            cipherByte = c1.doFinal(hex2byte(sInfo));  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        // return byte2hex(cipherByte);   
        return new String(cipherByte);  
    }  
    // /////////////////////////////////////////////////////////////////////////////   
    /** 
     * 创建密匙组，并将公匙，私匙放入到指定文件中 
     *  
     * 默认放入mykeys.bat文件中 
     */  
   private static void createPairKey() {  
        try {  
            // 根据特定的算法一个密钥对生成器   
            KeyPairGenerator keygen = KeyPairGenerator.getInstance("DSA");  
            // 加密随机数生成器 (RNG)   
            SecureRandom random = new SecureRandom();  
            // 重新设置此随机对象的种子   
            random.setSeed(1000);  
            // 使用给定的随机源（和默认的参数集合）初始化确定密钥大小的密钥对生成器   
            keygen.initialize(512, random);// keygen.initialize(512);   
            // 生成密钥组   
            KeyPair keys = keygen.generateKeyPair();  
            // 得到公匙   
            PublicKey pubkey = keys.getPublic();  
            // 得到私匙   
            PrivateKey prikey = keys.getPrivate();  
            // 将公匙私匙写入到文件当中   
            doObjToFile("mykeys.bat", new Object[] { prikey, pubkey });  
        } catch (NoSuchAlgorithmException e) {  
            e.printStackTrace();  
        }  
    }  
    /** 
     * 利用私匙对信息进行签名 把签名后的信息放入到指定的文件中 
     *  
     * @param info 
     *            要签名的信息 
     * @param signfile 
     *            存入的文件 
     */  
   private static void signToInfo(String info, String signfile) {  
        // 从文件当中读取私匙   
        PrivateKey myprikey = (PrivateKey) getObjFromFile("mykeys.bat", 1);  
        // 从文件中读取公匙   
        PublicKey mypubkey = (PublicKey) getObjFromFile("mykeys.bat", 2);  
        try {  
            // Signature 对象可用来生成和验证数字签名   
            Signature signet = Signature.getInstance("DSA");  
            // 初始化签署签名的私钥   
            signet.initSign(myprikey);  
            // 更新要由字节签名或验证的数据   
            signet.update(info.getBytes());  
            // 签署或验证所有更新字节的签名，返回签名   
            byte[] signed = signet.sign();  
            // 将数字签名,公匙,信息放入文件中   
            doObjToFile(signfile, new Object[] { signed, mypubkey, info });  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
    /** 
     * 读取数字签名文件 根据公匙，签名，信息验证信息的合法性 
     *  
     * @return true 验证成功 false 验证失败 
     */  
   private static boolean validateSign(String signfile) {  
        // 读取公匙   
        PublicKey mypubkey = (PublicKey) getObjFromFile(signfile, 2);  
        // 读取签名   
        byte[] signed = (byte[]) getObjFromFile(signfile, 1);  
        // 读取信息   
        String info = (String) getObjFromFile(signfile, 3);  
        try {  
            // 初始一个Signature对象,并用公钥和签名进行验证   
            Signature signetcheck = Signature.getInstance("DSA");  
            // 初始化验证签名的公钥   
            signetcheck.initVerify(mypubkey);  
            // 使用指定的 byte 数组更新要签名或验证的数据   
            signetcheck.update(info.getBytes());  
            System.out.println(info);  
            // 验证传入的签名   
            return signetcheck.verify(signed);  
        } catch (Exception e) {  
            e.printStackTrace();  
            return false;  
        }  
    }  
    /** 
     * 将二进制转化为16进制字符串 
     *  
     * @param b 
     *            二进制字节数组 
     * @return String 
     */  
   private static String byte2hex(byte[] b) {  
        String hs = "";  
        String stmp = "";  
        for (int n = 0; n < b.length; n++) {  
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {  
                hs = hs + "0" + stmp;  
            } else {  
                hs = hs + stmp;  
            }  
        }  
        return hs.toUpperCase();  
    }  
    /** 
     * 十六进制字符串转化为2进制 
     *  
     * @param hex 
     * @return 
     */  
   private static  byte[] hex2byte(String hex) {  
        byte[] ret = new byte[8];  
        byte[] tmp = hex.getBytes();  
        for (int i = 0; i < 8; i++) {  
            ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);  
        }  
        return ret;  
    }  
    /** 
     * 将两个ASCII字符合成一个字节； 如："EF"--> 0xEF 
     *  
     * @param src0 
     *            byte 
     * @param src1 
     *            byte 
     * @return byte 
     */  
   private static byte uniteBytes(byte src0, byte src1) {  
        byte _b0 = Byte.decode("0x" + new String(new byte[] { src0 }))  
                .byteValue();  
        _b0 = (byte) (_b0 << 4);  
        byte _b1 = Byte.decode("0x" + new String(new byte[] { src1 }))  
                .byteValue();  
        byte ret = (byte) (_b0 ^ _b1);  
        return ret;  
    }  
    /** 
     * 将指定的对象写入指定的文件 
     *  
     * @param file 
     *            指定写入的文件 
     * @param objs 
     *            要写入的对象 
     */  
   private static void doObjToFile(String file, Object[] objs) {  
        ObjectOutputStream oos = null;  
        try {  
            FileOutputStream fos = new FileOutputStream(file);  
            oos = new ObjectOutputStream(fos);  
            for (int i = 0; i < objs.length; i++) {  
                oos.writeObject(objs[i]);  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            try {  
                oos.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
    }  
    /** 
     * 返回在文件中指定位置的对象 
     *  
     * @param file 
     *            指定的文件 
     * @param i 
     *            从1开始 
     * @return 
     */  
   private static Object getObjFromFile(String file, int i) {  
        ObjectInputStream ois = null;  
        Object obj = null;  
        try {  
            FileInputStream fis = new FileInputStream(file);  
            ois = new ObjectInputStream(fis);  
            for (int j = 0; j < i; j++) {  
                obj = ois.readObject();  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            try {  
                ois.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
        return obj;  
    }  
    /** 
     * 测试 
     *  
     * @param args 
     */  
   private static void main(String[] args) {  
        /*StringBuffer sb = new StringBuffer("hello");
        // 执行MD5加密"Hello world!"   
        System.out.println("Hello经过MD5:" + CryptUtils.encryptToMD5(sb.toString()).length());  
        // 生成一个DES算法的密匙   
        SecretKey key = CryptUtils.createSecretKey("DES");  
        // 用密匙加密信息"Hello world!"   
        String str1 = CryptUtils.encryptToDES(key, sb.toString());  
        System.out.println("使用des加密信息Hello为:" + str1);  
        // 使用这个密匙解密   
        String str2 = CryptUtils.decryptByDES(key, str1);  
        System.out.println("解密后为：" + str2);  
        // 创建公匙和私匙   
*/        
    	
//    	<?xml version='1.0' encoding='utf-8'?>
//    	<TokenProtocol><Epid>84cedebb2fed95d25c8ef2a705ec72f6</Epid>
//    	<TokenSN>6FA7723CA56BF3313C108DDE35FC8506</TokenSN>
//    	<Dynamicode>0F293B3A3CA1829A</Dynamicode>
//    	<PackageMac>5010C2FF67011A266A3339608F942C244F4E8F6B</PackageMac>
//    	</TokenProtocol>
//    	<?xml version='1.0' encoding='utf-8'?>
//    	<TokenProtocol><Epid>84cedebb2fed95d25c8ef2a705ec72f6</Epid>
//    	<TokenSN>90324BC743686526819BAED12A31943F</TokenSN>
//    	<Dynamicode>0C26EE0531AA6052</Dynamicode>
//    	<PackageMac>UBDC/2cBGiZqMzlgj5QsJE9Oj2s=</PackageMac>
//    	</TokenProtocol>
    	
    	String str ="62810440;68606773f1412f65022c09ab419bff29dd77414e730e8bbf76ae81617d6f8df85532d4cc741924a71b5de01e045e1a2be7b61cd6c2e90c92bd80edb9152e6b29b93506fa2bb3719d432c425d0b13c97afb575b3ce5fa0dc3a55df3f89c196cc0737363e256781c4e1ff0d3081c33157728f1d3ac0b16f80cf9bb8aada33e2a19ca0e11eb81d67025ba2b19e0c7b6e026";
    	System.out.println(CryptUtils.encryptToSHA(str));
    	//e10adc3949ba59abbe56e057f20f883e
    	//21232F297A57A5A743894A0E4A801FC3
    	System.out.println("#################"+CryptUtils.encryptToMD5("123456"));
    	
    	/*try {
			System.out.println("##########MD5#######"+MD5.md5("123456"));
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
    	
		/*byte[] arrBTmp = {(byte)0xB0,(byte)0x0D,(byte)0xDF,(byte)0x9D,(byte)0x93,(byte)0xE1,(byte)0x99,(byte)0xEF,(byte)0xEA,(byte)0xE9,(byte)0x67,(byte)0x80,(byte)0x5E,(byte)0x0A,(byte)0x52,(byte)0x28};
System.out.println(arrBTmp.toString());*/
    }  
    
    /**
     * AES加密
     * 
     * @param content 需要加密的内容
     * @param password  加密密码
     * @return
     */
   private static byte[] encryptToAES(String content, String password) {
            try {           
                KeyGenerator kgen = KeyGenerator.getInstance("AES");
                kgen.init(128, new SecureRandom(password.getBytes()));
                SecretKey secretKey = kgen.generateKey();
                byte[] enCodeFormat = secretKey.getEncoded();
                SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
                Cipher cipher = Cipher.getInstance("AES");// 创建密码器
                byte[] byteContent = content.getBytes("utf-8");
                cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
                byte[] result = cipher.doFinal(byteContent);
                return result; // 加密
            } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
            } catch (InvalidKeyException e) {
                    e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
            } catch (BadPaddingException e) {
                    e.printStackTrace();
            }
            return null;
    }
}  
