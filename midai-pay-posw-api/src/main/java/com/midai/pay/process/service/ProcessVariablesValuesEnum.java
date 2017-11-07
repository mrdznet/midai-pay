package com.midai.pay.process.service;

public enum ProcessVariablesValuesEnum {
	approval_approve("1"),
	approval_reject("0");
	
	
	
	private String value;
	private ProcessVariablesValuesEnum(String s){
		value = s;
	}
	
	public String toString(){
		return value;
	}
}
