package com.midai.resbean;

import lombok.Data;

/**
 * Created by justin on 2017/6/28.
 */

@Data
public class AgentAuthResBean {

    public AgentAuthResBean(String status, String type, String msg) {
        this.status = status;
        this.type = type;
        this.msg = msg;
    }

    public AgentAuthResBean() {
    }

    /**
     * status : false
     * type : insData
     * msg : 具体问题描述
     */


    private String status;
    private String type;
    private String msg;

}
