package com.midai.reqbean;

import lombok.Data;

/**
 * Created by justin on 2017/6/29.
 */
@Data
public class ReqBean {
    private String data;  // 加密后的数据
    private String sync;  // 校验值
}
