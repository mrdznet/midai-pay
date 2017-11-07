/**
 * Project Name:midai-pay-posp-service
 * File Name:TestXml.java
 * Package Name:com.midai.pay.posp.config
 * Date:2016年9月27日下午9:43:11
 * Copyright (c) 2016, www midaigroup com Technology Co., Ltd. All Rights Reserved.
 *
*/

package com.midai.pay.posp.config;

import javax.xml.bind.JAXBException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ClassName:TestXml <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年9月27日 下午9:43:11 <br/>
 * @author   陈勋
 * @version  
 * @since    JDK 1.7
 * @see 	 
 */
public class TestXml {

    public static void main(String[] args) throws JAXBException, IOException {

//        JAXBContext jc = JAXBContext.newInstance(Pay8583Bean.class);  
//        Unmarshaller unmar = jc.createUnmarshaller();  
//        Pay8583Bean stu = (Pay8583Bean) unmar.unmarshal(TestXml.class.getClassLoader().getResourceAsStream("pay8583.xml"));
//        System.out.println(stu.getItem());
        
        //将xml字符串转换为java对象  
//        JaxbUtil resultBinder = new JaxbUtil(Pay8583Bean.class,  
//                CollectionWrapper.class);  
//        Pay8583Bean hotelObj = resultBinder.fromXml(TestXml.class.getClassLoader().getResourceAsStream("pay8583.xml"));
//        System.out.println(hotelObj.getPay8583bean().getTpdu());
        
        

                    //0200
//        //异或算法
//        byte[] mab=null;
//        String content="0200702006C320C09A11166259061507996889000000000001100000447532051000000006313030303030313220200800001960376259061507996889D18102010000038700000030313031353834383830303239303035373232303234343135365AEDF8B525877D18260000000000000001459F26083E5FF8152CC88BA79F2701809F101307090103A0A000010A0100000000006F181C949F3704000040419F360200E2950580000008009A031609289C01009F02060000011000005F2A02015682027C009F1A0201569F03060000000000009F330390C8C09F34033F00009F3501229F1E0831323334353637388408A0000003330101039F090200209F41040000000200132200000100050";
//      // String content="166259061507996889000000000001100000447532051000000006313030303030313220200800001960376259061507996889D18102010000038700000030313031353834383830303239303035373232303234343135365AEDF8B525877D18260000000000000001459F26083E5FF8152CC88BA79F2701809F101307090103A0A000010A0100000000006F181C949F3704000040419F360200E2950580000008009A031609289C01009F02060000011000005F2A02015682027C009F1A0201569F03060000000000009F330390C8C09F34033F00009F3501229F1E0831323334353637388408A0000003330101039F090200209F41040000000200132200000100050";
//        mab=arrayApend(mab,Pay8583Util.hexStringToBytes(content));
//        //补全
//        if(mab.length%8!=0){
//            byte[] b=new byte[8-mab.length%8];
//            mab=arrayApend(mab,b);
//        }
//        int k=0;
//       
//       byte[] aaa=new byte[8];
//       byte[] bbb=new byte[8];   //00000000
//       while(true){
//           if(k>=mab.length){
//               break ;
//           }          
//           System.arraycopy(mab, k, aaa, 0, aaa.length);
//           k+=aaa.length;
//           for(int i=0;i<aaa.length;i++){
//               bbb[i]=(byte) (aaa[i]^bbb[i]);
//           }
//           
//       }
//       System.out.println(Pay8583Util.bytesToHexString(bbb));
//       
//       String macData=Pay8583Util.bytesToHexString(Pay8583Util.bytesToHexString(bbb).getBytes());
//       System.out.println(macData);
//       
//     String sss=  new String(Pay8583Util.hexStringToBytes("37303233374634383345423033453637"));
//     System.out.println(sss);
     
        
//        System.out.println(Math.pow(2, 44));
//       int len= Pay8583Util.hexStringToBytes(String.format("%04x", 315)).length;
//        String sss=new String(Pay8583Util.hexStringToBytes("B3ACB3F6CFFBB7D1B4CECAFDCFDED6C600"), Charset.forName("GBK"));
//        System.out.println(sss);
//       byte[] aa=new byte[]{25};
//        System.out.println(Pay8583Util.bytesToHexString(aa));
    	System.out.println(Long.parseLong("5E",16)); 
    	
//        
        String s="008D600000000060100031120302007020000200D00153166214850218456421290000000000000993195103313030303030333520203031303639353738383030323930303537323230353537123330383239303030333439320006DEC9CFFED4A8001530313032393030353732333035353000089020120300123132393131343430333839393646384539393531";
        byte[] out=Pay8583Util.hexStringToBytes(s);
        Socket iSocketHandle = null;
		InputStream iInputStream = null;
		OutputStream iOutputStream = null;
		byte[] in=new byte[2048];
			iSocketHandle = new Socket();
			InetSocketAddress hsmAddr = new InetSocketAddress("117.185.7.189", 6667);
			iSocketHandle.connect(hsmAddr, 50000);
			iInputStream = iSocketHandle.getInputStream();
			iOutputStream = iSocketHandle.getOutputStream();
			iOutputStream.write(out, 0, out.length);
			iInputStream.read(in, 0, 2048);
			
			System.out.println(Pay8583Util.bytesToHexString(in));
			
			


        
     
     

    }
    
    public static byte[] arrayApend(byte[] a,byte[] b){
        int a_len=(a==null?0:a.length);
        int b_len=(b==null?0:b.length);
        byte[] c=new byte[a_len+b_len];
        if(a_len==0&&b_len==0){
            return null;
        }else if(a_len==0){
            System.arraycopy(b, 0, c, 0, b.length);
        }else if(b_len==0){
            System.arraycopy(a, 0, c, 0, a.length);
        }else{
            System.arraycopy(a, 0, c, 0, a.length);
            System.arraycopy(b, 0, c, a.length, b.length);
        }
        return c;
    }
    

}

