package com.midai.pay.common.pay;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 常量类
 * @author Administrator
 *
 */
public class ConstantUtil {
	
	/** 畅捷报文头返回：处理成功 **/
	public static String HEAD_RET_CODE_0000 = "0000";
	
	/** 畅捷报文头返回：报文内容检查错或者处理错 **/
	public static String HEAD_RET_CODE_1000 = "1000";
	
	/** 畅捷报文头返回：系统正在对数据处理 **/
	public static String HEAD_RET_CODE_2000 = "2000";
	
	/** 畅捷报文头返回：不通过受理**/
	public static String HEAD_RET_CODE_2004 = "2004";
	
	/** 畅捷报文头返回：交易不存在 **/
	public static String HEAD_RET_CODE_2009 = "2009";
	
	
	public static String CJ_STATE_0 = "0";
	/** 畅捷扫码初始状态 **/
	public static String CJ_STATE_10 = "10";
	/** 畅捷渠道编号 **/
	public static String CJ_WECHAT_CODE ="CJ0001";
	
	/**
	 * String字符转换分
	 * @param amount
	 * @return
	 */
	 public static String changeY2F(String amount){    
        String currency =  amount.replaceAll("\\$|\\￥|\\,", "");  //处理包含, ￥ 或者$的金额    
        int index = currency.indexOf(".");    
        int length = currency.length();    
        Long amLong = 0l;    
        if(index == -1){    
            amLong = Long.valueOf(currency+"00");    
        }else if(length - index >= 3){    
            amLong = Long.valueOf((currency.substring(0, index+3)).replace(".", ""));    
        }else if(length - index == 2){    
            amLong = Long.valueOf((currency.substring(0, index+2)).replace(".", "")+0);    
        }else{    
            amLong = Long.valueOf((currency.substring(0, index+1)).replace(".", "")+"00");    
        }    
        return amLong.toString();    
	 }    

	//金额验证  
	public static boolean isNumber(String str){   
	     Pattern pattern=Pattern.compile("^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?$"); // 判断小数点后2位的数字的正则表达式  
	     Matcher match=pattern.matcher(str);   
	     if(match.matches()==false){   
	        return false;   
	     }else{   
	        return true;   
	     }   
	 }  
}
