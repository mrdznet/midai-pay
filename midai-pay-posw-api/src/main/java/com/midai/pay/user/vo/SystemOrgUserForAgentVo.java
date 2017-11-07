package com.midai.pay.user.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class SystemOrgUserForAgentVo {
	
    private int parentId;
    
    private String organizationName;
	
    private String agentNum;
    
    private String provinceCode;
	
    private String provinceName;
	
    private String cityCode;
	
    private String cityName;
    
    private int level;
    
    private String loginname;
    
    private String password;
    
    private String username;
    
    private String mobile;
    
    private String mail;
    
    private String inscode;
    
}
