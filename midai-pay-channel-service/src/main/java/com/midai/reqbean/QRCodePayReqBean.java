package com.midai.reqbean;

import lombok.Data;

/**
 * Created by justin on 2017/6/28.
 */
@Data
public class QRCodePayReqBean {


    private String sendTime; //发送时间yyyyMMddHHmmss
    private String sendSeqId;//发送流水号
    private String transType;//交易类型码 //固定值 微信：B001   支付宝：Z001
    private String organizationId;//机构号
    private String payPass;//支付通道    //1-微信， 2-支付宝
    private String transAmt;//交易金额
    private String fee;//手续费
    private String cardNo;//结算卡号
    private String name;//持卡人姓名
    private String idNum;//持卡人身份证号
    private String body;//商品描述
    private String notifyUrl; //通知地址
    private String mobile;  //收款方手机号
//    private String macKey;  //加密秘匙
    private String mac;  //加密mac



}
