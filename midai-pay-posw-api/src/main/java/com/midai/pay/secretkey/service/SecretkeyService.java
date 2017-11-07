package com.midai.pay.secretkey.service;

import com.midai.framework.common.BaseService;
import com.midai.pay.secretkey.entity.Secretkey;
import com.midai.pay.secretkey.vo.SecretkeyVo;

public interface SecretkeyService extends BaseService<Secretkey>{

	/**
	 * 生成密钥
	 */
	public SecretkeyVo createSecretkey(String facture,int num);
	
	/**
	 * 导出密钥
	 */
	public String exportSecretkey(String facture,String batch, int num);
	
	
}
