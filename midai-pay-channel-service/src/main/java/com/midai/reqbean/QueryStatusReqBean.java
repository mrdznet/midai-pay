package com.midai.reqbean;

import lombok.Data;

/**
 * Created by justin on 2017/7/6.
 */
@Data
public class QueryStatusReqBean {

    private String sendSeqId; //查询订单号
    private String transType; //微信：B001 支付宝：Z001
    private String payType; //微信：WeChat   支付宝：AliPay

}
