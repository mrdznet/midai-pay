/**
 * Project Name:midai-pay-posp-service
 * File Name:Pay8583Util.java
 * Package Name:com.midai.pay.posp.service.impl
 * Date:2016年9月27日上午9:38:48
 * Copyright (c) 2016, www midaigroup com Technology Co., Ltd. All Rights Reserved.
 *
*/

package com.midai.pay.posp.config;

/**
 * ClassName:Pay8583Util <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年9月27日 上午9:38:48 <br/>
 * @author   陈勋
 * @version  
 * @since    JDK 1.7
 * @see 	 
 */
public class Pay8583Util {
    
/*	private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
	public static String bytesToHex(byte[] bytes) {
	    char[] hexChars = new char[bytes.length * 2];
	    for ( int j = 0; j < bytes.length; j++ ) {
	        int v = bytes[j] & 0xFF;
	        hexChars[j * 2] = hexArray[v >>> 4];
	        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	    }
	    return new String(hexChars);
	}*/
	
    /** 
     * Convert byte[] to hex string.这里我们可以将byte转换成int，然后利用Integer.toHexString(int)来转换成16进制字符串。 
     * @param src byte[] data 
     * @return hex string 
     */     
    public static String bytesToHexString(byte[] src){  
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
        return stringBuilder.toString().toUpperCase();  
    }  
    /** 
     * Convert hex string to byte[] 
     * @param hexString the hex string 
     * @return byte[] 
     */  
    public static byte[] hexStringToBytes(String hexString) {  
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
    /** 
     * Convert char to byte 
     * @param c char 
     * @return byte 
     */  
     public static byte charToByte(char c) {  
        return (byte) "0123456789ABCDEF".indexOf(c);  
    }  
     
     /** 
      * @功能: BCD码转为10进制串(阿拉伯数据) 
      * @参数: BCD码 
      * @结果: 10进制串 
      */  
     public static String bcd2Str(byte[] bytes) {  
         StringBuffer temp = new StringBuffer(bytes.length * 2);  
         for (int i = 0; i < bytes.length; i++) {  
             temp.append((byte) ((bytes[i] & 0xf0) >>> 4));  
             temp.append((byte) (bytes[i] & 0x0f));  
         }  
         return temp.toString().substring(0, 1).equalsIgnoreCase("0") ? temp  
                 .toString().substring(1) : temp.toString();  
     }  
   
     /** 
      * @功能: 10进制串转为BCD码 
      * @参数: 10进制串 
      * @结果: BCD码 
      */  
     public static byte[] str2Bcd(String asc) {  
         int len = asc.length();  
         int mod = len % 2;  
         if (mod != 0) {  
             asc = "0" + asc;  
             len = asc.length();  
         }  
         byte abt[] = new byte[len];  
         if (len >= 2) {  
             len = len / 2;  
         }  
         byte bbt[] = new byte[len];  
         abt = asc.getBytes();  
         int j, k;  
         for (int p = 0; p < asc.length() / 2; p++) {  
             if ((abt[2 * p] >= '0') && (abt[2 * p] <= '9')) {  
                 j = abt[2 * p] - '0';  
             } else if ((abt[2 * p] >= 'a') && (abt[2 * p] <= 'z')) {  
                 j = abt[2 * p] - 'a' + 0x0a;  
             } else {  
                 j = abt[2 * p] - 'A' + 0x0a;  
             }  
             if ((abt[2 * p + 1] >= '0') && (abt[2 * p + 1] <= '9')) {  
                 k = abt[2 * p + 1] - '0';  
             } else if ((abt[2 * p + 1] >= 'a') && (abt[2 * p + 1] <= 'z')) {  
                 k = abt[2 * p + 1] - 'a' + 0x0a;  
             } else {  
                 k = abt[2 * p + 1] - 'A' + 0x0a;  
             }  
             int a = (j << 4) + k;  
             byte b = (byte) a;  
             bbt[p] = b;  
         }  
         return bbt;  
     }  
     
     
     public static byte[] ASCII_To_BCD(byte[] ascii, int asc_len) {  
         byte[] bcd = new byte[asc_len / 2];  
         int j = 0;  
         for (int i = 0; i < (asc_len + 1) / 2; i++) {  
             bcd[i] = asc_to_bcd(ascii[j++]);  
             bcd[i] = (byte) (((j >= asc_len) ? 0x00 : asc_to_bcd(ascii[j++])) + (bcd[i] << 4));  
         }  
         return bcd;  
     }


     
     public static byte asc_to_bcd(byte asc) {  
         byte bcd;  
   
         if ((asc >= '0') && (asc <= '9'))  
             bcd = (byte) (asc - '0');  
         else if ((asc >= 'A') && (asc <= 'F'))  
             bcd = (byte) (asc - 'A' + 10);  
         else if ((asc >= 'a') && (asc <= 'f'))  
             bcd = (byte) (asc - 'a' + 10);  
         else  
             bcd = (byte) (asc - 48);  
         return bcd;  
     }  
     
     public static void main(String[] args) throws Exception {
       /*  String s = "E9E4A9A456FD7C44";  
        String ss=bytesToHexString(s.substring(0,8).getBytes()) ; 320, 160
        System.out.println(ss);*/
        
         String bcd = "30343636334441464432353837304136454630423935314246354241383235353139423045343238384145414438413036304533374246323332303344333241433835444244324135344445343137323846394230374538423444434543383143353446324137423942313230334243393743453136373038423034413330304442383046464239384236413832384330303541314642453330314132324144";
         System.out.println(bcd.length());
         
         String asc = "3034363633444146443235383730413645463042393531424635424138323535313942304534323838414541443841303630453337424632333230334433324143383544424432413534444534313732";
         System.out.println(asc.length());
     }
     
    
     


    
}
