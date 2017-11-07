package com.midai.resbean;

import lombok.Data;

/**
 * Created by justin on 2017/6/28.
 */
@Data
public class QRCodeSignResBean {
    /**
     * sendTime : 20170628161106
     * sendSeqId : ZFBTest_0006
     * respDesc : 交易成功
     * terminalInfo : B339A1E9676EC03FD1BF797E831912D1
     * respCode : 00
     * transType : A001
     */

    private String sendTime;
    private String sendSeqId;
    private String respDesc;
    private String terminalInfo;
    private String respCode;
    private String transType;

    public QRCodeSignResBean(String respDesc, String respCode) {
        this.respDesc = respDesc;
        this.respCode = respCode;
    }

    public QRCodeSignResBean() {
    }
}
