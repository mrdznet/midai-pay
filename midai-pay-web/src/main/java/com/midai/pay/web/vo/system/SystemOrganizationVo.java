/**
 * Project Name:midai-pay-user-api
 * File Name:SystemOrganizationModel.java
 * Package Name:com.midai.pay.user.entity
 * Date:2016年9月13日下午2:57:36
 * Copyright (c) 2016, Shanghai Law Cloud Technology Co., Ltd. All Rights Reserved.
 *
*/

package com.midai.pay.web.vo.system;

import java.io.Serializable;

import javax.persistence.Column;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * ClassName:SystemOrganizationModel <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年9月13日 下午2:57:36 <br/>
 * @author   屈志刚
 * @version  
 * @since    JDK 1.7
 * @see 	 
 */

@ApiModel(description="组织机构")
@Data
@EqualsAndHashCode(callSuper=false)
public class SystemOrganizationVo implements Serializable {
	
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty("组织机构Id")
	private int organizationId;
	
	@ApiModelProperty("组织机构父Id")
    private int parentId;
    
	@ApiModelProperty("组织机构名称")
    private String organizationName;
	
	@ApiModelProperty("省/市行政区划编码")
    private String provinceCode;
	
	@ApiModelProperty("省/市行政区划名称")
    private String provinceName;
	
	@ApiModelProperty("市/区行政区划编码")
    private String cityCode;
	
	@ApiModelProperty("市/区行政区划名称")
    private String cityName;
	
	@ApiModelProperty("类型 1公司 2部门 3代理商")
    private Integer orgType;
	
	@ApiModelProperty("代理商编号")
	private String agentNum;;
    
	@ApiModelProperty("层次")
    @Column(name = "t_level", nullable = false, length = 32) 
    private int level;
    
    

}

