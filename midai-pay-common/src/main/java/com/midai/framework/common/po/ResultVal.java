package com.midai.framework.common.po;

import java.io.Serializable;
import java.util.List;

public class ResultVal<T> implements Serializable{

	private static final long serialVersionUID = 1L;
	private static final int SUCCESS_CODE = 200;
	private static final int ERROR_CODE = 500;

	private int code;
	private String msg;
	private T value;
	private List<String> existsList;
	
	public ResultVal(){
		
	}
	
	public ResultVal(int c, String m, T va){
		this.code = c;
		this.msg = m;
		this.value = va;
	}

	public ResultVal(String m, List<String> existsList){
		this.code = ERROR_CODE;
		this.msg = m;
		this.existsList = existsList;
	}
	
	public static <T> ResultVal<T> SUCCESS(){
		return new ResultVal<T>(SUCCESS_CODE,"",null);
	}
	
	public static <T> ResultVal<T> SUCCESS(T t){
		return new ResultVal<T>(SUCCESS_CODE,"",t);
	}
	
	public static <T> ResultVal<T> SUCCESS(String msg,T t){
		return new ResultVal<T>(SUCCESS_CODE,msg,t);
	}
	
	public static <T> ResultVal<T> FAIL(int code,String msg){
		return new ResultVal<T>(code,msg,null);
	}
	
	public static <T> ResultVal<T> FAIL(int code,String msg,T t){
		return new ResultVal<T>(code,msg,t);
	}

	public static <T> ResultVal<T> FAIL(String msg, List<String> existsList){
		return new ResultVal<T>(msg,existsList);
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

	public List<String> getExistsList() {
		return existsList;
	}

	public void setExistsList(List<String> existsList) {
		this.existsList = existsList;
	}
}
