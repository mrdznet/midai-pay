package com.midai.pay.mobile.service;

public interface MobileSMSCodeService {

	Object execute(String content, String code);

	Object ticket(String value, String code);

}
