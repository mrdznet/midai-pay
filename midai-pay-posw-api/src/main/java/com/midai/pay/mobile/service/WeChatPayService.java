package com.midai.pay.mobile.service;




/**   
*    
* 项目名称：midai-pay-posw-api   
* 类名称：WeChatPayService   
* 类描述：   
* 创建人：wrt   
* 创建时间：2016年11月4日 下午3:06:06   
* 修改人：wrt   
* 修改时间：2016年11月4日 下午3:06:06   
* 修改备注：   
* @version    
*    
*/
public interface WeChatPayService {

	Object changJieSign(String value);

	Object getCodeImage(String value);

	Object executeQuery(String value);



}
