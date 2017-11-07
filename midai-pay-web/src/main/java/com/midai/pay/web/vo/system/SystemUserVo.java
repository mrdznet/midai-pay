package com.midai.pay.web.vo.system;

import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.midai.pay.user.entity.MiFuResource;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@ApiModel(description="用户")
@Data
@EqualsAndHashCode(callSuper=false)
public class SystemUserVo {

	@ApiModelProperty("用户ID")
	private int id;
	
	@ApiModelProperty("登陆账号")
    @Column(unique=true)
    @Length(max=50)
    @NotNull
    private String loginname;
    
	@ApiModelProperty("用户名称")
    @Length(max=50)
    @NotNull
    private String username;

	@ApiModelProperty("所属组织机构ID")
    @NotNull
    private Integer orgid;
    
	@ApiModelProperty("手机")
    private String mobile;
    
	@ApiModelProperty("邮箱")
    private String mail;
    
	@ApiModelProperty("角色集合，逗号分隔")
	@NotBlank
	private String roleIds;
	
	@ApiModelProperty("登录名称")
	private String name;
	
	@ApiModelProperty("角色名称，逗号分隔")
	private String roleName;
	
	@ApiModelProperty("推送服务链接")
	private String midaiPayMessage;
	
	@ApiModelProperty("组织结构名称, a/b/c")
	private String orgNames;
	
	@ApiModelProperty("一级资源")
	Map<String, List<String>> firResource;
	
	@ApiModelProperty("二级资源")
	Map<String, List<String>> secResource;
	
	@ApiModelProperty("OSS地址")
	private String host;
	
	@ApiModelProperty("OSS图片")
	private String hostImg;
}
