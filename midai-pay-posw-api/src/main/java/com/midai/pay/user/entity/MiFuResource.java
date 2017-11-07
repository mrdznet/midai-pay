package com.midai.pay.user.entity;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class MiFuResource implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Integer moduleId;
	
	private String pageId;
	
	private Integer pageLevel;
	
	private Integer pageParentId;
	
}
