package com.midai.pay.web.vo.system;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@ApiModel(description="角色")
@Data
@EqualsAndHashCode(callSuper=false)
public class SystemRoleVo {

	@ApiModelProperty("角色ID")
    private int id;
	
	@ApiModelProperty("角色名称")
	@Column(unique=true)
    @Length(max=50)
    @NotNull
    private String rolename;
	
	@ApiModelProperty("角色描述")
    @Column(unique=true)
    @Length(max=50)
    @NotNull
    private String description;
    
	@ApiModelProperty("角色类型  1：系统角色，0：业务角色")
    private Integer system;
	@ApiModelProperty("所属组织机构ID")
	private Integer orgid;
	@ApiModelProperty("流程节点ID集，逗号分隔")
	private String specIds;
	@ApiModelProperty("菜单及子菜单ID集，逗号分隔")
	private String smvIds;
	
}
