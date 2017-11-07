package com.midai.pay.mobile.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ZUtil {
	//金额加 减 乘 除
	/**
	 * 提供精确加法计算的add方法
	 * @param value1 被加数
	 * @param value2 加数
	 * @return 两个参数的和
	 */
	public static double add(double value1,double value2){
	BigDecimal b1 = new BigDecimal(Double.valueOf(value1));
	BigDecimal b2 = new BigDecimal(Double.valueOf(value2));
	return round(b1.add(b2).doubleValue(),2);
	}

	/**
	 * 提供精确减法运算的sub方法
	 * @param value1 被减数
	 * @param value2 减数
	 * @return 两个参数的差
	 */
	public static double sub(double value1,double value2){
	BigDecimal b1 = new BigDecimal(Double.valueOf(value1));
	BigDecimal b2 = new BigDecimal(Double.valueOf(value2));
	return round(b1.subtract(b2).doubleValue(),2);
	}

	/**
	 * 提供精确乘法运算的mul方法
	 * @param value1 被乘数
	 * @param value2 乘数
	 * @return 两个参数的积
	 */
	public static double mul(double value1,double value2){
	BigDecimal b1 = new BigDecimal(Double.valueOf(value1));
	BigDecimal b2 = new BigDecimal(Double.valueOf(value2));
	return round(b1.multiply(b2).doubleValue(),2);
	}

	/**
	 * 提供精确的除法运算方法div
	 * @param value1 被除数
	 * @param value2 除数
	 * @param scale 精确范围
	 * @return 两个参数的商
	 * @throws IllegalAccessException
	 */
	public static double div(double value1,double value2,int scale) throws IllegalAccessException{
	//如果精确范围小于0，抛出异常信息
	if(scale<0){ 
	  throw new IllegalAccessException("精确度不能小于0");
	}
	BigDecimal b1 = new BigDecimal(Double.valueOf(value1));
	BigDecimal b2 = new BigDecimal(Double.valueOf(value2));
	return b1.divide(b2, scale,BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	public static double round(double v,int scale){
		if(scale<0){
		throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}
		BigDecimal b = new BigDecimal(Double.toString(v));
		BigDecimal one = new BigDecimal("1");
		return b.divide(one,scale,BigDecimal.ROUND_HALF_UP).doubleValue();

		}
		
	
	/**
	 * 验证手机号是否符合规则
	 * @param mobiles
	 * @return
	 */
	public static boolean isMobileNO(String mobiles){       
		String regExp = "^[1]([3][0-9]{1}|47|50|51|52|53|54|55|56|57|59|58|80|81|82|83|84|85|86|87|88|89|70|71|72|73|74|75|76|77|78)[0-9]{8}$";  
       // Pattern p = Pattern.compile("^((13[0-9])|(177[^4,\\D])|(15[^4,\\D])|(18[0-9]))\\d{8}$"); 
		 Pattern p = Pattern.compile(regExp); 
        Matcher m = p.matcher(mobiles);       
        return m.matches();       
    }   
	/**
	 * 判断是否是纯数字
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str){
		  for (int i = 0; i < str.length(); i++){
		   if (!Character.isDigit(str.charAt(i))){
		    return false;
		   }
		  }
		  return true;
		 }
	/**
	 * 判断是否为null
	 * @param s1
	 * @return
	 */
	 public static int isNull(String s1)  {
		  if(s1==null || s1.equals("")){
				return 0;
		  }
          return 1;
	 }
	
	 
	 
	/**
	 * 比较两个值
	 * @param s1
	 * @param s2
	 * @return
	 */
	 public static int getUns(String s1,String s2)  {
		 try {
	 			  long t1 =Long.parseLong(s1) ;
	 			  long t2 =Long.parseLong(s2);
	 			  int result = (int) (t2 - t1);
	 			  return result;		
		 } catch (Exception e) {
			 e.printStackTrace();
			 return 0;
				}
		
	 }
	 /**
	  * 获取字符串前和字符串后的值
	  * @param s1
	  * @return
	  */
	 public static String[] getStr2(String s1)  {
		 int n=0;
    	 char[] psamcs=s1.toCharArray();
    		for(int i=0;i<psamcs.length;i++){
    			char c=psamcs[i];
    			if(!isNumeric(String.valueOf(c))){
    				n=i+1;
    				continue;
    			}else{
    				if("0".equals(String.valueOf(c))){
    					n=i+1;
    				}else {
						break;
					}
    			}
    			
    		}
    		return new String[]{s1.substring(0, n).toString(),s1.substring(n,psamcs.length).toString()};
		
	 }
	 
	 public static String getLastMonth(){
		 java.text.SimpleDateFormat   sdf=new   java.text.SimpleDateFormat("yyyy-MM-dd");  
         java.util.Calendar   calendar=java.util.Calendar.getInstance();  
         calendar.setTime(new   java.util.Date());  
         //取得现在时间
         System.out.println(sdf.format(new   java.util.Date()));  
        //取得上一个时间
         calendar.set(Calendar.MONDAY,calendar.get(Calendar.MONDAY)-1);  
        //取得上一个月的下一天
         calendar.set(Calendar.DAY_OF_MONTH,calendar.get(Calendar.DAY_OF_MONTH)+1);  
         return sdf.format(calendar.getTime());
	 }
	   /**
		 * 转换时间yyyy/MM/dd HH:mm:ss--->yyyyMMddHHmmss
		 * @return
		 */
		public static String paseDTimess(String time){
			try {
				SimpleDateFormat format1=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				SimpleDateFormat format2=new SimpleDateFormat("yyyyMMddHHmmss");
				Date date = format1.parse(time);
				return format2.format(date);
			} catch (ParseException e) {
				e.printStackTrace();
				return null;
			}
		}
		/**
		 * 转换时间yyyy/MM/dd HH:mm--->yyyyMMddHHmm
		 * @return
		 */
		public static String paseHyr2(String time){
			try {
				SimpleDateFormat format1=new SimpleDateFormat("yyyy/MM/dd HH:mm");
				SimpleDateFormat format2=new SimpleDateFormat("yyyyMMddHHmm");
				Date date = format1.parse(time);
				return format2.format(date);
			} catch (ParseException e) {
				e.printStackTrace();
				return null;
			}
		}
		/**
		 * 转换时间yyyy/MM/dd HH:mm--->yyyy-MM-dd
		 * @return
		 */
		public static String paseHyr(String time){
			try {
				SimpleDateFormat format1=new SimpleDateFormat("yyyy/MM/dd HH:mm");
				SimpleDateFormat format2=new SimpleDateFormat("yyyy-MM-dd");
				Date date = format1.parse(time);
				return format2.format(date);
			} catch (ParseException e) {
				e.printStackTrace();
				return null;
			}
		}
		 /**
		 * 获取昨天
		 * @return
		 */
		public static String getYesterDate(String time){
			try {
				SimpleDateFormat format1=new SimpleDateFormat("yyyy-MM-dd");
				SimpleDateFormat format2=new SimpleDateFormat("yyyyMMdd");
				Date date = format1.parse(time);
				Date newdate=new Date(date.getTime() - 24*60*60*1000);
				return format2.format(newdate);
			} catch (ParseException e) {
				e.printStackTrace();
				return null;
			}
		}
		/**
		 * 讯联特用对账时间处理
		 * @param time  MM/dd HH:mm --->HHmmss
		 * @return
		 */
		public static String paseXUNDTimes(String time){
			try {
				SimpleDateFormat format1=new SimpleDateFormat("MM/dd HH:mm");
				SimpleDateFormat format2=new SimpleDateFormat("HHmmss");
				Date date = format1.parse(time);
				return format2.format(date);
			} catch (ParseException e) {
				e.printStackTrace();
				return null;
			}
		}
	 /**
		 * 获得时间yyyyMMddHHmmss
		 * @return
		 */
		public static String getDTimess(){
			 Date date=new Date();
			return new SimpleDateFormat("yyyyMMddHHmmss").format(date.getTime());
		}
	 /**
		 * 获得时间yyyy-MM-dd HH:mm:ss
		 * @return
		 */
		public static String getDTimes(){
			 Date date=new Date();
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date.getTime());
		}
		/**
		 * 获取毫秒时间
		 * @return
		 */
		public static java.sql.Timestamp getTimestamp(){
			java.util.Date a = new java.util.Date();
			  java.sql.Timestamp d = new java.sql.Timestamp(a.getTime());
			  return d;
		}
		/**
		 * 字符串时间格式转换yyyy-MM-dd
		 * @param time
		 * @return
		 */
		public static String paseDate(String time) {
			try {
				SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
				Date date = format.parse(time);
				return format.format(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return null;
		}
		/**
		 * yyyyMMdd--->yyyy-MM -dd
		 * @return
		 */
		public static String paseDate1(String time){
			try {
				SimpleDateFormat format1=new SimpleDateFormat("yyyyMMdd");
				SimpleDateFormat format2=new SimpleDateFormat("yyyy-MM-dd");
				Date date = format1.parse(time);
				return format2.format(date);
			} catch (ParseException e) {
				e.printStackTrace();
				return null;
			}
			
		}
		/**
		 * yyyy-MM-dd--->yyyyMMdd
		 * @param time
		 * @return
		 */
		public static String toDate(String time) {
			
			return time.replace("-", "");
		}
		/**
		 * yyyy-MM-dd--->yyyyMMdd
		 * @return
		 */
		public static String paseDate2(String time){
			try {
				SimpleDateFormat format1=new SimpleDateFormat("yyyy-MM-dd");
				SimpleDateFormat format2=new SimpleDateFormat("yyyyMMdd");
				Date date = format1.parse(time);
				return format2.format(date);
			} catch (ParseException e) {
				e.printStackTrace();
				return null;
			}
			
		}
	/**
	 * 获得时间yyyy-MM-dd
	 * @return
	 */
	public static String getTimes(){
		 Date date=new Date();
		return new SimpleDateFormat("yyyy-MM-dd").format(date);
	}
	//获得永久天数
	public static String getTimeForever(){
		Date date=new Date();
        Calendar c = Calendar.getInstance();  
        c.setTime(date);  
        c.add(c.YEAR, 99);//属性很多也有月等等，可以操作各种时间日期  
        Date temp_date = c.getTime();   
	           
		return new SimpleDateFormat("yyyy-MM-dd").format(temp_date);
	}
	
	
	//获取两个时间间隔天数
	 public static int getDays(String beginTime,String endTime)  {
		  try {
	 			  Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(beginTime);
	 			  Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse(endTime);
	 			  long t1 = date1.getTime();
	 			  long t2 = date2.getTime();
	 			  int result = (int) ((t2 - t1) / (60 * 60 * 1000) / 24);
	 			  return result;		
				} catch (ParseException e) {
					e.printStackTrace();
					return 0;
				}
		
	 }
	 //判断两个时间是否属于相同的月
	 public static  boolean monthCheck(String date1,String date2){
		if(!date1.substring(0, 7).equals(date2.substring(0, 7))){
			return false;
		}
		return true;
	}
	 
	public static String seasonCheck(String date1,String date2){
		if(!date1.substring(0, 4).equals(date2.substring(0, 4))){
			return null;
		}
		if(date1.substring(5).equals("01-01") && date2.substring(5).equals("03-31")){
			return "一";
		}
		if(date1.substring(5).equals("04-01") && date2.substring(5).equals("06-30")){
			return "二";
		}
		if(date1.substring(5).equals("07-01") && date2.substring(5).equals("09-30")){
			return "三";
		}
		if(date1.substring(5).equals("10-01") && date2.substring(5).equals("12-31")){
			return "四";
		}
		return null;
	}
	
	public static String changeCharset(String str, String newCharset) {
	  if (str != null) {
	   //用默认字符编码解码字符串。
	    byte[] bs = str.getBytes();
	   //用新的字符编码生成字符串
	    try {
		 return new String(bs, newCharset);
		} catch (UnsupportedEncodingException e) {
			  return null;
		}
	  }
			  return null;
	}
	
	public static List<String> addNO(String str,String addStr) {
		String result;

		String str1;

		int strLenght = str.length();
		int addLength = addStr.length();

		//String str2 = str.substring(strLenght-addLength, strLenght);
		String str2 = str.substring(9, strLenght);
		int lastNum = Integer.parseInt(str2);// 101
		int addNum = Integer.parseInt(addStr);
		List<String> list=new ArrayList<String>();
		for (int i = 0; i < addNum; i++) {
			int sumNum = lastNum + i;
			String sumStr = sumNum + "";
			int n=Math.abs(sumStr.length() - addLength);
			if (sumStr.length() > addLength) {
				str1 = str.substring(0, strLenght - addLength - n);
				str2 = str.substring(strLenght - addLength - n);
				sumNum = Integer.parseInt(str2) + i;
				sumStr = sumNum + "";
			}
			else if(sumStr.length() < addLength){
				str1 = str.substring(0, strLenght - addLength + n);
			}
			else {
				str1 = str.substring(0, strLenght - addLength);
			}
			result = str1+sumStr  ;
			list.add(result);
			System.out.println("str********"+result);
		}
		return list;
	}
	public static String getWeekOfDate(Date dt) {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }
	
	public static byte[] imageToByteArray(String imgPath) {
		BufferedInputStream in;
		try {
		in = new BufferedInputStream(new FileInputStream(imgPath));
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int size = 0;
		byte[] temp = new byte[1024];
		while((size = in.read(temp))!=-1) {
		out.write(temp, 0, size);
		}
		in.close();
		return out.toByteArray();
		} catch (IOException e) {
		e.printStackTrace();
		return null;
		}
		} 
     public static void main(String[] args) {
    	 String inserdate=ZUtil.paseHyr("2015/9/22 17:44");//格式化处理时间
			int tt=ZUtil.getDays(inserdate,"2015-9-22");
			if(tt!=1){
				System.out.println("对账时间与上传时间不一致,不允许对账!"); 
			}
    	 //addNO("CD14000100009999","10");
    	// addNO("CD14000100000001","100");
//    	 String PSAMCARDNO="4123";
//    	 int n=0;
//    	 char[] psamcs=PSAMCARDNO.toCharArray();
//    		//StringBuffer sb1=new StringBuffer();
//    		//StringBuffer sb2=new StringBuffer();
//    		for(int i=0;i<psamcs.length;i++){
//    			char c=psamcs[i];
//    			if(!isNumeric(String.valueOf(c))){
//    				n=i+1;
//    				continue;
//    			}
//    			
//    		}
//    		System.out.println(PSAMCARDNO.substring(0, n).toString());
//    		System.out.println(PSAMCARDNO.substring(n,psamcs.length ).toString());
//    		System.out.println(getTimeForever());
//    	 System.out.println(ZUtil.getDays("2015-09-25", "2015-09-24"));
    		 
	}
}
