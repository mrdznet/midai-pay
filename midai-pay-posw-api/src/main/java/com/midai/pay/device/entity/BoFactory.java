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
@Table(name = "tbl_bo_factory")
public class BoFactory extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 7115379062172972779L;

    @Id
    private Integer id;
    /* 名称 */
    private String name;
    /* 地址 */
    private String address;
    /* 备注 */
    private String note;
    /* 状态 */
    private String state;

}
