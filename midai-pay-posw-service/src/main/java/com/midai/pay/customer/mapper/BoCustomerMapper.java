package com.midai.pay.customer.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import com.midai.pay.customer.vo.BoCustomerDeviceVo;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;

import com.midai.framework.config.mybatis.MyMapper;
import com.midai.pay.common.po.TasksInfo;
import com.midai.pay.customer.entity.BoCustomer;
import com.midai.pay.customer.query.BoCustomerCountQuery;
import com.midai.pay.customer.query.CustomerApplyQuery;
import com.midai.pay.customer.vo.CustomerApplyVo;
import com.midai.pay.mobile.vo.CustomerExtendVo;
import com.midai.pay.customer.query.BoCustomerQuery;
import com.midai.pay.customer.vo.BoCustomerVo;

/**
 * ClassName:User <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年9月21日  <br/>
 * @author cjy
 * @version  
 * @since    JDK 1.7
 * @see
 */
public interface BoCustomerMapper extends MyMapper<BoCustomer> {

	@SelectProvider(type=com.midai.pay.customer.provider.BoCustomerProvider.class, method="applyCount")
	int applyCount(CustomerApplyQuery query);
	
	@SelectProvider(type=com.midai.pay.customer.provider.BoCustomerProvider.class, method="applyQuery")
	List<CustomerApplyVo> applyQuery(CustomerApplyQuery query);
   
	String select = " mer_type merType, mer_auto merAuto, state state, opstate opstate, channel channel, customer_level customerLevel, "
				+ " merc_no mercNo, agent_id agentId, agent_name agentName, merc_name mercName, merc_id mercId, "
		     + " industry industry, mcc mcc, address address, business_license businessLicense, legal_name legalName, mobile mobile, id_card idCard, "
		     + " peovince_id peovinceId,city_id cityId, area_code areaCode,business_address businessAddress, bank_id bankId, branchbank_id branchbankId, branchbank_name branchbankName, account_name accountName, account_no  accountNo, "
		     + " swing_card_limit swingCardLimit, swing_card_debit_rate swingCardDebitRate, swing_card_credit_rate swingCardCreditRate, swing_card_settle_fee swingCardSettleFee, non_card_debit_rate nonCardDebitRate, non_card_credit_rate nonCardCreditRate,"
		     + "pos_debit_limit posDebitLimit, pos_debit_rate posDebitRate, pos_credit_rate posCreditRate, pos_settle_fee posSettleFee, scan_code_wx_rate scanCodeWxRate, scan_code_zfb_rate scanCodeZfbRate, scan_code_yl_rate scanCodeYlRate, scan_code_jdbt_rate scanCodeJdbtRate, scan_code_other_rate scanCodeOtherRate, "
		     + " inscode inscode, merc_name_short mercNameShort ";
	
	@SelectProvider(type=com.midai.pay.customer.provider.BoCustomerProvider.class,method="findQueryBoCustomer")
	public List<BoCustomerVo> findQueryBoCustomer(BoCustomerQuery query);
	
	@SelectProvider(type=com.midai.pay.customer.provider.BoCustomerProvider.class,method="findQueryBoCustomerCount")
	int  findQueryBoCustomerCount(BoCustomerQuery query);
	
	@SelectProvider(type=com.midai.pay.customer.provider.BoCustomerProvider.class,method="ExcelDownloadBoCustomer")
	public List<BoCustomerVo> ExcelDownloadBoCustomer(BoCustomerQuery query);
	
	@SelectProvider(type=com.midai.pay.customer.provider.BoCustomerProvider.class,method="ExcelDownloadBoCustomerCount")
	int  ExcelDownloadBoCustomerCount(BoCustomerCountQuery query);
	
	@SelectProvider(type=com.midai.pay.customer.provider.BoCustomerProvider.class,method="findBoCustomerMercNo")
	public String findBoCustomerMercNo(String agentno);

	@Select("select bcd.merc_no AS mercNo,bc.merc_name AS mercName,bcd.merc_id AS mercId,bcd.bunding_time AS bundingTime,bcd.unbunding_time AS unbundingTime " +
			" from tbl_bo_customer_device bcd " +
			" INNER JOIN tbl_bo_customer bc ON bcd.merc_no=bc.merc_no where bcd.body_no=#{deviceNo} and bcd.state=0")
	public List<BoCustomerDeviceVo> fetchByDeviceNo(String deviceNo);
	
	@UpdateProvider(type=com.midai.pay.customer.provider.BoCustomerProvider.class,method="updateBoCustomer")
	public int updateBoCustomer(BoCustomer boCustomer);
	

	@Select("select "+ select +" from tbl_bo_customer where merc_no=#{id}")
	public BoCustomer selectByMercNo(String id);
	
	@Update(" UPDATE tbl_bo_customer SET state=#{state}  WHERE merc_no=#{mercNo} ")
	int updateState(@Param("state")int state, @Param("mercNo")String mercNo);
	
	@SelectProvider(type=com.midai.pay.customer.provider.BoCustomerProvider.class,method="findByMrecId")
	BoCustomerVo  findByMrecId (String mrecId);
	
	/*批量修改商户冻结状态*/
	
	@UpdateProvider(type=com.midai.pay.customer.provider.BoCustomerProvider.class,method="bacthUpdateOpstatefrozen")
	int bacthUpdateOpstatefrozen(String mercIds);
	
	/*批量修改商户解冻状态*/
	@UpdateProvider(type=com.midai.pay.customer.provider.BoCustomerProvider.class,method="bacthUpdateOpstateunfrozen")
	int bacthUpdateOpstateunfrozen(String mercIds);
	
	/**
	 * 检验数据库中是否存在这一个手机号
	 */
	@Select("select count(1) from tbl_bo_customer where mobile=#{mobile}")
	int selectBymobile( @Param("mobile") String mobile);
	
	
	/**
	 * 待审进件列表使用
	 */
	@SelectProvider(type=com.midai.pay.customer.provider.BoCustomerProvider.class,method="selectbymercId")
	List<TasksInfo> selectbymercId(String mercIds);
	
	@Select(" SELECT '商户申请' subject, merc_id mercId, merc_name mercName, state state, merc_no mercNo, agent_id agentId, create_time createTime, mer_auto merAuto "
			+" from tbl_bo_customer where merc_no=#{mercId} and state != 4 and state != 3 ")
	TasksInfo findByMercId(String mercId);
	
	@Select(" SELECT create_time FROM tbl_bo_customer_review WHERE review_level=0 and merc_no=#{mercNo} ORDER BY id DESC LIMIT 0,1 ")
	String findFirstReviewTime(String mercNo);
	
	@Select(" SELECT a.state, a.merc_name mercName, a.account_name accountName, a.scobus scobus, a.address , a.branchbank_name branchbankName, bank.bankname, bank.bankcode, a.account_no accountNo, a.id_card idcard, a.mer_type merType ,customerdevice.body_no termno, "
			+ " pro.`code` provincecode, pro.`name` provincename "
			+ " , a.opstate, a.merc_no mercNo, '' businesslicense, '' organizationno, '' taxcertificateno, "
			+ "  (case when pro.name like '%市' then (SELECT ca.code from tbl_cn_area ca where ca.code=a.city_id) else (select cc.code from tbl_cn_city cc where cc.code=a.city_id) end) CITYCODE, "
			+ "  (case when pro.name like '%市' then (SELECT ca.name from tbl_cn_area ca where ca.code=a.city_id) else (select cc.name from tbl_cn_city cc where cc.code=a.city_id) end) CITYNAME "
			+ "   from tbl_bo_customer a left join tbl_bank bank on a.bank_id=bank.bankcode LEFT JOIN tbl_bo_customer_device customerdevice on a.merc_no=customerdevice.merc_no "
			+ " LEFT JOIN tbl_cn_province pro on a.peovince_id=pro.`code` "
			+ " where a.mobile=#{mobile} ")
	List<CustomerExtendVo> getCustomerExtendVoByMobile(String mobile);

	@Select(" SELECT CUST.*  FROM tbl_bo_customer CUST "
			+ " where exists (select 1 from (SELECT CUSTDEVICE.merc_no from tbl_bo_customer_device CUSTDEVICE  "
			+ " INNER JOIN  tbl_bo_device DEVICE ON DEVICE.device_no=CUSTDEVICE.body_no  WHERE DEVICE.device_no=#{deviceid}  ) s"
			+ " where CUST.merc_no=s.merc_no) ")
	List<BoCustomer> getCustomerByDeviceNo(String deviceid);
	
	/*更新custemer表的单笔费率*/
	@Update(" update tbl_bo_customer set single_fee = #{singleMoney}  where agent_id =#{agentNo} ")
	int updateCustomer(@Param("agentNo") String agentNo,@Param("singleMoney") Double singleMoney);

	
	/*海贝商户修改申请审核通过更新信息*/
	@UpdateProvider(type=com.midai.pay.customer.provider.BoCustomerProvider.class,method="updateHaibeCustomer")
	public int updateHaibeCustomer(BoCustomer boCustomer);

	@Update("  update tbl_bo_customer set mobile=#{newPhonenumber} where mobile=#{userId} and id_card=#{idCard}")
	int updateMobileByidCardAndMobile(@Param("newPhonenumber")String newPhonenumber, @Param("userId")String userId, @Param("idCard")String idCard);

	@Update("  update tbl_bo_customer  state=#{state} where mobile=#{mobile} ")
	int updateStateByMobile(@Param("mobile")String mobile, @Param("state")int state);
	
	@Select(" SELECT inscode from tbl_bo_customer where mobile=#{mobile} ")
	String findInscodeByMobile(String mobile);
	
	@Select(" select merc_id from tbl_bo_customer where mobile=#{mobile} ")
	String findMercIdByMobile(String mobile);
	
	@Insert(" insert into tbl_inst_merc(inst_code,inst_merc_id,inst_device_id,create_time) values(#{instCode},#{mercId},#{deviceId},now())")
	int insertInstMerc(@Param("instCode") String instCode,@Param("mercId") String mercId,@Param("deviceId")String deviceId);
	
	
	@Update(" UPDATE tbl_bo_customer SET customer_level=#{customerLevel}  WHERE merc_no=#{mercNo} ")
	int updateCustomerLevel(@Param("customerLevel")String customerLevel, @Param("mercNo")String mercNo);
	
	@Update(" UPDATE tbl_bo_customer SET qr_code_flag=#{qrCodeFlag}  WHERE merc_no=#{mercNo} ")
	int updateBoCustomerQrCodeFlag( @Param("mercNo") String mercNo, @Param("qrCodeFlag") int qrCodeFlag);

	@SelectProvider(type=com.midai.pay.customer.provider.BoCustomerProvider.class,method="queryCustomerInfo")
	BoCustomer queryCustomerInfo(String qrcode);
}
