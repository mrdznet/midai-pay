package com.midai.pay.customer.provider;

import org.springframework.util.StringUtils;

import com.midai.pay.customer.query.BoCustomerCountQuery;
import com.midai.pay.customer.query.CustomerApplyQuery;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.midai.pay.customer.entity.BoCustomer;
import com.midai.pay.customer.query.BoCustomerQuery;

/**
 * ClassName:User <br/>
 * Date:     2016年9月21日  <br/>
 * @author cjy
 * @version  
 * @since    JDK 1.7
 * @see
 */
public class BoCustomerProvider {
	
	public String applyCount(CustomerApplyQuery query){
		StringBuffer sql = new StringBuffer(" SELECT COUNT(1) FROM tbl_bo_customer WHERE 1=1 ");
		
		if(null != query){
			if(!StringUtils.isEmpty(query.getInscode())){
				sql.append(" AND inscode = #{inscode} ");
			}
			
			if(!StringUtils.isEmpty(query.getMercNo())){	// 商户编号
				sql.append(" AND merc_no LIKE '%" + query.getMercNo() + "%' ");
			}
			
			if(!StringUtils.isEmpty(query.getFromDate())){	// 申请日期
				sql.append(" AND create_time >= #{fromDate} ");	
			}
			
			if(!StringUtils.isEmpty(query.getToDate())){
				sql.append(" AND create_time <= #{toDate} ");
			}
			
			if(!StringUtils.isEmpty(query.getState())){	//状态
				sql.append(" AND state = #{state} ");
			}
		}
		
		return sql.toString();
	}
	
	public String applyQuery(CustomerApplyQuery query){
		StringBuffer sql = new StringBuffer(" SELECT merc_no AS mercNo , apply_name AS applyName, create_time AS createTime, ");
		
		sql.append(" CASE WHEN state=0 THEN '初审中' WHEN state=1 THEN '风控审中' WHEN state=2 THEN '初审未通过' ");
		sql.append(" 	  WHEN state=3 THEN '风控未通过' WHEN state=4 THEN '审核通过' ELSE '' END state, ");
		
		sql.append(" CONCAT(merc_name, ' 申请') title ");
		
		sql.append(" FROM tbl_bo_customer WHERE 1=1 ");
		
		if(null != query){
			if(!StringUtils.isEmpty(query.getInscode())){
				sql.append(" AND inscode = #{inscode} ");
			}
			
			if(!StringUtils.isEmpty(query.getMercNo())){	// 商户编号
				sql.append(" AND merc_no LIKE '%" + query.getMercNo() + "%' ");
			}
			
			if(!StringUtils.isEmpty(query.getFromDate())){	// 申请日期
				sql.append(" AND create_time >= #{fromDate} ");	
			}
			
			if(!StringUtils.isEmpty(query.getToDate())){
				sql.append(" AND create_time <= #{toDate} ");
			}
			
			if(!StringUtils.isEmpty(query.getState())){	//状态
				sql.append(" AND state = #{state} ");
			}
			if (!StringUtils.isEmpty(query.getOrder())) {
				sql.append(" order by create_time " + query.getOrder());
			} else {
				sql.append(" order by create_time desc ");
			}
			if (query.getPageSize() > 0) {
				sql.append(" limit " + (query.getPageNumber()-1)*query.getPageSize()+","+query.getPageSize());
			}
		}
		
		return sql.toString();
	}

	public String columns=" a.inscode inscode,  a.merc_no mercNo,a.agent_id agentId,a.agent_name agentName,a.merc_name mercName ,a.merc_id mercId,a.mobile mobile,a.account_name accountName,a.account_no accountNo,a.branchbank_id branchbankId,a.state state,a.create_time createTime,a.branchbank_name branchBankName,"
						+ " a.mer_auto merAuto, (case when p.num > 0 then 1 else 0 end ) qrCodeFlag, ";
	
	
	public String excelcolumns=" a.inscode inscode,  a.merc_no mercNo,a.agent_id agentId,a.agent_name agentName,a.merc_name mercName ,a.merc_id mercId,a.mobile mobile,a.account_name accountName,a.account_no accountNo,a.branchbank_id branchbankId,a.state state,a.create_time createTime,a.branchbank_name branchBankName,"
			+ " a.mer_auto merAuto, ";

	public String findQueryBoCustomer(BoCustomerQuery query)
	{
		StringBuffer sql=new StringBuffer("select " + columns + 
				" (SELECT r.oper_user FROM tbl_bo_customer_review r WHERE r.merc_no=a.merc_no AND r.review_level=0 ORDER BY r.id DESC LIMIT 0,1)  firstInstancePerson,(SELECT r.oper_user FROM tbl_bo_customer_review r WHERE r.merc_no=a.merc_no AND r.review_level=1 ORDER BY r.id DESC LIMIT 0,1) windControlPerson, " //风控员
				+ " (SELECT create_time FROM tbl_deal_total WHERE mchnt_code_in=a.merc_id ORDER BY create_time DESC LIMIT 0,1) firstTradeTime "
				+ "from tbl_bo_customer a  LEFT JOIN (select qc.merc_no, count(1) num from tbl_bo_agent_qrcode_customer qc where qc.merc_no is not null and qc.merc_no!='' group by qc.merc_no) p on a.merc_no=p.merc_no  where 1=1 ");
		  sql.append(this.commStringBuffer(query));
		  sql.append("  order by a.create_time DESC limit "+ (query.getPageNumber()-1)*query.getPageSize()+","+query.getPageSize());
	
		return sql.toString();
		
	}
	
	public String findQueryBoCustomerCount(BoCustomerQuery query)
	{
		StringBuffer sql=new StringBuffer("select count(1) from tbl_bo_customer a where 1=1 ");
		sql.append(this.commStringBuffer(query));
		return sql.toString();
	}
	
     private StringBuffer commStringBuffer(BoCustomerQuery query) {
    	 StringBuffer sw=new StringBuffer("");
    	 if (query!=null) {
    		if(!StringUtils.isEmpty(query.getLevel()) && query.getLevel().equals("0")){ //所有下级代理商(包括子级, 子子级...)
 				if(!StringUtils.isEmpty(query.getAllAgents())){
 					sw.append(" AND  a.agent_id in( " + query.getAllAgents() + " )");
 				}
 			}else{ //所有下级代理商
 				if(!StringUtils.isEmpty(query.getAgentNo())){
 					sw.append(" AND  a.agent_id = #{agentNo} ");
 				}
 				
 			}
    		if(!StringUtils.isEmpty(query.getAgentName())){
    			sw.append(" AND  a.agent_name like '%"+query.getAgentName()+"%'");
    		}
    		
    		if(!StringUtils.isEmpty(query.getMobile())) {
    			sw.append(" and a.mobile  like '%"+query.getMobile()+"%'");
    		}
    		
    		 if (!StringUtils.isEmpty(query.getMercName())) {
    			 sw.append(" and a.merc_name  like '%"+query.getMercName()+"%'");
    		 }
    		 if (!StringUtils.isEmpty(query.getMercNo())) {
    			 sw.append(" and a.merc_no  like '%"+query.getMercNo()+"%'");
    		 }
    		 if (!StringUtils.isEmpty(query.getState())&&query.getState()!=-1&&query.getState()!=3) {
    			 sw.append(" and a.state=#{state} ");
    		 }
    		 /*当查询状态为3时，把状态为3和0的展示出来*/
    		 if(!StringUtils.isEmpty(query.getState())&&query.getState()==3){
    			 sw.append(" and (a.state=#{state} || a.state = 0) ");
    		 }
    	 }
    	 
	    return sw;
	 }
	
     //支持excel下载
     public String ExcelDownloadBoCustomer(BoCustomerQuery query){
 		StringBuffer sql=new StringBuffer("select " + excelcolumns + 
				" (SELECT r.oper_user FROM tbl_bo_customer_review r WHERE r.merc_no=a.merc_no AND r.review_level=0 ORDER BY r.id DESC LIMIT 0,1)  firstInstancePerson,(SELECT r.oper_user FROM tbl_bo_customer_review r WHERE r.merc_no=a.merc_no AND r.review_level=1 ORDER BY r.id DESC LIMIT 0,1) windControlPerson, " //风控员
				+ " (SELECT create_time FROM tbl_deal_total WHERE mchnt_code_in=a.merc_id ORDER BY create_time DESC LIMIT 0,1) firstTradeTime "
				+ "from tbl_bo_customer a  where 1=1 ");
     
    	sql.append(ExcelCommStringBuffer(query));
    	 return sql.toString();	 
     }
     
     private StringBuffer ExcelCommStringBuffer(BoCustomerQuery query) {
    	 StringBuffer sw=new StringBuffer("");
    	 if (query!=null) {
     		if(!StringUtils.isEmpty(query.getLevel()) && query.getLevel().equals("0")){ //所有下级代理商(包括子级, 子子级...)
 				if(!StringUtils.isEmpty(query.getAllAgents())){
 					sw.append(" AND  a.agent_id in( " + query.getAllAgents() + " )");
 				}
 			}else{ //所有下级代理商
 				if(!StringUtils.isEmpty(query.getAgentNo())){
 					sw.append(" AND  a.agent_id = #{agentNo} ");
 				}
 			}
     		
     		if(!StringUtils.isEmpty(query.getAgentName())){
					sw.append(" AND  a.agent_name like '%"+query.getAgentName()+"%'");
				}
    		
     		if(!StringUtils.isEmpty(query.getMobile())) {
    			sw.append(" and a.mobile  like '%"+query.getMobile()+"%'");
    		}
     		
    		 if (!StringUtils.isEmpty(query.getMercName())) {
    			 sw.append("and a.merc_name  like '%"+query.getMercName()+"%'");
    		 }
    		 if (!StringUtils.isEmpty(query.getMercNo())) {
    			 sw.append("and a.merc_no  like '%"+query.getMercNo()+"%'");
    		 }
    		 if (!StringUtils.isEmpty(query.getState())&&query.getState()!=-1&&query.getState()!=3) {
    			 sw.append("and a.state=#{state} ");
    		 }
    		 /*当查询状态为3时，把状态为3和0的展示出来*/
    		 if(!StringUtils.isEmpty(query.getState())&&query.getState()==3){
    			 sw.append("and (a.state=#{state} || a.state = 0) ");
    		 }
     		sw.append("  order by a.create_time DESC  ");
 		}
    	 
	    return sw;
     } 
     
     
     public String ExcelDownloadBoCustomerCount(BoCustomerCountQuery query)
     {
    	 StringBuffer sql=new StringBuffer("select count(1) from tbl_bo_customer  where 1=1 ");
 		sql.append(this.ExcelcommStringBuffer1(query));
    	 return sql.toString();	 
     }
     
     private StringBuffer ExcelcommStringBuffer1(BoCustomerCountQuery query) {
    	 StringBuffer sw=new StringBuffer("");
    	 
    	 if (query!=null) {
    		 if(!StringUtils.isEmpty(query.getLevel()) && query.getLevel().equals("0")){ //所有下级代理商(包括子级, 子子级...)
  				if(!StringUtils.isEmpty(query.getAllAgents())){
  					sw.append(" AND  agent_id in( " + query.getAllAgents() + " )");
  				}
  			}else{ //所有下级代理商
  				if(!StringUtils.isEmpty(query.getAgentNo())){
  					sw.append(" AND  agent_id = #{agentNo} ");
  				}
  			}
     		
    		 if(!StringUtils.isEmpty(query.getMobile())) {
     			sw.append(" and mobile  like '%"+query.getMobile()+"%'");
     		}
      		
     		 if (!StringUtils.isEmpty(query.getMercName())) {
     			 sw.append("and merc_name  like '%"+query.getMercName()+"%'");
     		 }
     		 if (!StringUtils.isEmpty(query.getMercNo())) {
     			 sw.append("and merc_no  like '%"+query.getMercNo()+"%'");
     		 }
     		 if (!StringUtils.isEmpty(query.getState())&&query.getState()!=-1&&query.getState()!=3) {
     			 sw.append("and state=#{state} ");
     		 }
     		 /*当查询状态为3时，把状态为3和0的展示出来*/
     		 if(!StringUtils.isEmpty(query.getState())&&query.getState()==3){
     			 sw.append("and (state=#{state} || state = 0) ");
     		 }
     		 
     		sw.append("  order by create_time DESC  ");
 		}
    	 
	    return sw;
 	 }
     
        
     public String findBoCustomerMercNo(String agentno)
     {
    	 SimpleDateFormat sf=new SimpleDateFormat("YYMMdd");
    	 String date=sf.format(new Date());
    	 StringBuffer sql=new StringBuffer("select merc_no  from tbl_bo_customer where merc_no like '"+agentno+"%"+date+"'  order by create_time DESC limit 0,1");
    	 
    	 return sql.toString();
     }
     
     
     String updates = " mer_type=#{merType}, mer_auto=#{merAuto}, opstate=#{opstate}, channel=#{channel}, customer_level=#{customerLevel},"
     				+" agent_id=#{agentId}, agent_name=#{agentName}, merc_name=#{mercName}, merc_name_short=#{mercNameShort}, "
     				+" industry=#{industry}, mcc=#{mcc}, address=#{address}, business_license=#{businessLicense}, legal_name=#{legalName}, mobile=#{mobile}, id_card=#{idCard}, peovince_id=#{peovinceId}, city_id=#{cityId}, area_code=#{areaCode}, business_address=#{businessAddress}, "
     				+" bank_id=#{bankId},branchbank_id=#{branchbankId},branchbank_name=#{branchbankName},  account_name=#{accountName}, account_no=#{accountNo}, "
     				+" swing_card_limit=#{swingCardLimit}, swing_card_debit_rate=#{swingCardDebitRate}, swing_card_credit_rate=#{swingCardCreditRate}, swing_card_settle_fee=#{swingCardSettleFee}, non_card_debit_rate=#{nonCardDebitRate}, non_card_credit_rate=#{nonCardCreditRate},"
     				+" pos_debit_limit=#{posDebitLimit}, pos_debit_rate=#{posDebitRate}, pos_credit_rate=#{posCreditRate}, pos_settle_fee=#{posSettleFee}, "
     				+" scan_code_wx_rate=#{scanCodeWxRate}, scan_code_zfb_rate=#{scanCodeZfbRate}, scan_code_yl_rate=#{scanCodeYlRate}, scan_code_jdbt_rate=#{scanCodeJdbtRate}, scan_code_other_rate=#{scanCodeOtherRate}"
     				;
     
     public String updateBoCustomer(BoCustomer boCustomer)
     {
    	 StringBuffer  sql=new StringBuffer("update tbl_bo_customer set "+updates+" where merc_no=#{mercNo}");
    	 return sql.toString();
     }
     
     /**
      * 清分清算里面查询开户行支行名称
      */
     String seleString="a.branchbank_name branchBankName,a.account_name accountName,a.account_no accountNo,a.branchbank_id branchbankId,a.merc_name mercName";
     public String findByMrecId(String mrecId)
     {
    	 StringBuffer sql=new StringBuffer("select "+seleString +"  from tbl_bo_customer a where a.merc_id='"+mrecId+"'");
         return sql.toString();
     }
     
     /*批量冻结商户修改商户状态*/
     public String bacthUpdateOpstatefrozen(String mercIds)
     {
     	StringBuffer sql=new StringBuffer("update tbl_bo_customer set opstate=2  where merc_id in (");
     	sql.append( mercIds +")");
     	return sql.toString();
     }
     /*批量解冻商户修改商户状态*/
     public String bacthUpdateOpstateunfrozen(String mercIds)
     {
     	StringBuffer sql=new StringBuffer("update tbl_bo_customer set opstate=1  where merc_id in (");
     	sql.append( mercIds +")");
     	return sql.toString();
     }
     
     
     /**
      * 代办事项
      */
     public String selectbymercId(String mercIds)
     {
    	 String sql = " SELECT '商户申请' subject, merc_id mercId, merc_name mercName, state state, merc_no mercNo, agent_id agentId, create_time createTime "
    			 	+ " from tbl_bo_customer where merc_no in ("+mercIds+")"
    			 	+ "	and state!=4  order by create_time desc";
    	 
    	 return sql;
     }
 	
     /*海贝商户申请修改审核通过更新信息*/
     String update="  account_name=#{accountName} ,account_no=#{accountNo},bank_id=#{bankId},branchbank_name=#{branchbankName},branchbank_id=#{branchbankId},peovince_id=#{peovinceId},city_id=#{cityId} ";
     
     public String updateHaibeCustomer(BoCustomer boCustomer){
     StringBuffer sql =new StringBuffer(" update tbl_bo_customer set "+ update+" where merc_id =#{mercId} ");
     return sql.toString();
     }
     
     public String queryCustomerInfo(String qrCode) {
    	 
    	 StringBuffer sql=new StringBuffer(" select merc_no mercNo, merc_name mercName from tbl_bo_customer t where exists ( ");
    	 sql.append(" select 1 from (select qc.merc_no from tbl_bo_agent_qrcode_customer qc where qc.file_name='" + qrCode + "') c where t.merc_no=c.merc_no ) ");
      	return sql.toString();
     }
}
