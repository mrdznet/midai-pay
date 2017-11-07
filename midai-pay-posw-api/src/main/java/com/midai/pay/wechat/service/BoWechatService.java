package com.midai.pay.wechat.service;

import com.midai.pay.wechat.entity.BoWechatEntity;

/**   
*    
* 项目名称：midai-pay-posw-api   
* 类名称：BoWechatService   
* 类描述：   
* 创建人：wrt   
* 创建时间：2016年11月4日 下午6:02:31   
* 修改人：wrt   
* 修改时间：2016年11月4日 下午6:02:31   
* 修改备注：   
* @version    
*    
*/
public interface BoWechatService {

	BoWechatEntity getBoWeChatByTixilogno(String outTradeNo);


}
