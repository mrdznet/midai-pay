package com.midai.pay.device.service;

import java.util.List;

import com.midai.framework.common.BaseService;
import com.midai.pay.device.entity.BoIostorage;

public interface BoIostorageService extends BaseService<BoIostorage> {

	/**
	 * 更新库存记录,标识库存终端设备已被使用
	 * @param bodyNo 机身号
	 * @return
	 */
	int afterStore(String bodyNo, Integer operatorId);

	int beforeStoreNew(int _sboid, String mobile, Integer operatorId);

	int batchInsertBIo(List<String> deviceNos, String userName,String sourceAgentName, String sourceAgentId, String destagentAgentName, String destagentAgentId, String string);

	
}
