package com.midai.pay.mobile.service;

public interface MobileUserAuthService {

	Object authentication(String content, String code);

	Object authSubmitCompleted(String value, String code);

	Object getAuthSimpleInfo(String value, String code);

	Object getAuthUserInfor(String value, String code);

	Object checkFirstStep(String value);

}
