package com.midai.pay.autopaymoney.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;

import com.midai.framework.config.mybatis.MyMapper;
import com.midai.pay.autopaymoney.entity.BoAutoPayMoney;
import com.midai.pay.autopaymoney.entity.PayInfo;
import com.midai.pay.autopaymoney.query.BoAutoPayMoneyQuery;
import com.midai.pay.autopaymoney.vo.AutoPayMoneyVo;
import com.midai.pay.common.po.SqlParam;

public interface AutoPayMoneyMapper extends MyMapper<BoAutoPayMoney> {

	@InsertProvider(type=com.midai.pay.autopaymoney.provider.AutoPayMoneyProvider.class,method="batchInsert")
	public long batchInsert(Map<String, List<BoAutoPayMoney>> batchData);
	
	/**
	 * 打款结果查询、excel下载
	 */
	@SelectProvider(type=com.midai.pay.autopaymoney.provider.AutoPayMoneyProvider.class,method="findQueryAutoPayMoney")
	List<AutoPayMoneyVo>  findQueryAutoPayMoney(BoAutoPayMoneyQuery query);
	
	@SelectProvider(type=com.midai.pay.autopaymoney.provider.AutoPayMoneyProvider.class,method="findQueryAutoPayMoneyCount")
	int findQueryAutoPayMoneyCount(BoAutoPayMoneyQuery query);
	
	@SelectProvider(type=com.midai.pay.autopaymoney.provider.AutoPayMoneyProvider.class,method="excelQueryAutoPayMoney")
	List<AutoPayMoneyVo>  excelQueryAutoPayMoney(BoAutoPayMoneyQuery query);
	
	@SelectProvider(type=com.midai.pay.autopaymoney.provider.AutoPayMoneyProvider.class,method="excelQueryAutoPayMoneyCount")
	int excelQueryAutoPayMoneyCount(BoAutoPayMoneyQuery query);
	/**
	 * 自动打款列表查询
	 */
	@SelectProvider(type=com.midai.pay.autopaymoney.provider.AutoPayMoneyProvider.class,method="findQueryAutoPayMoney1")
	List<AutoPayMoneyVo>  findQueryAutoPayMoney1(BoAutoPayMoneyQuery query);
	
	@SelectProvider(type=com.midai.pay.autopaymoney.provider.AutoPayMoneyProvider.class,method="findQueryAutoPayMoneyCount1")
	int findQueryAutoPayMoneyCount1(BoAutoPayMoneyQuery query);
	
	@SelectProvider(type=com.midai.pay.autopaymoney.provider.AutoPayMoneyProvider.class,method="findPayInfo")
	List<PayInfo> findPayInfo(String tixiLogno);
	
	@Update(" update tbl_bo_auto_paymoney "
			+" set pay_state=#{payState}, channel_code=#{channelCode}, pay_time=#{payTime}, pay_person=#{payPerson}, "
			+" pay_channel=#{payChannel}, error_code=#{errorCode}, error_msg=#{errorMsg}, "
			+" bank_name=#{bankName}, account_name=#{accountName}, account_no=#{accountNo}, bank_code=#{bankCode} "
			+" where tixi_logno=#{tixiLogno} and pay_state=1")
	int updatePaySettle(BoAutoPayMoney pay);
	
	@Update(" update tbl_bo_auto_paymoney "
			+" set pay_state=#{payState}, channel_code=#{channelCode}, pay_time=#{payTime}, pay_person=#{payPerson}, "
			+" pay_channel=#{payChannel}, error_code=#{errorCode}, error_msg=#{errorMsg}, "
			+" bank_name=#{bankName}, account_name=#{accountName}, account_no=#{accountNo}, bank_code=#{bankCode} "
			+" where tixi_logno=#{tixiLogno} and pay_state=0")
	int updatePaySettleForShenXin(BoAutoPayMoney pay);

	@SelectProvider(type=com.midai.pay.autopaymoney.provider.AutoPayMoneyProvider.class,method="queryPaginationList")
	public List<AutoPayMoneyVo> queryPaginationList(Map<String, String> map);
	
	@Select(" select pay_state from  tbl_bo_auto_paymoney  where tixi_logno=#{tixiLogno} ")
	public int findPayState(String tixiLogno);
	
	@Update(" update tbl_bo_auto_paymoney set pay_state=1 where (pay_state=0 or pay_state=4)  and tixi_logno=#{tixiLogno} ")
	public int updatePayState(String tixiLogno);
	
	@Select(" select tixi_logno tixiLogno, pay_channel payChannel, channel_code channelCode "
			+ " from tbl_bo_auto_paymoney where pay_state=5 AND TIMESTAMPDIFF(MINUTE,update_time,NOW())>5 ")
	List<AutoPayMoneyVo> findPendingPay();
	
	@Update(" update tbl_bo_auto_paymoney set pay_state=#{param_4},error_code=#{param_3},error_msg=#{param_2} where pay_state=5 and tixi_logno=#{param_1} ")
	public int updatePendingState(SqlParam param);
	
	@Select(" SELECT TIMESTAMPDIFF(MINUTE, DATE_FORMAT(update_time,'%Y-%m-%d %H:%i'), DATE_FORMAT(NOW(), '%Y-%m-%d %H:%i')) "
			+ " FROM tbl_bo_auto_paymoney WHERE tixi_logno=#{tixiLogno} ")
	public int payTimeDiff(String tixiLogno);
	
	/**
	 * 打款失败查询
	 */
	@SelectProvider(type=com.midai.pay.autopaymoney.provider.AutoPayMoneyProvider.class,method="findErrList")
	List<AutoPayMoneyVo>  findErrList(BoAutoPayMoneyQuery query);
	
	@SelectProvider(type=com.midai.pay.autopaymoney.provider.AutoPayMoneyProvider.class,method="findErrListCount")
	int findErrListCount(BoAutoPayMoneyQuery query);
	
	@UpdateProvider(type=com.midai.pay.autopaymoney.provider.AutoPayMoneyProvider.class,method="batchUpdatePayState")
	public int batchUpdatePayState(SqlParam param);
	
	/**
	 * 全部到帐记录查询（mpos,支付宝微信）
	 * @param map
	 * @return
	 */
	@SelectProvider(type=com.midai.pay.autopaymoney.provider.AutoPayMoneyProvider.class,method="queryPaginationListWithQuickPay")
	public List<AutoPayMoneyVo> queryPaginationWithQuickPayList(Map<String, String> map);
	/**
	 * 查询所有收入
	 * @param map
	 * @return
	 */
	@SelectProvider(type=com.midai.pay.autopaymoney.provider.AutoPayMoneyProvider.class,method="queryAllPayMoneyWithDate")
	public Long queryAllPayMoneyWithDate(Map<String, String> map);
	
	
}
