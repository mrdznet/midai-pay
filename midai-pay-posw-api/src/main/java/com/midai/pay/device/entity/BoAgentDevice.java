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
@Table(name = "tbl_bo_agent_device")
public class BoAgentDevice extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -1743121819094252686L;

    @Id
    private String agentId; /* 代理商编号 */

    @Id
    private String deviceNo; /* 设备编号 */

    //private Integer isInventory; /* 是否库存设备 1 库存 2已出库 */

}
