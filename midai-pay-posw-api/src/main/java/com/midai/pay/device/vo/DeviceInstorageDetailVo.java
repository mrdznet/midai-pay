package com.midai.pay.device.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@ApiModel
@Data
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeviceInstorageDetailVo implements Serializable {

    private static final long serialVersionUID = -2820331805786212419L;

    @NotNull
    @ApiModelProperty("机身号")
    private String deviceNo;

    @NotNull
    @ApiModelProperty("sim卡号")
    private String simNo;

    @NotNull
    @ApiModelProperty("状态")
    private String deviceStatus;

    @ApiModelProperty("创建时间")
    private Date createTime;
}
