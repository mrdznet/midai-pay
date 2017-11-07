package com.midai.pay.handpay.service;

import java.util.List;

import com.midai.framework.common.BaseService;
import com.midai.pay.handpay.entity.HyTztxEntity;
import com.midai.pay.handpay.query.HyTztxQuery;

public interface HyTztxService extends BaseService<HyTztxEntity>{

	List<HyTztxEntity> query(HyTztxQuery query);

	int queryCount(HyTztxQuery query);

}
