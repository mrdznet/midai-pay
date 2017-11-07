package com.midai.pay.device.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel
public class DeviceOutstorageDetailVo implements Serializable {

    @ApiModelProperty("编号机身号")
    private String deviceNo;
    private Date createTime;
}
