package com.midai.pay.mobile.service;


/**   
*    
* 项目名称：midai-pay-posw-api   
* 类名称：MobileCCBDService   
* 类描述：   
* 创建人：wrt   
* 创建时间：2016年10月31日 下午4:34:59   
* 修改人：wrt   
* 修改时间：2016年10月31日 下午4:34:59   
* 修改备注：   
* @version    
*    
*/
public interface MobileCCBDService {

	Object getDeviceModes(String content, String code);

	Object getBigBanks();

	Object getProvinces();

	Object getCitys(String value);
	
	Object getAreas(String value);
	
	Object getIndustry();
	Object getCategory(String value);
}
