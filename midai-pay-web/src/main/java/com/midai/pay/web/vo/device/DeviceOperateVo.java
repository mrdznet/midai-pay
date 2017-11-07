package com.midai.pay.web.vo.device;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.util.List;

import com.midai.pay.agent.entity.Agent;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel
public class DeviceOperateVo implements Serializable {

    @ApiModelProperty("批次号")
    private String batchNo;
    @ApiModelProperty("源代理商编号")
    private String agentId;
    @ApiModelProperty("源代理商名称")
    private String agentName;
    
    private List<Agent> agentList;
}
