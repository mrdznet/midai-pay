package com.midai.pay.device.entity;

import com.midai.framework.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "tbl_bo_device_mode")
public class BoDeviceMode extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -9203562804393834233L;

    @Id
    private Integer id;
    /* 型号名称 */
    private String name;
    /* 厂商编号 */
    private Integer factoryId;
    /* 对应DeviceType表id */
    private Integer deviceTypeId;
    /* 状态 状态 0:废除,1:启用,2:暂停 */
    private Integer state;
    /* 备注 */
    private String note;
    /* 创建人 */
    private String createUser;
    /* 修改人 */
    private String updateUser;

}
