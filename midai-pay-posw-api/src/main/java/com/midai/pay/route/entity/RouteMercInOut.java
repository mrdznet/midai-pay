package com.midai.pay.route.entity;

import java.io.Serializable;

import javax.persistence.Table;

import com.midai.framework.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Table(name="tbl_route_merc_in_out")
public class RouteMercInOut extends BaseEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3737935033766325114L;
	
	private int inId;
	
	private int outId;
	
	private int priority;

}
