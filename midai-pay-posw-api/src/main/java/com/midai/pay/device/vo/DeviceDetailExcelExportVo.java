package com.midai.pay.device.vo;

import lombok.Data;
import java.io.Serializable;

@Data
public class DeviceDetailExcelExportVo implements Serializable {

    private static final long serialVersionUID = 2840161235196223956L;

    /* 机身号 */
    private String bodyNo;
    /* 型号名 */
    private String modeName;
    /* 类型名 */
    private String typeName;
    /* sim卡号 */
    private String simNo;
    /* 商户编号 */
    private String mercNo;
    /*手机号*/
    private String mobile;
    /* 商户名称 */
    private String mercName;
    /* 小票号 */
    private String mercId;
    /* 直属代理商编号 */
    private String agentId;
    /* 直属代理商名称 */
    private String agentName;
    /* 顶级代理商编号 */
    private String topagentId;
    /* 顶级代理商名称 */
    private String topagentName;
    /* 绑定状态 */
    private String bindState;
    /* 终端状态 */
    private String deviceStatus;
}
