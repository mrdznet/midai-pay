package com.midai.pay.route.entity;

import java.io.Serializable;

import javax.persistence.Table;

import com.midai.framework.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Table(name="tbl_route_merc_out_group_config")
public class RouteMercOutGroupConfig extends BaseEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6343333458446946501L;
	
	private int id;
	
	private String name;
	
	private String ruleExp;
	
	private int isAvailable;
	
	private String remarks;

	

}
