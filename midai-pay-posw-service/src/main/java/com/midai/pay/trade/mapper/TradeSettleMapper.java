package com.midai.pay.trade.mapper;

import java.util.List;

import org.apache.ibatis.annotations.SelectProvider;

import com.midai.framework.config.mybatis.MyMapper;
import com.midai.pay.trade.entity.DealTotal;
import com.midai.pay.trade.vo.TradeEticketVo;

public interface TradeSettleMapper extends MyMapper<DealTotal>  {

	@SelectProvider(type=com.midai.pay.trade.provider.TradeSettleProvider.class,method="getPageDetailList")
	List<TradeEticketVo> getPageDetailList(String mercId);
}
