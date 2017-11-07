package com.midai.pay.customer.enums;

public enum ImgTypeEnum {

	TYPE_SC(1,"手持银行卡"), 
	TYPE_SZ(2,"身份证正面"),
	TYPE_SF(3,"身份证反面"),
	TYPE_YH(4,"银行卡"),
	TYPE_JY(5,"经营资质"),
	TYPE_JC1(6,"经营场景1"),
	TYPE_JC2(7,"经营场景2"),
	TYPE_BC(8,"补充照片"),
	TYPE_SY(9,"身份证和银行卡合照")
	;
	

	private Integer id;
	private String name;

	private ImgTypeEnum() {
	}

	private ImgTypeEnum(Integer id, String name) {
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

	public static Integer getKey(String enumName) {
		ImgTypeEnum[] enums = values();
		for (ImgTypeEnum e : enums) {
			if (e.toString().equalsIgnoreCase(enumName)) {
				return e.getId();
			}
		}
		return null;
	}
	
	public static Integer getKeyByName(String name) {
		ImgTypeEnum[] enums = values();
		for (ImgTypeEnum e : enums) {
			if (e.getName().equalsIgnoreCase(name)) {
				return e.getId();
			}
		}
		return null;
	}
	
}
