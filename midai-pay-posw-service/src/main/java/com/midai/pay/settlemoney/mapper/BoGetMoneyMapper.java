package com.midai.pay.settlemoney.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import com.midai.framework.config.mybatis.MyMapper;
import com.midai.pay.frozen.vo.CustomerFrozenVo;
import com.midai.pay.frozen.vo.TradeFrozenVo;
import com.midai.pay.settlemoney.entity.BoGetMoney;
import com.midai.pay.settlemoney.query.BoGetMoneyQuery;
import com.midai.pay.settlemoney.vo.SettleCustomerVo;

public interface BoGetMoneyMapper extends MyMapper<BoGetMoney> {

	@SelectProvider(type=com.midai.pay.settlemoney.provider.BoGetMoneyProvider.class,method="findqueryBoGetMoney")
    List<SettleCustomerVo> findqueryBoGetMoney(BoGetMoneyQuery query);
	
	@SelectProvider(type=com.midai.pay.settlemoney.provider.BoGetMoneyProvider.class,method="findqueryBoGetMoneyCount")
	int findqueryBoGetMoneyCount(BoGetMoneyQuery query);
	
	@UpdateProvider(type=com.midai.pay.settlemoney.provider.BoGetMoneyProvider.class,method="bacthUpdateSettleState")
     int bacthUpdateSettleState(String logNos);
	
	@SelectProvider(type=com.midai.pay.settlemoney.provider.BoGetMoneyProvider.class,method="selectBoGetMoneyList")
	List<BoGetMoney> selectBoGetMoneyList(String mercids);
	
	//交易结算
	@SelectProvider(type=com.midai.pay.settlemoney.provider.BoGetMoneyProvider.class,method="findqueryToTrade")
    List<SettleCustomerVo> findqueryToTrade(BoGetMoneyQuery query);
	
	@SelectProvider(type=com.midai.pay.settlemoney.provider.BoGetMoneyProvider.class,method="findqueryToTradeCount")
	int findqueryToTradeCount(BoGetMoneyQuery query);
	
	@UpdateProvider(type=com.midai.pay.settlemoney.provider.BoGetMoneyProvider.class,method="bacthUpdateCheckState")
    int bacthUpdateCheckState(String lognos);
	
	@Insert(" insert tbl_bo_getmoney(logno,mercid,tixian_datetime,tixian_amt,tixian_feeamt,txdesc,settle_state,feeratio,check_state,create_time)"
			+ " values(#{logNo},#{mercId},#{tixianDatetime},#{tixianAmt},#{tixianFeeamt},#{txdesc},0,#{feeratio},0,#{createTime}) ")
	int insertBoGetMoney(BoGetMoney getMoney);
	
	/*交易批量冻结*/
	@UpdateProvider(type=com.midai.pay.settlemoney.provider.BoGetMoneyProvider.class,method="bacthUpdateCheckStatefrozen")
    int bacthUpdateCheckStatefrozen(String lognos);
	
	@SelectProvider(type=com.midai.pay.settlemoney.provider.BoGetMoneyProvider.class,method="queryData")
	List<TradeFrozenVo> queryData(String lognos);
	
	/*交易批量解冻*/
	@UpdateProvider(type=com.midai.pay.settlemoney.provider.BoGetMoneyProvider.class,method="bacthUpdateCheckStateunfrozen")
    int bacthUpdateCheckStateunfrozen(String lognos);
	
	/*商户批量冻结*/
	@UpdateProvider(type=com.midai.pay.settlemoney.provider.BoGetMoneyProvider.class,method="customerUpdateCheckStatefrozen")
    int customerUpdateCheckStatefrozen(String mercIds);

	@SelectProvider(type=com.midai.pay.settlemoney.provider.BoGetMoneyProvider.class,method="queryCustomerData")
	List<CustomerFrozenVo> queryCustomerData(String mercIds);
	
	/*商户批量解冻更新状态*/
	@UpdateProvider(type=com.midai.pay.settlemoney.provider.BoGetMoneyProvider.class,method="customerUpdateCheckStateunfrozen")
	int customerUpdateCheckStateunfrozen(String mercIds);
	
	/*判断是否存在该小票号*/
	@SelectProvider(type=com.midai.pay.settlemoney.provider.BoGetMoneyProvider.class,method="selectCountMercId")
	public int selectCountMercId(String mercIds);
	
}
