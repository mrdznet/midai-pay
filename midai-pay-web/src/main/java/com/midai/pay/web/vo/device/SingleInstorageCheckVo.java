package com.midai.pay.web.vo.device;

import com.midai.pay.device.vo.DeviceInstorageDetailVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@ApiModel
@Data
public class SingleInstorageCheckVo {

    @ApiModelProperty("入库设备信息")
    private DeviceInstorageDetailVo instorageDetail;

    @ApiModelProperty("入库列表中的设备编号逗号分隔字符串")
    private String beforeBodyNos;

    @ApiModelProperty("结果success,fail")
    private String result;

    private String errorMsg;
}
