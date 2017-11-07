package com.midai.resbean;

import lombok.Data;

/**
 * Created by justin on 2017/6/29.
 */
@Data
public class ResBean {
    private String data; // 加密后的数据

    public ResBean(String data) {
        this.data = data;
    }

    public ResBean() {
    }
}
