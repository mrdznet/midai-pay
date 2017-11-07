package com.midai.pay.quick.qb.common.encry;
import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public  class Tools {
	public static String leftPad(String value,int len){
		String temp="";
		for(int i=value.length();i<len;i++){
			temp+="0";
		}
		return temp+value;
	}
	public static String rightPad(String value,int len){
		String temp="";
		for(int i=value.length();i<len;i++){
			temp+="0";
		}
		return value+temp;
	}
	public static String rightPad(String value,int len,String padStr){
		String temp="";
		for(int i=value.length();i<len;i++){
			temp+=padStr;
		}
		return value+temp;
	}
	public static String leftPad(String value,int len,String padStr){
		String temp="";
		for(int i=value.length();i<len;i++){
			temp+=padStr;
		}
		return temp+value;
	}
	
	public static Map<String, String> parserToMap(String s){  
		ObjectMapper objMap = new ObjectMapper();
		Map<String, String> map = null;
		try {
			map = objMap.readValue(s, Map.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
	    return map;  
	}  
	
	
}
