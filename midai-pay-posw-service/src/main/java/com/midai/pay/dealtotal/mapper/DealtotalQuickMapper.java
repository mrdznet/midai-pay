package com.midai.pay.dealtotal.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import com.midai.framework.config.mybatis.MyMapper;
import com.midai.pay.autopaymoney.entity.BoAutoPayMoney;
import com.midai.pay.autopaymoney.vo.AutoPayMoneyVo;
import com.midai.pay.dealtotal.entity.DealtotalQuick;
import com.midai.pay.dealtotal.query.DealtotalQuickQuery;
import com.midai.pay.dealtotal.vo.DealtotalQuickQueryVo;
import com.midai.pay.dealtotal.vo.DealtotalQuickStateUpdateVo;


public interface DealtotalQuickMapper  extends MyMapper<DealtotalQuick>{

	
	@SelectProvider(type=com.midai.pay.dealtotal.provider.DealtotalQuickProvider.class,method="queryList")
	public List<DealtotalQuickQueryVo> queryList(DealtotalQuickQuery query);
	@SelectProvider(type=com.midai.pay.dealtotal.provider.DealtotalQuickProvider.class,method="queryCount")
	public int queryCount(DealtotalQuickQuery query);
	
	
	@Update(" update tbl_deal_total_quick set pay_desc=#{payDesc}, pay_result=#{payResult}, t0_resp_code=#{t0RespCode}, t0_resp_desc=#{t0RespDesc} where seq_id=#{seqId} ")
	public int updateState(DealtotalQuickStateUpdateVo vo);
	
	
	/**
	 * App交易记录查询
	 * @param map
	 * @param transChanel支付方式（1微信 2支付宝）
	 * @return
	 */
	@SelectProvider(type=com.midai.pay.dealtotal.provider.DealtotalQuickProvider.class,method="queryQuickPayRecord")
	public List<AutoPayMoneyVo> queryTransRecordForApp(Map<String, String> map);
}
