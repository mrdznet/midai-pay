package com.midai.reqbean;

import lombok.Data;

/**
 * Created by justin on 2017/6/28.
 */
@Data
public class QRCodeSignReqBean {

    private String payPass;//1-微信， 2-支付宝
    private String sendTime;//              发送时间yyyyMMddHHmmss
    private String sendSeqId;//             发送流水号
    private String transType;//             交易类型码
    private String organizationId;//        机构号

}

