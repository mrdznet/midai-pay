/**
 * Project Name:midai-pay-user-api
 * File Name:SystemOrganizationModel.java
 * Package Name:com.midai.pay.user.entity
 * Date:2016年9月13日下午2:57:36
 * Copyright (c) 2016, Shanghai Law Cloud Technology Co., Ltd. All Rights Reserved.
 *
*/

package com.midai.pay.user.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.midai.framework.common.BaseEntity;

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
@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name="tbl_system_organization")
public class SystemOrganizationModel extends BaseEntity implements Serializable, Comparable<SystemOrganizationModel> {
	
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private int organizationId;
	
    private int parentId;
    

    private String organizationName;
    
    private String provinceCode;
    private String provinceName;
    private String cityCode;
    private String cityName;
    private Integer orgType;
    private String agentNum;
    private Integer status;
    @Column(name = "t_level", nullable = false, length = 32) 
    private int level;
    
    @Override  
    public int compareTo(SystemOrganizationModel o) {  
        int i = this.getParentId() - o.getParentId();
        return i;  
    }  

}

