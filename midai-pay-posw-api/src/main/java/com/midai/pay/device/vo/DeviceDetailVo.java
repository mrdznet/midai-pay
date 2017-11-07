package com.midai.pay.device.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.util.Date;

@ApiModel
@Data
@EqualsAndHashCode(callSuper = false)
public class DeviceDetailVo implements Serializable {

    @ApiModelProperty("编号机身号")
    private String deviceNo;
    
    @ApiModelProperty("商户编号")
    private String customerId;

    @ApiModelProperty("设备类型名")
    private String typeName;
    @ApiModelProperty("设备型号名")
    private String modeName;

    @ApiModelProperty("绑定类型")
    private String stateName;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("首选项")
    private Integer isFirst;

    @ApiModelProperty("历史绑定商户")
    private Integer historyCustCount;
    
    @ApiModelProperty("商户手机号")
    private String mobile;
    
    @ApiModelProperty("代理商编号")
    private String agentId;
    
    @ApiModelProperty("代理商名称")
    private String agentName;
}
