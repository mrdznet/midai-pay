package com.midai.service;


import com.midai.reqbean.QRCodePayReqBean;
import com.midai.reqbean.QRCodeSignReqBean;
import com.midai.reqbean.QueryStatusReqBean;
import com.midai.reqbean.QueryStatusResBean;
import com.midai.resbean.QRCodePayResBean;
import com.midai.resbean.QRCodeSignResBean;

/**
 * Created by justin on 2017/6/28.
 * 二维码支付服务
 */
public interface QRCodePayService {

	/**
	 * 
	 * @param reqBean
	 * @param checkMacKey 是否校验Mak
	 * @param type gdm 固定码， dtm 动态码
	 * @return
	 */
    QRCodePayResBean getQRCode(QRCodePayReqBean reqBean, Boolean checkMac, String type, Boolean isSwit);

    QRCodeSignResBean signForMacKey(QRCodeSignReqBean reqBean, Boolean isSwit);

    QueryStatusResBean queryOrderStatus(QueryStatusReqBean reqBean);

}
