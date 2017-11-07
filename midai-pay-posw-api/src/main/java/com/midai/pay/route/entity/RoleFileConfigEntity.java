package com.midai.pay.route.entity;

import java.io.Serializable;

import javax.persistence.Id;
import javax.persistence.Table;

import com.midai.framework.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Table(name = "tbl_inst_role_config")
@EqualsAndHashCode(callSuper=false)
public class RoleFileConfigEntity extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	private Integer Id;
	/** 名称 */
	private String name;
	/** 编码 */
	private String code;
	/** 文件名称 */
	private String fileName;
	/** 类型 0 通用*/
	private Integer instType;
	/** 是否可用 1可用， 0不可用 */
	private Integer status;

}
