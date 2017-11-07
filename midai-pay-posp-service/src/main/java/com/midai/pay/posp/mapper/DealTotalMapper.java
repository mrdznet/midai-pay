package com.midai.pay.posp.mapper;

import org.apache.ibatis.annotations.Select;

import com.midai.framework.config.mybatis.MyMapper;
import com.midai.pay.trade.entity.DealTotal;

public interface DealTotalMapper extends MyMapper<DealTotal> {
	
	@Select(" SELECT SUM(trans_amt) FROM tbl_deal_total "
			+ " where TO_DAYS(create_time)=TO_DAYS(NOW()) AND mchnt_code_in=#{mercId} AND resp_cd_loc='00' ")
	String curCount(String mercId);
}
