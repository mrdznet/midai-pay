package com.midai.pay.device.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.midai.framework.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "tbl_bo_device_type")
public class BoDeviceType extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 5161492331988257798L;

    @Id
    private Integer id;
    /* 名称 */
    private String name;
    /* 状态: 0:废除,1:启用,2:暂停 */
    private Integer state;
    /* 备注 */
    private String note;
    /* 创建人 */
    private String createUser;
    /* 修改人 */
    private String updateUser;

}
