package com.midai.pay.mobile.entity;

import java.util.List;
import java.util.Map;

import com.midai.pay.mobile.AppBaseEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**   
*    
* 项目名称：midai-pay-posw-api   
* 类名称：MobileCCBDEntity   
* 类描述：   
* 创建人：wrt   
* 创建时间：2016年10月31日 下午4:34:29   
* 修改人：wrt   
* 修改时间：2016年10月31日 下午4:34:29   
* 修改备注：   
* @version    
*    
*/
@EqualsAndHashCode(callSuper=false)
@Data
public class MobileListsEntity extends AppBaseEntity {

	private List<Map<String, String>> LISTS;
	
	private Map<String, Map<String, Object>> DAYRECORD;
	private Long TOTALMONEY;
	
	private String NOWDATE;
}
