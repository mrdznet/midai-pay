package com.midai.pay.handpay.mapper;

import java.util.List;

import org.apache.ibatis.annotations.SelectProvider;

import com.midai.framework.config.mybatis.MyMapper;
import com.midai.pay.handpay.entity.HyTztxEntity;
import com.midai.pay.handpay.query.HyTztxQuery;

public interface HyTztxMapper extends MyMapper<HyTztxEntity> {

	@SelectProvider(type=com.midai.pay.handpay.provider.HyTztxProvider.class,method="selectListByQuery")
	List<HyTztxEntity> selectListByQuery(HyTztxQuery query);

	@SelectProvider(type=com.midai.pay.handpay.provider.HyTztxProvider.class,method="selectCountByQuery")
	int selectCountByQuery(HyTztxQuery query);

}
