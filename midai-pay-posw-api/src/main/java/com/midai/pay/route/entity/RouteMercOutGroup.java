package com.midai.pay.route.entity;

import java.io.Serializable;

import javax.persistence.Table;

import com.midai.framework.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Table(name="tbl_route_merc_out_group")
public class RouteMercOutGroup extends BaseEntity implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = -7736629719407471881L;
	
	private int outConfigId;
	
	private String mercOutId;
	
	
	

}
