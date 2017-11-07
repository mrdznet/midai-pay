package com.midai.pay.customer.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public enum CustomerStateProcessEnum {
	
	// 状态:0:申请中 |1:初审中|2:复审中|3:初审未通过|4:审核通过
	apply(0, "apply"),
	firstTrial(1, "firstTrial"),
	recheck(2, "recheck"),
	check_un(3, "check_no"),
	complete(4, "complete");
	
	private Integer id; 
	private String name;

	private CustomerStateProcessEnum() {
	}

	private CustomerStateProcessEnum(Integer id, String name) {
		this.id = id;
		this.name = name;
	}


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public static Map<String,String> getNameMap() {
		Map<String,String> result=new HashMap<String,String>();
		CustomerStateProcessEnum[] enums = values();
		for (CustomerStateProcessEnum e : enums) {
			result.put(e.toString(),e.getName());
		}
		return result;
	}
	
	public static String getStatusName(Integer id) {
		CustomerStateProcessEnum[] enums = values();
		for (CustomerStateProcessEnum e : enums) {
			if (e.getId() == id) {
				return e.getName();
			}
		}
		return null;
	}
	
	public static Integer getKey(String roleName) {
		CustomerStateProcessEnum[] enums = values();
		for (CustomerStateProcessEnum e : enums) {
			if (e.toString().equalsIgnoreCase(roleName)) {
				return e.getId();
			}
		}
		return null;
	}

	public static List<CustomerStateProcessEnum> getStatusName() {
		List<CustomerStateProcessEnum> list = new ArrayList<CustomerStateProcessEnum>();
		CustomerStateProcessEnum[] enums = values();
		for (CustomerStateProcessEnum e : enums) {
			list.add(e);

		}
		return list;
	}

}
