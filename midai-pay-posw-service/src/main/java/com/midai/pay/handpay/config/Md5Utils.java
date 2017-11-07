package com.midai.pay.handpay.config;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;

/**
 * Created by qjj on 15-11-1.
 */
public class Md5Utils {

	public static String md5(byte[] mesage) {
		Hasher hasher = Hashing.md5().newHasher();
		hasher.putBytes(mesage);
		String md5 = hasher.hash().toString().toUpperCase();
		return md5;
	}
	
	public static String md5Common(String msg) throws NoSuchAlgorithmException, UnsupportedEncodingException{
		 //用于加密的字符  
        char md5String[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };  
        //使用平台的默认字符集将此 String 编码为 byte序列，并将结果存储到一个新的 byte数组中  
        byte[] btInput = msg.getBytes();  
           
        //信息摘要是安全的单向哈希函数，它接收任意大小的数据，并输出固定长度的哈希值。  
        MessageDigest mdInst = MessageDigest.getInstance("MD5");  
           
        //MessageDigest对象通过使用 update方法处理数据， 使用指定的byte数组更新摘要  
        mdInst.update(btInput);  
           
        // 摘要更新之后，通过调用digest（）执行哈希计算，获得密文  
        byte[] md = mdInst.digest();  
           
        // 把密文转换成十六进制的字符串形式  
        int j = md.length;  
        char str[] = new char[j * 2];  
        int k = 0;  
        for (int i = 0; i < j; i++) {   //  i = 0  
            byte byte0 = md[i];  //95  
            str[k++] = md5String[byte0 >>> 4 & 0xf];    //    5   
            str[k++] = md5String[byte0 & 0xf];   //   F  
        }  
           
        //返回经过加密后的字符串  
        return new String(str);  
	}
	
	public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException{
		String str="123456790|重庆小面|王艳峰|上海市|上海市|上海市浦东新区峨山路155号|01|412725198806055714|15618703352|6214830217712859|王艳峰|中国招商银行|303100000006|中国招商银行新客站支行|上海市|上海市|0.2|0.006|0.2|0.006|2FC19851227CD17066E9AA5894740504";
		System.out.println(md5Common(str));
	}
}
