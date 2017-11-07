package com.midai.pay.device.entity;

import com.midai.framework.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "tbl_bo_device_instorage")
public class BoDeviceInstorage extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 3409930089277442115L;

    @Id
    private Integer id;
    /* 入库单号 */
    private String rkNo;
    /* 厂商编号 */
    private Integer factoryId;
    /* 厂商名称 */
    private String factoryName;
    /* 对应设备类型id */
    private Integer deviceTypeId;
    /* 设备类型名 */
    private String deviceTypeName;
    /* 对应设备型号id */
    private Integer deviceModeId;
    /* 设备型号名 */
    private String deviceModeName;
    /* 代理商编号 */
    private String agentId;
    /* 代理商名称 */
    private String agentName;
    /* 操作人 */
    private String appUser;
    /* 入库设备数量 */
    private Integer num;
    
}
