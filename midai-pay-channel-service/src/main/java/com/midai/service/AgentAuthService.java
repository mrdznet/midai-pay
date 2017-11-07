package com.midai.service;


import com.midai.reqbean.AgentAuthReqBean;
import com.midai.resbean.AgentAuthResBean;

/**
 * Created by justin on 2017/6/28.
 * 代理商认证服务
 */
public interface AgentAuthService {

    AgentAuthResBean applyForAgent(AgentAuthReqBean bean);

}
