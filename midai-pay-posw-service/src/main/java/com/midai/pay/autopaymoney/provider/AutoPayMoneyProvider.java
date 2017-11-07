package com.midai.pay.autopaymoney.provider;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;
import com.midai.pay.autopaymoney.entity.BoAutoPayMoney;
import com.midai.pay.autopaymoney.query.BoAutoPayMoneyQuery;
import com.midai.pay.common.po.SqlParam;

public class AutoPayMoneyProvider {
	
     
	/**
	 * 清分清算通过之后批量插入数据
	 * @param batchData
	 * @return
	 */
	public String batchInsert(Map<String, List<BoAutoPayMoney>> batchData)
	{
		List<BoAutoPayMoney>  list=batchData.get("list");
		StringBuffer sql=new StringBuffer("insert into tbl_bo_auto_paymoney(tixi_logno,pay_time,pay_person,merc_id,pay_money,bank_name,account_name,account_no,bank_code,merc_name,create_time,create_user)values");
		MessageFormat messageFormat=new MessageFormat("(#'{'list[{0}].tixiLogno},#'{'list[{0}].payTime},#'{'list[{0}].payPerson},#'{'list[{0}].mercId},#'{'list[{0}].payMoney},#'{'list[{0}].bankName},#'{'list[{0}].accountName},"
				                                    + "#'{'list[{0}].accountNo},#'{'list[{0}].bankCode},#'{'list[{0}].mercName},#'{'list[{0}].createTime},#'{'list[{0}].createUser})");
		for (int i = 0; i < list.size(); i++) {
			sql.append(messageFormat.format(new Integer[]{i}));
			sql.append(",");
		}
		sql.setLength(sql.length()-1);
		return sql.toString();
	}
	
	/**
	 * 打款结果查询、excel下载
	 * tbl_bo_auto_paymoney a
	 * tbl_bo_customer b
	 * tbl_bo_getmoney c
	 */
	
	String columns="p.tixi_logno tixiLogno,p.merc_id mercId,p.merc_name mercName,p.tixian_datetime tixianDatetime,p.pay_time payTime,p.pay_money/100 payMoney,p.tixian_feeamt/100 tixianFeeamt,p.bank_name bankName,p.account_name accountName,p.account_no accountNo,p.pay_state payState"
			+ " ,(case p.pay_state when 0 then '待结算' when 1 then '系统受理中' when 3 then '结算成功' when 4 then '结算失败' when 5 then '渠道受理中' else '状态异常' end ) payStatestr"
			+ " ,p.error_msg errorMsg, p.pay_channel payChannel, p.channel_code channelCode"
			+ " ,p.mchnt_single_fee mchntSingleFee"
			+ " ,p.agent_name agentName,p.agent_levle agentLevel";
	
	String columns1 = " a.tixi_logno,a.merc_id,b.merc_name,c.tixian_datetime,a.pay_time,a.pay_money,c.tixian_feeamt,a.bank_name,a.account_name,a.account_no,a.pay_state "
					+ " ,CASE a.error_msg WHEN '受理成功' THEN '' WHEN '交易成功' THEN '' ELSE a.error_msg END error_msg "
					+ " ,CASE a.pay_channel WHEN '500302' THEN '钜真代付' WHEN 'G10002' THEN '畅捷代付' WHEN 'MISHUA' THEN '米刷代付' ELSE '' END pay_channel"
					+ " ,a.channel_code"
					+ " ,Round(d.mchnt_single_fee/100,2) mchnt_single_fee"
					+ " ,d.agent_name,ba.agent_levle";
	
	public String findQueryAutoPayMoney(BoAutoPayMoneyQuery query){ //打款结果查询
		StringBuffer sql=new StringBuffer("select  "+columns +"  ");
		sql.append(this.commBuffer(query));
		sql.append(" ) p");
		sql.append("  order by p.tixian_datetime DESC limit "+ (query.getPageNumber()-1)*query.getPageSize()+","+query.getPageSize());
		return sql.toString();
	}
	
	public String findQueryAutoPayMoneyCount(BoAutoPayMoneyQuery query){
		StringBuffer sql=new StringBuffer("select count(1)  ");
		sql.append(this.commBuffer(query));
		sql.append(" ) p");
		return sql.toString();
	}
	
	public String excelQueryAutoPayMoney(BoAutoPayMoneyQuery query){//打款结果下载
		StringBuffer sql=new StringBuffer("select  "+columns +"  ");
		sql.append(this.commBuffer(query));
		sql.append(" ) p");
		sql.append("  order by p.tixian_datetime DESC  ");
		return sql.toString();
	}
	
	public String excelQueryAutoPayMoneyCount(BoAutoPayMoneyQuery query){
		StringBuffer sql=new StringBuffer("select count(1)  ");
		sql.append(this.commBuffer(query));
		sql.append(" ) p");
		return sql.toString();
	}
	
	
	
	/**
	 * 自动打款列表查询
	 * @param query
	 * @return
	 */
	
	public String findQueryAutoPayMoney1(BoAutoPayMoneyQuery query){
		StringBuffer sql=new StringBuffer("select  "+columns +"  ");
		sql.append(this.commBuffer(query));
		sql.append(" ) p where p.pay_state=0 or p.pay_state=4");
		sql.append("  order by p.tixian_datetime DESC limit "+ (query.getPageNumber()-1)*query.getPageSize()+","+query.getPageSize());
		return sql.toString();
	}
	
	public String findQueryAutoPayMoneyCount1(BoAutoPayMoneyQuery query){
		StringBuffer sql=new StringBuffer("select count(1)  ");
		sql.append(this.commBuffer(query));
		sql.append(" ) p where p.pay_state=0 or p.pay_state=4");
		return sql.toString();
	}
	
	public StringBuffer commBuffer(BoAutoPayMoneyQuery query){
		
		StringBuffer str=new StringBuffer(" ");
		if (query!=null) {
			
			if(!StringUtils.isEmpty(query.getInscode())){
 				str.append("  AND d.inscode = #{inscode} ");
			}
			if (!StringUtils.isEmpty(query.getMercId())) {
				str.append("  and a.merc_id like '%"+query.getMercId()+"%'");
			}
			if (!StringUtils.isEmpty(query.getMercName())) {
				str.append("  and b.merc_name like '%"+query.getMercName()+"%'");
			}
			if (!StringUtils.isEmpty(query.getAccountName())) {
				str.append("  and a.account_name like '%"+query.getAccountName()+"%'");
			}
			if (!StringUtils.isEmpty(query.getAccountNo())) {
				str.append("  and a.account_no like '%"+query.getAccountNo()+"%'");
			}
			if (!StringUtils.isEmpty(query.getBranchBankName())) {
				str.append("  and a.bank_name like '%"+query.getBranchBankName()+"%'");
			}
			if (!StringUtils.isEmpty(query.getPayState())) {
				str.append(" and a.pay_state=#{payState} ");
			}
			if (!StringUtils.isEmpty(query.getPayTimeStart())) {
				str.append(" and date_format(a.pay_time,'%Y-%m-%d') >=#{payTimeStart}");
			}
			if (!StringUtils.isEmpty(query.getPayTimeEnd())) {
				str.append(" and date_format(a.pay_time,'%Y-%m-%d') <=#{payTimeEnd}");
			}
            if (!StringUtils.isEmpty(query.getTixianDatetimeStart())) {
				
				str.append("  and date_format(a.tixian_datetime,'%Y-%m-%d') >=#{tixianDatetimeStart} ");
			}
			if (!StringUtils.isEmpty(query.getTixianDatetimeEnd())) {
				
				str.append("  and date_format(a.tixian_datetime,'%Y-%m-%d') <=#{tixianDatetimeEnd}");
			}
		}
		StringBuffer sql=new StringBuffer(" from (select "+ columns1+" ");
		sql.append("from tbl_bo_auto_paymoney a  ");
		sql.append("left join tbl_bo_customer b on a.merc_id=b.merc_id  ");
		sql.append("left join tbl_bo_getmoney c on a.tixi_logno=c.logno ");
		sql.append("left join  tbl_deal_total d on d.host_trans_ssn=a.tixi_logno ");
		sql.append("LEFT JOIN tbl_bo_agent ba on ba.agent_no=d.agent_id where 1=1  ");
		sql.append(str);
		return sql;
	}
	
	public String findPayInfo(String tixiLogno){
		String sql = " SELECT pa.merc_id mercId, pa.tixi_logno tixiLogno, pa.pay_money payMoney, "
					+" 		  c.bank_id bankId, c.branchbank_id branchbankId, c.account_name accountName, c.account_no accountNo, c.branchbank_name bankName ,b.bankname bkName "
					
					+" FROM tbl_bo_auto_paymoney pa "
					+" LEFT JOIN tbl_bo_customer c ON pa.merc_id=c.merc_id "
					+" LEFT JOIN tbl_bank b ON c.bank_id=b.bankcode "
					
					+" WHERE pa.tixi_logno IN('"+tixiLogno+"') AND pa.pay_state != 3 AND pa.pay_state != 5 ";
		
		return sql;
	}
	
	public String queryPaginationList(Map<String, String> param) {
		String phonenumber = param.get("phonenumber");
		String queryDate = param.get("queryDate");
		StringBuffer sql = new StringBuffer(" SELECT a.trans_time payTime, a.trans_amt payMoney, a.trans_card_no accountNo,a.resp_cd_loc state,a.resp_cd_loc_dsp payStatestr from tbl_deal_total a ");
		sql.append(" where EXISTS (select 1 from ( SELECT b.merc_id from tbl_bo_customer b where b.mobile='" + phonenumber + "') t where a.mchnt_code_in=t.merc_id)");
		sql.append(" and DATE_FORMAT(a.trans_time,'%Y-%m') = DATE_FORMAT('" + queryDate +"','%Y-%m')  order by a.trans_time  desc  ");
		return sql.toString();
	}
	
	public String queryPaginationListWithQuickPay(Map<String, String> param){
		String phonenumber = param.get("phonenumber");
    	String queryDate = param.get("queryDate");
		StringBuffer sql = new StringBuffer(" SELECT '0' payType, a.trans_time payTime, a.trans_amt payMoney, a.trans_card_no accountNo,a.resp_cd_loc state,a.resp_cd_loc_dsp payStatestr from tbl_deal_total a  ");
		sql.append(" where EXISTS (select 1 from ( SELECT c.merc_id from tbl_bo_customer c where c.mobile='" + phonenumber + "') t where a.mchnt_code_in=t.merc_id) and a.resp_cd_loc='00' ");
		sql.append(" and DATE_FORMAT(a.trans_time,'%Y-%m') = DATE_FORMAT('" + queryDate +"','%Y-%m') ");
    	sql.append(" UNION ALL SELECT b.trans_channel payType,b.create_time payTime, b.trans_amt payMoney,b.card_no accountNo,b.pay_result state,b.pay_desc payStatestr from tbl_deal_total_quick b  ");
		sql.append(" where b.type = 1 and b.in_mobile = " + phonenumber + " and b.source = 1");
		sql.append(" and DATE_FORMAT(b.create_time,'%Y-%m') = DATE_FORMAT('" + queryDate +"','%Y-%m') order by payTime desc  ");
    	System.out.println(sql.toString());
		return sql.toString();
	}
	public String queryAllPayMoneyWithDate(Map<String, String> param){
		String phonenumber = param.get("phonenumber");
		String queryDate = param.get("queryDate");
		StringBuffer sql = new StringBuffer(" SELECT SUM(m.payMoney) from (SELECT a.trans_time payTime, a.trans_amt payMoney from tbl_deal_total a  ");
		sql.append(" where EXISTS (select 1 from ( SELECT c.merc_id from tbl_bo_customer c where c.mobile='" + phonenumber + "') t where a.mchnt_code_in=t.merc_id) and a.resp_cd_loc='00' ");
		sql.append(" and DATE_FORMAT(a.trans_time,'%Y-%m') = DATE_FORMAT('" + queryDate +"','%Y-%m') ");
		sql.append(" UNION ALL SELECT b.create_time payTime,b.trans_amt payMoney from tbl_deal_total_quick b  ");
		sql.append(" where b.type = 1 and b.in_mobile = " + phonenumber + " and b.source = 1 and b.pay_result = '00' ");
		sql.append(" and DATE_FORMAT(b.create_time,'%Y-%m') = DATE_FORMAT('" + queryDate +"','%Y-%m') order by payTime desc) m  ");
		return sql.toString();
	}
	
	/**
	 * 打款失败查询
	 * @param query
	 * @return
	 */
	public String findErrList(BoAutoPayMoneyQuery query){
		StringBuffer sql=new StringBuffer("select  "+columns +"  ");
		sql.append(this.commBuffer(query));
		sql.append(" ) p where p.pay_state=6 or p.pay_state=4");
		sql.append("  order by p.tixian_datetime DESC limit "+ (query.getPageNumber()-1)*query.getPageSize()+","+query.getPageSize());
		return sql.toString();
	}
	
	public String findErrListCount(BoAutoPayMoneyQuery query){
		StringBuffer sql=new StringBuffer("select count(1)  ");
		sql.append(this.commBuffer(query));
		sql.append(" ) p where p.pay_state=6 or p.pay_state=4");
		return sql.toString();
	}
	
	public String batchUpdatePayState(SqlParam param){
		String sql = " update tbl_bo_auto_paymoney set pay_state=#{param_4} "
				   + " where (pay_state=4 or pay_state=6)  and tixi_logno in ('"+param.getParam_2().trim()+"')";
				
		return sql;
	}
}
