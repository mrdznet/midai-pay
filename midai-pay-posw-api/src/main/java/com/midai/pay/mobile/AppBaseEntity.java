package com.midai.pay.mobile;

import lombok.Data;

/**   
*    
* 项目名称：midai-pay-posw-api   
* 类名称：AppBaseEntity   
* 类描述：   
* 创建人：wrt   
* 创建时间：2016年10月17日 下午1:45:37   
* 修改人：wrt   
* 修改时间：2016年10月17日 下午1:45:37   
* 修改备注：   
* @version    
*    
*/
@Data
public class AppBaseEntity {

	private String RSPCOD;
	private String RSPMSG;
	
	public AppBaseEntity() {}
	
	public AppBaseEntity(String rSPCOD, String rSPMSG) {
		super();
		RSPCOD = rSPCOD;
		RSPMSG = rSPMSG;
	}
	
	
}
