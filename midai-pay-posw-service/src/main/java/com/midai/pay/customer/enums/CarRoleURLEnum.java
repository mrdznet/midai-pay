package com.midai.pay.customer.enums;

import java.util.ArrayList;
import java.util.List;

public enum CarRoleURLEnum {

	MERC_APPLY("url_1",""), 	//申请页面
	MERC_FIRSTTRIAL("url_2",""), //初审页面
	MERC_RECHECK("url_3","");	//风控
	

	private String id;
	private String name;

	private CarRoleURLEnum() {
	}

	private CarRoleURLEnum(String id, String name) {
		this.id = id;
		this.name = name;
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static String getKey(String enumName) {
		CarRoleURLEnum[] enums = values();
		for (CarRoleURLEnum e : enums) {
			if (e.toString().equalsIgnoreCase(enumName)) {
				return e.getId();
			}
		}
		return null;
	}
	
	public static String getKeyByName(String name) {
		CarRoleURLEnum[] enums = values();
		for (CarRoleURLEnum e : enums) {
			if (e.getName().equalsIgnoreCase(name)) {
				return e.getId();
			}
		}
		return null;
	}
	
	
	public static void main(String[] args) {
		String skipURL = "";
//		System.out.println(LeaseRoleURLEnum.getKey("PRELIMINARY_AUDIT"));
		List<String> runStateURLList = new ArrayList<String>();	//流程状态的页面跳转
		List<String> rolesURLList = new ArrayList<String>();	//角色的页面跳转
		
		
		List<String> roles = new ArrayList<String>();
		roles.add("LEASE_PRELIMINARY_AUDIT");
		roles.add("LEASE_UPLOAD_CONTRACT");
		roles.add("LEASE_CAR_EVALUTION_LV1");
		String runState = "lease_order_first_evaluate,lease_car_first_evaluate";
		
		if(!runState.contains(",")) runState+=","; 
		
		String[] runStateArr = runState.split(",");
		for(String url : runStateArr){
			if(url.contains("!")){
				url = url.substring(0, url.indexOf("!"));
			}
			runStateURLList.add(CarRoleURLEnum.getKeyByName(url));
		}
		
		for(String role : roles){
			rolesURLList.add(CarRoleURLEnum.getKey(role));
		}
		
		for(String runStateURL : runStateURLList){
			for(String rolesURL : rolesURLList){
				if(runStateURL.equals(rolesURL))  skipURL = runStateURL;
			}
		}
		System.out.println("----------------------------end");
	}
}
