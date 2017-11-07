package com.midai.reqbean;

import lombok.Data;

/**
 * Created by justin on 2017/7/6.
 */
@Data
public class QueryStatusResBean {

//    {"orgSendSeqId":"ZFBTest_0024","payResult":"99","payDesc":"未支付"}

    private String orgSendSeqId;
    private String payResult;
    private String payDesc;
}
