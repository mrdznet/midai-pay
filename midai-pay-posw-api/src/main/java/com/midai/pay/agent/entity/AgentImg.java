package com.midai.pay.agent.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.midai.framework.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name="tbl_bo_agent_img")
public class AgentImg extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer id;
	
	/**
	 * 商户编号
	 */
	@Column(name="agent_no")
	private String agentNo;
	
	private String url;
	
	private Integer type; // 类型 (1:营业执照, 2:税务登记证, 3:开户许可证, 4:组织机构代码证, 5:法人证件正面照, 6:法人证件背面照, 7:法人手持照片或场地照片， 8:协议)
}
