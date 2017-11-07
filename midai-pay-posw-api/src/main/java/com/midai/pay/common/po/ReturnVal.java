package com.midai.pay.common.po;

import java.io.Serializable;

public class ReturnVal<T> implements Serializable{
	private static final long serialVersionUID = 1L;
	private static final int SUCCESS_CODE = 200; 
	
	private int code;
	private String msg;
	private T value;
	
	public ReturnVal(){
		
	}
	
	public ReturnVal(int c,String m,T va){
		this.code = c;
		this.msg = m;
		this.value = va;
	}
	
	public static <T> ReturnVal<T> SUCCESS(){
		return new ReturnVal<T>(SUCCESS_CODE,"",null);
	}
	
	public static <T> ReturnVal<T> SUCCESS(T t){
		return new ReturnVal<T>(SUCCESS_CODE,"",t);
	}
	
	public static <T> ReturnVal<T> SUCCESS(String msg,T t){
		return new ReturnVal<T>(SUCCESS_CODE,msg,t);
	}
	
	public static <T> ReturnVal<T> FAIL(int code,String msg){
		return new ReturnVal<T>(code,msg,null);
	}
	
	public static <T> ReturnVal<T> FAIL(int code,String msg,T t){
		return new ReturnVal<T>(code,msg,t);
	}
	
	public boolean isSuccess(){
		return this.code == SUCCESS_CODE;
	}
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public T getValue() {
		return value;
	}
	public void setValue(T value) {
		this.value = value;
	}
}
