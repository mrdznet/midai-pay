package com.midai.pay.mobile.service;

import com.midai.pay.customer.entity.BoCustomer;
import com.midai.pay.mobile.ScanAppBaseEntity;

public interface MobileQRCodePayService {


	public ScanAppBaseEntity transScanHandler(String userAgent, String merNo, Double transAmt, String remarks, String notifyUrl);

	public BoCustomer queryCustomerInfo(String qrcode);

}
