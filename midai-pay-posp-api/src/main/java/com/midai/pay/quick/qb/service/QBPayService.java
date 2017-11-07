package com.midai.pay.quick.qb.service;

import com.midai.pay.quick.qb.entity.QBQrCodePayReq;
import com.midai.pay.quick.qb.entity.QBQrCodeResultEntity;

public interface QBPayService {

	QBQrCodeResultEntity generateQBQRCode(QBQrCodePayReq param);
}
