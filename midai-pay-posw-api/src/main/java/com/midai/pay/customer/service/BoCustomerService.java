package com.midai.pay.customer.service;

import java.util.List;

import com.midai.framework.common.BaseService;
import com.midai.pay.customer.entity.BoCustomer;
import com.midai.pay.customer.entity.BoCustomerInst;
import com.midai.pay.customer.entity.BoCustomerReview;
import com.midai.pay.customer.query.BoCustomerCountQuery;
import com.midai.pay.customer.query.BoCustomerQuery;
import com.midai.pay.customer.query.CustomerApplyQuery;
import com.midai.pay.customer.query.CustomerPendingTaskQuery;
import com.midai.pay.customer.vo.AgentAuthReqBeanVo;
import com.midai.pay.customer.vo.BoCustomerAddVo;
import com.midai.pay.customer.vo.BoCustomerVo;
import com.midai.pay.customer.vo.CustomerApplyVo;
import com.midai.pay.customer.vo.CustomerFirstReviewVo;
import com.midai.pay.customer.vo.CustomerSecondReviewVo;
import com.midai.pay.device.vo.BoIostorageCustomerVo;
import com.midai.pay.device.vo.DeviceDetailVo;
import com.midai.pay.mobile.vo.CustomerExtendVo;
import com.midai.reqbean.AgentAuthReqBean;

/**
 * ClassName:User <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2016年9月21日  <br/>
 * @author   cjy
 * @version  
 * @since    JDK 1.7
 * @see
 */
public interface BoCustomerService extends BaseService<BoCustomer> {
	
	int applyCount(CustomerApplyQuery query);
	
	List<CustomerApplyVo> applyQuery(CustomerApplyQuery query);
	
	CustomerFirstReviewVo loadFirstReview(String mercNo);
	public int insertBoCustomer(BoCustomerAddVo vo,String name, int id);
	public List<BoCustomerVo> findQueryBoCustomer(BoCustomerQuery query);
    public int  findQueryBoCustomerCount(BoCustomerQuery query);
    
    public List<BoCustomerVo> ExcelDownloadBoCustomer(BoCustomerQuery query);
    public 	int  ExcelDownloadBoCustomerCount(BoCustomerCountQuery query);
    
    public int update(CustomerFirstReviewVo vo);

    public List<BoIostorageCustomerVo> findByDeviceNo(String deviceNo);

    public CustomerSecondReviewVo loadByBoCustomer(String id);
    
    int updateFirstReview(CustomerFirstReviewVo vo, String userName);
    
    CustomerSecondReviewVo loadSecondReview(String mercNo);
    
    int updateSecondReview(BoCustomerReview vo, String userName);
    List<DeviceDetailVo> loadCustomerDevice(String mercNo);
    
    public String  createMercNo(String agentno);
    public String  createMercId(String mercnostr,String cityid);
    /**
     * 检验手机号唯一性
     */
    public int selectBymobile(String mobile);
    
    /**
     * 代办事项   
     */
    public CustomerPendingTaskQuery dolist(CustomerPendingTaskQuery query,String userName);

	BoCustomer getCustomerByMobile(String mobile);

	List<CustomerExtendVo> getCustomerExtendVoByMobile(String mobile);

	List<BoCustomer> getCustomerByDeviceNo(String deviceid);

	BoCustomer getCustomerByMercId(String mercId);
	
	public  String updatebymobile(String mobile,String mercNo);
	
	public String getInstMercId(String mercId, String instCode);
	
	public List<BoCustomerInst> getBoCustomerInstList(String mercNo);
	
	public boolean updatePrepareInformation(BoCustomer customer, String instMercId);
	
	/**
	 * 保存下游渠道商户信息
	 * @param o
	 * @return
	 */
	public void customerEntry(AgentAuthReqBeanVo req);
	
	/**
	 * 生成二维码
	 * @param mercNo
	 * @param mercName
	 * @return
	 */
	public String createCustomerQrCode(String mercNo, String mercName);
	
	public void copyCus2AgentAuthReqBean(BoCustomer cus, AgentAuthReqBean auth);
}
