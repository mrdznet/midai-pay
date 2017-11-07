/**
 * Project Name:midai-pay-common
 * File Name:OtherayUtils.java
 * Package Name:com.midai.framework.common
 * Date:2016年9月20日上午9:06:18
 * Copyright (c) 2016, Shanghai Law Cloud Technology Co., Ltd. All Rights Reserved.
 *
*/

package com.midai.framework.common;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;
import java.util.UUID;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.xml.sax.InputSource;

import com.midai.framework.common.http.HttpClientConnectionManager;

/**
 * ClassName:OtherayUtils <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年9月20日 上午9:06:18 <br/>
 * @author   屈志刚
 * @version  
 * @since    JDK 1.7
 * @see 	 
 */
public class OtherayUtils {
	
	public static DefaultHttpClient httpclient;

	  static
	  {
	    httpclient = new DefaultHttpClient();
	    httpclient = (DefaultHttpClient)HttpClientConnectionManager.getSSLInstance(httpclient);
	  }

	
	/**
	 * 定义字符数组
	 */
	public static String[] chars = new String[] { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3",
		"4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };

    // 生成短8位的UUID
	public static String generateShortUuid() {
		StringBuffer shortBuffer = new StringBuffer();
		String uuid = UUID.randomUUID().toString().replace("-", "");
		for (int i = 0; i < 8; i++) {
			String str = uuid.substring(i * 4, i * 4 + 4);
			int x = Integer.parseInt(str, 16);
			shortBuffer.append(chars[x % 0x3E]);
		}
		return shortBuffer.toString();
	
	}
	
	
	/**
	 * 
	 * getRandomStringByLength:(获取一定长度的随机字符串). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 * TODO(这里描述这个方法的执行流程 – 可选).<br/>
	 * TODO(这里描述这个方法的使用方法 – 可选).<br/>
	 * TODO(这里描述这个方法的注意事项 – 可选).<br/>
	 *
	 * @author 屈志刚
	 * @param length
	 * @return
	 * @since JDK 1.7
	 */
    public static String getRandomStringByLength(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }
	
	
	/**
	 * 
	 * createSign:(签名). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 * TODO(这里描述这个方法的执行流程 – 可选).<br/>
	 * TODO(这里描述这个方法的使用方法 – 可选).<br/>
	 * TODO(这里描述这个方法的注意事项 – 可选).<br/>
	 *
	 * @author 屈志刚
	 * @param packageParams
	 * @param key
	 * @return
	 * @since JDK 1.7
	 */
	public String createSign(SortedMap<String, String> packageParams,String key) {
		StringBuffer sb = new StringBuffer();      
		Set es = packageParams.entrySet();
		//所有参与传参的参数按照accsii排序（升序）      
		Iterator it = es.iterator();  
			while(it.hasNext()) {       
				Map.Entry entry = (Map.Entry)it.next();        
				String k = (String)entry.getKey();       
				Object v = entry.getValue();       
				if(null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {         
				  sb.append(k + "=" + v + "&");        
				}    
		}
		sb.append("key=" + key);  
		
		String sign = MD5Util.MD5Encode(sb.toString(), "UTF-8").toUpperCase();  
		
		return sign;  
		
	}
	
    /**
     * 
     * mapToXMLTest2:(map转XML). <br/>
     * TODO(这里描述这个方法适用条件 – 可选).<br/>
     * TODO(这里描述这个方法的执行流程 – 可选).<br/>
     * TODO(这里描述这个方法的使用方法 – 可选).<br/>
     * TODO(这里描述这个方法的注意事项 – 可选).<br/>
     *
     * @author 屈志刚
     * @param map
     * @param sb
     * @return
     * @since JDK 1.7
     */
	@SuppressWarnings("rawtypes")
    private  static String mapToXMLTest2(Map map, StringBuffer sb) {  
        Set set = map.keySet();  
        
        sb.append("<xml>");
        
        for (Iterator it = set.iterator(); it.hasNext();) {  
            String key = (String) it.next();  
            Object value = map.get(key);  
            if (null == value)  
                value = "";  
            if (value.getClass().getName().equals("java.util.ArrayList")) {  
                ArrayList list = (ArrayList) map.get(key);  
                sb.append("<" + key + ">");  
                for (int i = 0; i < list.size(); i++) {  
                    HashMap hm = (HashMap) list.get(i);  
                    mapToXMLTest2(hm, sb);  
                }  
                sb.append("</" + key + ">");  
  
            } else {  
                if (value instanceof HashMap) {  
                    sb.append("<" + key + ">");  
                    mapToXMLTest2((HashMap) value, sb);  
                    sb.append("</" + key + ">");  
                } else {  
                    sb.append("<" + key + ">" + value + "</" + key + ">");  
                }  
  
            }  
        } 
        
        sb.append("</xml>");
        
        return sb.toString();
    }  
	
	
	public static InputStream String2Inputstream(String str) {
			return new ByteArrayInputStream(str.getBytes());
	}
	
	/**
	 * 
	 * getChildrenText:(获取子节点的XML). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 * TODO(这里描述这个方法的执行流程 – 可选).<br/>
	 * TODO(这里描述这个方法的使用方法 – 可选).<br/>
	 * TODO(这里描述这个方法的注意事项 – 可选).<br/>
	 *
	 * @author 屈志刚
	 * @param children
	 * @return
	 * @since JDK 1.7
	 */
	public static String getChildrenText(List children) {
		StringBuffer sb = new StringBuffer();
		if(!children.isEmpty()) {
			Iterator it = children.iterator();
			while(it.hasNext()) {
				Element e = (Element) it.next();
				String name = e.getName();
				String value = e.getTextNormalize();
				List list = e.getChildren();
				sb.append("<" + name + ">");
				if(!list.isEmpty()) {
					sb.append(getChildrenText(list));
				}
				sb.append(value);
				sb.append("</" + name + ">");
			}
		}
		
		return sb.toString();
	}
	
	/**
	 * 
	 * doXMLParse:(XML 转 map). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 * TODO(这里描述这个方法的执行流程 – 可选).<br/>
	 * TODO(这里描述这个方法的使用方法 – 可选).<br/>
	 * TODO(这里描述这个方法的注意事项 – 可选).<br/>
	 *
	 * @author 屈志刚
	 * @param strxml
	 * @return
	 * @throws Exception
	 * @since JDK 1.7
	 */
	@SuppressWarnings("unchecked")
	public static Map doXMLParse(String strxml) throws Exception {
		if(null == strxml || "".equals(strxml)) {
			return null;
		}
		
		Map m = new HashMap();
		InputStream in = String2Inputstream(strxml);
		SAXBuilder builder = new SAXBuilder();
		Document doc = builder.build(in);
		Element root = doc.getRootElement();
		List list = root.getChildren();
		Iterator it = list.iterator();
		while(it.hasNext()) {
			Element e = (Element) it.next();
			String k = e.getName();
			String v = "";
			List children = e.getChildren();
			if(children.isEmpty()) {
				v = e.getTextNormalize();
			} else {
				v = getChildrenText(children);
			}
			
			m.put(k, v);
			
		}
		return m;
	}
	
	/**
	 * 
	 * parseXmlToList2:(解析通知XML). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 * TODO(这里描述这个方法的执行流程 – 可选).<br/>
	 * TODO(这里描述这个方法的使用方法 – 可选).<br/>
	 * TODO(这里描述这个方法的注意事项 – 可选).<br/>
	 *
	 * @author 屈志刚
	 * @param xml
	 * @return
	 * @since JDK 1.7
	 */
	@SuppressWarnings({ "unused", "rawtypes", "unchecked" })
	public static Map parseXmlToList2(String xml) {
		Map retMap = new HashMap();
		try {
			StringReader read = new StringReader(xml);
			// 创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
			InputSource source = new InputSource(read);
			// 创建一个新的SAXBuilder
			SAXBuilder sb = new SAXBuilder();
			// 通过输入源构造一个Document
			Document doc = (Document) sb.build(source);
			Element root = doc.getRootElement();// 指向根节点
			List<Element> es = root.getChildren();
			if (es != null && es.size() != 0) {
				for (Element element : es) {
					retMap.put(element.getName(), element.getValue());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retMap;
	}
		
	/**
	 * 
	 * sendMethod:(发送请求). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 * TODO(这里描述这个方法的执行流程 – 可选).<br/>
	 * TODO(这里描述这个方法的使用方法 – 可选).<br/>
	 * TODO(这里描述这个方法的注意事项 – 可选).<br/>
	 *
	 * @author 屈志刚
	 * @param url
	 * @param xmlParam
	 * @return
	 * @since JDK 1.7
	 */
	@SuppressWarnings("rawtypes")
	public static Map sendMethod(String url, String xmlParam) {
		Map map = new HashMap<String, String>();
		DefaultHttpClient client = new DefaultHttpClient();
		client.getParams().setParameter(ClientPNames.ALLOW_CIRCULAR_REDIRECTS,
				true);
		HttpPost httpost = HttpClientConnectionManager.getPostMethod(url);
		try {
			httpost.setEntity(new StringEntity(xmlParam, "UTF-8"));
			HttpResponse response = httpclient.execute(httpost);
			String jsonStr = EntityUtils
					.toString(response.getEntity(), "UTF-8");
			if (jsonStr.indexOf("FAIL") != -1) {
				return null;
			}
			 map = doXMLParse(jsonStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	

   /**
    * 
    * localIp:(获取本机Ip). <br/>
    * TODO(这里描述这个方法适用条件 – 可选).<br/>
    * TODO(这里描述这个方法的执行流程 – 可选).<br/>
    * TODO(这里描述这个方法的使用方法 – 可选).<br/>
    * TODO(这里描述这个方法的注意事项 – 可选).<br/>
    *
    * @author 屈志刚
    * @return
    * @since JDK 1.7
    */
   @SuppressWarnings("rawtypes")
   private static  String localIp(){
       String ip = null;
       Enumeration allNetInterfaces;
       try {
           allNetInterfaces = NetworkInterface.getNetworkInterfaces();            
           while (allNetInterfaces.hasMoreElements()) {
               NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
               List<InterfaceAddress> InterfaceAddress = netInterface.getInterfaceAddresses();
               for (InterfaceAddress add : InterfaceAddress) {
                   InetAddress Ip = add.getAddress();
                   if (Ip != null && Ip instanceof Inet4Address) {
                       ip = Ip.getHostAddress();
                   }
               }
           }
       } catch (SocketException e) {
    	   System.out.println(("获取本机Ip失败:异常信息:"+e.getMessage()));
       }
       return ip;
  }

}

