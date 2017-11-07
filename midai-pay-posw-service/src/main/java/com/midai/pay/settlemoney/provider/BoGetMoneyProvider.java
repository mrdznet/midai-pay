package com.midai.pay.settlemoney.provider;

import org.springframework.util.StringUtils;

import com.midai.pay.settlemoney.query.BoGetMoneyQuery;

public class BoGetMoneyProvider {

	
	public String columns="p.mercid mercid,p.merc_name mercName,p.tixian_datetime tixianDatetime,p.tixian_amt/100  tixianAmt,p.tixian_feeamt/100 tixianFeeamt,"
            + "p.account_no accountNo,p.account_name accountName,p.branchbank_name branchBankName,p.settle_person settlePerson,p.settle_date settleDate,p.settle_state settleState,"
            + "p.logno logNo,p.peovince_id peovinceId,p.city_id cityId,p.check_state checkState,p.provinceName provinceName,p.cityName cityName";

    public String columns1="a.mercid ,b.merc_name ,a.tixian_datetime ,a.tixian_amt ,a.tixian_feeamt ,"
             + "b.account_no ,b.account_name ,b.branchbank_name ,a.settle_person ,a.settle_date ,a.settle_state,a.logno,b.peovince_id,b.city_id,a.check_state,d.name provinceName,e.name cityName";

    
	public String columns2="p.mercid mercid,p.merc_name mercName,p.tixian_datetime tixianDatetime,p.tixian_amt/100 tixianAmt,p.tixian_feeamt/100 tixianFeeamt,"
            + "p.account_no accountNo,p.account_name accountName,p.branchbank_name branchBankName,p.settle_person settlePerson,p.settle_date settleDate,p.settle_state settleState,"
            + "p.logno logNo,p.peovince_id peovinceId,p.city_id cityId,p.check_state checkState";

    public String columns3="a.mercid ,b.merc_name ,a.tixian_datetime ,a.tixian_amt ,a.tixian_feeamt ,"
             + "b.account_no ,b.account_name ,b.branchbank_name ,a.settle_person ,a.settle_date ,a.settle_state,a.logno,b.peovince_id,b.city_id,a.check_state";

  /**
   * 清分清算查询列表
   * tbl_bo_getmoney a
   * tbl_bo_customer b
   * tbl_bank_branch c
   */

	public String findqueryBoGetMoney(BoGetMoneyQuery query)
	{
		StringBuffer sql=new StringBuffer("select "+columns2+"  ");
		sql.append(this.commStringBuffer(query));
		 sql.append("  order by p.settle_date DESC limit "+ (query.getPageNumber()-1)*query.getPageSize()+","+query.getPageSize());
		
		return sql.toString();
	}
	
	public String findqueryBoGetMoneyCount(BoGetMoneyQuery query)
	{

		StringBuffer sql=new StringBuffer("select count(1) ");
		sql.append(this.commStringBuffer(query));
		return sql.toString();
	}
	
    public StringBuffer commStringBuffer(BoGetMoneyQuery query)
	{
		StringBuffer sql=new StringBuffer(" ");
		if (query!=null) {
			
 			if(!StringUtils.isEmpty(query.getInscode())){
 				sql.append(" AND c.inscode = #{inscode} ");
			}
			if (!StringUtils.isEmpty(query.getMercId())) {
				sql.append(" and a.mercid like '%"+query.getMercId()+"%'");
			}
			if (!StringUtils.isEmpty(query.getMercName())) {
				sql.append(" and b.merc_name like '%"+query.getMercName()+"%'");
			}
			if (!StringUtils.isEmpty(query.getStartTime())) {
				
				sql.append(" and date_format(a.tixian_datetime,'%Y-%m-%d') >=#{startTime} ");
			}
			if (!StringUtils.isEmpty(query.getEndTime())) {
				
				sql.append("  and date_format(a.tixian_datetime,'%Y-%m-%d') <=#{endTime}");
			}
		}
		StringBuffer str=new StringBuffer(" from (");
		str.append(" select  "+ columns3+"  ");
		str.append("from tbl_bo_getmoney a left join tbl_bo_customer b on a.mercid=b.merc_id  ");
		//str.append("left join tbl_bank_branch c on b.branchbank_id =c.branchbankcode where 1=1 and a.check_state=2  ");
		str.append("left join  tbl_deal_total c on c.host_trans_ssn=a.logno  ");
		str.append("   where 1=1 and a.check_state=2  ");
		str.append(sql);
		str.append(") p where p.settle_state <>1");
		return str;
	}
    /**
     * 清分清算通过批量修改状态settle_state
     */
    
    public String bacthUpdateSettleState(String logNos)
    {
    	StringBuffer sql=new StringBuffer("update tbl_bo_getmoney set settle_state=1 ,settle_date=now()  where logno in (");
    	sql.append( logNos +")");
    	return sql.toString();
    }
    
    /**
     * 通过小票号查询列表信息
     */
    String selects="logno logNo,mercid mercId,tixian_datetime tixianDatetime,tixian_amt tixianAmt,tixian_feeamt tixianFeeamt,current_amt currentAmt"
	           + "latitude,longitude,txdesc,settle_state settleState,settle_date settleDate,settle_person settlePerson,feeratio,check_state checkState";

    public String selectBoGetMoneyList(String logNos)
    {
    	StringBuffer sql=new StringBuffer("select "+selects+" from tbl_bo_getmoney where logno in (");
    	sql.append( logNos +")");
    	return sql.toString();
    }
    
    /***
     * 交易结算列表  也用上面的columns，筛选条件不一样
     */
    public String findqueryToTrade(BoGetMoneyQuery query)
	{
		StringBuffer sql=new StringBuffer("select "+columns+"  ");
		sql.append(this.commStringBuffer1(query));
		 sql.append("  order by p.tixian_datetime DESC limit "+ (query.getPageNumber()-1)*query.getPageSize()+","+query.getPageSize());
		
		return sql.toString();
	}
	
	public String findqueryToTradeCount(BoGetMoneyQuery query)
	{

		StringBuffer sql=new StringBuffer("select count(1) ");
		sql.append(this.commStringBuffer1(query));
		return sql.toString();
	}
    public StringBuffer commStringBuffer1(BoGetMoneyQuery query)
	{
		StringBuffer sql=new StringBuffer(" ");
		if (query!=null) {
 			if(!StringUtils.isEmpty(query.getInscode())){
 				sql.append(" AND tt.inscode = #{inscode} ");
			}
 			
			if (!StringUtils.isEmpty(query.getMercId())) {
				sql.append(" and a.mercid like '%"+query.getMercId()+"%'");
			}
			if (!StringUtils.isEmpty(query.getMercName())) {
				sql.append(" and b.merc_name like '%"+query.getMercName()+"%'");
			}
			if (!StringUtils.isEmpty(query.getStartTime())) {
				
				sql.append(" and date_format(a.tixian_datetime,'%Y-%m-%d') >=#{startTime} ");
			}
			if (!StringUtils.isEmpty(query.getEndTime())) {
				
				sql.append(" and date_format(a.tixian_datetime,'%Y-%m-%d') <=#{endTime}");
			}
			if (!StringUtils.isEmpty(query.getCheckState())) {
				sql.append(" and a.check_state=#{checkState}");
			}
			if (!StringUtils.isEmpty(query.getTixianAmtStart())) {
				sql.append(" and a.tixian_amt/100 >=#{tixianAmtStart}");
			}
			if (!StringUtils.isEmpty(query.getTixianAmtEnd())) {
				sql.append(" and  a.tixian_amt/100 <=#{tixianAmtEnd}");
			}
			if (!StringUtils.isEmpty(query.getLogNo())) {
				sql.append(" and a.logno like '%"+query.getLogNo()+"%'");
			}
			if (!StringUtils.isEmpty(query.getAccountNo())) {
				sql.append("  and b.account_no like '%"+query.getAccountNo()+"%'");
			}
		}
		StringBuffer str=new StringBuffer(" from (");
		str.append(" select  "+ columns1+"  ");
		str.append("from tbl_bo_getmoney a  LEFT JOIN tbl_deal_total tt ON a.logno=tt.host_trans_ssn ");
		str.append("	 left join tbl_bo_customer b on a.mercid=b.merc_id ");
		//str.append("left join tbl_bank_branch c on b.branchbank_id =c.branchbankcode  ");
		str.append("left join tbl_cn_province d on d.code=b.peovince_id  ");
		str.append("left join tbl_cn_city e on b.city_id=e.code");
		str.append("  where 1=1   ");
		str.append(sql);
		str.append(") p ");
		return str;
	}
 
    /**
     * 交易结算通过批量修改check_state
     */
    
    public String bacthUpdateCheckState(String lognos)
    {
    	StringBuffer sql=new StringBuffer("update tbl_bo_getmoney set check_state=2  where logno in (");
    	sql.append( lognos +") and check_state=0 " );
    	return sql.toString();
    }
    
    /**
     * 交易冻结批量修改check_state
     */
    
    public String bacthUpdateCheckStatefrozen(String lognos)
    {
    	StringBuffer sql=new StringBuffer("update tbl_bo_getmoney set check_state=4  where logno in (");
    	sql.append( lognos +")");
    	return sql.toString();
    }
    
    public String queryData(String lognos) {
    	StringBuffer sql=new StringBuffer("select g.logno hostTransssn,g.mercid mercId,b.merc_name mercName from tbl_bo_getmoney g inner join tbl_bo_customer b on g.mercid = b.merc_id where logno in (");
    	sql.append( lognos +")");
    	return sql.toString();
    }
    /**
     * 交易解冻批量修改check_state
     */
    
    public String bacthUpdateCheckStateunfrozen(String lognos){
    	StringBuffer sql=new StringBuffer("update tbl_bo_getmoney set check_state=0  where logno in (");
    	sql.append( lognos +")");
    	return sql.toString();
    }
    /*商户批量冻结更新流水号状态*/
    public String customerUpdateCheckStatefrozen(String mercIds)
    {
    	StringBuffer sql=new StringBuffer("update tbl_bo_getmoney set check_state=4  where mercid in (");
    	sql.append( mercIds +")");
    	return sql.toString();
    }
    
    public String queryCustomerData(String mercIds) {
    	StringBuffer sql=new StringBuffer("select  g.mercid mercId,b.merc_name mercName from tbl_bo_getmoney g inner join tbl_bo_customer b on g.mercid = b.merc_id where g.mercid in (");
    	sql.append( mercIds +")");
    	return sql.toString();
    }
    
     /*商户批量解冻*/
    public String customerUpdateCheckStateunfrozen(String mercIds) {
    	StringBuffer sql=new StringBuffer("update tbl_bo_getmoney set check_state=0  where mercid in (");
    	sql.append( mercIds +")");
    	return sql.toString();
    }
    
    /*判断小票号是否存在或者已经审核通过*/
   public String  selectCountMercId(String mercIds){
	   StringBuffer sql =new StringBuffer(" select count(1) from tbl_bo_customer bc  "
	   		+ " left join tbl_bo_getmoney bg  on  bc.merc_id=bg.mercid "
	   		+ "  where bc.merc_id in (");
	   sql.append( mercIds +")");
	   sql.append(" and bg.check_state=0 ");
	   return sql.toString();
   }
    
}
