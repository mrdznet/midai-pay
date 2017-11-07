package com.midai.pay.inst.query;

import java.io.Serializable;

import com.midai.framework.query.PaginationQuery;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class InstAllQuery extends PaginationQuery implements Serializable {

	private static final long serialVersionUID = 1L;

	private String instCode;

	private String instName;
	
	private int instState;
	
	private int serviceType;
}
