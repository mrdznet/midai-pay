package com.midai.pay.user.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.midai.framework.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name="tbl_system_mcc")
public class SystemMCC extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	
	@Id 
    private Integer id;
    
    private String mcc;
    
    private String name;
    
    private String father;
	
}	
