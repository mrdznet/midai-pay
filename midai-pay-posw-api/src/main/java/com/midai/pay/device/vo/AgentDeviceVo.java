package com.midai.pay.device.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
public class AgentDeviceVo implements Serializable {

    /* 代理商编号 */
    private String agentId;

    /* 设备表id */
    private Integer deviceNo;
}
