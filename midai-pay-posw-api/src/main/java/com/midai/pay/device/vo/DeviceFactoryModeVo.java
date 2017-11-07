package com.midai.pay.device.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import java.io.Serializable;

@ApiModel
@Data
public class DeviceFactoryModeVo implements Serializable {

    private static final long serialVersionUID = 724251734028373573L;

    /* 厂商编号 */
    private Integer factoryId;
    /* 厂商名称 */
    private String factoryName;
    /* 类型编号 */
    private Integer typeId;
    /* 类型名称 */
    private String typeName;
    /* 型号编号 */
    private Integer modeId;
    /* 型号名称 */
    private String modeName;
}
