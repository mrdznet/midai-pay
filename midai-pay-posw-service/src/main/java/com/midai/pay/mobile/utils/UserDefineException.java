package com.midai.pay.mobile.utils;

import com.midai.pay.mobile.AppBaseEntity;


/**   
*    
* 项目名称：midai-pay-posw-service   
* 类名称：UserDefineException   
* 类描述：   
* 创建人：wrt   
* 创建时间：2016年10月20日 下午5:27:33   
* 修改人：wrt   
* 修改时间：2016年10月20日 下午5:27:33   
* 修改备注：   
* @version    
*    
*/
public class UserDefineException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	AppBaseEntity app = new AppBaseEntity();
	
	public UserDefineException(String msg, AppBaseEntity entity) {
		super(msg);
		this.app = entity;
	}

	public AppBaseEntity getApp() {
		return app;
	}
	
}
