package com.midai.enums;

public enum QBQrCodePayReqPayPassEnum {

	TYPE_WX("1", "微信", "B001"), 
	TYPE_ZFB("2", "支付宝", "Z001"), 
	;
	
	private String code;
	private String desc;
	private String type;

	private QBQrCodePayReqPayPassEnum(String code, String desc, String type) {
		this.code = code;
		this.desc = desc;
		this.type = type;
	}

	public String getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}

	public String getType() {
		return type;
	}
	
	public static String getDesc(String code) {
		QBQrCodePayReqPayPassEnum[] enums = values();
		for (QBQrCodePayReqPayPassEnum e : enums) {
			if (e.getCode().equals(code)) {
				return e.getDesc();
			}
		}
		return null;
	}
}
