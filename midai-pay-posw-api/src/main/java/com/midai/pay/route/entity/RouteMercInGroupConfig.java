package com.midai.pay.route.entity;

import java.io.Serializable;

import javax.persistence.Id;
import javax.persistence.Table;

import com.midai.framework.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Table(name="tbl_route_merc_in_group_config")
public class RouteMercInGroupConfig extends BaseEntity implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = -1286246794956180910L;
	
	@Id
	private int id;
	
	private String name;
	
	private int viewIndex;
	
	private int isAvailable;
	
	private String remarks;
	


}
