package com.midai.pay.agent.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@ApiModel
public class AgentImgVo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer id;
	
	private String agentNo;
	
	private String url;
	
	// 类型 (1:营业执照, 2:税务登记证, 3:开户许可证, 4:组织机构代码证, 5:法人证件正面照, 6:法人证件背面照, 7:法人手持照片或场地照片， 8:协议)
	private Integer type;
}
