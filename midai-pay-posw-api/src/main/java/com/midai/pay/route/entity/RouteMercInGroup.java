package com.midai.pay.route.entity;

import java.io.Serializable;

import javax.persistence.Table;

import com.midai.framework.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Table(name="tbl_route_merc_in_group")
public class RouteMercInGroup extends BaseEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1636773277565768459L;
	
	
	private int inConfigId;
	
	private String unitType;
	
	private String unitId;

}
