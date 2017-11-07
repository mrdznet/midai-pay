package com.midai.pay.customer.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.midai.pay.customer.entity.BoChannel;
import com.midai.pay.customer.entity.BoCustomerDevice;
import com.midai.pay.customer.mapper.BoChannelMapper;
import com.midai.pay.customer.service.BoChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.List;


@Service
public class BoChannelServiceImpl implements BoChannelService {

    @Autowired
    private BoChannelMapper boChannelMapper;


    @Override
    public String getAgentIDByOrgID(String orginizationId) {
        Example example = new Example(BoChannel.class);
        example.createCriteria().andEqualTo("orginizationId", orginizationId);
        List<BoChannel> boChannels = boChannelMapper.selectByExample(example);
        if (boChannels != null && !boChannels.isEmpty()) {
            return boChannels.get(0).getAgentId();
        } else {
            return null;
        }
    }
}
