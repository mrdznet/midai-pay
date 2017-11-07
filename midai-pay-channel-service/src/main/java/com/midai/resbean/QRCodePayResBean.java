package com.midai.resbean;

import lombok.Data;

/**
 * Created by justin on 2017/6/28.
 */
@Data
public class QRCodePayResBean {

    /**
     * respDesc : 订单号已存在
     * transType : Z001
     * mac : 43314236
     * sendTime : 20170628161401
     * respCode : 30
     * transAmt : 2
     * sendSeqId : ZFBTest_0018
     * organizationId : 15901101057
     */

    /**
     * sendTime : 20170628161501
     * imgUrl : https://qr.alipay.com/bax093439aysnngcrggp006c
     * transType : Z001 B001
     * organizationId : 15901101057
     * transAmt : 2
     * mac : 35354435
     * sendSeqId : ZFBTest_0019
     * respDesc : 获取成功
     * respCode : 00
     */
//{"respDesc":"获取成功","respCode":"00",
// "imgUrl":"http://real.izhongyin.com/middlepaytrx/alipay/authRedirect/HAIKE/HK975a120170802181234"}
    private String sendTime;
    private String imgUrl;
    private String transType;
    private String organizationId;
    private String transAmt;
    private String mac;
    private String sendSeqId;
    private String respDesc;
    private String respCode;


    public QRCodePayResBean() {
    }

    public QRCodePayResBean(String respDesc, String respCode) {
        this.respDesc = respDesc;
        this.respCode = respCode;
    }
}
