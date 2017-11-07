package com.midai.service;


import com.midai.reqbean.ReqBean;
import com.midai.resbean.ResBean;

public interface ChannelService {

    ResBean applyForAgent(ReqBean reqBean);

    ResBean getQRCode(ReqBean reqBean);

    ResBean signForMacKey(ReqBean reqBean);

}
