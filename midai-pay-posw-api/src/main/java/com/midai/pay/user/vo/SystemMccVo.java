package com.midai.pay.user.vo;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class SystemMccVo implements Serializable {

	private static final long serialVersionUID = 1L;
	
    private int id;
    
    private String mcc;
    
    private String name;
}
