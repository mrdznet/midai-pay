package com.midai.pay.routeweight.entity;

import java.io.Serializable;

import com.midai.framework.common.BaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class InstWeightEntity extends BaseEntity implements Serializable  {

	private static final long serialVersionUID = 1L;
	
	private Integer weight;
	private String instCode;

}
