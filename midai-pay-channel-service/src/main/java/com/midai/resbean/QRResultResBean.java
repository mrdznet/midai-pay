package com.midai.resbean;

import lombok.Data;

/**
 * Created by justin on 2017/7/8.
 */
@Data
public class QRResultResBean {
    /**
     * fee : 1
     * mac : 32344642
     * orgSendSeqId : ZFBTest_0044
     * organizationId : 15901101057
     * payDesc : 支付成功
     * payResult : 00
     * t0RespCode : 66
     * t0RespDesc : 代付状态未知
     * transAmt : 2
     * transType : B001
     */

    private String fee;
    private String mac;
    private String orgSendSeqId;
    private String organizationId;
    private String payDesc;
    private String payResult;
    private String t0RespCode;
    private String t0RespDesc;
    private String transAmt;
    private String transType;
}
